package com.teamxod.unilink;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class UserPictureAdapter extends RecyclerView.Adapter<UserPictureAdapter.SimpleProfileViewHolder> {

    private ArrayList<User> users;

    public UserPictureAdapter(ArrayList<User> users){
        this.users = users;
    }

    @NonNull
    @Override
    public SimpleProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.simple_profile_item, parent, false);

        return new SimpleProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleProfileViewHolder holder, int position) {
        //User user = (User) getItemId(position);

        User user = users.get(position);

        holder.yearTextView.setText(user.getYearGraduate());

        holder.nameTextView.setText(user.getName());

        holder.pictureImageView.setImageResource(user.getUserPhoto());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class SimpleProfileViewHolder extends RecyclerView.ViewHolder {
        ImageView pictureImageView;
        TextView nameTextView;
        TextView yearTextView;

        public SimpleProfileViewHolder (View itemView) {
            super(itemView);
            pictureImageView = (ImageView) itemView.findViewById(R.id.user_profile_image);
            nameTextView = (TextView) itemView.findViewById(R.id.user_name);
            yearTextView = (TextView) itemView.findViewById(R.id.user_school_year);
        }

    }
}
