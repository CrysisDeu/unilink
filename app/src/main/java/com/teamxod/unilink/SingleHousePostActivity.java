package com.teamxod.unilink;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.ArrayList;

public class SingleHousePostActivity extends AppCompatActivity {

    private ViewPager housePicture;
    private UserPictureAdapter userPictureAdapter;
    private RecyclerView roommateListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_post);


        //House house = new House();
        //HousePictureAdapter housePictureAdapter = new HousePictureAdapter(this, house);
        housePicture = (ViewPager)findViewById(R.id.house_image);
        HousePictureAdapter housePictureAdapter = new HousePictureAdapter(this);
        housePicture.setAdapter(housePictureAdapter);


        //test statements
        ArrayList<User> roommateList = new ArrayList<User>();
        roommateList.add(new User());
        roommateList.add(new User());

        roommateListView = (RecyclerView) findViewById(R.id.roommate_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        roommateListView.setLayoutManager(layoutManager);
        roommateListView.setHasFixedSize(true);

        UserPictureAdapter roommateAdapter = new UserPictureAdapter(roommateList);
        roommateListView.setAdapter(roommateAdapter);
    }

}
