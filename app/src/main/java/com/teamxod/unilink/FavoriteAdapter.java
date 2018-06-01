package com.teamxod.unilink;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private ArrayList<String> postList;

    Context context;

    private DatabaseReference favoriteReference;

    private DatabaseReference housePostReference;

    RecyclerView mRecyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    FavoriteAdapter(Context context, ArrayList<String> postList){
        this.postList = postList;
        this.context = context;

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        favoriteReference = database.child("Users").child(uid).child("favorite_houses");
        housePostReference = database.child("House_post");
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.house_list_item_favorite, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(v);
                String postID = postList.get(itemPosition);
                Intent myIntent = new Intent(context, SingleHousePostActivity.class);
                myIntent.putExtra("postID",postID);
                context.startActivity(myIntent);
            }
        });

        return new FavoriteAdapter.FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteAdapter.FavoriteViewHolder holder, int position) {

        String postID = postList.get(position);
        Log.i("debug", postID);
        housePostReference.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                House house = dataSnapshot.getValue(House.class);
                String price = "$ " + house.getRooms().get(0).getPrice() + "/Mo From " + house.getStartDate();

                Glide.with(context)
                        .load(house.getPictures().get(0))
                        .into(holder.housePictureView);

                holder.titleTextView.setText(house.getTitle());
                holder.priceTextView.setText(price);
                holder.addressTextView.setText(house.getLocation());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode()); }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView housePictureView;
        TextView titleTextView;
        TextView priceTextView;
        TextView addressTextView;

        FavoriteViewHolder (View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.post_title);
            priceTextView = (TextView) itemView.findViewById(R.id.post_price);
            addressTextView = (TextView) itemView.findViewById(R.id.post_address);
            housePictureView = (ImageView) itemView.findViewById(R.id.post_image);
        }
    }
}
