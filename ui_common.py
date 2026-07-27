"""
Small shared UI building blocks so screens don't repeat boilerplate.
Stands in for gradient_bg.xml / gradient_card.xml drawables + Fonts.java.
"""

from kivy.animation import Animation
from kivy.graphics import Color, RoundedRectangle, Rectangle
from kivy.uix.behaviors import ButtonBehavior
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.label import Label

import colors


class BackgroundBox(BoxLayout):
    """A BoxLayout with a flat dark background (port of gradient_bg.xml)."""

    def __init__(self, bg_color=None, **kwargs):
        super().__init__(**kwargs)
        self._bg_color = bg_color or colors.BG_DARK
        with self.canvas.before:
            Color(*self._bg_color)
            self._rect = Rectangle(size=self.size, pos=self.pos)
        self.bind(size=self._update_rect, pos=self._update_rect)

    def _update_rect(self, *args):
        self._rect.size = self.size
        self._rect.pos = self.pos


class Card(BoxLayout):
    """A BoxLayout with a rounded card background (port of gradient_card.xml)."""

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        with self.canvas.before:
            Color(*colors.BG_CARD)
            self._rect = RoundedRectangle(size=self.size, pos=self.pos, radius=[14])
        self.bind(size=self._update_rect, pos=self._update_rect)

    def _update_rect(self, *args):
        self._rect.size = self.size
        self._rect.pos = self.pos


class TapLabel(ButtonBehavior, Label):
    """A Label that behaves like a clickable button/text-link."""
    pass


class TapCard(ButtonBehavior, Card):
    """A Card that can be tapped (used for the day-schedule cards)."""
    pass


class TapBox(ButtonBehavior, BoxLayout):
    """A plain (non-card) BoxLayout that can be tapped."""
    pass


def pulse_label(label, scale_min=0.75, scale_max=1.0, duration=0.8):
    """Repeating alpha pulse, stand-in for the ObjectAnimator scale pulse
    used on the app name / avatar in LoginActivity/ProfileActivity."""
    anim = (
        Animation(color=(*label.color[:3], scale_max), duration=duration)
        + Animation(color=(*label.color[:3], scale_min), duration=duration)
    )
    anim.repeat = True
    anim.start(label)
    return anim
