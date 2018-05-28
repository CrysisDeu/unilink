package com.teamxod.unilink;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;
import java.util.List;

/* TODO @ Etsu:
    First, try to read through the whole file. I did lots of comments. Just make sure u know what i did.
    Then:
        (1) use the values got from the UI fields (store in Java fields) to create a house obj and push to firebase.
            All the values from XML are passed into java fields(If i didn't make mistake), except the date. (search "INFO FOR DATE").
            Problems: inconsistence between the values we got from xml and the fields in House class. (talk to neal see if we can change it).
        (2) Grid of image, then pass the images to House obj.
        (3) Upload imgs from local.
        (4) U can do a check to see if edit text all have input (not necessary i can do this)
        (5) Do what else we may need.
*/
public class AddPost extends AppCompatActivity implements IPickResult {

    /* Copied from another file
    private final int PICK_IMAGE_REQUEST = 71;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    */

    // Database field
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private DatabaseReference post;

    // Java fields (Used to create House obj and push to firebase)
    // TODO @ etsu  (bs. we use start date + lease length.  no end date)
    private String _posterId;
    private String _postId;

    private String _houseType;
    private String _roomType;
    private String _bedroom_number;
    private String _bathroom_number;

    private String _title;
    private String _location;
    private String _description;
    private int _price;

    private long _startDate;
    private String _leaseLength;

    private String _tv;
    private String _ac;
    private String _bus;
    private String _parking;
    private String _videoGame;
    private String _gym;
    private String _laundry;
    private String _pet;

    private List<String> pictures;
    private List<Room> rooms;


    //  ------------- UI fields ---------------- //

    // gridview and things inside
    private ImageView addpic;
    private GridLayout photoGrid;
    private ArrayList<LinearLayout> photoBoxList;
    private PickImageDialog dialog;
    private Uri picture;
    private ArrayList<Uri> pictureList;

    // edit text
    private EditText title;
    private EditText price;
    private EditText street;
    private EditText building;
    private EditText city;
    private EditText start_date;
    private EditText description;

    // room type
    private RadioButton living_room;
    private RadioButton private_room;
    private RadioButton entire_place;
    // house type
    private RadioButton apartment;
    private RadioButton house;
    private RadioButton town_house;
    // bedroom amount
    private RadioButton bed_zero;
    private RadioButton bed_one;
    private RadioButton bed_two;
    private RadioButton bed_three;
    private RadioButton bed_four_plus;
    // bathroom amount
    private RadioButton bath_zero;
    private RadioButton bath_one;
    private RadioButton bath_two;
    private RadioButton bath_three;
    private RadioButton bath_four_plus;
    // lease length
    private RadioButton annual;
    private RadioButton quarterly;
    private RadioButton short_term;

    // Facilities
    private CheckedTextView ac;
    private CheckedTextView allow_pet;
    private CheckedTextView parking;
    private CheckedTextView tv;
    private CheckedTextView video_game;
    private CheckedTextView gym;
    private CheckedTextView laundry;
    private CheckedTextView bus;

    private Button submit;

    // toolbar
    private Toolbar toolbar;
    private Button backBtn;

    // variables
    private boolean filledIn;
    private final String INVALID_FORM = "Please fill in all the information!";

    // --------------- UI fields above --------------- //

    // On create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        // variables
        filledIn = false;

        // Data base part
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // get UI in gridlayout
        addpic = findViewById(R.id.addpic_btn);
        photoGrid = findViewById(R.id.gridlayout);
        photoBoxList = new ArrayList<>();
        pictureList = new ArrayList<>();

        addpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pickImage
                dialog = PickImageDialog.build(new PickSetup()).show(AddPost.this);
            }
        });

        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        street = findViewById(R.id.street);
        building = findViewById(R.id.building);
        city = findViewById(R.id.city);
        living_room = findViewById(R.id.living_room);
        private_room = findViewById(R.id.private_room);
        entire_place = findViewById(R.id.entire_place);
        apartment = findViewById(R.id.apartment);
        house = findViewById(R.id.house);
        town_house = findViewById(R.id.town_house);
        bed_zero = findViewById(R.id.bed_zero);
        bed_one = findViewById(R.id.bed_one);
        bed_two = findViewById(R.id.bed_two);
        bed_three = findViewById(R.id.bed_three);
        bed_four_plus = findViewById(R.id.bed_four_plus);
        bath_one = findViewById(R.id.bath_one);
        bath_two = findViewById(R.id.bath_two);
        bath_three = findViewById(R.id.bath_three);
        bath_four_plus = findViewById(R.id.bath_four_plus);
        annual = findViewById(R.id.annual);
        quarterly = findViewById(R.id.quarterly);
        short_term = findViewById(R.id.short_term);
        start_date = findViewById(R.id.start_date);
        ac = findViewById(R.id.ac);
        allow_pet = findViewById(R.id.allow_pet);
        parking = findViewById(R.id.parking);
        tv = findViewById(R.id.tv);
        video_game = findViewById(R.id.video_game);
        gym = findViewById(R.id.gym);
        laundry = findViewById(R.id.laundry);
        bus = findViewById(R.id.bus);
        description = findViewById(R.id.description);

        submit = findViewById(R.id.submit);

        // for validation
        TextWatcher tw = new MyTextWatcher();
        title.addTextChangedListener(tw);
        price.addTextChangedListener(tw);
        street.addTextChangedListener(tw);
        building.addTextChangedListener(tw);
        city.addTextChangedListener(tw);
        start_date.addTextChangedListener(tw);
        description.addTextChangedListener(tw);

        // set check text view, using helper method
        setCheckedTextView(ac);
        setCheckedTextView(allow_pet);
        setCheckedTextView(parking);
        setCheckedTextView(tv);
        setCheckedTextView(video_game);
        setCheckedTextView(gym);
        setCheckedTextView(laundry);
        setCheckedTextView(bus);

        //  -------------- set listeners ---(pass values in xml to java)---------- //
        // Room type
        living_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: what happens if clicked again
                _roomType = "Living Room";
            }
        });
        private_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _roomType = "Private Room";
            }
        });
        entire_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _roomType = "Entire Place";
            }
        });

        // House type
        apartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _houseType = "Apartment";
            }
        });
        town_house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _houseType = "Town House";
            }
        });
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _houseType = "House";
            }
        });

        // Bedroom number
        bed_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bedroom_number = "1";
            }
        });
        bed_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bedroom_number = "2";
            }
        });
        bed_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bedroom_number = "3";
            }
        });
        bed_four_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bedroom_number = "4+";
            }
        });

        // Bathroom number
        bath_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bathroom_number = "1";
            }
        });
        bath_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bathroom_number = "2";
            }
        });
        bath_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bathroom_number = "3";
            }
        });
        bath_four_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bathroom_number = "4+";
            }
        });

        // Lease length
        annual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _leaseLength = "Annual";
            }
        });
        quarterly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _leaseLength = "Quarterly";
            }
        });
        short_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _leaseLength = "Short";
            }
        });

        // SUBMIT button !!!
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filledIn) {
                    Toast.makeText(AddPost.this, INVALID_FORM, Toast.LENGTH_LONG).show();
                    return;
                }

                _posterId = mAuth.getCurrentUser().getUid();
                // TODO use Java fields values to create house obj

                // get edit text content
                _title = title.getText().toString();
                if (!price.getText().toString().equals(""))
                    _price = Integer.parseInt(price.getText().toString());
                _location = street.getText().toString() + ";" + building.getText().toString() + ";" + city.getText().toString();

                // choosing buttons done already by setting listeners

                // Facilities check box
                _ac = isChecked(ac);
                _pet = isChecked(allow_pet);
                _parking = isChecked(parking);
                _tv = isChecked(tv);
                _videoGame = isChecked(video_game);
                _gym = isChecked(gym);
                _laundry = isChecked(laundry);
                _bus = isChecked(bus);

                _description = description.getText().toString();

                writeNewPost();
                uploadToFirebase(pictureList);
                Intent mainIntent = new Intent(AddPost.this, MainActivity.class);
                startActivity(mainIntent);
                // notify user submitted
                Snackbar.make(findViewById(R.id.Coordinator), "Congrudulation, you successfully post your house! ", Snackbar.LENGTH_LONG).show();
            }
        });

        // toolbar setup
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // back button setup
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }// on create

    class MyTextWatcher implements TextWatcher {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (title.getText().toString().length() > 0 &&
                    price.getText().toString().length() > 0 &&
                    street.getText().toString().length() > 0 &&
                    building.getText().toString().length() > 0 &&
                    city.getText().toString().length() > 0 &&
                    start_date.getText().toString().length() > 0 &&
                    description.getText().toString().length() > 0) {
                filledIn = true;
            }
            else {
                filledIn = false;
            }
        }
    }


    //PickImage Plug-in
    //choose picture from camera or gallery
    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            // create photo box
            final LinearLayout photoBox = (LinearLayout) View.inflate(this,
                    R.layout.add_photo, null);
            photoBox.findViewById(R.id.deletepic_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = photoBoxList.indexOf(photoBox);
                    photoBoxList.remove(index);
                    pictureList.remove(index);
                    photoBox.removeAllViews();
                }
            });
            photoBoxList.add(photoBox);

            //Mandatory to refresh image from Uri.
            // upload to firebase storage
            picture = r.getUri();
            Glide.with(AddPost.this)
                    .load(picture)
                    .apply(RequestOptions.centerCropTransform())
                    .into((ImageView) photoBoxList.get(photoBoxList.size() - 1).findViewById(R.id.pic));

            // add photo box to UI
            photoGrid.addView(photoBox);
            pictureList.add(picture);

        } else {
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    // helper method for ChekedTextView in XML
    private static void setCheckedTextView(final CheckedTextView ctv) {

        ctv.setCheckMarkDrawable(R.drawable.unchecked);
        ctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ctv.isChecked()) {
                    ctv.setChecked(true);
                    ctv.setCheckMarkDrawable(R.drawable.checked);
                } else {
                    ctv.setChecked(false);
                    ctv.setCheckMarkDrawable(R.drawable.unchecked);
                }

            }
        });
    }

    // Get content of check text view
    private static String isChecked(final CheckedTextView ctv) {
        if (ctv.isChecked())
            return "1";
        else
            return "0";
    }


    // TODO create house obj here
    private void writeNewPost() {
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        TempHouse h = new TempHouse("User", true, list);
        post = mDatabase.child("House_post").push();
        post.setValue(h);
    }

    // TODO DELETE
    static class TempHouse {
        public String poster;
        public boolean gym;
        public List<String> pictures;

        public TempHouse(String p, boolean g, List<String> pictures) {
            this.poster = new String(p);
            this.gym = g;
            this.pictures = pictures;
        }
    }


    private void uploadToFirebase(ArrayList<Uri> uriList) {
        final StorageReference house_images = mStorageRef.child("House_Images").child(post.getKey());

        for (Uri uri : uriList) {
            house_images.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            house_images.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                /*picture = uri;
                                Glide.with(AddPost.this)
                                        .load(picture)
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(mProfilePic);*/
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
        }
    }
}
