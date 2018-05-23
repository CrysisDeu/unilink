package com.teamxod.unilink;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

public class MyFragment extends Fragment {

    private ImageView mProfilePic;
    private LinearLayout my_favorite;
    private LinearLayout my_post;
    private LinearLayout changePassword;
    private TextView logout;
    private LinearLayout changeProfile;
    private LinearLayout preference;


    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my, container, false); // get the GUI

        mAuth = FirebaseAuth.getInstance();
        // change name
        final TextView mName = (TextView) layout.findViewById(R.id.name);
        mProfilePic = (ImageView) layout.findViewById(R.id.profile_pic);
        if(mAuth != null && mAuth.getCurrentUser() != null) {
            mName.setText(mAuth.getCurrentUser().getDisplayName());
            Uri mPhoto = mAuth.getCurrentUser().getPhotoUrl();
            if (mPhoto != null) {
                Glide.with(this)
                        .load(mPhoto)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mProfilePic);
            }

        }

        // my :
        my_favorite = (LinearLayout) layout.findViewById(R.id.my_favorite_l);
        my_post = (LinearLayout) layout.findViewById(R.id.my_post_l);
        changePassword = (LinearLayout) layout.findViewById(R.id.change_password_l);
        logout = (TextView) layout.findViewById(R.id.logout);
        changeProfile = (LinearLayout) layout.findViewById(R.id.change_profile_l);
        preference = (LinearLayout) layout.findViewById(R.id.set_preference_l);
        /*View my_favorite = layout.findViewById(R.id.my_favorite);
        View my_post = layout.findViewById(R.id.my_post);
        View changePassword = layout.findViewById(R.id.changePassword);
        View logout = layout.findViewById(R.id.logout);
        View preference = layout.findViewById(R.id.setPreference);*/

        // GoogleApiClient to logout
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()) //Use app context to prevent leaks using activity
                //.enableAutoManage(this /* FragmentActivity */, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


        mProfilePic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getActivity(), Profile.class);
                i.putExtra("uid", mAuth.getCurrentUser().getUid());
                startActivity(i);
            }
        });

        // set on click
        my_favorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getActivity(), My_favorite.class);
                startActivity(i);
            }
        });

        my_post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getActivity(), My_post.class);
                startActivity(i);
            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getActivity(), my_change_profile.class);
                startActivity(i);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (!getLoginmethod()) {
                    Intent i = new Intent(getActivity(), My_changePassword.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(),"You can only change your password if you sign in using email.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        preference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),My_preference.class);
                startActivity(i);
        }
    });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FirebaseAuth.getInstance().signOut();
                if (mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                }
                Intent reset = new Intent(getActivity(), StartActivity.class);
                reset.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(reset);
            }
        });

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private boolean getLoginmethod() {
        if(mAuth != null && mAuth.getCurrentUser() != null) {
            for (UserInfo user : mAuth.getCurrentUser().getProviderData()) {
                Log.d("TAG",user.getProviderId());
                if (user.getProviderId().equals("google.com")) { // google login
                    return true;
                }
            }
            return false;
        } else {
            return true; // not login
        }
    }
}
