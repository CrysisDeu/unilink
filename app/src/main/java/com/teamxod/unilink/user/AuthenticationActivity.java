package com.teamxod.unilink.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamxod.unilink.MainActivity;
import com.teamxod.unilink.R;

public class AuthenticationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "AuthenticationActivity";
    private static final int RC_SIGN_IN = 9001;
    private final String INVALID_EMAIL = "Please enter a valid Email!";
    private final String PASSWORD_TOO_SHORT = "Your Password is too short!";
    private final String PASSWORD_TOO_SHORT_2 = "Password is at least 6 characters long";
    private final String PASSWORD_NOT_MATCH = "Your Password does not match!";
    private final String PASSWORD_RESST_SENT = "Your password reset link is sent to your email";

    //logo
    private ImageView mSignInLogo;

    //start card
    private CardView mStartCard;
    private LinearLayout mStartContainer;
    private Button mGoogleSignInButton;
    private Button mSignUpButton;
    private TextView mSignInButton;

    //sign-up card
    private LinearLayout mSignUpContainer;
    private ImageView mCancleSignUpButton;
    private Button mConfirmSignUp;

    //sign-in card
    private LinearLayout mSignInContainer;
    private ImageView mCancleSignInButton;
    private Button mConfirmSignIn;
    private TextView mForgotPassword;


    //firebase
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //general
        mSignInLogo = findViewById(R.id.sign_in_logo);
        mStartCard = findViewById(R.id.sign_in_card);

        // start card
        mStartContainer = findViewById(R.id.start_container);
        mGoogleSignInButton =  findViewById(R.id.google_sign_in_button);
        mSignUpButton = findViewById(R.id.email_sign_up);


        // sign-up card
        mSignUpContainer = findViewById(R.id.sign_up_container);
        mSignUpButton = findViewById(R.id.email_sign_up);
        mCancleSignUpButton = findViewById(R.id.cancle_sign_up_button);
        mConfirmSignUp = findViewById(R.id.confirm_sign_up);

        // sign-in card
        mSignInContainer = findViewById(R.id.sign_in_container);
        mSignInButton = findViewById(R.id.sign_in_button);
        mCancleSignInButton = findViewById(R.id.cancle_sign_in_button);
        mConfirmSignIn = findViewById(R.id.confirm_sign_in);
        mForgotPassword = findViewById(R.id.forget_password);


        // Define the animators
        final Animation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        final Animation fadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);

        // Duration of animation
        fadeInAnimation.setDuration(300);
        fadeOutAnimation.setDuration(300);


        // Set click listeners
        mGoogleSignInButton.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //SignUp onClick
        mSignUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //fadeout and resize
                FadeOutAnimation(mStartContainer,225);
                FadeOutAnimation(mSignInLogo,300);
                resizeAnimation(mStartCard,dpToPx(344),450);//200
                FadeInAnimation(mSignUpContainer,225);

            }
        });

        //Cancle SignUp onClick
        mCancleSignUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //fadeout and resize
                FadeOutAnimation(mSignUpContainer,225);
                resizeAnimation(mStartCard,dpToPx(144),450); //-200
                FadeInAnimation(mStartContainer,225);
                FadeInAnimation(mSignInLogo,300);

            }
        });

        //Confirm SignUp onClick
        mConfirmSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText mEmailSignUpInput = findViewById(R.id.email_sign_up_input);
                String mEmail = mEmailSignUpInput.getText().toString();

                EditText mPasswordSignUpInput = findViewById(R.id.password_sign_up_input);
                String mPassword = mPasswordSignUpInput.getText().toString();

                EditText mPasswordReenterInput = findViewById(R.id.password_reenter_input);
                String mPasswordReenter = mPasswordReenterInput.getText().toString();
                if(!isEmailValid(mEmail)) {
                    Toast toast = Toast.makeText(getApplicationContext(), INVALID_EMAIL,
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (mPassword.length() < 6) {
                    Toast toast = Toast.makeText(getApplicationContext(), PASSWORD_TOO_SHORT,
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                if (!mPassword.equals(mPasswordReenter)) {
                    Toast toast = Toast.makeText(getApplicationContext(), PASSWORD_NOT_MATCH,
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                } else {
                    createAccount(mEmail,mPassword);
                }



            }
        });



        //SignIn onClick
        mSignInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //fadeout and resize
                FadeOutAnimation(mStartContainer,225);
                FadeOutAnimation(mSignInLogo,300);
                resizeAnimation(mStartCard,dpToPx(304),300);//136
                FadeInAnimation(mSignInContainer,225);

            }
        });

        //Cancel SignIn onClick
        mCancleSignInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //fadeout and resize
                FadeOutAnimation(mSignInContainer,225);
                resizeAnimation(mStartCard,dpToPx(144),300);//-136
                FadeInAnimation(mStartContainer,225);
                FadeInAnimation(mSignInLogo,300);

            }
        });

        //Confirm SignIn onClick
        mConfirmSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText mEmailInput = findViewById(R.id.email_input);
                String mEmail = mEmailInput.getText().toString();
                EditText mPasswordInput = findViewById(R.id.password_input);
                String mPassword = mPasswordInput.getText().toString();

                if(!isEmailValid(mEmail)) {
                    Toast toast = Toast.makeText(getApplicationContext(), INVALID_EMAIL,
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (mPassword.length() < 6) {
                    Toast toast = Toast.makeText(getApplicationContext(), PASSWORD_TOO_SHORT_2,
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                } else {
                    signIn(mEmail,mPassword);
                }

            }
        });

        //SignIn Forget Password onClick
        mForgotPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText mEmailInput = findViewById(R.id.email_input);
                String mEmail = mEmailInput.getText().toString();

                if(!isEmailValid(mEmail)) {
                    Toast toast = Toast.makeText(getApplicationContext(), INVALID_EMAIL,
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                sendResetEmail(mEmail);

                Toast toast = Toast.makeText(getApplicationContext(), PASSWORD_RESST_SENT,
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }



    private EditText createEditText (String hint) {
        EditText newInputView = new EditText(this);
        newInputView.setHint(hint);
        newInputView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        newInputView.setMaxLines(1);
        return newInputView;
    }

    //Animation helpers
    private void FadeOutAnimation(View view, int duration) {
        // Define the animators
        final Animation fadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        // Duration of animation
        fadeOutAnimation.setDuration(duration);
        view.startAnimation(fadeOutAnimation);
        view.setVisibility(View.GONE);
    }

    private void FadeInAnimation(View view, int duration) {
        // Define the animators
        final Animation fadeInAnimation = new AlphaAnimation(0.1f, 1.0f);
        // Duration of animation
        fadeInAnimation.setDuration(duration);
        view.startAnimation(fadeInAnimation);
        view.setVisibility(View.VISIBLE);
    }

    private void resizeAnimation (View view, int target, int duration) {
        ResizeAnimation resizeAnimation = new ResizeAnimation(
                view,
                target,
                view.getHeight()
        );
        resizeAnimation.setDuration(duration);
        view.startAnimation(resizeAnimation);
    }


    private void handleFirebaseAuthResult(AuthResult authResult) {
        if (authResult != null) {
            // Welcome the user
            FirebaseUser user = authResult.getUser();
            Toast.makeText(this, "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();

            // Go back to the main activity
            startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_sign_in_button:
                signIn();
                break;
            default:
                return;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            DatabaseReference users = FirebaseDatabase.getInstance().getReference();
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    // already set profile
                    /*if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {*/
                    Intent mainIntent = new Intent(AuthenticationActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                    // need to set profile
                    /*} else {
                        Intent initiateProfileIntent = new Intent(AuthenticationActivity.this, InitiateProfile.class);
                        startActivity(initiateProfileIntent);
                        finish();
                    }*/
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } else {
            Log.e(TAG, "Sign up failed.");
        }
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(AuthenticationActivity.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(AuthenticationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }




    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(AuthenticationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                Toast.makeText(AuthenticationActivity.this, "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } catch (Exception e) {

                            }
                        }
                    }
                });
    }

    private void sendResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Foreget passowrd", "Email sent.");
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
        moveTaskToBack(true);
    }

    //helper method
    private int dpToPx (int dp) {
        Resources r = getResources();
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}