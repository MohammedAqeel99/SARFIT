package com.mohammedaqeel.sarfit;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Tracks a user's daily attendance ticks in Firestore.
 *  Used to gate the Beginner Week (7 ticks = unlock the full app) and,
 *  after that, continues on as a simple ongoing daily check-in. */
public class AttendanceManager {

    private static final SimpleDateFormat FMT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public interface ProgressCallback {
        void onResult(List<String> attendedDates, boolean beginnerCompleted);
    }

    public interface SimpleCallback {
        void onDone();
    }

    public static void getProgress(String uid, final ProgressCallback callback) {
        if (uid == null) { callback.onResult(new ArrayList<String>(), false); return; }
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot doc) {
                        List<String> dates = new ArrayList<>();
                        boolean completed = false;
                        if (doc.exists()) {
                            List<String> raw = (List<String>) doc.get("attendanceDates");
                            if (raw != null) dates = raw;
                            Boolean b = doc.getBoolean("beginnerCompleted");
                            completed = b != null && b;
                        }
                        callback.onResult(dates, completed);
                    }
                })
                .addOnFailureListener(new com.google.android.gms.tasks.OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) { callback.onResult(new ArrayList<String>(), false); }
                });
    }

    /** Marks today attended (safe to call multiple times same day - no duplicate). */
    public static void markTodayAttendance(String uid, final SimpleCallback callback) {
        if (uid == null) { if (callback != null) callback.onDone(); return; }
        String today = FMT.format(new Date());
        Map<String, Object> update = new HashMap<>();
        update.put("attendanceDates", FieldValue.arrayUnion(today));
        FirebaseFirestore.getInstance().collection("users").document(uid)
                .set(update, com.google.firebase.firestore.SetOptions.merge())
                .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(com.google.android.gms.tasks.Task<Void> task) {
                        if (callback != null) callback.onDone();
                    }
                });
    }

    public static void markBeginnerCompleted(String uid) {
        if (uid == null) return;
        Map<String, Object> update = new HashMap<>();
        update.put("beginnerCompleted", true);
        FirebaseFirestore.getInstance().collection("users").document(uid)
                .set(update, com.google.firebase.firestore.SetOptions.merge());
    }
}
