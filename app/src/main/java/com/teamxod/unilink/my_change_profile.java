package com.teamxod.unilink;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.Map;

public class my_change_profile extends AppCompatActivity implements IPickResult {

    //spinner selector
    private final int GENDER_MALE = 0;
    private final int GENDER_FEMALE = 1;
    private final int GENDER_NOT_DISCLOSE = 2;
    private final String NAME_INVALID = "Please enter a valid name!";

    //the most recent graduate the user can choose
    private final int MIN_GRADUATE_YEAR = 2018;

    //Button
    private ImageView mBackButton;
    private CardView mSaveButton;

    //Views
    private ImageView mProfilePic;
    private EditText mEditName;
    private TextView mEmail;
    private Spinner mGenderSpinner;
    private Spinner mYearSpinner;
    private EditText mDescription;

    //Firebase
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private String uid;
    private User user;
    private Uri picture;
    private DatabaseReference mUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_change_profile);

        //Button
        mBackButton = findViewById(R.id.back_button);
        mSaveButton = findViewById(R.id.save);

        //Views
        mProfilePic = findViewById(R.id.profile_pic);
        mEditName = findViewById(R.id.edit_name);
        mEmail = findViewById(R.id.email);
        mGenderSpinner = findViewById(R.id.gender_spinner);
        mYearSpinner = findViewById(R.id.year_spinner);
        mDescription = findViewById(R.id.description);

        //Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mUserReference = mDatabase.child("Users").child(uid);

        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                picture = Uri.parse(user.getPicture());
                mEditName.setText(user.getName());
                Uri mPhoto = (Uri.parse(user.getPicture()));
                Glide.with(getApplicationContext())
                        .load(mPhoto)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mProfilePic);
                mEmail.setText(mAuth.getCurrentUser().getEmail());
                mGenderSpinner.setSelection(getGenderSelection(user.getGender()));
                mYearSpinner.setSelection(getYearSelection(user.getYearGraduate()));
                mDescription.setText(user.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                user.setName(mEditName.getText().toString());
//                user.setGender(mGenderSpinner.getSelectedItem().toString());
//                user.setYearGraduate(mYearSpinner.getSelectedItem().toString());
//                user.setDescription(mDescription.getText().toString());
//                mUserReference.setValue(user);
                if (mEditName.getText().toString().equals("")) {
                    Toast.makeText(my_change_profile.this,NAME_INVALID,Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadToFirebase(picture);
                mUserReference.child("name").setValue(mEditName.getText().toString());
                mUserReference.child("gender").setValue(mGenderSpinner.getSelectedItem().toString());
                mUserReference.child("yearGraduate").setValue(mYearSpinner.getSelectedItem().toString());
                mUserReference.child("description").setValue(mDescription.getText().toString());
                finish();
            }
        });
        mProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup()).show(my_change_profile.this);

            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //}
    }

    //PickImage Plug-in
    //choose picture from camera or gallery
    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {

            //Mandatory to refresh image from Uri.
            // upload to firebase storage

            picture = r.getUri();
            Glide.with(getApplicationContext())
                    .load(picture)
                    .apply(RequestOptions.circleCropTransform())
                    .into(mProfilePic);
        } else {
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void uploadToFirebase (Uri uri) {
        final StorageReference profile_images = mStorageRef.child("Profile_Images").child(mAuth.getCurrentUser().getUid());

        profile_images.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        profile_images.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                picture = uri;
                                mUserReference.child("picture").setValue(picture.toString());
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(mEditName.getText().toString())
                                        .setPhotoUri(picture)
                                        .build();
                                mAuth.getCurrentUser().updateProfile(profileUpdates);
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


    private int getGenderSelection(String gender) {
        int genderSelection;
        switch (gender) {
            case "Male":
                genderSelection = GENDER_MALE;
                break;
            case "Female":
                genderSelection = GENDER_FEMALE;
                break;
            default:
                genderSelection = GENDER_NOT_DISCLOSE;
                break;
        }
        return genderSelection;
    }

    private int getYearSelection(String year) {

        Integer yearSelection;
        yearSelection = Integer.parseInt(year) - MIN_GRADUATE_YEAR;
        if (yearSelection < 0) {
            yearSelection = 0;
        }
        return yearSelection;
    }
}
