package com.mohammedaqeel.sarfit;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 101;
    private static final String PREFS_PROFILE = "sarfit_profile";

    private ObjectAnimator pulseAnimator;
    private String username;
    private ImageView ivProfilePicture;
    private TextView tvProfileAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Fonts.applyRecursively(this, findViewById(android.R.id.content));

        username = getIntent().getStringExtra("username");
        if (username == null || username.trim().isEmpty()) {
            username = "Athlete";
        }

        TextView tvName = findViewById(R.id.tvProfileName);
        tvProfileAvatar = findViewById(R.id.tvProfileAvatar);
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        FrameLayout avatarContainer = findViewById(R.id.avatarContainer);

        tvName.setText(username);
        tvProfileAvatar.setText(username.substring(0, 1).toUpperCase());

        // Circular clip for the profile picture (no extra libraries needed)
        ivProfilePicture.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        });
        ivProfilePicture.setClipToOutline(true);

        loadSavedProfilePicture();

        avatarContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        // Streak stats
        TextView tvStreakDays = findViewById(R.id.tvStreakDays);
        TextView tvTotalDays = findViewById(R.id.tvTotalDays);
        tvStreakDays.setText(String.valueOf(StreakManager.getStreak(this, username)));
        tvTotalDays.setText(String.valueOf(StreakManager.getTotalDays(this, username)));

        pulseAnimator = ObjectAnimator.ofFloat(avatarContainer, "scaleX", 1f, 1.06f, 1f);
        ObjectAnimator pulseY = ObjectAnimator.ofFloat(avatarContainer, "scaleY", 1f, 1.06f, 1f);
        pulseAnimator.setDuration(1400);
        pulseY.setDuration(1400);
        pulseAnimator.setRepeatCount(ValueAnimator.INFINITE);
        pulseY.setRepeatCount(ValueAnimator.INFINITE);
        pulseAnimator.setInterpolator(new LinearInterpolator());
        pulseY.setInterpolator(new LinearInterpolator());
        pulseAnimator.start();
        pulseY.start();

        TextView tvLogout = findViewById(R.id.tvLogout);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(LoginActivity.PREFS_SESSION, MODE_PRIVATE).edit().clear().apply();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (Exception ignored) { }

                getSharedPreferences(PREFS_PROFILE, MODE_PRIVATE)
                        .edit()
                        .putString(username + "_picture_uri", uri.toString())
                        .apply();

                showProfilePicture(uri);
            }
        }
    }

    private void loadSavedProfilePicture() {
        String saved = getSharedPreferences(PREFS_PROFILE, MODE_PRIVATE).getString(username + "_picture_uri", null);
        if (saved != null) {
            try {
                showProfilePicture(Uri.parse(saved));
            } catch (Exception ignored) { }
        }
    }

    private void showProfilePicture(Uri uri) {
        try {
            android.graphics.Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            ivProfilePicture.setImageBitmap(bitmap);
            ivProfilePicture.setVisibility(View.VISIBLE);
            tvProfileAvatar.setVisibility(View.GONE);
        } catch (Exception e) {
            // If loading fails, silently keep the letter avatar
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pulseAnimator != null) pulseAnimator.cancel();
    }
}
