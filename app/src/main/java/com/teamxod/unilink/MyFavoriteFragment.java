package com.teamxod.unilink;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyFavoriteFragment extends Fragment {

    private ArrayList<House> houseList;

    private DatabaseReference mDatabase;

    FavoriteAdapter favoriteListAdapter;

    RecyclerView houseListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my_favorite, container, false);

        setupButton(layout);

        setupFirebase(layout);

        return layout;
    }

    private void setupButton(View layout) {
        ImageView mBackButton = layout.findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Fragment fragment = getFragmentManager().findFragmentByTag("favorite");
                getFragmentManager()
                        .beginTransaction()
                        .remove(fragment)
                        .commit();
            }
        });
    }

    private void setupFirebase(View layout) {
        houseList = new ArrayList<>();
        houseListView = (RecyclerView) layout.findViewById(R.id.my_favorite_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        houseListView.setLayoutManager(layoutManager);
        houseListView.setHasFixedSize(true);
        favoriteListAdapter = new FavoriteAdapter(getContext(), houseList);
        houseListView.setAdapter(favoriteListAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth != null && mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();
            DatabaseReference favoriteReference = mDatabase.child("Users").child(uid).child("favorite_houses");
            favoriteReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    houseList.clear();
                    for (DataSnapshot postID : dataSnapshot.getChildren()) {
                        addHouse(postID.getValue(String.class));
                        favoriteListAdapter.notifyDataSetChanged();
                    }
                        //HousePostAdapter adapter = new HousePostAdapter(getActivity(), posts, listView);
                        //listView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }

    private void addHouse(String postID) {
        DatabaseReference postReference = mDatabase.child("House_post").child(postID);

        postReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                houseList.add(dataSnapshot.getValue(House.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
