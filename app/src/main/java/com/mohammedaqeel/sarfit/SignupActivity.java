package com.mohammedaqeel.sarfit;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Fonts.applyRecursively(this, findViewById(android.R.id.content));

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        final EditText etUser = findViewById(R.id.etNewUsername);
        final EditText etPass = findViewById(R.id.etNewPassword);
        final TextView tvError = findViewById(R.id.tvSignupError);
        Button btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUser.getText().toString().trim();
                String pass = etPass.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pass)) {
                    tvError.setText("Please fill in both fields.");
                    return;
                }
                if (pass.length() < 6) {
                    tvError.setText("Password must be at least 6 characters.");
                    return;
                }

                tvError.setText("Creating account...");
                String pseudoEmail = LoginActivity.toPseudoEmail(username);

                auth.createUserWithEmailAndPassword(pseudoEmail, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    tvError.setText("Sign up failed: username may already be taken.");
                                    return;
                                }
                                FirebaseUser user = auth.getCurrentUser();
                                if (user == null) {
                                    tvError.setText("Unexpected error, please try again.");
                                    return;
                                }
                                UserProfileChangeRequest profileUpdate =
                                        new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                                user.updateProfile(profileUpdate);

                                Map<String, Object> initialData = new HashMap<>();
                                initialData.put("username", username);
                                initialData.put("streak", 0);
                                initialData.put("total", 0);
                                FirebaseFirestore.getInstance().collection("users")
                                        .document(user.getUid())
                                        .set(initialData);

                                tvError.setText("");
                                finish(); // back to Login to sign in
                            }
                        });
            }
        });
    }
}
