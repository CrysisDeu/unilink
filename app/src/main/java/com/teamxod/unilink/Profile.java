package com.teamxod.unilink;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

public class Profile extends AppCompatActivity implements Serializable{

    private ImageView mBackButton;
    private String uid;
    private FirebaseAuth mAuth;
    private User user;

    //Views
    private ImageView mProfilePic;
    private TextView mName;
    private TextView mGender;
    private TextView mYear;
    private TextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mBackButton = findViewById(R.id.back_button);
        mAuth = FirebaseAuth.getInstance();
        mProfilePic = findViewById(R.id.profile_pic);
        mName = findViewById(R.id.name);
        mGender = findViewById(R.id.gender);
        mYear = findViewById(R.id.year);
        mDescription = findViewById(R.id.description);

        user = (User) getIntent().getSerializableExtra("USER");

//        Bundle b = getIntent().getExtras();
//        uid = b.getString("uid");






        mBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
