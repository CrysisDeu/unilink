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
import com.bumptech.glide.request.RequestOptions;
import com.teamxod.unilink.user.User;

import java.util.ArrayList;

public class HouseMateAdapter extends RecyclerView.Adapter<HouseMateAdapter.SimpleProfileViewHolder> {

    private ArrayList<User> users;

    private Context context;

    HouseMateAdapter(Context context, ArrayList<User> users){
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public SimpleProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.house_roommate_list_item, parent, false);

        return new SimpleProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleProfileViewHolder holder, int position) {

        User user = users.get(position);

        holder.yearTextView.setText(user.getYearGraduate());

        holder.nameTextView.setText(user.getName());

        Glide.with(context)
                .load(user.getPicture())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.pictureImageView);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class SimpleProfileViewHolder extends RecyclerView.ViewHolder {
        ImageView pictureImageView;
        TextView nameTextView;
        TextView yearTextView;

        SimpleProfileViewHolder (View itemView) {
            super(itemView);
            pictureImageView = (ImageView) itemView.findViewById(R.id.user_profile_image);
            nameTextView = (TextView) itemView.findViewById(R.id.user_name);
            yearTextView = (TextView) itemView.findViewById(R.id.user_school_year);
        }
    }

}
