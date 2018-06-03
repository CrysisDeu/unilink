package com.teamxod.unilink.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamxod.unilink.R;

import java.util.ArrayList;

public class MyFavoriteActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private ArrayList<String> postList;

    private MyFavoriteAdapter favoriteListAdapter;

    private RecyclerView houseListView;

    private ImageView mBackButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);

        mBackButton = findViewById(R.id.back_button);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        houseListView = findViewById(R.id.my_favorite_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyFavoriteActivity.this, LinearLayoutManager.VERTICAL, false);
        houseListView.setLayoutManager(layoutManager);
        houseListView.setHasFixedSize(true);
        houseListView.addItemDecoration(new DividerItemDecoration(houseListView.getContext(), DividerItemDecoration.VERTICAL));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        DatabaseReference favoriteReference = mDatabase.child("Users").child(uid).child("favorite_houses");
        favoriteReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String postID = snapshot.getValue(String.class);
                    postList.add(postID);
                }
                favoriteListAdapter = new MyFavoriteAdapter(MyFavoriteActivity.this, postList);
                houseListView.setAdapter(favoriteListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

}
