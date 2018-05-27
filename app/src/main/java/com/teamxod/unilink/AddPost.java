package com.teamxod.unilink;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class AddPost extends AppCompatActivity {//TODO implements IPickResult {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;


    /*
    private final int PICK_IMAGE_REQUEST = 71;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    */

    // UI fields
    private Button submit;

    // Java fields
    private String poster;
    private boolean gym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        // Data base part
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // get UI
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO DELTE
                gym = true;
                poster = mAuth.getCurrentUser().getUid();

                writeNewPost();

            }
        });
    }

    /*
    //PickImage Plug-in
    //choose picture from camera or gallery
    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {

            //Mandatory to refresh image from Uri.
            // upload to firebase storage
            uploadToFirebase(r.getUri());


        } else {
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    */


    private void writeNewPost(){
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        TempHouse h = new TempHouse("User",true,list);
        mDatabase.child("House_post").push().setValue(h);
    }


    static class TempHouse{
        public String poster;
        public boolean gym;
        public List<String> pictures;

        public TempHouse(String p, boolean g, List<String> pictures){
            this.poster = new String(p);
            this.gym = g;
            this.pictures = pictures;
        }
    }



    /*
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
                                Glide.with(InitiateProfile.this)
                                        .load(picture)
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(mProfilePic);
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
    */
}
