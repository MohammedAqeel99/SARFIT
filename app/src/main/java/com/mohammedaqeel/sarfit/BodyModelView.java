package com.mohammedaqeel.sarfit;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BodyModelView extends View {

    private Set<String> highlightedParts = new HashSet<>();
    private int highlightColor = Color.parseColor("#39FF14");
    private boolean showBack = false;
    private float pulse = 0.5f;
    private ValueAnimator animator;

    private final Paint outline = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint shapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint striation = new Paint(Paint.ANTI_ALIAS_FLAG);

    private static final int SKIN_BASE = Color.parseColor("#3A3A42");
    private static final int SKIN_LIGHT = Color.parseColor("#55555E");

    public BodyModelView(Context context) { super(context); init(); }
    public BodyModelView(Context context, AttributeSet attrs) { super(context, attrs); init(); }

    private void init() {
        outline.setColor(Color.parseColor("#000000"));
        outline.setStyle(Paint.Style.STROKE);
        outline.setStrokeWidth(2.5f);
        outline.setStrokeJoin(Paint.Join.ROUND);
        outline.setAlpha(140);

        striation.setColor(Color.parseColor("#00000070"));
        striation.setStyle(Paint.Style.STROKE);
        striation.setStrokeWidth(2f);

        startPulse();
    }

    public void setMuscleGroup(String muscleName) {
        showBack = false;
        highlightedParts.clear();
        highlightColor = Color.parseColor("#39FF14");
        if (muscleName == null) { invalidate(); return; }

        switch (muscleName) {
            case "Chest":
                highlightedParts.addAll(Arrays.asList("pecL", "pecR"));
                highlightColor = Color.parseColor("#39FF14");
                break;
            case "Side Delts":
            case "Front Delts":
                highlightedParts.addAll(Arrays.asList("deltL", "deltR"));
                highlightColor = Color.parseColor("#39FF14");
                break;
            case "Back":
                showBack = true;
                highlightedParts.addAll(Arrays.asList("lats", "traps"));
                highlightColor = Color.parseColor("#00F0FF");
                break;
            case "Rear Delts":
                showBack = true;
                highlightedParts.addAll(Arrays.asList("rearDeltL", "rearDeltR"));
                highlightColor = Color.parseColor("#00F0FF");
                break;
            case "Biceps":
                highlightedParts.addAll(Arrays.asList("bicepL", "bicepR"));
                highlightColor = Color.parseColor("#FF2E9F");
                break;
            case "Triceps":
                showBack = true;
                highlightedParts.addAll(Arrays.asList("tricepL", "tricepR"));
                highlightColor = Color.parseColor("#FF2E9F");
                break;
            case "Legs":
                highlightedParts.addAll(Arrays.asList("quadL", "quadR"));
                highlightColor = Color.parseColor("#FF2E9F");
                break;
            case "Core":
                highlightedParts.addAll(Arrays.asList("abs"));
                highlightColor = Color.parseColor("#FF2E9F");
                break;
            case "Cardio":
                highlightedParts.addAll(Arrays.asList("quadL", "quadR", "abs"));
                highlightColor = Color.parseColor("#FFD700");
                break;
            default: break;
        }
        invalidate();
    }

    public void toggleView() { showBack = !showBack; invalidate(); }
    public boolean isShowingBack() { return showBack; }

    private void startPulse() {
        animator = ValueAnimator.ofFloat(0.5f, 1f);
        animator.setDuration(900);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator a) { pulse = (float) a.getAnimatedValue(); invalidate(); }
        });
        animator.start();
    }

    public void stopPulse() { if (animator != null) animator.cancel(); }

    /** Fills a shape with a sculpted radial-gradient look: bright core fading to a darker rim. */
    private void fillSculpted(Canvas c, Path path, RectF bounds, String partId) {
        boolean active = highlightedParts.contains(partId);
        int centerColor, edgeColor;
        if (active) {
            int a = (int) (pulse * 255);
            centerColor = Color.argb(Math.min(255, a + 60), Color.red(highlightColor), Color.green(highlightColor), Color.blue(highlightColor));
            edgeColor = Color.argb(a, Color.red(highlightColor) / 2, Color.green(highlightColor) / 2, Color.blue(highlightColor) / 2);
        } else {
            centerColor = SKIN_LIGHT;
            edgeColor = SKIN_BASE;
        }
        float cx = bounds.centerX(), cy = bounds.centerY();
        float radius = Math.max(bounds.width(), bounds.height()) * 0.75f;
        shapePaint.setShader(new RadialGradient(cx, cy, Math.max(radius, 1f), centerColor, edgeColor, Shader.TileMode.CLAMP));
        c.drawPath(path, shapePaint);
        shapePaint.setShader(null);
        c.drawPath(path, outline);
    }

    private RectF bounds(Path p) {
        RectF r = new RectF();
        p.computeBounds(r, true);
        return r;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = getWidth(), h = getHeight();
        if (w == 0 || h == 0) return;
        if (showBack) drawBack(canvas, w, h); else drawFront(canvas, w, h);
    }

    private void drawFront(Canvas c, float w, float h) {
        float cx = w / 2f;
        float headR = h * 0.075f;
        float headCy = h * 0.13f;

        Path head = new Path();
        head.addOval(cx - headR, headCy - headR, cx + headR, headCy + headR, Path.Direction.CW);
        fillSculpted(c, head, bounds(head), "head");

        float shoulderY = headCy + headR + h * 0.05f;
        float torsoHalfWTop = w * 0.21f;
        float torsoHalfWWaist = w * 0.13f;
        float torsoBottom = shoulderY + h * 0.24f;

        // Deltoid caps
        float deltR = h * 0.05f;
        Path deltL = new Path();
        deltL.addOval(cx - torsoHalfWTop - deltR * 0.4f, shoulderY - deltR, cx - torsoHalfWTop + deltR * 0.9f, shoulderY + deltR * 1.1f, Path.Direction.CW);
        fillSculpted(c, deltL, bounds(deltL), "deltL");

        Path deltR2 = new Path();
        deltR2.addOval(cx + torsoHalfWTop - deltR * 0.9f, shoulderY - deltR, cx + torsoHalfWTop + deltR * 0.4f, shoulderY + deltR * 1.1f, Path.Direction.CW);
        fillSculpted(c, deltR2, bounds(deltR2), "deltR");

        // Two separate pec lobes (rounded ovals angled toward center)
        float pecTop = shoulderY + h * 0.01f;
        float pecW = torsoHalfWTop * 0.85f;
        float pecH = h * 0.09f;
        Path pecL = new Path();
        pecL.addOval(cx - pecW - w * 0.005f, pecTop, cx - w * 0.012f, pecTop + pecH, Path.Direction.CW);
        fillSculpted(c, pecL, bounds(pecL), "pecL");

        Path pecR = new Path();
        pecR.addOval(cx + w * 0.012f, pecTop, cx + pecW + w * 0.005f, pecTop + pecH, Path.Direction.CW);
        fillSculpted(c, pecR, bounds(pecR), "pecR");

        // Torso base beneath pecs (waist taper), drawn behind abs
        Path torso = new Path();
        torso.moveTo(cx - torsoHalfWTop * 0.85f, pecTop + pecH * 0.7f);
        torso.quadTo(cx - torsoHalfWTop * 0.6f, torsoBottom - h * 0.02f, cx - torsoHalfWWaist, torsoBottom);
        torso.lineTo(cx + torsoHalfWWaist, torsoBottom);
        torso.quadTo(cx + torsoHalfWTop * 0.6f, torsoBottom - h * 0.02f, cx + torsoHalfWTop * 0.85f, pecTop + pecH * 0.7f);
        torso.close();
        Path torsoBoundsPath = torso;
        RectF tb = bounds(torsoBoundsPath);
        shapePaint.setShader(new LinearGradient(0, tb.top, 0, tb.bottom, SKIN_LIGHT, SKIN_BASE, Shader.TileMode.CLAMP));
        c.drawPath(torso, shapePaint);
        shapePaint.setShader(null);
        c.drawPath(torso, outline);

        // Abs: 3x2 grid
        float absTop = torsoBottom - h * 0.115f;
        float absHalfW = torsoHalfWWaist * 0.75f;
        for (int row = 0; row < 3; row++) {
            float ry = absTop + row * h * 0.036f;
            Path l = new Path(); l.addRoundRect(cx - absHalfW, ry, cx - 4f, ry + h * 0.03f, 8f, 8f, Path.Direction.CW);
            Path r = new Path(); r.addRoundRect(cx + 4f, ry, cx + absHalfW, ry + h * 0.03f, 8f, 8f, Path.Direction.CW);
            fillSculpted(c, l, bounds(l), "abs");
            fillSculpted(c, r, bounds(r), "abs");
        }

        // Biceps (peaked oval-ish)
        float armTop = shoulderY + h * 0.005f;
        float armBottom = torsoBottom + h * 0.05f;
        float armOuterW = w * 0.058f;
        Path bicepL = peakedLimbPath(cx - torsoHalfWTop - 2f, armTop, armBottom, armOuterW, true);
        fillSculpted(c, bicepL, bounds(bicepL), "bicepL");
        Path bicepR = peakedLimbPath(cx + torsoHalfWTop + 2f, armTop, armBottom, armOuterW, false);
        fillSculpted(c, bicepR, bounds(bicepR), "bicepR");

        // Quads
        float legTop = absTop + h * 0.135f;
        float legBottom = h * 0.93f;
        float legOuterW = w * 0.078f;
        float legGap = w * 0.014f;
        Path quadL = taperedLimbPath(cx - legGap, legTop, legBottom, legOuterW, true);
        fillSculpted(c, quadL, bounds(quadL), "quadL");
        Path quadR = taperedLimbPath(cx + legGap, legTop, legBottom, legOuterW, false);
        fillSculpted(c, quadR, bounds(quadR), "quadR");
        c.drawLine(cx, legTop, cx, legBottom, striation);
    }

    private void drawBack(Canvas c, float w, float h) {
        float cx = w / 2f;
        float headR = h * 0.075f;
        float headCy = h * 0.13f;

        Path head = new Path();
        head.addOval(cx - headR, headCy - headR, cx + headR, headCy + headR, Path.Direction.CW);
        fillSculpted(c, head, bounds(head), "head");

        float shoulderY = headCy + headR + h * 0.05f;
        float torsoHalfWTop = w * 0.21f;
        float torsoHalfWWaist = w * 0.13f;
        float torsoBottom = shoulderY + h * 0.24f;

        // Traps
        Path traps = new Path();
        traps.moveTo(cx - w * 0.035f, shoulderY - h * 0.03f);
        traps.lineTo(cx - torsoHalfWTop * 0.8f, shoulderY + h * 0.015f);
        traps.lineTo(cx, shoulderY + h * 0.06f);
        traps.lineTo(cx + torsoHalfWTop * 0.8f, shoulderY + h * 0.015f);
        traps.lineTo(cx + w * 0.035f, shoulderY - h * 0.03f);
        traps.close();
        fillSculpted(c, traps, bounds(traps), "traps");

        // Deltoids + rear delt caps
        float deltR = h * 0.05f;
        Path deltL = new Path();
        deltL.addOval(cx - torsoHalfWTop - deltR * 0.4f, shoulderY - deltR, cx - torsoHalfWTop + deltR * 0.9f, shoulderY + deltR * 1.1f, Path.Direction.CW);
        fillSculpted(c, deltL, bounds(deltL), "rearDeltL");
        Path deltR2 = new Path();
        deltR2.addOval(cx + torsoHalfWTop - deltR * 0.9f, shoulderY - deltR, cx + torsoHalfWTop + deltR * 0.4f, shoulderY + deltR * 1.1f, Path.Direction.CW);
        fillSculpted(c, deltR2, bounds(deltR2), "rearDeltR");

        // Lats: V-taper from wide upper back to narrow waist
        Path lats = new Path();
        lats.moveTo(cx - torsoHalfWTop * 0.9f, shoulderY + h * 0.02f);
        lats.quadTo(cx - torsoHalfWTop - w * 0.015f, shoulderY + h * 0.13f, cx - torsoHalfWWaist, torsoBottom);
        lats.lineTo(cx + torsoHalfWWaist, torsoBottom);
        lats.quadTo(cx + torsoHalfWTop + w * 0.015f, shoulderY + h * 0.13f, cx + torsoHalfWTop * 0.9f, shoulderY + h * 0.02f);
        lats.lineTo(cx, shoulderY + h * 0.09f);
        lats.close();
        fillSculpted(c, lats, bounds(lats), "lats");
        c.drawLine(cx, shoulderY + h * 0.09f, cx, torsoBottom, striation);

        // Triceps
        float armTop = shoulderY + h * 0.005f;
        float armBottom = torsoBottom + h * 0.05f;
        float armOuterW = w * 0.058f;
        Path tricepL = peakedLimbPath(cx - torsoHalfWTop - 2f, armTop, armBottom, armOuterW, true);
        fillSculpted(c, tricepL, bounds(tricepL), "tricepL");
        Path tricepR = peakedLimbPath(cx + torsoHalfWTop + 2f, armTop, armBottom, armOuterW, false);
        fillSculpted(c, tricepR, bounds(tricepR), "tricepR");

        // Glutes
        float glutTop = torsoBottom - h * 0.02f;
        Path glutL = new Path(); glutL.addRoundRect(cx - torsoHalfWWaist, glutTop, cx - 3f, glutTop + h * 0.07f, 18f, 18f, Path.Direction.CW);
        Path glutR = new Path(); glutR.addRoundRect(cx + 3f, glutTop, cx + torsoHalfWWaist, glutTop + h * 0.07f, 18f, 18f, Path.Direction.CW);
        fillSculpted(c, glutL, bounds(glutL), "glutes");
        fillSculpted(c, glutR, bounds(glutR), "glutes");

        // Hamstrings + calves (neutral, not a target group here)
        float legTop = glutTop + h * 0.07f;
        float legBottom = h * 0.93f;
        float legOuterW = w * 0.075f;
        float legGap = w * 0.014f;
        Path hamL = taperedLimbPath(cx - legGap, legTop, legBottom, legOuterW, true);
        fillSculpted(c, hamL, bounds(hamL), "hamstrings");
        Path hamR = taperedLimbPath(cx + legGap, legTop, legBottom, legOuterW, false);
        fillSculpted(c, hamR, bounds(hamR), "hamstrings");
    }

    private Path peakedLimbPath(float x, float top, float bottom, float outerW, boolean left) {
        Path p = new Path();
        float dir = left ? -1f : 1f;
        p.moveTo(x, top);
        p.quadTo(x + dir * outerW * 1.3f, (top + bottom) / 2f, x + dir * outerW * 0.7f, bottom);
        p.lineTo(x + dir * outerW * 0.15f, bottom);
        p.quadTo(x + dir * outerW * 0.55f, (top + bottom) / 2f, x + dir * outerW * 0.15f, top);
        p.close();
        return p;
    }

    private Path taperedLimbPath(float x, float top, float bottom, float outerW, boolean left) {
        Path p = new Path();
        float dir = left ? -1f : 1f;
        p.moveTo(x, top);
        p.quadTo(x + dir * outerW * 1.15f, top + (bottom - top) * 0.35f, x + dir * outerW * 0.75f, bottom);
        p.lineTo(x + dir * outerW * 0.15f, bottom);
        p.quadTo(x + dir * outerW * 0.5f, top + (bottom - top) * 0.35f, x + dir * outerW * 0.15f, top);
        p.close();
        return p;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopPulse();
    }
}
