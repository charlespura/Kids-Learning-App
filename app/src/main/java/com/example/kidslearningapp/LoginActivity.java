    package com.example.kidslearningapp;

    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;
    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.biometric.BiometricPrompt;
    import androidx.biometric.BiometricManager;
    import androidx.core.content.ContextCompat;
    import java.util.concurrent.Executor;

    public class LoginActivity extends AppCompatActivity {

        EditText etUsername, etPassword;
        Button btnLogin, btnRegister, btnFingerprint;
        DatabaseHelper db;
        BiometricPrompt biometricPrompt;
        BiometricPrompt.PromptInfo promptInfo;
        SharedPreferences sharedPreferences;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            db = new DatabaseHelper(this);
            sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

            etUsername = findViewById(R.id.etUsername);
            etPassword = findViewById(R.id.etPassword);
            btnLogin = findViewById(R.id.btnLogin);
            btnRegister = findViewById(R.id.btnRegister);
            btnFingerprint = findViewById(R.id.btnFingerprint);

            btnLogin.setOnClickListener(view -> {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (db.checkUser(username, password)) {
                    loginSuccess(username);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            });

            btnRegister.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

            // Setup Fingerprint/Device Credential Authentication
            setupBiometricAuthentication();
        }

        private void setupBiometricAuthentication() {
            Executor executor = ContextCompat.getMainExecutor(this);
            biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);

                    String username = etUsername.getText().toString().trim();
                    if (!username.isEmpty()) {
                        loginSuccess(username); // ✅ Pass the username
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter your username", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            });

            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Login with Biometrics")
                    .setSubtitle("Use your fingerprint or device password to log in")
                    .setDeviceCredentialAllowed(true)  // ✅ Enables PIN, password, or pattern as fallback
                    .build();

            btnFingerprint.setOnClickListener(view -> biometricPrompt.authenticate(promptInfo));
        }

        private void loginSuccess(String username) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.apply(); // Save login state

            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("username", username);  // 👈 Pass username
            startActivity(intent);
            finish();
        }
    }