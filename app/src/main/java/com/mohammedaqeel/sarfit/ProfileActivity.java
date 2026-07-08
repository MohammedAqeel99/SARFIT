package com.mohammedaqeel.sarfit;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 101;

    private ObjectAnimator pulseAnimator;
    private String username;
    private String uid;
    private ImageView ivProfilePicture;
    private TextView tvProfileAvatar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Fonts.applyRecursively(this, findViewById(android.R.id.content));

        db = FirebaseFirestore.getInstance();
        com.google.firebase.auth.FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = fbUser != null ? fbUser.getUid() : null;

        username = getIntent().getStringExtra("username");
        if (username == null || username.trim().isEmpty()) username = "Athlete";

        TextView tvName = findViewById(R.id.tvProfileName);
        tvProfileAvatar = findViewById(R.id.tvProfileAvatar);
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        FrameLayout avatarContainer = findViewById(R.id.avatarContainer);

        tvName.setText(username);
        tvProfileAvatar.setText(username.substring(0, 1).toUpperCase());

        ivProfilePicture.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        });
        ivProfilePicture.setClipToOutline(true);

        final TextView tvStreakDays = findViewById(R.id.tvStreakDays);
        final TextView tvTotalDays = findViewById(R.id.tvTotalDays);

        if (uid != null) {
            // Load streak/total + profile picture from Firestore
            db.collection("users").document(uid).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot doc) {
                            Long streakL = doc.exists() ? doc.getLong("streak") : 0L;
                            Long totalL = doc.exists() ? doc.getLong("total") : 0L;
                            tvStreakDays.setText(String.valueOf(streakL != null ? streakL : 0));
                            tvTotalDays.setText(String.valueOf(totalL != null ? totalL : 0));

                            String pictureBase64 = doc.exists() ? doc.getString("profilePictureBase64") : null;
                            if (pictureBase64 != null) {
                                try {
                                    byte[] bytes = Base64.decode(pictureBase64, Base64.DEFAULT);
                                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    ivProfilePicture.setImageBitmap(bmp);
                                    ivProfilePicture.setVisibility(View.VISIBLE);
                                    tvProfileAvatar.setVisibility(View.GONE);
                                } catch (Exception ignored) { }
                            }
                        }
                    });
        }

        avatarContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

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
                FirebaseAuth.getInstance().signOut();
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
                    Bitmap original = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    Bitmap resized = Bitmap.createScaledBitmap(original, 240, 240, true);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    resized.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    String base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

                    ivProfilePicture.setImageBitmap(resized);
                    ivProfilePicture.setVisibility(View.VISIBLE);
                    tvProfileAvatar.setVisibility(View.GONE);

                    if (uid != null) {
                        Map<String, Object> update = new HashMap<>();
                        update.put("profilePictureBase64", base64);
                        db.collection("users").document(uid).set(update, SetOptions.merge());
                    }
                } catch (Exception e) {
                    // Keep the letter avatar if anything fails
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pulseAnimator != null) pulseAnimator.cancel();
    }
}
