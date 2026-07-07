package com.mohammedaqeel.sarfit;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/** Tracks daily app usage streak and total workout-days per local account.
 *  Stored locally via SharedPreferences (per-username keys), so it currently
 *  does NOT survive an app uninstall -- that requires a cloud backend. */
public class StreakManager {

    private static final String PREFS = "sarfit_streaks";
    private static final SimpleDateFormat FMT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    /** Call once when the main dashboard is opened for a logged-in user. */
    public static void recordVisit(Context context, String username) {
        if (username == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String lastDateKey = username + "_last_date";
        String streakKey = username + "_streak";
        String totalKey = username + "_total";

        String today = FMT.format(new Date());
        String lastDate = prefs.getString(lastDateKey, null);

        if (today.equals(lastDate)) {
            return; // already recorded today
        }

        int streak = prefs.getInt(streakKey, 0);
        int total = prefs.getInt(totalKey, 0);

        if (lastDate != null) {
            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DAY_OF_YEAR, -1);
            String yesterdayStr = FMT.format(yesterday.getTime());
            if (yesterdayStr.equals(lastDate)) {
                streak += 1; // consecutive day
            } else {
                streak = 1; // streak broken
            }
        } else {
            streak = 1; // first ever visit
        }

        total += 1;

        prefs.edit()
                .putString(lastDateKey, today)
                .putInt(streakKey, streak)
                .putInt(totalKey, total)
                .apply();
    }

    public static int getStreak(Context context, String username) {
        if (username == null) return 0;
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(username + "_streak", 0);
    }

    public static int getTotalDays(Context context, String username) {
        if (username == null) return 0;
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(username + "_total", 0);
    }
}
