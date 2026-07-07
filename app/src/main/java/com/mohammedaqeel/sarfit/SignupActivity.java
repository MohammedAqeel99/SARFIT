package com.mohammedaqeel.sarfit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Fonts.applyRecursively(this, findViewById(android.R.id.content));

        final EditText etUser = findViewById(R.id.etNewUsername);
        final EditText etPass = findViewById(R.id.etNewPassword);
        final TextView tvError = findViewById(R.id.tvSignupError);
        Button btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = etUser.getText().toString().trim();
                String pass = etPass.getText().toString();

                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                    tvError.setText("Please fill in both fields.");
                    return;
                }
                if (pass.length() < 4) {
                    tvError.setText("Password must be at least 4 characters.");
                    return;
                }

                SharedPreferences users = getSharedPreferences(LoginActivity.PREFS_USERS, MODE_PRIVATE);
                if (users.contains(user)) {
                    tvError.setText("Username already taken.");
                    return;
                }

                users.edit().putString(user, pass).apply();
                tvError.setText("");
                finish(); // back to Login to sign in
            }
        });
    }
}
