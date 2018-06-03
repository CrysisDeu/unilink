package com.teamxod.unilink.user;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamxod.unilink.R;

import java.io.Serializable;

public class Profile extends AppCompatActivity implements Serializable {

    private ImageView mBackButton;
    private String uid;
    private FirebaseAuth mAuth;
    private User user;
    private DatabaseReference mDatabaseRef;

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

        Bundle b = getIntent().getExtras();
        uid = b.getString("uid");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                setProfileUI(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setProfileUI(User user) {
        mName.setText(user.getName());
        Uri mPhoto = Uri.parse(user.getPicture());
        if (mPhoto != null) {
            Glide.with(getApplicationContext())
                    .load(mPhoto)
                    .apply(RequestOptions.circleCropTransform())
                    .into(mProfilePic);
        }
        mGender.setText(user.getGender());
        String graduate = "Year of graduation: " + user.getYearGraduate();
        mYear.setText(graduate);
        mDescription.setText(user.getDescription());
    }

}
