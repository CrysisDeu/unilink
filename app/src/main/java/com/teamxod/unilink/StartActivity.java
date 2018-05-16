package com.teamxod.unilink;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
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

public class StartActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "StartActivity";
    private static final int RC_SIGN_IN = 9001;
    private CardView mStartCard;
    private Button mGoogleSignInButton;
    private Button mEmailSignUpButton;
    private LinearLayout mSignInPrompt;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // start card
        mStartCard = findViewById(R.id.sign_in_card);
        mGoogleSignInButton =  findViewById(R.id.google_sign_in_button);
        mEmailSignUpButton = findViewById(R.id.email_sign_up);
        mSignInPrompt = findViewById(R.id.sign_in_prompt);

        // sign-in card
        LinearLayout mSignInCard = new LinearLayout(this);
        mSignInCard.setOrientation(LinearLayout.VERTICAL);
        mSignInCard.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));

        EditText emailInput = createEditText("email");
        emailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        EditText passwordInput = createEditText("password");
        passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mSignInCard.addView(emailInput);
        mSignInCard.addView(passwordInput);
        mStartCard.addView(mSignInCard);


        // sign-up card

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
        mFirebaseAuth = FirebaseAuth.getInstance();

        //email on click
        mEmailSignUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startFadeOut();
                resizeAnimation(mStartCard,500,450);
                //mStartCard.addView();

            }
        });
        /*fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationEnd(Animation animation) {

            }
        });*/


    }


    //
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
    private void startFadeOut () {
        // Define the animators
        final Animation fadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        // Duration of animation
        fadeOutAnimation.setDuration(225);
        mGoogleSignInButton.startAnimation(fadeOutAnimation);
        mEmailSignUpButton.startAnimation(fadeOutAnimation);
        mSignInPrompt.startAnimation(fadeOutAnimation);
        mGoogleSignInButton.setVisibility(View.GONE);
        mEmailSignUpButton.setVisibility(View.GONE);
        mSignInPrompt.setVisibility(View.GONE);
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
            startActivity(new Intent(StartActivity.this, MainActivity.class));
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
                Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(StartActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(StartActivity.this, MainActivity.class));
                            finish();
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


    //helper method
    private int dpToPx (int dp) {
        Resources r = getResources();
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }
}