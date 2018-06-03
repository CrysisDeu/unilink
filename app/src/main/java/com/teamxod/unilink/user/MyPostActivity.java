package com.teamxod.unilink.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamxod.unilink.R;

import java.util.ArrayList;

public class MyPostActivity extends AppCompatActivity {

    private ToggleButton mEditButton;
    private ArrayList<String> postList;
    private RecyclerView houseListView;
    private MyPostAdapter myPostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        ImageView mBackButton = findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mEditButton = findViewById(R.id.edit_btn);

        houseListView = findViewById(R.id.my_post_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyPostActivity.this, LinearLayoutManager.VERTICAL, false);
        houseListView.setLayoutManager(layoutManager);
        houseListView.setHasFixedSize(true);
        houseListView.addItemDecoration(new DividerItemDecoration(houseListView.getContext(), DividerItemDecoration.VERTICAL));

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        DatabaseReference favoriteReference = database.child("Users").child(uid).child("my_house_posts");
        favoriteReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot postID : dataSnapshot.getChildren()) {
                    postList.add(postID.getValue(String.class));
                }
                myPostAdapter = new MyPostAdapter(MyPostActivity.this, postList, mEditButton.isChecked());
                houseListView.setAdapter(myPostAdapter);
                mEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myPostAdapter.setFlag(mEditButton.isChecked());
                        myPostAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
