package com.example.kidslearningapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private Button btnLogout, btnFingerprint, btnVideosForKids, btnTriviaQuiz;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private SharedPreferences sharedPreferences;
    private boolean fingerprintEnrolled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is logged in
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Initialize buttons
        btnLogout = findViewById(R.id.btnLogout);
        btnFingerprint = findViewById(R.id.btnFingerprint);
        btnVideosForKids = findViewById(R.id.btnVideosForKids);
        btnTriviaQuiz = findViewById(R.id.btnTriviaQuiz);

        // Check biometric support
        checkBiometricSupport();

        // Navigation Buttons
        btnVideosForKids.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, VideosActivity.class);
            startActivity(intent);
        });

        btnTriviaQuiz.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TriviaActivity.class);
            startActivity(intent);
        });

        // Logout Button
        btnLogout.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void checkBiometricSupport() {
        BiometricManager biometricManager = BiometricManager.from(this);
        int biometricStatus = biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL
        );

        if (biometricStatus == BiometricManager.BIOMETRIC_SUCCESS) {
            setupBiometricAuthentication();
        } else if (biometricStatus == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
            Toast.makeText(this, "No biometric or screen lock enrolled.", Toast.LENGTH_SHORT).show();
            btnFingerprint.setOnClickListener(view -> {
                Intent intent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                intent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL);
                startActivity(intent);
                fingerprintEnrolled = true;
            });
        } else {
            Toast.makeText(this, "Authentication not available.", Toast.LENGTH_SHORT).show();
            btnFingerprint.setEnabled(false);
        }
    }

    private void setupBiometricAuthentication() {
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(MainActivity.this, "Authentication successful!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authenticate")
                .setSubtitle("Use fingerprint or device password")
                .setDeviceCredentialAllowed(true)  // ✅ Allows PIN, password, or pattern as fallback
                .build();

        btnFingerprint.setOnClickListener(view -> biometricPrompt.authenticate(promptInfo));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fingerprintEnrolled) {
            BiometricManager biometricManager = BiometricManager.from(this);
            if (biometricManager.canAuthenticate(
                    BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL
            ) == BiometricManager.BIOMETRIC_SUCCESS) {
                Toast.makeText(this, "Fingerprint successfully added!", Toast.LENGTH_LONG).show();
                setupBiometricAuthentication();
                fingerprintEnrolled = false;
            }
        }
    }
}
