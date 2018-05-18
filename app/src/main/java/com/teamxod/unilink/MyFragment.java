package com.teamxod.unilink;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

public class MyFragment extends Fragment {

    private TextView my_favorite;
    private TextView my_post;
    private TextView changePassword;
    private TextView logout;
    private TextView changeProfile;
    private TextView preference;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my, container, false); // get the GUI

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // change name
        final TextView mName = (TextView) layout.findViewById(R.id.name);
        final ImageView mProfilePic = (ImageView) layout.findViewById(R.id.profile_pic);
        if(mAuth != null) {
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
        my_favorite = (TextView) layout.findViewById(R.id.my_favorite);
        my_post = (TextView) layout.findViewById(R.id.my_post);
        changePassword = (TextView) layout.findViewById(R.id.changePassword);
        logout = (TextView) layout.findViewById(R.id.logout);
        changeProfile = (TextView) layout.findViewById(R.id.changeProfile);
        preference = (TextView)layout.findViewById(R.id.setPreference);

        // GoogleApiClient to logout
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()) //Use app context to prevent leaks using activity
                //.enableAutoManage(this /* FragmentActivity */, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


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
                Intent i = new Intent(getActivity(), My_changePassword.class);
                startActivity(i);
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

}
