package com.teamxod.unilink;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class SingleHousePostActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapFragment mapFragment;

    private House house;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_post);

        //data needed later from database

        house = new House();
        ArrayList<User> roommateList;
        roommateList = new ArrayList<User>();
        roommateList.add(new User());
        roommateList.add(new User());

        //google map
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.house_map);
        mapFragment.getMapAsync(this);

        //house picture list setting
        ViewPager housePicture = (ViewPager)findViewById(R.id.house_image);
        HousePictureAdapter housePictureAdapter = new HousePictureAdapter(this);//housePictureAdapter = new HousePictureAdapter(this, house);
        housePicture.setAdapter(housePictureAdapter);

        //roommate list setting
        RecyclerView roommateListView = (RecyclerView) findViewById(R.id.house_roommate);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        roommateListView.setLayoutManager(layoutManager);
        roommateListView.setHasFixedSize(true);
        UserPictureAdapter roommateAdapter = new UserPictureAdapter(roommateList);
        roommateListView.setAdapter(roommateAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapFragment.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapFragment.onPause();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title(house.getName()));
    }
}
