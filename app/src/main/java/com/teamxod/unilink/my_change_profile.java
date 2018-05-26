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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class my_change_profile extends AppCompatActivity {

    //spinner selector
    private final int GENDER_MALE = 0;
    private final int GENDER_FEMALE = 1;
    private final int GENDER_NOT_DISCLOSE = 2;

    //the most recent graduate the user can choose
    private final int MIN_GRADUATE_YEAR = 2018;

    //Button
    private ImageView mBackButton;
    private CardView mSaveButton;

    //Views
    private ImageView mProfilePic;
    private EditText mEditName;
    private TextView mEmail;
    private Spinner mGenderSpinner;
    private Spinner mYearSpinner;
    private EditText mDescription;

    //Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String uid;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_change_profile);

        //Button
        mBackButton = findViewById(R.id.back_button);
        mSaveButton = findViewById(R.id.save);

        //Views
        mProfilePic = findViewById(R.id.profile_pic);
        mEditName = findViewById(R.id.edit_name);
        mEmail = findViewById(R.id.email);
        mGenderSpinner = findViewById(R.id.gender_spinner);
        mYearSpinner = findViewById(R.id.year_spinner);
        mDescription = findViewById(R.id.description);

        //Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        final DatabaseReference mUserReference = mDatabase.child("Users").child(uid);

        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                mEditName.setText(user.getName());
                Uri mPhoto = (Uri.parse(user.getPicture()));
                Glide.with(my_change_profile.this)
                        .load(mPhoto)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mProfilePic);
                mEmail.setText(mAuth.getCurrentUser().getEmail());
                mGenderSpinner.setSelection(getGenderSelection(user.getGender()));
                mYearSpinner.setSelection(getYearSelection(user.getYearGraduate()));
                mDescription.setText(user.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setName(mEditName.getText().toString());
                user.setGender(mGenderSpinner.getSelectedItem().toString());
                user.setYearGraduate(mYearSpinner.getSelectedItem().toString());
                user.setDescription(mDescription.getText().toString());
                mUserReference.setValue(user);
                finish();
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //}
    }

    private int getGenderSelection(String gender) {
        int genderSelection;
        switch (gender) {
            case "Male":
                genderSelection = GENDER_MALE;
                break;
            case "Female":
                genderSelection = GENDER_FEMALE;
                break;
            default:
                genderSelection = GENDER_NOT_DISCLOSE;
                break;
        }
        return genderSelection;
    }

    private int getYearSelection(String year) {

        Integer yearSelection;
        yearSelection = Integer.parseInt(year) - MIN_GRADUATE_YEAR;
        if (yearSelection < 0) {
            yearSelection = 0;
        }
        return yearSelection;
    }
}
