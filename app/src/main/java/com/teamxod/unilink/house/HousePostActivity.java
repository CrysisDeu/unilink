package com.teamxod.unilink.house;

import android.content.Intent;
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
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamxod.unilink.R;
import com.teamxod.unilink.chat.RealtimeDbChatActivity;
import com.teamxod.unilink.roommate.Room;
import com.teamxod.unilink.roommate.RoomAdapter;
import com.teamxod.unilink.user.Profile;
import com.teamxod.unilink.user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HousePostActivity extends AppCompatActivity implements OnMapReadyCallback {

    private House house;
    private User poster;
    private String postID;
    private boolean isFavourite;
    private ArrayList<User> roommateList;
    private ArrayList<String> favoriteList;

    private RecyclerView roommateListView;

    private HouseMateAdapter roommateAdapter;

    private DatabaseReference database;
    private DatabaseReference favoriteReference;

    private ToggleButton favorite_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_post);
        loadData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (favorite_btn.isChecked() && !isFavourite) {
            favoriteList.add(postID);
            favoriteReference.setValue(favoriteList);
        } else if ((!favorite_btn.isChecked()) && isFavourite) {
            favoriteList.remove(postID);
            favoriteReference.setValue(favoriteList);
        }
    }

    private void loadData() {

        Intent intent = getIntent();

        if (intent.hasExtra("postID")) {
            postID = intent.getExtras().getString("postID");
        } else {
            Toast.makeText(this, "Post does not exist", Toast.LENGTH_LONG).show();
            finish();
        }

        database = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        favoriteReference = database.child("Users").child(uid).child("favorite_houses");
        favoriteReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isFavourite = false;
                favoriteList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String item = snapshot.getValue(String.class);
                    favoriteList.add(item);
                    if (postID.equals(item)) {
                        isFavourite = true;
                    }
                }
                loadHouseData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void loadHouseData() {
        DatabaseReference postReference = database.child("House_post").child(postID);
        postReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "Post does not exist", Toast.LENGTH_LONG).show();
                    finish();
                }
                house = dataSnapshot.getValue(House.class);
                loadPosterData();
                setupButton(isFavourite);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void loadPosterData() {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
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

        setupBasicData();

        setupFeatures();

        setupHousePicture();

        setupRoom();

        //setupRoommate();

        setupMap();
    }

    private void setupBasicData() {
        //set poster view
        ImageView posterImageView = findViewById(R.id.house_poster_image);
        String url = poster.getPicture();
        Glide.with(this)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(posterImageView);

        posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), Profile.class);
                myIntent.putExtra("uid", house.getPosterId());
                startActivity(myIntent);
            }
        });

        TextView houseTypeTextView = findViewById(R.id.house_type);
        String houseType = house.getNumBedroom() + "B" + house.getNumBathroom() + "B · " + house.getHouseType();
        houseTypeTextView.setText(houseType);

        TextView houseTitleTextView = findViewById(R.id.house_title);
        houseTitleTextView.setText(house.getTitle());

        TextView houseAddressTextView = findViewById(R.id.house_address);
        houseAddressTextView.setText(house.getLocation());

        TextView posterNameTextView = findViewById(R.id.house_poster);
        String temp = "posted by " + poster.getName();
        posterNameTextView.setText(temp);

        TextView houseDescriptionTextView = findViewById(R.id.house_description);
        houseDescriptionTextView.setText(house.getDescription());

        TextView bottomPriceTextView = findViewById(R.id.house_bottom_price);
        temp = "$ " + house.getRooms().get(0).getPrice() + " ";
        bottomPriceTextView.setText(temp);

        TextView bottomTimeTextView = findViewById(R.id.house_bottom_time);
        temp = house.getLeasingLength() + " · From " + house.getStartDate();
        bottomTimeTextView.setText(temp);
    }

    private void setupHousePicture() {
        ViewPager housePicture = findViewById(R.id.house_image);
        HousePictureAdapter housePictureAdapter = new HousePictureAdapter(this, (ArrayList<String>) house.getPictures());
        housePicture.setAdapter(housePictureAdapter);
    }

    private void setupRoom() {
        ArrayList<Room> roomList = (ArrayList) house.getRooms();
        RecyclerView roomListView = findViewById(R.id.house_rooms);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        roomListView.setLayoutManager(layoutManager);
        roomListView.setHasFixedSize(true);
        RoomAdapter roomAdapter = new RoomAdapter(this, roomList);
        roomListView.setAdapter(roomAdapter);
    }

    //disable for now
    /*
    private void setupRoommate() {
        roommateList = (ArrayList)(poster.getRoommates());
        if(roommateList == null)
            roommateList = new ArrayList<>();
        roommateList.add(poster);
        roommateListView = (RecyclerView) findViewById(R.id.house_roommate);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        roommateListView.setLayoutManager(layoutManager);
        roommateListView.setHasFixedSize(true);
        roommateAdapter = new HouseMateAdapter(this, roommateList);
        roommateListView.setAdapter(roommateAdapter);
    }
    */

    private void setupMap() {
        ScrollView scrollView = findViewById(R.id.house_content);
        MapContainer map_container = findViewById(R.id.map_container);
        map_container.setScrollView(scrollView);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.house_map);
        mapFragment.getMapAsync(this);
    }

    private void setupButton(boolean isChecked) {
        ImageView backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button contact_btn = findViewById(R.id.house_button_contact);
        contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(HousePostActivity.this, RealtimeDbChatActivity.class);
                chatIntent.putExtra("user_id", house.getPosterId());
                chatIntent.putExtra("user_name", poster.getName());
                startActivity(chatIntent);
            }
        });

        favorite_btn = findViewById(R.id.house_button_favorite);
        favorite_btn.setChecked(isChecked);
    }

    private void setupFeatures() {
        String temp;

        if (house.getAc().equals("1"))
            temp = "Equipped";
        else
            temp = "No Data";
        TextView acTextView = findViewById(R.id.house_ac);
        acTextView.setText(temp);

        if (house.getTv().equals("1"))
            temp = "Equipped";
        else
            temp = "No Data";
        TextView tvTextView = findViewById(R.id.house_tv);
        tvTextView.setText(temp);

        if (house.getParking().equals("1"))
            temp = "Reserved Parking";
        else
            temp = "Street Parking";
        TextView parkingTextView = findViewById(R.id.house_parking);
        parkingTextView.setText(temp);

        if (house.getBus().equals("1"))
            temp = "Close to Bus Station";
        else
            temp = "Far from Bus Station";
        TextView busTextView = findViewById(R.id.house_bus);
        busTextView.setText(temp);

        if (house.getGym().equals("1"))
            temp = "Equipped";
        else
            temp = "No Data";
        TextView gymTextView = findViewById(R.id.house_gym);
        gymTextView.setText(temp);

        if (house.getVideoGame().equals("1"))
            temp = "Equipped";
        else
            temp = "No Data";
        TextView gameTextView = findViewById(R.id.house_game);
        gameTextView.setText(temp);

        if (house.getPet().equals("1"))
            temp = "Allowed";
        else
            temp = "Not Allowed";
        TextView petTextView = findViewById(R.id.house_pet);
        petTextView.setText(temp);

        if (house.getLaundry().equals("1"))
            temp = "Built-in Laundry";
        else
            temp = "Public Laundry";
        TextView laundryTextView = findViewById(R.id.house_laundry);
        laundryTextView.setText(temp);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        GoogleMap houseMap = map;
        LatLng houseLatLng;

        String location = house.getLocation();
        List<Address> addressList = new ArrayList<>();
        if (location != null || !location.equals("")) {
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

