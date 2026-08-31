package com.mohammedaqeel.sarfit;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AttendanceManager {
    private static final String PREF_NAME = "SarfitAttendancePref";
    private static final String KEY_ATTENDANCE_COUNT = "attendance_count";
    private static final String KEY_LAST_CHECKIN = "last_checkin_date";
    private static final String KEY_IS_UNLOCKED = "is_main_unlocked";

    private final SharedPreferences prefs;

    public AttendanceManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean markAttendance() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String today = sdf.format(new Date());
        String lastCheckin = prefs.getString(KEY_LAST_CHECKIN, "");

        if (today.equals(lastCheckin)) {
            return false; // Already checked in today
        }

        // Calculate yesterday's date string to check for consecutive days
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String yesterday = sdf.format(calendar.getTime());

        int currentCount = prefs.getInt(KEY_ATTENDANCE_COUNT, 0);

        // Reset streak to 1 if the last check-in was not yesterday and not empty
        if (!lastCheckin.isEmpty() && !lastCheckin.equals(yesterday) && currentCount < 7) {
            currentCount = 1;
        } else {
            currentCount++;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_ATTENDANCE_COUNT, currentCount);
        editor.putString(KEY_LAST_CHECKIN, today);

        if (currentCount >= 7) {
            editor.putBoolean(KEY_IS_UNLOCKED, true);
        }

        editor.apply();
        return true;
    }

    public boolean isMainUnlocked() {
        return prefs.getBoolean(KEY_IS_UNLOCKED, false);
    }

    public int getAttendanceCount() {
        return prefs.getInt(KEY_ATTENDANCE_COUNT, 0);
    }
}
