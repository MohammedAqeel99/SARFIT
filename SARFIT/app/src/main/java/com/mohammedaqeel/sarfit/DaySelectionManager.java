package com.mohammedaqeel.sarfit;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Stores which muscle groups the user has assigned to each day of the week.
 *  Seeded with a sensible default split the first time the app runs. */
public class DaySelectionManager {

    private static final String PREFS = "sarfit_day_selection";
    public static final String[] DAYS = {
            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };

    private static Set<String> defaultFor(String day) {
        switch (day) {
            case "Sunday": return set("Cardio");
            case "Monday": return set("Chest", "Shoulders");
            case "Tuesday": return set("Back");
            case "Wednesday": return set("Biceps", "Triceps", "Legs", "Core");
            case "Thursday": return set("Chest", "Shoulders");
            case "Friday": return set("Back");
            case "Saturday": return set("Biceps", "Triceps", "Legs", "Core");
            default: return new LinkedHashSet<>();
        }
    }

    private static Set<String> set(String... items) {
        return new LinkedHashSet<>(Arrays.asList(items));
    }

    public static List<String> getMusclesForDay(Context context, String day) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String saved = prefs.getString(day, null);
        if (saved == null) {
            return new ArrayList<>(defaultFor(day));
        }
        if (saved.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(saved.split(",")));
    }

    public static void setMusclesForDay(Context context, String day, List<String> muscles) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit().putString(day, String.join(",", muscles)).apply();
    }

    public static String labelFor(List<String> muscles) {
        if (muscles.isEmpty()) return "Rest / Not set";
        return String.join(" + ", muscles);
    }
}
