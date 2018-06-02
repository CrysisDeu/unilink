package com.teamxod.unilink;


import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MeFragment extends Fragment {

    private ImageView mProfilePic;
    private TextView mName;
    private LinearLayout my_favorite;
    private LinearLayout my_post;
    private LinearLayout changePassword;
    private TextView logout;
    private LinearLayout changeProfile;
    private LinearLayout preference;


    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String uid;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_me, container, false); // get the GUI

        mName = layout.findViewById(R.id.name);
        mProfilePic = layout.findViewById(R.id.profile_pic);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        if(mAuth != null && mAuth.getCurrentUser() != null) {
        uid = mAuth.getCurrentUser().getUid();
        DatabaseReference mUserReference = mDatabase.child("Users").child(uid);
        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getContext() == null) {
                    return;
                }
                user = dataSnapshot.getValue(User.class);
                setProfileUI(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        // not logged in
        } else {
            Intent reset = new Intent(getActivity(), SplashActivity.class);
            reset.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(reset);
        }



        // my :
        my_favorite = layout.findViewById(R.id.my_favorite_l);
        my_post = layout.findViewById(R.id.my_post_l);
        changePassword = layout.findViewById(R.id.change_password_l);
        logout = layout.findViewById(R.id.logout);
        changeProfile = layout.findViewById(R.id.change_profile_l);
        preference = layout.findViewById(R.id.set_preference_l);

        // GoogleApiClient to logout
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()) //Use app context to prevent leaks using activity
                //.enableAutoManage(this /* FragmentActivity */, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


        mProfilePic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getActivity(), Profile.class);
                i.putExtra("uid", uid);
                startActivity(i);
            }
        });

        // set on click
        my_favorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                /*Fragment fragment = new MyFavoriteActivity();
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container,fragment,"favorite")
                        .commit();*/

                // TODO: CHANGE IT ; FOR TESTING
                Intent i = new Intent(getActivity(), MyFavoriteActivity.class);
                startActivity(i);
            }
        });

        my_post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Fragment fragment = new MyPostFragment();
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container,fragment,"my_post")
                        .commit();
            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getActivity(), MyChangeProfileActivity.class);
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
                Intent i = new Intent(getActivity(),MyPreferenceActivity.class);
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

    private void setProfileUI(User user) {
        mName.setText(user.getName());
        Uri mPhoto = Uri.parse(user.getPicture());
        if (mPhoto != null) {
            Glide.with(getActivity())
                    .load(mPhoto)
                    .apply(RequestOptions.circleCropTransform())
                    .into(mProfilePic);
        }
    }
}
