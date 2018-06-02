package com.teamxod.unilink;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPostFragment extends Fragment {

    private DatabaseReference database;

    private ArrayList<String> postList;

    private RecyclerView houseListView;

    private MyPostAdapter myPostAdapter;

    ToggleButton mEditButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_my_post, container, false);

        setupButton(layout);

        loadData(layout);

        return layout;
    }

    private void setupButton(View layout) {
        ImageView mBackButton = (ImageView)layout.findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Fragment fragment = getFragmentManager().findFragmentByTag("my_post");
                getFragmentManager()
                        .beginTransaction()
                        .remove(fragment)
                        .commit();
            }
        });

        mEditButton = (ToggleButton) layout.findViewById(R.id.edit_btn);
        mEditButton.setChecked(false);
    }

    private void loadData(View layout) {
        houseListView = (RecyclerView) layout.findViewById(R.id.my_post_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        houseListView.setLayoutManager(layoutManager);
        houseListView.setHasFixedSize(true);
        houseListView.addItemDecoration(new DividerItemDecoration(houseListView.getContext(), DividerItemDecoration.VERTICAL));

        database = FirebaseDatabase.getInstance().getReference();
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
                myPostAdapter = new MyPostAdapter(getActivity(), postList, mEditButton.isChecked());
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
