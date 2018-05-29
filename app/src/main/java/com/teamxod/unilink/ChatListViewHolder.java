package com.teamxod.unilink;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChatListViewHolder extends RecyclerView.ViewHolder {

    public View mView;
    private TextView userStatusView;
    private TextView userNameView;
    private ImageView userImageView;

    ChatListViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
        userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
        userNameView = mView.findViewById(R.id.user_single_name);
        userImageView = (ImageView) mView.findViewById(R.id.user_single_image);

    }

    public void setMessage(String message){

        userStatusView.setText(message);

    }

    public void setName(String name){

        userNameView.setText(name);
    }

    public void setUserImage(String thumb_image, Context ctx){



        Glide.with(ctx)
                .load(thumb_image)
                .apply(RequestOptions.circleCropTransform())
                .into(userImageView);

    }

    public void bind(String name, String message) {
        setName(name);
        setMessage(message);
//
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        setIsSender(currentUser != null && chat.getUid().equals(currentUser.getUid()));
    }


}