package com.teamxod.unilink.user;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamxod.unilink.R;
import com.teamxod.unilink.house.House;
import com.teamxod.unilink.house.HousePostActivity;

import java.util.ArrayList;

public class MyFavoriteAdapter extends RecyclerView.Adapter<MyFavoriteAdapter.FavoriteViewHolder> {

    private ArrayList<String> postList;

    private Context context;

    private DatabaseReference favoriteReference;

    private DatabaseReference housePostReference;

    private RecyclerView mRecyclerView;

    MyFavoriteAdapter(Context context, ArrayList<String> postList) {
        this.postList = postList;
        this.context = context;

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        favoriteReference = database.child("Users").child(uid).child("favorite_houses");
        housePostReference = database.child("House_post");
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MyFavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.house_list_item_favorite, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(v);
                String postID = postList.get(itemPosition);
                Intent myIntent = new Intent(context, HousePostActivity.class);
                myIntent.putExtra("postID", postID);
                context.startActivity(myIntent);
            }
        });

        return new MyFavoriteAdapter.FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteViewHolder holder, final int position) {

        final String postID = postList.get(position);
        housePostReference.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    postList.remove(postID);
                    notifyItemRemoved(holder.getAdapterPosition());
                } else {

                    House house = dataSnapshot.getValue(House.class);
                    String price = "$ " + house.getRooms().get(0).getPrice() + "/Mo From " + house.getStartDate();

                    Glide.with(context)
                            .load(house.getPictures().get(0))
                            .into(holder.housePictureView);

                    holder.titleTextView.setText(house.getTitle());
                    holder.priceTextView.setText(price);
                    holder.addressTextView.setText(house.getLocation());
                    holder.favorite_btn.setChecked(true);
                    holder.favorite_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postID = postList.get(holder.getAdapterPosition());
                            if (!holder.favorite_btn.isChecked()) {
                                postList.remove(postID);
                                favoriteReference.setValue(postList);
                                notifyDataSetChanged();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ToggleButton favorite_btn;
        ImageView housePictureView;
        TextView titleTextView;
        TextView priceTextView;
        TextView addressTextView;

        FavoriteViewHolder(View itemView) {
            super(itemView);
            favorite_btn = itemView.findViewById((R.id.favorite_btn));
            titleTextView = itemView.findViewById(R.id.post_title);
            priceTextView = itemView.findViewById(R.id.post_price);
            addressTextView = itemView.findViewById(R.id.post_address);
            housePictureView = itemView.findViewById(R.id.post_image);
        }
    }
}
