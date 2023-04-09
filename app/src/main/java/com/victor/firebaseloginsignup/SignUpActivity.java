package com.victor.firebaseloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.victor.firebaseloginsignup.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize view binding
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Hiding the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();

        // Set up click listener for sign up button
        binding.signUpButton.setOnClickListener(view -> {
            // Get email and password from edit texts
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();

            // Check if email and password are not empty
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                // Create a new user with email and password
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Sign up successful, go to main activity
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                finish();
                            } else {
                                // Sign up failed, display error message
                                Toast.makeText(SignUpActivity.this, "Sign up failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // Email or password is empty, display error message
                Toast.makeText(SignUpActivity.this, "Please enter email and password.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up click listener for already signed up text view
        binding.alreadySignedUpTextview.setOnClickListener(view -> {
            // Go to login activity
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }
}