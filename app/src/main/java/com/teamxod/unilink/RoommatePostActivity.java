package com.teamxod.unilink;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class RoommatePostActivity extends AppCompatActivity {

    String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roommate_post);

        Intent intent = getIntent();

        if(intent.hasExtra("uid")){
            uid = intent.getExtras().getString("uid");
        }else{
            Toast.makeText(this, "User does not exist", Toast.LENGTH_LONG).show();
            finish();
        }

        loadData();
    }

    private void loadData() {

    }
}
