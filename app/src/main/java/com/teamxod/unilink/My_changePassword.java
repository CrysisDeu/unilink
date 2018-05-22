package com.teamxod.unilink;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.Editable;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.CryptoPrimitive;

public class My_changePassword extends AppCompatActivity {

    private ImageView mBackButton;
    private EditText original_password;
    private EditText new_password;
    private EditText confirm_password;
    private CardView save;
    private TextView forget_password;
    private FirebaseAuth mAuth;
    private String newpasswd;


    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_change_password);

        mBackButton = findViewById(R.id.back_button);

        mAuth = FirebaseAuth.getInstance();
        original_password = (EditText) findViewById(R.id.original_password);
        new_password = (EditText) findViewById(R.id.new_password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        save = (CardView) findViewById(R.id.Save);
        forget_password = (TextView) findViewById(R.id.forget_password);
        original_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        new_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        confirm_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                newpasswd = new_password.getText().toString();

                // hide kb
                View v = findViewById(android.R.id.content);
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                // check all filled first
                if( new_password.getText().toString().equals("")||new_password.getText().toString().equals("") ||original_password.getText().toString().equals("")) {
                    Snackbar.make(findViewById(R.id.Coordinator), "Please fill in all information !",
                            Snackbar.LENGTH_LONG).show();
                }
                // check new == confirm then
                else if (! new_password.getText().toString().equals(confirm_password.getText().toString())){

                    Snackbar.make(findViewById(R.id.Coordinator), "Please make sure your new password is same as your confirm password !",
                            Snackbar.LENGTH_LONG).show();
                }
                // ok
                else {

                    //re-auth to verify old password

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), original_password.getText().toString());
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Log.d("My_changePassword", "User re-authenticated.");

                                        // update password
                                        mAuth.getCurrentUser().updatePassword(newpasswd)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                           @Override
                                                                           public void onComplete(@NonNull Task<Void> task) {
                                                                               if (task.isSuccessful()) {
                                                                                   Log.d("My_changePassword", "User password updated.");
                                                                                   // logout user
                                                                                   Snackbar.make(findViewById(R.id.Coordinator), "Your password has been changed!" , Snackbar.LENGTH_LONG)
                                                                                           .show();
                                                                                   FirebaseAuth.getInstance().signOut();
                                                                                   Intent reset = new Intent(My_changePassword.this, StartActivity.class);
                                                                                   reset.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                   startActivity(reset);
                                                                               } else {
                                                                                   Snackbar.make(findViewById(R.id.Coordinator), task.getException().getMessage(), Snackbar.LENGTH_LONG)
                                                                                           .show();
                                                                               }
                                                                           }
                                                                       }
                                                );
                                    } else {
                                        Snackbar.make(findViewById(R.id.Coordinator), task.getException().getMessage(), Snackbar.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            });

                }
                // clear the inputs
//                new_password.setText("");
//                original_password.setText("");
//                confirm_password.setText("");
            }
        });

        forget_password.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // hide kb
                View v = findViewById(android.R.id.content);
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                sendRestEmail(mAuth.getCurrentUser().getEmail());
                Snackbar.make(findViewById(R.id.Coordinator), "Your password is sent to your account email !",
                        Snackbar.LENGTH_LONG).show();

            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void sendRestEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Foreget passowrd", "Email sent.");
                        }
                    }
                });
    }

}
