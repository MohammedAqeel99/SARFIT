package com.mohammedaqeel.sarfit;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Tracks daily app usage streak and total workout-days per Firebase account,
 *  stored in Firestore so it survives uninstall/reinstall and works across devices. */
public class StreakManager {

    private static final SimpleDateFormat FMT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public interface StreakCallback {
        void onResult(int streak, int total);
    }

    /** Call once when the main dashboard opens; updates streak/total in Firestore then returns them. */
    public static void recordVisitAndFetch(String uid, final StreakCallback callback) {
        if (uid == null) { callback.onResult(0, 0); return; }

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final com.google.firebase.firestore.DocumentReference ref = db.collection("users").document(uid);

        ref.get().addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                String today = FMT.format(new Date());
                String lastDate = doc.exists() ? doc.getString("lastDate") : null;
                Long streakL = doc.exists() ? doc.getLong("streak") : 0L;
                Long totalL = doc.exists() ? doc.getLong("total") : 0L;
                int streak = streakL != null ? streakL.intValue() : 0;
                int total = totalL != null ? totalL.intValue() : 0;

                if (today.equals(lastDate)) {
                    callback.onResult(streak, total);
                    return;
                }

                if (lastDate != null) {
                    Calendar yesterday = Calendar.getInstance();
                    yesterday.add(Calendar.DAY_OF_YEAR, -1);
                    String yesterdayStr = FMT.format(yesterday.getTime());
                    streak = yesterdayStr.equals(lastDate) ? streak + 1 : 1;
                } else {
                    streak = 1;
                }
                total += 1;

                Map<String, Object> update = new HashMap<>();
                update.put("lastDate", today);
                update.put("streak", streak);
                update.put("total", total);

                final int finalStreak = streak;
                final int finalTotal = total;
                ref.set(update, com.google.firebase.firestore.SetOptions.merge())
                        .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(com.google.android.gms.tasks.Task<Void> task) {
                                callback.onResult(finalStreak, finalTotal);
                            }
                        });
            }
        }).addOnFailureListener(new com.google.android.gms.tasks.OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                callback.onResult(0, 0);
            }
        });
    }

    /** Fetches streak/total without modifying them (used by the Profile page). */
    public static void fetchOnly(String uid, final StreakCallback callback) {
        if (uid == null) { callback.onResult(0, 0); return; }
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot doc) {
                        Long streakL = doc.exists() ? doc.getLong("streak") : 0L;
                        Long totalL = doc.exists() ? doc.getLong("total") : 0L;
                        callback.onResult(streakL != null ? streakL.intValue() : 0, totalL != null ? totalL.intValue() : 0);
                    }
                }).addOnFailureListener(new com.google.android.gms.tasks.OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) { callback.onResult(0, 0); }
                });
    }
}
