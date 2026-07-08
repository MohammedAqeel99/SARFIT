package com.mohammedaqeel.sarfit;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // Kept for reference; session state is now managed by FirebaseAuth itself.
    public static final String PREFS_SESSION = "sarfit_session";

    private EditText etUsername, etPassword;
    private TextView tvError;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Fonts.applyRecursively(this, findViewById(android.R.id.content));

        auth = FirebaseAuth.getInstance();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvError = findViewById(R.id.tvError);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvGoSignup = findViewById(R.id.tvGoSignup);

        final TextView tvTogglePassword = findViewById(R.id.tvTogglePassword);
        tvTogglePassword.setOnClickListener(new View.OnClickListener() {
            private boolean visible = false;
            @Override
            public void onClick(View v) {
                visible = !visible;
                int cursor = etPassword.getSelectionStart();
                if (visible) {
                    etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    tvTogglePassword.setText("\uD83D\uDE48");
                } else {
                    etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    tvTogglePassword.setText("\uD83D\uDC41");
                }
                etPassword.setSelection(cursor);
            }
        });

        TextView tvAppName = findViewById(R.id.tvAppName);
        ObjectAnimator pulseX = ObjectAnimator.ofFloat(tvAppName, "scaleX", 1f, 1.05f, 1f);
        ObjectAnimator pulseY = ObjectAnimator.ofFloat(tvAppName, "scaleY", 1f, 1.05f, 1f);
        pulseX.setDuration(1600);
        pulseY.setDuration(1600);
        pulseX.setRepeatCount(ValueAnimator.INFINITE);
        pulseY.setRepeatCount(ValueAnimator.INFINITE);
        pulseX.setInterpolator(new LinearInterpolator());
        pulseY.setInterpolator(new LinearInterpolator());
        pulseX.start();
        pulseY.start();

        // Auto-login if Firebase already has a signed-in session
        FirebaseUser current = auth.getCurrentUser();
        if (current != null) {
            goToMain(displayNameOf(current));
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        tvGoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void attemptLogin() {
        final String username = etUsername.getText().toString().trim();
        String pass = etPassword.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pass)) {
            tvError.setText("Please enter username and password.");
            return;
        }

        String pseudoEmail = toPseudoEmail(username);
        tvError.setText("Signing in...");

        auth.signInWithEmailAndPassword(pseudoEmail, pass)
                .addOnCompleteListener(this, new com.google.android.gms.tasks.OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult> task) {
                        if (task.isSuccessful()) {
                            tvError.setText("");
                            goToMain(username);
                        } else {
                            tvError.setText("Login failed: incorrect username/password, or no account exists.");
                        }
                    }
                });
    }

    static String toPseudoEmail(String username) {
        return username.trim().toLowerCase().replaceAll("[^a-z0-9._-]", "") + "@sarfit.app";
    }

    static String displayNameOf(FirebaseUser user) {
        String name = user.getDisplayName();
        return (name != null && !name.isEmpty()) ? name : "Athlete";
    }

    private void goToMain(String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
