package com.victor.firebaseloginsignup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.victor.firebaseloginsignup.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the Firebase authentication instance
        auth = FirebaseAuth.getInstance();

        // get the reference to the ActionBar
        ActionBar actionBar = getSupportActionBar();

        // set the title of the ActionBar
        assert actionBar != null;
        actionBar.setTitle("Homepage");


    }

    // Inflate the menu layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Handle the back button press and logout button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.action_logout) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}