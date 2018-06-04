package com.teamxod.unilink.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
import com.teamxod.unilink.chat.RealtimeDbChatActivity;

import java.io.Serializable;

public class Profile extends AppCompatActivity implements Serializable {

    private User user;

    //Views
    private ImageView mProfilePic;
    private TextView mName;
    private TextView mGender;
    private TextView mYear;
    private TextView mDescription;
    private Button mContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ImageView mBackButton = findViewById(R.id.back_button);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mProfilePic = findViewById(R.id.profile_pic);
        mName = findViewById(R.id.name);
        mGender = findViewById(R.id.gender);
        mYear = findViewById(R.id.year);
        mDescription = findViewById(R.id.description);
        mContact = findViewById(R.id.profile_contact);



        Bundle b = getIntent().getExtras();
        String uid = b.getString("uid");
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

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

    private void setProfileUI(final User user) {
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

        mContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(Profile.this, RealtimeDbChatActivity.class);
                chatIntent.putExtra("user_id", uid);
                chatIntent.putExtra("user_name", user.getName());
                startActivity(chatIntent);
            }
        });

        if (uid.equals(mAuth.getCurrentUser().getUid())) {
            mContact.setVisibility(View.GONE);
        }
    }

}
