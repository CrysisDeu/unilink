package com.teamxod.unilink;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

public class my_change_profile extends AppCompatActivity {

    //Button fields
    private ImageView mBackButton;
    private CardView saveProfile;

    //Views
    private ImageView mProfilePic;
    private EditText mEditName;
    private TextView mEmail;
    private Spinner mGenderSpinner;
    private Spinner mYearSpinner;
    private EditText mDescription;

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_change_profile);

        mBackButton = findViewById(R.id.back_button);
        saveProfile = findViewById(R.id.save);

        mProfilePic = findViewById(R.id.profile_pic);
        mEditName = findViewById(R.id.edit_name);
        mEmail = findViewById(R.id.email);
        mGenderSpinner = findViewById(R.id.gender_spinner);
        mYearSpinner = findViewById(R.id.year_spinner);
        mDescription = findViewById(R.id.description);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator stateListAnimator = AnimatorInflater
                    .loadStateListAnimator(this, R.animator.lift_on_touch);
            card.setStateListAnimator(stateListAnimator);
        }*/

        mAuth = FirebaseAuth.getInstance();
        // change name
        final TextView mName = (TextView) findViewById(R.id.name);
        mProfilePic = (ImageView) findViewById(R.id.profile_pic);
        if (mAuth != null && mAuth.getCurrentUser() != null) {
            mEditName.setText(mAuth.getCurrentUser().getDisplayName());
            Uri mPhoto = mAuth.getCurrentUser().getPhotoUrl();
            if (mPhoto != null) {
                Glide.with(this)
                        .load(mPhoto)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mProfilePic);
            }
            mEmail.setText(mAuth.getCurrentUser().getEmail());


            mBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }
}
