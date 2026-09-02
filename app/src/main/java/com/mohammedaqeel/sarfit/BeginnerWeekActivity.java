package com.mohammedaqeel.sarfit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Map;

public class BeginnerWeekActivity extends AppCompatActivity {

    private String uid;
    private String username;
    private LinearLayout container;
    private TextView tvUnlockMain;
    private List<String> attendedDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginner_week);

        username = getIntent().getStringExtra("username");
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = fbUser != null ? fbUser.getUid() : null;

        container = findViewById(R.id.beginnerDaysContainer);
        tvUnlockMain = findViewById(R.id.tvUnlockMain);

        tvUnlockMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uid != null) AttendanceManager.markBeginnerCompleted(uid);
                goToMain();
            }
        });

        loadProgress();
    }

    private void loadProgress() {
        AttendanceManager.getProgress(uid, new AttendanceManager.ProgressCallback() {
            @Override
            public void onResult(List<String> attendedDatesResult, boolean beginnerCompleted) {
                attendedDates = attendedDatesResult;
                if (beginnerCompleted) {
                    goToMain();
                    return;
                }
                renderDays();
            }
        });
    }

    private void renderDays() {
        container.removeAllViews();
        Map<Integer, MuscleSection> days = BeginnerWeekData.getDays();
        int attendedCount = attendedDates != null ? attendedDates.size() : 0;

        for (int dayNum = 1; dayNum <= 7; dayNum++) {
            final MuscleSection section = days.get(dayNum);
            boolean completed = dayNum <= attendedCount;
            boolean isNext = dayNum == attendedCount + 1;

            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(20, 18, 20, 18);
            card.setBackgroundResource(R.drawable.gradient_card);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.bottomMargin = 10;
            card.setLayoutParams(lp);

            TextView title = new TextView(this);
            title.setText(section.muscleName + (completed ? "  \u2713" : isNext ? "" : "  \uD83D\uDD12"));
            title.setTextColor(completed ? Color.parseColor("#39FF14") : isNext ? Color.WHITE : Color.parseColor("#666666"));
            title.setTextSize(16);
            title.setTypeface(title.getTypeface(), android.graphics.Typeface.BOLD);
            card.addView(title);

            if (completed || isNext) {
                for (Exercise ex : section.exercises) {
                    TextView row = new TextView(this);
                    row.setText("\u2022 " + ex.name + "  -  " + ex.getSetsReps());
                    row.setTextColor(Color.parseColor("#CFCFCF"));
                    row.setTextSize(13);
                    LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    rlp.topMargin = 8;
                    row.setLayoutParams(rlp);
                    card.addView(row);
                }
            }

            if (isNext) {
                TextView markBtn = new TextView(this);
                markBtn.setText("Mark Today's Attendance");
                markBtn.setTextColor(Color.parseColor("#0A0A0A"));
                markBtn.setTextSize(13);
                markBtn.setTypeface(markBtn.getTypeface(), android.graphics.Typeface.BOLD);
                markBtn.setGravity(Gravity.CENTER);
                markBtn.setPadding(0, 14, 0, 14);
                markBtn.setBackgroundColor(Color.parseColor("#39FF14"));
                LinearLayout.LayoutParams mlp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mlp.topMargin = 14;
                markBtn.setLayoutParams(mlp);
                markBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AttendanceManager.markTodayAttendance(uid, new AttendanceManager.SimpleCallback() {
                            @Override
                            public void onDone() {
                                loadProgress();
                            }
                        });
                    }
                });
                card.addView(markBtn);
            }

            container.addView(card);
        }

        tvUnlockMain.setVisibility(attendedCount >= 7 ? View.VISIBLE : View.GONE);
        Fonts.applyRecursively(this, container);
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
