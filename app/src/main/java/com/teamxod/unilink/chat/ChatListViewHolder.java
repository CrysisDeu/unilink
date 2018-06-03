package com.teamxod.unilink.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.teamxod.unilink.R;

public class ChatListViewHolder extends RecyclerView.ViewHolder {

    public View mView;
    private TextView userStatusView;
    private TextView userNameView;
    public ImageView userImageView;
    private TextView chatTimeView;

    ChatListViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
        userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
        userNameView = mView.findViewById(R.id.user_single_name);
        userImageView = (ImageView) mView.findViewById(R.id.user_single_image);
        chatTimeView = mView.findViewById(R.id.time_view);

    }

    public void setMessage(String message){

        userStatusView.setText(message);

    }

    public void setName(String name){

        userNameView.setText(name);
    }

    public String  getChatTimeView() {
        return chatTimeView.getText().toString();
    }

    public void setChatTimeView(String time) {
        this.chatTimeView.setText(time);
    }

    public void setUserImage(String thumb_image, Context ctx){

        if(ctx == null) {
            return;
        }

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