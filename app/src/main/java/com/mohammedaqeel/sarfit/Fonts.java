package com.mohammedaqeel.sarfit;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

public class Fonts {

    private static Typeface medium;

    public static Typeface medium(Context context) {
        if (medium == null) {
            medium = ResourcesCompat.getFont(context, R.font.europa_medium);
        }
        return medium;
    }

    /** Applies the custom font to every TextView/EditText/Button in a view tree.
     * Headings (bold-styled or large text) keep bold via Typeface.create(medium, BOLD). */
    public static void applyRecursively(Context context, View root) {
        Typeface base = medium(context);
        if (base == null) return;
        applyToView(root, base);
    }

    private static void applyToView(View view, Typeface base) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            int style = tv.getTypeface() != null ? tv.getTypeface().getStyle() : Typeface.NORMAL;
            tv.setTypeface(Typeface.create(base, style));
        }
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                applyToView(vg.getChildAt(i), base);
            }
        }
    }
}
