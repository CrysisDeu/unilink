package com.teamxod.unilink;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SingleHousePostActivity extends AppCompatActivity implements OnMapReadyCallback{

    House house;
    User poster;
    ArrayList<User> roommateList;

    ViewPager housePicture;
    RecyclerView roommateListView;
    RecyclerView roomListView;

    HousePictureAdapter housePictureAdapter;
    UserPictureAdapter roommateAdapter;
    RoomAdapter roomAdapter;

    LinearLayoutManager layoutManager;

    GoogleMap houseMap;
    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_post);

        loadHouseData();
    }

    private void loadHouseData(){
        Bundle bundle = getIntent().getExtras();
        String uid = bundle.getString("uid");

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postReference = database.child("House_post").child(uid);

        postReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                house = dataSnapshot.getValue(House.class);
                loadPosterData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void loadPosterData() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference posterReference = database.child("Users").child(house.getPosterId());
        posterReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                poster = dataSnapshot.getValue(User.class);
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void updateUI() {
        setupButton();

        setupBasicData();

        setupFeatures();

        setupHousePicture();

        setupRoom();

        setupRoommate();

        setupMap();
    }

    private void setupBasicData() {
        //set poster view
        ImageView posterImageView = (ImageView)findViewById(R.id.house_poster_image);
        String url = poster.getPicture();
        Glide.with(this)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(posterImageView);

        TextView houseTypeTextView = (TextView)findViewById(R.id.house_type);
        String houseType = house.getNumBedroom() + "B" + house.getNumBathroom() + "B · " + house.getHouseType();
        houseTypeTextView.setText(houseType);

        TextView houseTitleTextView = (TextView)findViewById(R.id.house_title);
        houseTitleTextView.setText(house.getTitle());

        TextView houseAddressTextView = (TextView)findViewById(R.id.house_address);
        houseAddressTextView.setText(house.getLocation());

        TextView posterNameTextView = (TextView)findViewById(R.id.house_poster);
        String temp = "posted by " + poster.getName();
        posterNameTextView.setText(temp);

        TextView houseDescriptionTextView = (TextView)findViewById(R.id.house_description);
        houseDescriptionTextView.setText(house.getDescription());

        TextView bottomPriceTextView = (TextView)findViewById(R.id.house_bottom_price);
        temp = "$ " + house.getRooms().get(0).getPrice() + " ";
        bottomPriceTextView.setText(temp);

        TextView bottomTimeTextView = (TextView)findViewById(R.id.house_bottom_time);
        temp = house.getLeasingLength() + " · From " + house.getStartDate();
        bottomTimeTextView.setText(temp);
    }

    private void setupHousePicture() {
        housePicture = (ViewPager)findViewById(R.id.house_image);
        housePictureAdapter = new HousePictureAdapter(this, (ArrayList<String>) house.getPictures());
        housePicture.setAdapter(housePictureAdapter);
    }

    private void setupRoom() {
        ArrayList<Room> roomList = (ArrayList)house.getRooms();
        roomListView = (RecyclerView) findViewById(R.id.house_rooms);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        roomListView.setLayoutManager(layoutManager);
        roomListView.setHasFixedSize(true);
        roomAdapter = new RoomAdapter(this, roomList);
        roomListView.setAdapter(roomAdapter);
    }

    private void setupRoommate() {
        roommateList = (ArrayList)(poster.getRoommates());
        if(roommateList == null)
            roommateList = new ArrayList<>();
        roommateList.add(poster);
        roommateListView = (RecyclerView) findViewById(R.id.house_roommate);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        roommateListView.setLayoutManager(layoutManager);
        roommateListView.setHasFixedSize(true);
        roommateAdapter = new UserPictureAdapter(this, roommateList);
        roommateListView.setAdapter(roommateAdapter);
    }

    private void setupMap() {
        ScrollView scrollView = (ScrollView) findViewById(R.id.house_content);
        MapContainer map_container = (MapContainer) findViewById(R.id.map_container);
        map_container.setScrollView(scrollView);
        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.house_map);
        mapFragment.getMapAsync(this);
    }

    private void setupButton() {
        Button backBtn = (Button)findViewById(R.id.house_button_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button moreBtn = (Button)findViewById(R.id.house_button_favorite);
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setupFeatures() {
        TextView acTextView = (TextView)findViewById(R.id.house_ac);
        acTextView.setText(house.getAc());

        TextView tvTextView = (TextView)findViewById(R.id.house_tv);
        tvTextView.setText(house.getTv());

        TextView parkingTextView = (TextView)findViewById(R.id.house_parking);
        parkingTextView.setText(house.getParking());

        TextView busTextView = (TextView)findViewById(R.id.house_bus);
        busTextView.setText(house.getBus());

        TextView gymTextView = (TextView)findViewById(R.id.house_gym);
        gymTextView.setText(house.getGym());

        TextView gameTextView = (TextView)findViewById(R.id.house_game);
        gameTextView.setText(house.getVideoGame());

        TextView petTextView = (TextView)findViewById(R.id.house_pet);
        petTextView.setText(house.getPet());

        TextView laundryTextView = (TextView)findViewById(R.id.house_laundry);
        laundryTextView.setText(house.getLaundry());
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
            if (addressList == null || addressList.size() < 1) {
                houseLatLng = new LatLng(40.7487, -73.9857);
                Toast.makeText(this, "Invalid Location", Toast.LENGTH_LONG).show();
            } else {
                Address address = addressList.get(0);
                houseLatLng = new LatLng(address.getLatitude(), address.getLongitude());
            }

        } else {
            houseLatLng = new LatLng(40.7487, -73.9857);
            Toast.makeText(this, "Invalid Location", Toast.LENGTH_LONG).show();
        }

        CameraPosition target = CameraPosition.builder().target(houseLatLng).zoom(15).build();
        houseMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        MarkerOptions houseMarker = new MarkerOptions()
                .position(houseLatLng)
                .title(house.getLocation());
        houseMap.addMarker(houseMarker);
    }
}

