package com.teamxod.unilink;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.text.Editable;

public class My_changePassword extends AppCompatActivity {

    private EditText original_password;
    private EditText new_password;
    private EditText confirm_password;
    private TextView done;
    private TextView forget_password;

    private final int done_orig_size = 13;

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_change_password);

        original_password = (EditText) findViewById(R.id.original_password);
        new_password = (EditText) findViewById(R.id.new_password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        done = (TextView) findViewById(R.id.Done);
        forget_password = (TextView) findViewById(R.id.forget_password);

        original_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0 && !new_password.getText().toString().equals("") && !confirm_password.getText().toString().equals("")) {
                    done.setTextSize(20);
                    done.setTypeface(null, Typeface.BOLD);
                }
                else{
                    done.setTextSize(done_orig_size);
                    done.setTypeface(null, Typeface.NORMAL);
                }
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
                if(s.length() != 0 && !original_password.getText().toString().equals("") && !confirm_password.getText().toString().equals("")) {
                    done.setTextSize(20);
                    done.setTypeface(null, Typeface.BOLD);
                }
                else{
                    done.setTextSize(done_orig_size);
                    done.setTypeface(null, Typeface.NORMAL);
                }
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
                if(s.length() != 0 && !new_password.getText().toString().equals("") && !original_password.getText().toString().equals("")) {
                    done.setTextSize(20);
                    done.setTypeface(null, Typeface.BOLD);
                }
                else{
                    done.setTextSize(done_orig_size);
                    done.setTypeface(null, Typeface.NORMAL);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        done.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                // hide kb
                View v = findViewById(android.R.id.content);
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                // change done icon back
                done.setTextSize(done_orig_size);
                done.setTypeface(null, Typeface.NORMAL);

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
                    Snackbar.make(findViewById(R.id.Coordinator), "OJBK !" , Snackbar.LENGTH_LONG)
                            .show();
                }
                // clear the inputs
                new_password.setText("");
                original_password.setText("");
                confirm_password.setText("");
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
                Snackbar.make(findViewById(R.id.Coordinator), "Your password is sent to your account email !",
                        Snackbar.LENGTH_LONG).show();

            }
        });

    }
}
