package com.teamxod.unilink;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SingleHousePostActivity extends AppCompatActivity implements OnMapReadyCallback{

    House house;
    ArrayList<User> roommateList;

    ViewPager housePicture;
    RecyclerView roommateListView;

    HousePictureAdapter housePictureAdapter;
    UserPictureAdapter roommateAdapter;

    LinearLayoutManager layoutManager;

    GoogleMap houseMap;
    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_post);

        loadData();

        setupBasicData();

        setupHousePicture();

        setupRoom();

        setupRoommate();

        setupMap();
    }

    @Override
    public void onMapReady(GoogleMap map) {

        houseMap = map;
        LatLng houseLatLng;

        String location = house.getLocation();
        List<Address> addressList = new ArrayList<>();
        if(location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            houseLatLng = new LatLng(address.getLatitude(), address.getLongitude());
        } else {
            houseLatLng = new LatLng(40.7487, -73.9857);
        }

        CameraPosition target = CameraPosition.builder().target(houseLatLng).zoom(15).build();
        houseMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        MarkerOptions houseMarker = new MarkerOptions()
                .position(houseLatLng)
                .title(house.getLocation());
        houseMap.addMarker(houseMarker);
    }

    private void loadData(){
        house = new House();
        roommateList = new ArrayList<>();
        roommateList.add(new User());
        roommateList.add(new User());
    }

    private void setupBasicData() {
        //set poster view
        ImageView posterImageView = (ImageView)findViewById(R.id.house_poster_image);
        String url = "http://k2.jsqq.net/uploads/allimg/1711/17_171129092304_1.jpg";
        Glide.with(this)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(posterImageView);

        //TODO
    }

    private void setupHousePicture() {
        housePicture = (ViewPager)findViewById(R.id.house_image);
        housePictureAdapter = new HousePictureAdapter(this, house);//housePictureAdapter = new HousePictureAdapter(this, house);
        housePicture.setAdapter(housePictureAdapter);
    }

    private void setupRoom() {
        //TODO
    }

    private void setupRoommate() {
        //TODO
        roommateListView = (RecyclerView) findViewById(R.id.house_roommate);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        roommateListView.setLayoutManager(layoutManager);
        roommateListView.setHasFixedSize(true);
        roommateAdapter = new UserPictureAdapter(this, roommateList);
        roommateListView.setAdapter(roommateAdapter);
    }

    private void setupMap() {
        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.house_map);
        mapFragment.getMapAsync(this);
    }
}

