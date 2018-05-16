package com.teamxod.unilink;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SingleHousePostActivity extends AppCompatActivity {

    ViewPager housePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_post);

        //House house = new House();

        housePicture = (ViewPager)findViewById(R.id.house_image);

        //HousePictureAdapter housePictureAdapter = new HousePictureAdapter(this, house);
        HousePictureAdapter housePictureAdapter = new HousePictureAdapter(this);

        housePicture.setAdapter(housePictureAdapter);
    }

}
