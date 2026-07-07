package com.mohammedaqeel.sarfit;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DayDetailActivity extends AppCompatActivity {

    private WorkoutDay day;
    private BodyModelView bodyModelView;
    private TextView tvViewLabel;
    private LinearLayout weekTabsContainer;
    private LinearLayout exerciseListContainer;
    private int selectedWeek = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);

        int dayIndex = getIntent().getIntExtra("dayIndex", 0);
        day = WorkoutData.getSchedule().get(dayIndex);

        TextView tvTitle = findViewById(R.id.tvDayTitle);
        TextView tvFocus = findViewById(R.id.tvDayFocus);
        TextView tvSetsReps = findViewById(R.id.tvSetsRepsInfo);
        FrameLayout bodyContainer = findViewById(R.id.bodyModelContainer);
        final TextView tvFlipView = findViewById(R.id.tvFlipView);
        tvViewLabel = findViewById(R.id.tvViewLabel);
        weekTabsContainer = findViewById(R.id.weekTabsContainer);
        exerciseListContainer = findViewById(R.id.exerciseListContainer);

        tvTitle.setText(day.dayName);
        tvFocus.setText(day.focus);
        tvSetsReps.setText("Compound lifts: 4 sets x 6-10 reps, rest 2-3 min  |  Isolation: 3 sets x 10-15 reps, rest 60-90 sec");

        bodyModelView = new BodyModelView(this);
        bodyContainer.addView(bodyModelView, 0, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        // Default: highlight the first muscle section of the day
        if (!day.weeks.isEmpty() && !day.weeks.get(0).sections.isEmpty()) {
            bodyModelView.setMuscleGroup(day.weeks.get(0).sections.get(0).muscleName);
        }
        tvViewLabel.setText(bodyModelView.isShowingBack() ? "Back" : "Front");

        tvFlipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodyModelView.toggleView();
                tvViewLabel.setText(bodyModelView.isShowingBack() ? "Back" : "Front");
            }
        });

        buildWeekTabs();
        renderWeek(0);

        Fonts.applyRecursively(this, findViewById(android.R.id.content));
    }

    private void buildWeekTabs() {
        weekTabsContainer.removeAllViews();
        List<WeekPlan> weeks = day.weeks;

        for (int w = 0; w < weeks.size(); w++) {
            final int weekIdx = w;
            TextView tab = new TextView(this);
            tab.setText("Week " + weeks.get(w).weekNumber);
            tab.setTextColor(Color.WHITE);
            tab.setTextSize(13);
            tab.setGravity(Gravity.CENTER);
            tab.setPadding(20, 14, 20, 14);
            tab.setBackgroundColor(weekIdx == selectedWeek ? Color.parseColor(accentFor(day.bodyHighlight)) : Color.parseColor("#1A1A1E"));
            if (weekIdx == selectedWeek) tab.setTextColor(Color.BLACK);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            lp.setMargins(w == 0 ? 0 : 6, 0, 0, 0);
            tab.setLayoutParams(lp);

            tab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedWeek = weekIdx;
                    buildWeekTabs();
                    renderWeek(weekIdx);
                }
            });

            weekTabsContainer.addView(tab);
        }
    }

    private void renderWeek(int weekIdx) {
        exerciseListContainer.removeAllViews();
        WeekPlan plan = day.weeks.get(weekIdx);

        for (MuscleSection section : plan.sections) {
            TextView header = new TextView(this);
            header.setText(section.muscleName);
            header.setTextColor(Color.parseColor(accentFor(day.bodyHighlight)));
            header.setTextSize(16);
            header.setTypeface(header.getTypeface(), Typeface.BOLD);
            LinearLayout.LayoutParams hlp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            hlp.topMargin = 18;
            hlp.bottomMargin = 8;
            header.setLayoutParams(hlp);
            exerciseListContainer.addView(header);

            for (Exercise ex : section.exercises) {
                exerciseListContainer.addView(buildExerciseRow(ex, section.muscleName));
            }
        }
        Fonts.applyRecursively(this, exerciseListContainer);
    }

    private View buildExerciseRow(final Exercise ex, final String muscleName) {
        final LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(18, 14, 18, 14);
        row.setBackgroundResource(R.drawable.gradient_card);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = 8;
        row.setLayoutParams(lp);

        LinearLayout headerRow = new LinearLayout(this);
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setGravity(Gravity.CENTER_VERTICAL);
        headerRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout nameCol = new LinearLayout(this);
        nameCol.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams nameColLp = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        nameCol.setLayoutParams(nameColLp);

        TextView name = new TextView(this);
        name.setText(ex.name);
        name.setTextColor(Color.WHITE);
        name.setTextSize(15);
        nameCol.addView(name);

        TextView details = new TextView(this);
        details.setText(ex.getSetsReps());
        details.setTextColor(Color.parseColor("#9E9E9E"));
        details.setTextSize(12);
        LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dlp.topMargin = 2;
        details.setLayoutParams(dlp);
        nameCol.addView(details);

        headerRow.addView(nameCol);

        final TextView arrow = new TextView(this);
        arrow.setText("\u25BE"); // small down-caret
        arrow.setTextColor(Color.parseColor(accentFor(day.bodyHighlight)));
        arrow.setTextSize(16);
        headerRow.addView(arrow);

        row.addView(headerRow);

        final TextView expandedInfo = new TextView(this);
        expandedInfo.setTextColor(Color.parseColor("#CFCFCF"));
        expandedInfo.setTextSize(12.5f);
        expandedInfo.setLineSpacing(4f, 1f);
        LinearLayout.LayoutParams elp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        elp.topMargin = 12;
        expandedInfo.setLayoutParams(elp);

        String equipTag = ex.getEquipment();
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append("Equipment: ").append(equipTag).append("\n");
        ssb.append(ex.getDescription(muscleName));
        expandedInfo.setText(ssb);
        expandedInfo.setVisibility(View.GONE);
        row.addView(expandedInfo);

        final TextView watchBtn = new TextView(this);
        watchBtn.setText("\u25B6  Watch proper form");
        watchBtn.setTextColor(Color.parseColor("#00F0FF"));
        watchBtn.setTextSize(13);
        watchBtn.setTypeface(watchBtn.getTypeface(), android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams wlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        wlp.topMargin = 14;
        watchBtn.setLayoutParams(wlp);
        watchBtn.setVisibility(View.GONE);
        watchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DayDetailActivity.this, VideoActivity.class);
                intent.putExtra("exerciseName", ex.name);
                startActivity(intent);
            }
        });
        row.addView(watchBtn);

        headerRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean expanding = expandedInfo.getVisibility() == View.GONE;
                expandedInfo.setVisibility(expanding ? View.VISIBLE : View.GONE);
                watchBtn.setVisibility(expanding ? View.VISIBLE : View.GONE);
                arrow.animate().rotation(expanding ? 180f : 0f).setDuration(200).start();
                bodyModelView.setMuscleGroup(muscleName);
                tvViewLabel.setText(bodyModelView.isShowingBack() ? "Back" : "Front");
            }
        });

        return row;
    }

    private String accentFor(String group) {
        switch (group) {
            case "chest_delts": return "#39FF14";
            case "back_rear_delts": return "#00F0FF";
            case "arms_legs_core": return "#FF2E9F";
            case "cardio": return "#FFD700";
            default: return "#888888";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bodyModelView != null) bodyModelView.stopPulse();
    }
}
