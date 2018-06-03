package com.teamxod.unilink;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


// Firebase
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME = 1000;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();

                // need register or login
                if (currentUser == null) {
                    Intent startIntent = new Intent(SplashActivity.this, AuthenticationActivity.class);
                    startActivity(startIntent);
                    finish();

                // already logged in, go to home page
                } else {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }

            }

            },SPLASH_TIME
        );

    };

}
