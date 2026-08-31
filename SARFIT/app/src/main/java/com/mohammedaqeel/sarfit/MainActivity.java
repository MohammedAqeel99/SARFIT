package com.mohammedaqeel.sarfit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AttendanceManager attendanceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        attendanceManager = new AttendanceManager(this);

        // Check if user has completed 7 days attendance
        if (!attendanceManager.isMainUnlocked()) {
            setupBeginnerUI();
        } else {
            setupMainUI();
        }
    }

    private void setupBeginnerUI() {
        setContentView(R.layout.activity_beginner);

        TextView tvStatus = findViewById(R.id.tv_attendance_status);
        Button btnAttendance = findViewById(R.id.btn_mark_attendance);

        updateAttendanceText(tvStatus);

        btnAttendance.setOnClickListener(v -> {
            boolean marked = attendanceManager.markAttendance();
            if (marked) {
                Toast.makeText(this, "Attendance Marked!", Toast.LENGTH_SHORT).show();
                updateAttendanceText(tvStatus);

                // If this check-in completed 7 days, refresh layout to main interface
                if (attendanceManager.isMainUnlocked()) {
                    Toast.makeText(this, "Main Interface Unlocked!", Toast.LENGTH_LONG).show();
                    setupMainUI();
                }
            } else {
                Toast.makeText(this, "Already marked today!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAttendanceText(TextView tvStatus) {
        tvStatus.setText("Attendance: " + attendanceManager.getAttendanceCount() + " / 7 Days");
    }

    private void setupMainUI() {
        // Load main weekly layout
        setContentView(R.layout.activity_main);

        // Warmup Button / Action Handler (Replace btn_warmup with your view ID if different)
        View warmupBtn = findViewById(R.id.btn_warmup);
        if (warmupBtn != null) {
            warmupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, WarmupActivity.class));
                }
            });
        }

        LinearLayout container = findViewById(R.id.dayListContainer);
        if (container != null) {
            String[] days = DaySelectionManager.DAYS;

            for (int idx = 0; idx < days.length; idx++) {
                View card = buildDayCard(days[idx]);
                container.addView(card);
                animateCardIn(card, idx);
            }
        }

        Fonts.applyRecursively(this, findViewById(android.R.id.content));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (attendanceManager != null && attendanceManager.isMainUnlocked()) {
            // Refresh day cards in case the user changed a combination and came back
            LinearLayout container = findViewById(R.id.dayListContainer);
            if (container != null && container.getChildCount() > 0) {
                container.removeAllViews();
                String[] days = DaySelectionManager.DAYS;
                for (int idx = 0; idx < days.length; idx++) {
                    container.addView(buildDayCard(days[idx]));
                }
            }
        }
    }

    private void animateCardIn(View view, int index) {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0.25f, Animation.RELATIVE_TO_SELF, 0f);
        AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
        set.addAnimation(translate);
        set.addAnimation(alpha);
        set.setDuration(320);
        set.setStartOffset(index * 60L);
        view.startAnimation(set);
    }

    private View buildDayCard(final String dayName) {
        final List<String> muscles = DaySelectionManager.getMusclesForDay(this, dayName);
        final boolean isRest = muscles.isEmpty();

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 14);
        card.setLayoutParams(lp);
        card.setPadding(24, 22, 24, 22);
        card.setBackgroundResource(R.drawable.gradient_card);
        card.setGravity(Gravity.CENTER_VERTICAL);

        TextView tvDay = new TextView(this);
        tvDay.setText(dayName);
        tvDay.setTextColor(Color.WHITE);
        tvDay.setTextSize(19);
        tvDay.setTypeface(tvDay.getTypeface(), android.graphics.Typeface.BOLD);
        card.addView(tvDay);

        TextView tvFocus = new TextView(this);
        tvFocus.setText(DaySelectionManager.labelFor(muscles));
        tvFocus.setTextColor(Color.parseColor(isRest ? "#777777" : "#39FF14"));
        tvFocus.setTextSize(14);
        LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flp.topMargin = 4;
        tvFocus.setLayoutParams(flp);
        card.addView(tvFocus);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DayDetailActivity.class);
                intent.putExtra("dayName", dayName);
                startActivity(intent);
            }
        });

        return card;
    }
}
