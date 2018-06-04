package com.teamxod.unilink.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.teamxod.unilink.R;
import com.teamxod.unilink.user.Profile;

public class ChatHolder extends RecyclerView.ViewHolder {
    //private final TextView mNameField;
    private final TextView mTextField;
    //    private final FrameLayout mLeftArrow;
//    private final FrameLayout mRightArrow;
    private final RelativeLayout mMessageContainer;
    private final LinearLayout mMessage;
    private final int mGreen300;
    private final int mGray300;
    private final ImageView mLeftImage;
    private final ImageView mRightImage;

    public ChatHolder(View itemView) {
        super(itemView);
        mTextField = itemView.findViewById(R.id.message_text);
//        mLeftArrow = itemView.findViewById(R.id.left_arrow);
//        mRightArrow = itemView.findViewById(R.id.right_arrow);
        mMessageContainer = itemView.findViewById(R.id.message_container);
        mMessage = itemView.findViewById(R.id.message);
        mGreen300 = ContextCompat.getColor(itemView.getContext(), R.color.material_green_300);
        mGray300 = ContextCompat.getColor(itemView.getContext(), R.color.material_gray_300);
        mLeftImage = itemView.findViewById(R.id.left_image);
        mRightImage = itemView.findViewById(R.id.right_image);
    }

    public void bind(Chat chat) {
        setText(chat.getmMessage());

        // Check if the messages is sent by current user
        setIsSender(is_sender(chat));
    }

    public boolean is_sender(Chat chat) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null && chat.getmUid().equals(currentUser.getUid());
    }


    public void setText(String text) {
        mTextField.setText(text);
    }

    // if the message is sent by current user, the message should appear at right
    public void setIsSender(boolean isSender) {
        final int color;
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 24, 0, 0);
        if (isSender) {
            color = mGreen300;
//            mLeftArrow.setVisibility(View.GONE);
            mLeftImage.setVisibility(View.GONE);
//            mRightArrow.setVisibility(View.GONE);
            mRightImage.setVisibility(View.VISIBLE);
            mMessageContainer.setGravity(Gravity.END);
            params.addRule(RelativeLayout.START_OF, R.id.right_image);
            mMessage.setLayoutParams(params);
        } else {
            color = mGray300;
//            mLeftArrow.setVisibility(View.GONE);
            mLeftImage.setVisibility(View.VISIBLE);
//            mRightArrow.setVisibility(View.GONE);
            mRightImage.setVisibility(View.GONE);
            mMessageContainer.setGravity(Gravity.START);
            params.addRule(RelativeLayout.END_OF, R.id.left_image);
            mMessage.setLayoutParams(params);
        }

        ((GradientDrawable) mMessage.getBackground()).setColor(color);
//        ((RotateDrawable) mLeftArrow.getBackground()).getDrawable()
//                .setColorFilter(color, PorterDuff.Mode.SRC);
//        ((RotateDrawable) mRightArrow.getBackground()).getDrawable()
//                .setColorFilter(color, PorterDuff.Mode.SRC);
    }

    public void setImage(final Chat chat, final Context context, Uri mOtherPhoto, Uri mPhoto) {
        ImageView img;
        Uri uri;
        if (is_sender(chat)) {
            img = mRightImage;
            uri = mPhoto;
        } else {
            img = mLeftImage;
            uri = mOtherPhoto;
        }
        Glide.with(context)
                .load(uri)
                .apply(RequestOptions.circleCropTransform())
                .into(img);

        // click avatar to show profile
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Profile.class);
                i.putExtra("uid", chat.getmUid());
                context.startActivity(i);
            }
        });
    }
}
