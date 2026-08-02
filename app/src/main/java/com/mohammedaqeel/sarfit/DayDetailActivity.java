package com.mohammedaqeel.sarfit;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DayDetailActivity extends AppCompatActivity {

    private String dayName;
    private List<String> muscles;
    private Map<String, WeekPlan[]> pools;
    private BodyModelView bodyModelView;
    private TextView tvViewLabel;
    private LinearLayout weekTabsContainer;
    private LinearLayout exerciseListContainer;
    private ImageView ivBackground;
    private int selectedWeek = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);

        dayName = getIntent().getStringExtra("dayName");
        if (dayName == null) dayName = "Monday";
        pools = MasterMuscleData.getPools();

        TextView tvTitle = findViewById(R.id.tvDayTitle);
        TextView tvFocus = findViewById(R.id.tvDayFocus);
        TextView tvSetsReps = findViewById(R.id.tvSetsRepsInfo);
        FrameLayout bodyContainer = findViewById(R.id.bodyModelContainer);
        ivBackground = findViewById(R.id.ivDayBackground);
        final TextView tvFlipView = findViewById(R.id.tvFlipView);
        final TextView tvChangeCombo = findViewById(R.id.tvChangeCombo);
        tvViewLabel = findViewById(R.id.tvViewLabel);
        weekTabsContainer = findViewById(R.id.weekTabsContainer);
        exerciseListContainer = findViewById(R.id.exerciseListContainer);

        tvTitle.setText(dayName);
        tvSetsReps.setText("Compound lifts: 4 sets x 6-10 reps, rest 2-3 min  |  Isolation: 3 sets x 10-15 reps, rest 60-90 sec");

        bodyModelView = new BodyModelView(this);
        bodyContainer.addView(bodyModelView, 0, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        tvFlipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodyModelView.toggleView();
                tvViewLabel.setText(bodyModelView.isShowingBack() ? "Back" : "Front");
            }
        });

        tvChangeCombo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCombinationPicker();
            }
        });

        loadDayState();
        Fonts.applyRecursively(this, findViewById(android.R.id.content));
    }

    private void loadDayState() {
        muscles = DaySelectionManager.getMusclesForDay(this, dayName);
        TextView tvFocus = findViewById(R.id.tvDayFocus);
        tvFocus.setText(DaySelectionManager.labelFor(muscles));

        bodyModelView.setMuscleGroups(muscles);
        tvViewLabel.setText(bodyModelView.isShowingBack() ? "Back" : "Front");

        updateBackground();
        buildWeekTabs();
        renderWeek(selectedWeek);
    }

    private void updateBackground() {
        int bg;
        if (muscles.contains("Chest") || muscles.contains("Shoulders")) {
            bg = R.drawable.bg_theme_push;
        } else if (muscles.contains("Back") || muscles.contains("Triceps")) {
            bg = R.drawable.bg_theme_pull;
        } else if (muscles.contains("Cardio") && muscles.size() == 1) {
            bg = R.drawable.bg_theme_cardio;
        } else if (!muscles.isEmpty()) {
            bg = R.drawable.bg_theme_arms_legs;
        } else {
            bg = R.drawable.bg_theme_cardio;
        }
        ivBackground.setImageResource(bg);
    }

    private void showCombinationPicker() {
        final String[] allMuscles = MasterMuscleData.ALL_MUSCLES;
        final boolean[] checked = new boolean[allMuscles.length];
        for (int i = 0; i < allMuscles.length; i++) {
            checked[i] = muscles.contains(allMuscles[i]);
        }

        new AlertDialog.Builder(this)
                .setTitle("Pick muscles for " + dayName)
                .setMultiChoiceItems(allMuscles, checked, new android.content.DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which, boolean isChecked) {
                        checked[which] = isChecked;
                    }
                })
                .setPositiveButton("Save", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        List<String> selected = new ArrayList<>();
                        for (int i = 0; i < allMuscles.length; i++) {
                            if (checked[i]) selected.add(allMuscles[i]);
                        }
                        DaySelectionManager.setMusclesForDay(DayDetailActivity.this, dayName, selected);
                        selectedWeek = 0;
                        loadDayState();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void buildWeekTabs() {
        weekTabsContainer.removeAllViews();

        for (int w = 0; w < 4; w++) {
            final int weekIdx = w;
            TextView tab = new TextView(this);
            tab.setText("Week " + (w + 1));
            tab.setTextColor(Color.WHITE);
            tab.setTextSize(13);
            tab.setGravity(Gravity.CENTER);
            tab.setPadding(20, 14, 20, 14);
            tab.setBackgroundColor(weekIdx == selectedWeek ? Color.parseColor("#39FF14") : Color.parseColor("#1A1A1E"));
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

        if (muscles.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No muscles set for " + dayName + " yet. Tap \"Change Muscle Combination\" above to build this day.");
            empty.setTextColor(Color.parseColor("#AAAAAA"));
            empty.setTextSize(13);
            exerciseListContainer.addView(empty);
            return;
        }

        for (String muscle : muscles) {
            WeekPlan[] weekPlans = pools.get(muscle);
            if (weekPlans == null || weekIdx >= weekPlans.length) continue;
            WeekPlan plan = weekPlans[weekIdx];

            for (MuscleSection section : plan.sections) {
                TextView header = new TextView(this);
                header.setText(section.muscleName);
                header.setTextColor(Color.parseColor("#39FF14"));
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
        arrow.setText("\u25BE");
        arrow.setTextColor(Color.parseColor("#39FF14"));
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
        watchBtn.setTypeface(watchBtn.getTypeface(), Typeface.BOLD);
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
                bodyModelView.setMuscleGroups(java.util.Collections.singletonList(muscleName));
                tvViewLabel.setText(bodyModelView.isShowingBack() ? "Back" : "Front");
            }
        });

        return row;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bodyModelView != null) bodyModelView.stopPulse();
    }
}
