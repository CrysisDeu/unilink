package com.teamxod.unilink;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private ArrayList<House> houses;

    Context context;

    FavoriteAdapter(Context context, ArrayList<House> houses){
        this.houses = houses;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.house_list_item_favorite, parent, false);

        return new FavoriteAdapter.FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, int position) {

        House house = houses.get(position);
        String price = "$ " + house.getRooms().get(0).getPrice() + "/Mo From " + house.getStartDate();

        Glide.with(context)
                .load(house.getPictures().get(0))
                .into(holder.housePictureView);

        holder.titleTextView.setText(house.getTitle());
        holder.priceTextView.setText(price);
        holder.addressTextView.setText(house.getLocation());

    }

    @Override
    public int getItemCount() {
        return houses.size();
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
