package com.teamxod.unilink;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// Firebase
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME = 2000;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();

    };

}
