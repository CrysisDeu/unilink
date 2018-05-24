package com.teamxod.unilink;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class InitiateProfile extends AppCompatActivity {
    private final String MALE_PROFILE_PIC = "https://firebasestorage.googleapis.com/v0/b/fir-project-7cabd.appspot.com/o/male.png?alt=media&token=02a80321-a6ae-4194-af4d-bd658de9348f";
    private final String FEMALE_PROFILE_PIC = "https://firebasestorage.googleapis.com/v0/b/fir-project-7cabd.appspot.com/o/female.png?alt=media&token=69a0c9c9-eda5-481d-9043-b718d899121b";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String uid;
    private String name;
    private String picture;
    private String gender;
    private String yearGraduate;
    private String description;

    private ImageView mProfilePic;
    private EditText mEditName;
    private TextView mEmail;
    private Spinner mGenderSpinner;
    private Spinner mYearSpinner;
    private EditText mDescription;
    private Bitmap imageBitmap;
    private CardView mSave;

    private final int PICK_IMAGE_REQUEST = 71;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiate_profile);

        mProfilePic = findViewById(R.id.profile_pic);
        mEditName = findViewById(R.id.edit_name);
        mEmail = findViewById(R.id.email);
        mGenderSpinner = findViewById(R.id.gender_spinner);
        mYearSpinner = findViewById(R.id.year_spinner);
        mDescription = findViewById(R.id.description);
        mSave = findViewById(R.id.save);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth != null && mAuth.getCurrentUser() != null) {
            uid = mAuth.getCurrentUser().getUid();

            // using google login
            if(getLoginmethod()) {
                mEditName.setText(mAuth.getCurrentUser().getDisplayName());
                Uri mPhoto = mAuth.getCurrentUser().getPhotoUrl();
                if (mPhoto != null) {
                    Glide.with(this)
                            .load(mPhoto)
                            .apply(RequestOptions.circleCropTransform())
                            .into(mProfilePic);
                }
            }


            mEmail.setText(mAuth.getCurrentUser().getEmail());
        }

        mProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mEditName.getText().toString();
                gender = mGenderSpinner.getSelectedItem().toString();
                if (imageBitmap != null) {
                    picture = encodeBitmap(imageBitmap);
                } else {
                    //FIXME check gender and set default pic
                    if (gender == "female") {
                        picture = FEMALE_PROFILE_PIC;
                    }
                    else {
                        picture = MALE_PROFILE_PIC;
                    }
                }
                yearGraduate = mYearSpinner.getSelectedItem().toString();
                description = mDescription.getText().toString();
                writeNewUser(name, picture,gender,yearGraduate,description);
                finish();
            }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            mProfilePic.setImageBitmap(imageBitmap);
//            encodeBitmapAndSaveToFirebase(imageBitmap);
        }
    }


    public String  encodeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
//        DatabaseReference ref = FirebaseDatabase.getInstance()
//                .getReference("Users")
//                .child(uid)
//                .child("picture");
//        ref.setValue(imageEncoded);
        return imageEncoded;
    }

    private void writeNewUser(String name, String picture, String gender, String yearGraduate,
                              String description) {
        User user = new User(name, picture, gender, yearGraduate, description);

        //mDatabase.child("Users").child(uid).setValue(user);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


}
