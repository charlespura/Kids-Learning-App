package com.example.kidslearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etNewUsername, etNewPassword;
    Button btnCreateAccount, btnGoToLogin;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        etNewUsername = findViewById(R.id.etNewUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        btnCreateAccount.setOnClickListener(view -> {
            String newUsername = etNewUsername.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();

            if (newUsername.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isRegistered = db.registerUser(newUsername, newPassword);
            if (isRegistered) {
                Toast.makeText(RegisterActivity.this, "Account Created! Please log in.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Username already exists. Try another.", Toast.LENGTH_SHORT).show();
            }
        });

        btnGoToLogin.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }
}
