package com.mohammedaqeel.sarfit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = getIntent().getStringExtra("username");
        com.google.firebase.auth.FirebaseUser fbUser = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser != null) {
            StreakManager.recordVisitAndFetch(fbUser.getUid(), new StreakManager.StreakCallback() {
                @Override
                public void onResult(int streak, int total) {
                    // Streak/total are shown on the Profile page; no action needed here.
                }
            });
        }
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText("Welcome back, " + (username != null ? username : "Athlete") + "!");

        TextView tvYou = findViewById(R.id.tvYouButton);
        tvYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        TextView tvWarmup = findViewById(R.id.tvWarmupButton);
        tvWarmup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WarmupActivity.class));
            }
        });

        LinearLayout container = findViewById(R.id.dayListContainer);
        String[] days = DaySelectionManager.DAYS;

        for (int idx = 0; idx < days.length; idx++) {
            View card = buildDayCard(days[idx]);
            container.addView(card);
            animateCardIn(card, idx);
        }

        Fonts.applyRecursively(this, findViewById(android.R.id.content));
    }

    @Override
    protected void onResume() {
        super.onResume();
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
