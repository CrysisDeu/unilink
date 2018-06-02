package com.teamxod.unilink;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoommatePostActivity extends AppCompatActivity {

    private String currentUserUid;
    private User currentUser;
    private User roommate;
    private String uid;
    private Recommendation recommend;
    private double matchScore;
    private ArrayList<String> tags;
    private DatabaseReference database;
    private DatabaseReference userReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roommate_post);

        Intent intent = getIntent();

        if(intent.hasExtra("uid")){
            uid = intent.getExtras().getString("uid");
        }else{
            Toast.makeText(this, "User does not exist", Toast.LENGTH_LONG).show();
            finish();
        }
        //get current user and user in post
        //loadData();

        recommend = new Recommendation(uid,uid);
        matchScore = recommend.calculate();
        tags = recommend.getTagList();
        Log.d("check score","****"+matchScore);

        //load tag

    }

    private void loadData() {
        Intent intent = getIntent();

        if(intent.hasExtra("postID")){
            uid = intent.getExtras().getString("postID");
        }else{
            Toast.makeText(this, "People does not exist", Toast.LENGTH_LONG).show();
            finish();
        }

        database = FirebaseDatabase.getInstance().getReference();

        //current user
        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userReference = database.child("Users").child(currentUserUid);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                currentUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //roommate user
        userReference = database.child("Users").child(uid);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                roommate = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }


}
