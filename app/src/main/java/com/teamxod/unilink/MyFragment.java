package com.teamxod.unilink;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragment extends Fragment {

    private TextView my_favorite;
    private TextView my_post;
    private TextView changePassword;
    private TextView logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my, container, false); // get the GUI

        // my :
        my_favorite = (TextView) layout.findViewById(R.id.my_favorite);
        my_post = (TextView) layout.findViewById(R.id.my_post);
        changePassword = (TextView) layout.findViewById(R.id.changePassword);
        logout = (TextView) layout.findViewById(R.id.logout);

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

        changePassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getActivity(), My_changePassword.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getActivity(), My_logout.class);
                startActivity(i);
            }
        });

        return layout;
    }

}
