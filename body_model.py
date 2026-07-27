"""
Port of BodyModelView.java.

Android's Canvas/Path/RadialGradient APIs don't exist in Kivy, so shapes are
rebuilt as filled polygons (quadratic-bezier curves sampled into point lists,
then triangulated with Kivy's Tesselator + rendered as a Mesh) instead of
Path + RadialGradient. The pulsing highlight animation is reproduced with a
Clock-driven alpha/brightness pulse instead of ValueAnimator.

Coordinates are computed in a "logical" top-down space (0,0 = top-left,
y increases downward, matching the original Android math 1:1) and then
flipped/offset into Kivy's bottom-left-origin widget space at draw time.
"""

import math

from kivy.clock import Clock
from kivy.graphics import Color, Mesh, Line
from kivy.graphics.tesselator import Tesselator
from kivy.uix.widget import Widget

import colors

SKIN_BASE = colors.SKIN_BASE
SKIN_LIGHT = colors.SKIN_LIGHT
OUTLINE_COLOR = (0, 0, 0, 0.55)


def _quad_bezier(p0, ctrl, p1, steps=14):
    pts = []
    for n in range(steps + 1):
        t = n / steps
        mt = 1 - t
        x = mt * mt * p0[0] + 2 * mt * t * ctrl[0] + t * t * p1[0]
        y = mt * mt * p0[1] + 2 * mt * t * ctrl[1] + t * t * p1[1]
        pts.append((x, y))
    return pts


def _oval(cx, cy, rx, ry, steps=28):
    return [
        (cx + rx * math.cos(2 * math.pi * k / steps), cy + ry * math.sin(2 * math.pi * k / steps))
        for k in range(steps)
    ]


def _rect(left, top, right, bottom):
    return [(left, top), (right, top), (right, bottom), (left, bottom)]


def _peaked_limb(x, top, bottom, outer_w, left):
    """Port of peakedLimbPath(): a peaked (biceps/triceps-like) limb shape."""
    d = -1 if left else 1
    mid = (top + bottom) / 2
    pts = [(x, top)]
    pts += _quad_bezier((x, top), (x + d * outer_w * 1.3, mid), (x + d * outer_w * 0.7, bottom))
    pts.append((x + d * outer_w * 0.15, bottom))
    pts += _quad_bezier((x + d * outer_w * 0.15, bottom), (x + d * outer_w * 0.55, mid), (x + d * outer_w * 0.15, top))
    return pts


def _tapered_limb(x, top, bottom, outer_w, left):
    """Port of taperedLimbPath(): a tapered (quad/hamstring-like) limb shape."""
    d = -1 if left else 1
    knee_y = top + (bottom - top) * 0.35
    pts = [(x, top)]
    pts += _quad_bezier((x, top), (x + d * outer_w * 1.15, knee_y), (x + d * outer_w * 0.75, bottom))
    pts.append((x + d * outer_w * 0.15, bottom))
    pts += _quad_bezier((x + d * outer_w * 0.15, bottom), (x + d * outer_w * 0.5, knee_y), (x + d * outer_w * 0.15, top))
    return pts


class BodyModel(Widget):
    """Front/back highlighted body diagram. Call set_muscle_group(name) to
    highlight the relevant region, toggle_view() to flip front/back, and
    stop_pulse() when the screen holding this widget is left."""

    MUSCLE_MAP = {
        "Chest": (["pecL", "pecR"], False, "NEON_GREEN"),
        "Side Delts": (["deltL", "deltR"], False, "NEON_GREEN"),
        "Front Delts": (["deltL", "deltR"], False, "NEON_GREEN"),
        "Back": (["lats", "traps"], True, "NEON_CYAN"),
        "Rear Delts": (["rearDeltL", "rearDeltR"], True, "NEON_CYAN"),
        "Biceps": (["bicepL", "bicepR"], False, "NEON_PINK"),
        "Triceps": (["tricepL", "tricepR"], True, "NEON_PINK"),
        "Legs": (["quadL", "quadR"], False, "NEON_PINK"),
        "Core": (["abs"], False, "NEON_PINK"),
        "Cardio": (["quadL", "quadR", "abs"], False, "NEON_GOLD"),
    }

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.highlighted_parts = set()
        self.highlight_color = colors.NEON_GREEN
        self.show_back = False
        self.pulse = 0.5
        self._t = 0.0
        self._clock_ev = Clock.schedule_interval(self._update_pulse, 1 / 30)
        self.bind(size=self._redraw, pos=self._redraw)
        self._redraw()

    # ---- public API (mirrors BodyModelView.java) --------------------------
    def set_muscle_group(self, muscle_name):
        self.show_back = False
        self.highlighted_parts = set()
        self.highlight_color = colors.NEON_GREEN

        entry = self.MUSCLE_MAP.get(muscle_name)
        if entry:
            parts, show_back, color_name = entry
            self.highlighted_parts = set(parts)
            self.show_back = show_back
            self.highlight_color = getattr(colors, color_name)
        self._redraw()

    def toggle_view(self):
        self.show_back = not self.show_back
        self._redraw()

    def is_showing_back(self):
        return self.show_back

    def stop_pulse(self):
        if self._clock_ev:
            self._clock_ev.cancel()
            self._clock_ev = None

    # ---- animation ----------------------------------------------------
    def _update_pulse(self, dt):
        self._t += dt
        cycle = self._t % 1.8  # 900ms up + 900ms down, like the Java ValueAnimator
        frac = cycle / 0.9 if cycle < 0.9 else 1 - (cycle - 0.9) / 0.9
        self.pulse = 0.5 + 0.5 * frac
        self._redraw()

    # ---- drawing --------------------------------------------------------
    def _to_widget(self, logical_pts, w, h):
        """Flip logical (y-down) coords into Kivy's bottom-left-origin space."""
        return [c for (x, y) in logical_pts for c in (self.x + x, self.y + (h - y))]

    def _fill(self, logical_pts, part_id, w, h):
        active = part_id in self.highlighted_parts
        if active:
            r, g, b, _ = self.highlight_color
            a = 0.35 + self.pulse * 0.55
            fill_color = (r, g, b, a)
        else:
            fill_color = SKIN_BASE

        flat = self._to_widget(logical_pts, w, h)
        tess = Tesselator()
        tess.add_contour(flat)
        if tess.tesselate():
            Color(*fill_color)
            for vertices, indices in tess.meshes:
                Mesh(vertices=vertices, indices=indices, mode="triangle_fan")
        Color(*OUTLINE_COLOR)
        Line(points=flat + flat[:2], width=1.1)

    def _redraw(self, *args):
        self.canvas.clear()
        w, h = self.width, self.height
        if w <= 0 or h <= 0:
            return
        with self.canvas:
            if self.show_back:
                self._draw_back(w, h)
            else:
                self._draw_front(w, h)

    def _draw_front(self, w, h):
        cx = w / 2
        head_r = h * 0.075
        head_cy = h * 0.13
        self._fill(_oval(cx, head_cy, head_r, head_r), "head", w, h)

        shoulder_y = head_cy + head_r + h * 0.05
        torso_half_top = w * 0.21
        torso_half_waist = w * 0.13
        torso_bottom = shoulder_y + h * 0.24

        delt_r = h * 0.05
        self._fill(_oval(cx - torso_half_top + delt_r * 0.25, shoulder_y + delt_r * 0.05, delt_r * 0.65, delt_r * 1.05), "deltL", w, h)
        self._fill(_oval(cx + torso_half_top - delt_r * 0.25, shoulder_y + delt_r * 0.05, delt_r * 0.65, delt_r * 1.05), "deltR", w, h)

        pec_top = shoulder_y + h * 0.01
        pec_w = torso_half_top * 0.85
        pec_h = h * 0.09
        self._fill(_oval(cx - pec_w / 2 - w * 0.008, pec_top + pec_h / 2, pec_w / 2, pec_h / 2), "pecL", w, h)
        self._fill(_oval(cx + pec_w / 2 + w * 0.008, pec_top + pec_h / 2, pec_w / 2, pec_h / 2), "pecR", w, h)

        torso = [(cx - torso_half_top * 0.85, pec_top + pec_h * 0.7)]
        torso += _quad_bezier((cx - torso_half_top * 0.85, pec_top + pec_h * 0.7),
                               (cx - torso_half_top * 0.6, torso_bottom - h * 0.02),
                               (cx - torso_half_waist, torso_bottom))
        torso.append((cx + torso_half_waist, torso_bottom))
        torso += _quad_bezier((cx + torso_half_waist, torso_bottom),
                               (cx + torso_half_top * 0.6, torso_bottom - h * 0.02),
                               (cx + torso_half_top * 0.85, pec_top + pec_h * 0.7))
        self._fill(torso, "torso", w, h)

        abs_top = torso_bottom - h * 0.115
        abs_half_w = torso_half_waist * 0.75
        for row in range(3):
            ry = abs_top + row * h * 0.036
            self._fill(_rect(cx - abs_half_w, ry, cx - 4, ry + h * 0.03), "abs", w, h)
            self._fill(_rect(cx + 4, ry, cx + abs_half_w, ry + h * 0.03), "abs", w, h)

        arm_top = shoulder_y + h * 0.005
        arm_bottom = torso_bottom + h * 0.05
        arm_outer_w = w * 0.058
        self._fill(_peaked_limb(cx - torso_half_top - 2, arm_top, arm_bottom, arm_outer_w, True), "bicepL", w, h)
        self._fill(_peaked_limb(cx + torso_half_top + 2, arm_top, arm_bottom, arm_outer_w, False), "bicepR", w, h)

        leg_top = abs_top + h * 0.135
        leg_bottom = h * 0.93
        leg_outer_w = w * 0.078
        leg_gap = w * 0.014
        self._fill(_tapered_limb(cx - leg_gap, leg_top, leg_bottom, leg_outer_w, True), "quadL", w, h)
        self._fill(_tapered_limb(cx + leg_gap, leg_top, leg_bottom, leg_outer_w, False), "quadR", w, h)

    def _draw_back(self, w, h):
        cx = w / 2
        head_r = h * 0.075
        head_cy = h * 0.13
        self._fill(_oval(cx, head_cy, head_r, head_r), "head", w, h)

        shoulder_y = head_cy + head_r + h * 0.05
        torso_half_top = w * 0.21
        torso_half_waist = w * 0.13
        torso_bottom = shoulder_y + h * 0.24

        traps = [
            (cx - w * 0.035, shoulder_y - h * 0.03),
            (cx - torso_half_top * 0.8, shoulder_y + h * 0.015),
            (cx, shoulder_y + h * 0.06),
            (cx + torso_half_top * 0.8, shoulder_y + h * 0.015),
            (cx + w * 0.035, shoulder_y - h * 0.03),
        ]
        self._fill(traps, "traps", w, h)

        delt_r = h * 0.05
        self._fill(_oval(cx - torso_half_top + delt_r * 0.25, shoulder_y + delt_r * 0.05, delt_r * 0.65, delt_r * 1.05), "rearDeltL", w, h)
        self._fill(_oval(cx + torso_half_top - delt_r * 0.25, shoulder_y + delt_r * 0.05, delt_r * 0.65, delt_r * 1.05), "rearDeltR", w, h)

        lats = [(cx - torso_half_top * 0.9, shoulder_y + h * 0.02)]
        lats += _quad_bezier((cx - torso_half_top * 0.9, shoulder_y + h * 0.02),
                              (cx - torso_half_top - w * 0.015, shoulder_y + h * 0.13),
                              (cx - torso_half_waist, torso_bottom))
        lats.append((cx + torso_half_waist, torso_bottom))
        lats += _quad_bezier((cx + torso_half_waist, torso_bottom),
                              (cx + torso_half_top + w * 0.015, shoulder_y + h * 0.13),
                              (cx + torso_half_top * 0.9, shoulder_y + h * 0.02))
        lats.append((cx, shoulder_y + h * 0.09))
        self._fill(lats, "lats", w, h)

        arm_top = shoulder_y + h * 0.005
        arm_bottom = torso_bottom + h * 0.05
        arm_outer_w = w * 0.058
        self._fill(_peaked_limb(cx - torso_half_top - 2, arm_top, arm_bottom, arm_outer_w, True), "tricepL", w, h)
        self._fill(_peaked_limb(cx + torso_half_top + 2, arm_top, arm_bottom, arm_outer_w, False), "tricepR", w, h)

        glut_top = torso_bottom - h * 0.02
        self._fill(_rect(cx - torso_half_waist, glut_top, cx - 3, glut_top + h * 0.07), "glutes", w, h)
        self._fill(_rect(cx + 3, glut_top, cx + torso_half_waist, glut_top + h * 0.07), "glutes", w, h)

        leg_top = glut_top + h * 0.07
        leg_bottom = h * 0.93
        leg_outer_w = w * 0.075
        leg_gap = w * 0.014
        self._fill(_tapered_limb(cx - leg_gap, leg_top, leg_bottom, leg_outer_w, True), "hamstrings", w, h)
        self._fill(_tapered_limb(cx + leg_gap, leg_top, leg_bottom, leg_outer_w, False), "hamstrings", w, h)
