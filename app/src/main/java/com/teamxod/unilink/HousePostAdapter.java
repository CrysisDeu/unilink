package com.teamxod.unilink;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HousePostAdapter extends ArrayAdapter<HousePost> {


    public HousePostAdapter(Context context, List<HousePost> objects){
        super(context,0,objects);
    }


    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = view;


        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.house_list_item, parent, false);
        }

        HousePost my_post= (HousePost) getItem(position);

        TextView typeTextView = (TextView) listItemView.findViewById(R.id.room_type);

        typeTextView.setText(my_post.getRoom_type());

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.room_title);

        titleTextView.setText(my_post.getRoom_title());

        TextView priceTextView = (TextView) listItemView.findViewById(R.id.price);

        priceTextView.setText(my_post.getRoom_price());

        TextView locationTextView = (TextView) listItemView.findViewById(R.id.location);

        locationTextView.setText(my_post.getRoom_location());

        ImageView imageImageView = (ImageView) listItemView.findViewById(R.id.image);


        imageImageView.setImageResource(my_post.getImageResourceId());


        return listItemView;
    }
}
