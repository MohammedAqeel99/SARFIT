package com.mohammedaqeel.sarfit;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    public static final String PREFS_USERS = "sarfit_users";
    public static final String PREFS_SESSION = "sarfit_session";

    private EditText etUsername, etPassword;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Fonts.applyRecursively(this, findViewById(android.R.id.content));

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

        // Auto-login if a session already exists
        SharedPreferences session = getSharedPreferences(PREFS_SESSION, MODE_PRIVATE);
        String loggedInUser = session.getString("current_user", null);
        if (loggedInUser != null) {
            goToMain(loggedInUser);
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
        String user = etUsername.getText().toString().trim();
        String pass = etPassword.getText().toString();

        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
            tvError.setText("Please enter username and password.");
            return;
        }

        SharedPreferences users = getSharedPreferences(PREFS_USERS, MODE_PRIVATE);
        String storedPass = users.getString(user, null);

        if (storedPass == null) {
            tvError.setText("No account found. Please sign up first.");
            return;
        }

        if (!storedPass.equals(pass)) {
            tvError.setText("Incorrect password.");
            return;
        }

        tvError.setText("");
        getSharedPreferences(PREFS_SESSION, MODE_PRIVATE)
                .edit()
                .putString("current_user", user)
                .apply();

        goToMain(user);
    }

    private void goToMain(String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
