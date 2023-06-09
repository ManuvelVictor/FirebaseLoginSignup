package com.victor.firebaseloginsignup;

import static com.google.android.gms.auth.api.signin.GoogleSignIn.getClient;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.victor.firebaseloginsignup.databinding.ActivitySignUpBinding;


public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 123;

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

        // Initialize Google SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = getClient(this, gso);

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

        if (auth.getCurrentUser() != null) {
            // User is already signed in, start the Main Activity
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish(); // close the SignUp Activity
        }

        // Set up click listener for already signed up text view
        binding.alreadySignedUpTextview.setOnClickListener(view -> {
            // Go to login activity
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });

        binding.btnGoogle.setOnClickListener(v -> login());
    }

    private void login() {
        Intent LoginIntent = new Intent(mGoogleSignInClient.getSignInIntent());
        startActivityForResult(LoginIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("TAG", "Google Sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "signInWithCredential:success");
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        Toast.makeText(SignUpActivity.this, "Login with Google successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w("TAG", "LoginWithCredential:failure", task.getException());
                    }
                });
    }

}