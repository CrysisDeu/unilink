package com.teamxod.unilink.user;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.teamxod.unilink.R;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

public class ChangeProfileActivity extends AppCompatActivity implements IPickResult {

    private final String NAME_INVALID = "Please enter a valid name!";

    private final String MALE_PROFILE_PIC = "https://firebasestorage.googleapis.com/v0/b/fir-project-7cabd.appspot.com/o/male.png?alt=media&token=02a80321-a6ae-4194-af4d-bd658de9348f";
    private final String FEMALE_PROFILE_PIC = "https://firebasestorage.googleapis.com/v0/b/fir-project-7cabd.appspot.com/o/female.png?alt=media&token=69a0c9c9-eda5-481d-9043-b718d899121b";
    //the most recent graduate the user can choose
    private final int MIN_GRADUATE_YEAR = 2018;

    private LinearLayout _gender_layout;
    private LinearLayout _year_layout;

    //Button
    private ImageView mBackButton;
    private Button mSaveButton;
    //Views
    private ImageView mProfilePic;
    private EditText mEditName;
    private TextView mEmail;
    private Spinner mGenderSpinner;
    private Spinner mYearSpinner;
    private EditText mDescription;

    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private User user;
    private Uri picture;
    private DatabaseReference mUserReference;

    private Boolean new_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_change_profile);

        _gender_layout = findViewById(R.id.gender_layout);
        _year_layout = findViewById(R.id.year_layout);

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
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mUserReference = mDatabase.child("Users").child(uid);

        new_image = false;

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
                hideKeyBoard(view);
                if (mEditName.getText().toString().equals("")) {
                    Toast.makeText(ChangeProfileActivity.this, NAME_INVALID, Toast.LENGTH_SHORT).show();
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
                PickImageDialog.build(new PickSetup()).show(ChangeProfileActivity.this);
                hideKeyBoard(v);

            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                hideKeyBoard(view);
            }
        });

        // hide kb for gender spinner
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyBoard(view);
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Add new category")) {
                    // do your stuff
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyBoard(view);
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Add new category")) {
                    // do your stuff
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        _gender_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard(view);
            }
        });
        _year_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard(view);
            }
        });


    }

    //PickImage Plug-in
    //choose picture from camera or gallery
    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {

            //Mandatory to refresh image from Uri.
            // upload to firebase storage
            new_image = true;
            picture = r.getUri();
            Glide.with(getApplicationContext())
                    .load(picture)
                    .apply(RequestOptions.circleCropTransform())
                    .into(mProfilePic);
        } else {
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void uploadToFirebase(Uri uri) {
        final StorageReference profile_images = mStorageRef.child("Profile_Images").child(mAuth.getCurrentUser().getUid());

        if (new_image) {
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
        } else {
            if (uri.toString().equals(MALE_PROFILE_PIC) || uri.toString().equals(FEMALE_PROFILE_PIC)) {
                String gender = mGenderSpinner.getSelectedItem().toString();
                if (gender.equals("Female")) {
                    picture = Uri.parse(FEMALE_PROFILE_PIC);
                } else {
                    picture = Uri.parse(MALE_PROFILE_PIC);
                }
                mUserReference.child("picture").setValue(picture.toString());
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(mEditName.getText().toString())
                        .setPhotoUri(picture)
                        .build();
            }
        }


    }


    private int getGenderSelection(String gender) {
        int genderSelection;
        switch (gender) {
            case "Male":
                int GENDER_MALE = 0;
                genderSelection = GENDER_MALE;
                break;
            case "Female":
                int GENDER_FEMALE = 1;
                genderSelection = GENDER_FEMALE;
                break;
            default:
                int GENDER_NOT_DISCLOSE = 2;
                genderSelection = GENDER_NOT_DISCLOSE;
                break;
        }
        return genderSelection;
    }

    private int getYearSelection(String year) {

        Integer yearSelection;
        int MIN_GRADUATE_YEAR = 2018;
        yearSelection = Integer.parseInt(year) - MIN_GRADUATE_YEAR;
        if (yearSelection < 0) {
            yearSelection = 0;
        }
        return yearSelection;
    }

    // hide keyboard
    // This method is used to hide the keyboard
    // If want to use, simply copy the whole method to your file
    // Then call hideKeyBoard(v) in the onclick method
    private void hideKeyBoard(View view) {
        View vv = findViewById(android.R.id.content);
        if (vv != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
