package com.teamxod.unilink;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import info.hoang8f.android.segmented.SegmentedGroup;

public class ChangePreferenceActivity extends AppCompatActivity {

    // UI variables
    private ImageView mBackButton;
    private SeekBar sleep_seekBar;
    private TextView sleep_seekbar_text;
    private SeekBar clean_seekBar;
    private TextView clean_seekbar_text;
    private SegmentedGroup bring;
    private SegmentedGroup pet;
    private SegmentedGroup smoke;
    private SegmentedGroup drink;
    private SegmentedGroup party;
    private CheckedTextView surfing;
    private CheckedTextView hiking;
    private CheckedTextView skiing;
    private CheckedTextView gaming;
    private SeekBar smoke_seekBar;
    private SeekBar drink_seekBar;
    private TextView smoke_seekbar_text;
    private TextView drink_seekbar_text;
    private Spinner languageSpinner;
    private Button save;

    private RadioButton bring1;
    private RadioButton bring2;
    private RadioButton bring3;
    private RadioButton pet1;
    private RadioButton pet2;
    private RadioButton smoke1;
    private RadioButton smoke2;
    private RadioButton drink1;
    private RadioButton drink2;
    private RadioButton party1;
    private RadioButton party2;
    private RadioButton party3;

    // preference variables, used to report to the firebase
    private int Bring;
    private int Pet;
    private double Smoke;
    private double Drink;
    private int Party;
    private double Sleep;
    private double Clean;
    private int Surfing;
    private int Hiking;
    private int Skiing;
    private int Gaming;
    private int language;

    // firebase variables
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String uid;
    private preference existedPreference;
    private int needToRestore = 0;
    private boolean finish;

    private boolean smokeFinish;
    private boolean drinkFinish;
    private boolean partyFinish;
    private boolean bringFinish;
    private boolean petFinish;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_preference);
        // firebase declaration
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

                /**
                 * UI components logic, including :
                 *      1 spinner: language
                 *      2 buttons : save and back
                 *      2 seekBars: sleep and clean (with corresponding text)
                 *      2 buttonGroups with seekBars: smoke and drink
                 *      4 checkedTextView : surfing, hiking, skiing, gaming
                 *      3 buttonGroups: bring, pet and party
                 *
                 *      sections are managed by : declaring + logic in order
                 */

                languageSpinner = (Spinner) findViewById(R.id.languege_spinner);

                save = (Button) findViewById(R.id.save);
                mBackButton = findViewById(R.id.back_button);

                sleep_seekBar = (SeekBar) findViewById(R.id.sleep_seekbar);
                sleep_seekbar_text = (TextView) findViewById(R.id.sleep_seekbar_text);
                clean_seekBar = (SeekBar) findViewById(R.id.clean_seekbar);
                clean_seekbar_text = (TextView) findViewById(R.id.clean_seekbar_text);

                smoke = (SegmentedGroup) findViewById(R.id.smoke_button);
                drink = (SegmentedGroup) findViewById(R.id.drink_button);
                smoke_seekBar = (SeekBar) findViewById(R.id.smoke_seekbar);
                drink_seekBar = (SeekBar) findViewById(R.id.drink_seekbar);
                smoke_seekbar_text = (TextView) findViewById(R.id.smoke_seekbar_text);
                drink_seekbar_text = (TextView) findViewById(R.id.drink_seekbar_text);
                smoke1 = (RadioButton) findViewById(R.id.smoke_button_1);
                smoke2 = (RadioButton) findViewById(R.id.smoke_button_2);
                drink1 = (RadioButton) findViewById(R.id.drink_button_1);
                drink2 = (RadioButton) findViewById(R.id.drink_button_2);

                surfing = (CheckedTextView) findViewById(R.id.surfing);
                hiking = (CheckedTextView) findViewById(R.id.hiking);
                skiing = (CheckedTextView) findViewById(R.id.skiing);
                gaming = (CheckedTextView) findViewById(R.id.gaming);

                bring = (SegmentedGroup) findViewById(R.id.bring_button);
                pet = (SegmentedGroup) findViewById(R.id.pet_button);
                party = (SegmentedGroup) findViewById(R.id.party_button);

                bring1 = (RadioButton) findViewById(R.id.bring_button_1);
                bring2 = (RadioButton) findViewById(R.id.bring_button_2);
                bring3 = (RadioButton) findViewById(R.id.bring_button_3);
                pet1 = (RadioButton) findViewById(R.id.pet_button_1);
                pet2 = (RadioButton) findViewById(R.id.pet_button_2);
                party1 = (RadioButton) findViewById(R.id.party_button_1);
                party2 = (RadioButton) findViewById(R.id.party_button_2);
                party3 = (RadioButton) findViewById(R.id.party_button_3);


                // language spinner logic
                ArrayAdapter<CharSequence> languageAdaptor = ArrayAdapter.createFromResource(getApplicationContext(), R.array.language_array, android.R.layout.simple_spinner_item);
                languageAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                languageSpinner.setAdapter(languageAdaptor);


                // two buttons logic
                mBackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(getFinish() == true) {
                            // map getProgress() to value between -1 and 1.
                            Sleep = 1 - 2 * sleep_seekBar.getProgress() / (double) 9;
                            Clean = 1 - 2 * clean_seekBar.getProgress() / (double) 7;
                            if (Smoke != -1) {
                                Smoke = -1 + 2 * smoke_seekBar.getProgress() / (double) 7;
                            }
                            if (Drink != -1) {
                                Drink = -1 + 2 * drink_seekBar.getProgress() / (double) 7;
                            }
                            String languageSelected = languageSpinner.getSelectedItem().toString();
                            setLanguage(languageSelected);
                            // report data to the firebase
                            newPreference();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"Please finish the survey.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                // sleep_seekBar and clean_seekBar logic
                sleep_seekBar.setMax(9);
                sleep_seekbar_text.setText("9PM - 6AM");
                sleep_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int progressV;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progressV = progress;
                        if (progressV < 4) {
                            int time = progressV + 9;
                            sleep_seekbar_text.setText(time + " PM");
                        } else {
                            int time = progressV - 3;
                            sleep_seekbar_text.setText(time + " AM");
                        };
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                clean_seekBar.setMax(7);
                clean_seekbar_text.setText("0 - 7 times");
                clean_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int progressV;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progressV = progress;
                        clean_seekbar_text.setText(progressV + " times");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                // smoke and drink seekBar with texts logic
                smoke_seekBar.setMax(7);
                smoke_seekbar_text.setText("0 - 7 days");
                smoke_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int progressV;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progressV = progress;
                        smoke_seekbar_text.setText(progressV + " days");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                drink_seekBar.setMax(7);
                drink_seekbar_text.setText("0 - 7 days");
                drink_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int progressV;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progressV = progress;
                        drink_seekbar_text.setText(progressV + " days");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                smoke.setTintColor(Color.parseColor("#6D48E5"));
                drink.setTintColor(Color.parseColor("#6D48E5"));

                smoke1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Smoke = 1;
                        smokeFinish = true;
                    }
                });
                smoke2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Smoke = -1;
                        smokeFinish = true;
                    }
                });

                drink1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Drink = 1;
                        drinkFinish = true;
                    }
                });
                drink2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Drink = -1;
                        drinkFinish = true;
                    }
                });

                // four checkedTextView logic
                surfing.setCheckMarkDrawable(R.drawable.unchecked);
                hiking.setCheckMarkDrawable(R.drawable.unchecked);
                skiing.setCheckMarkDrawable(R.drawable.unchecked);
                gaming.setCheckMarkDrawable(R.drawable.unchecked);
                surfing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!surfing.isChecked()) {
                            surfing.setChecked(true);
                            Surfing = 1;
                            surfing.setCheckMarkDrawable(R.drawable.checked);
                        } else {
                            surfing.setChecked(false);
                            Surfing = -1;
                            surfing.setCheckMarkDrawable(R.drawable.unchecked);
                        }

                    }
                });

                hiking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!hiking.isChecked()) {
                            hiking.setCheckMarkDrawable(R.drawable.checked);
                            hiking.setChecked(true);
                            Hiking = 1;
                        } else {
                            hiking.setCheckMarkDrawable(R.drawable.unchecked);
                            hiking.setChecked(false);
                            Hiking = -1;
                        }
                    }
                });

                skiing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!skiing.isChecked()) {
                            skiing.setChecked(true);
                            skiing.setCheckMarkDrawable(R.drawable.checked);
                            Skiing = 1;
                        } else {
                            skiing.setCheckMarkDrawable(R.drawable.unchecked);
                            skiing.setChecked(false);
                            Skiing = -1;
                        }
                    }
                });

                gaming.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!gaming.isChecked()) {
                            gaming.setChecked(true);
                            gaming.setCheckMarkDrawable(R.drawable.checked);
                            Gaming = 1;
                        } else {
                            gaming.setCheckMarkDrawable(R.drawable.unchecked);
                            gaming.setChecked(false);
                            Gaming = -1;
                        }
                    }
                });

                // three buttonGroups logic
                bring.setTintColor(Color.parseColor("#6D48E5"));
                pet.setTintColor(Color.parseColor("#6D48E5"));
                party.setTintColor(Color.parseColor("#6D48E5"));


                // Button onClick methods
                bring1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bring = 1;
                        bringFinish = true;
                    }
                });
                bring2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bring = -1;
                        bringFinish = true;
                    }
                });
                bring3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bring = 0;
                        bringFinish = true;
                    }
                });

                pet1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Pet = 1;
                        petFinish = true;
                    }
                });
                pet2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Pet = -1;
                        petFinish = true;
                    }
                });

                party1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Party = 1;
                        partyFinish = true;
                    }
                });
                party2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Party = -1;
                        partyFinish = true;
                    }
                });
                party3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Party = 0;
                        partyFinish = true;
                    }
                });

        // if the user has finished the survey before, we will load the choices they made
        mDatabase.child("Preference").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // if the user is already in our database, we load the data they put in
                if (dataSnapshot.exists()) {
                    existedPreference = dataSnapshot.getValue(preference.class);
                    if(dataSnapshot.child("sleepTime").getValue() instanceof Long)
                        Sleep = ((Long)dataSnapshot.child("sleepTime").getValue()).doubleValue();
                    else
                        Sleep = (double)dataSnapshot.child("sleepTime").getValue();
                    if(dataSnapshot.child("cleanTime").getValue() instanceof Long)
                        Clean = ((Long)dataSnapshot.child("cleanTime").getValue()).doubleValue();
                    else
                        Clean = (double)dataSnapshot.child("cleanTime").getValue();
                    if(dataSnapshot.child("smoke").getValue() instanceof Long)
                        Smoke = ((Long)dataSnapshot.child("smoke").getValue()).doubleValue();
                    else
                        Smoke = (double)dataSnapshot.child("smoke").getValue();
                    if(dataSnapshot.child("drink").getValue() instanceof Long)
                        Drink = ((Long)dataSnapshot.child("drink").getValue()).doubleValue();
                    else
                        Drink = (double)dataSnapshot.child("drink").getValue();
                    existedPreference.setSleepTime(Sleep);
                    existedPreference.setCleanTime(Clean);
                    existedPreference.setSmoke(Smoke);
                    existedPreference.setDrink(Drink);
                    needToRestore = 1;
                    smokeFinish = true;
                    drinkFinish = true;
                    partyFinish = true;
                    bringFinish = true;
                    petFinish = true;
                    restoreInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // report new preference to the firebase
    private void newPreference(){
        preference preference = new preference(Sleep, Clean, Bring, Pet, Surfing, Hiking,
                Skiing, Gaming, Smoke, Drink, Party, language);
        mDatabase.child("Preference").child(uid).setValue(preference);
    }

    // method to restore users' information if they have finished the survey before.
    private void restoreInfo(){
        language = existedPreference.getLanguage();
        Surfing = existedPreference.getSurfing();
        Hiking = existedPreference.getHiking();
        Skiing = existedPreference.getSkiing();
        Gaming = existedPreference.getGaming();
        Pet = existedPreference.getPet();
        Bring = existedPreference.getBring();
        Party = existedPreference.getParty();

        languageSpinner.setSelection(existedPreference.getLanguage());

        sleep_seekBar.setProgress((int) Math.rint(((1 - existedPreference.getSleepTime()) * 9 / (double)2)));
        clean_seekBar.setProgress((int) Math.rint(((1 - existedPreference.getCleanTime()) * 7 / (double)2)));

        smoke_seekBar.setProgress((int)Math.rint(((1 + existedPreference.getSmoke()) * 7 / (double)2)));
        drink_seekBar.setProgress((int)Math.rint(((1 + existedPreference.getDrink()) * 7 / (double)2)));

        if(existedPreference.getSmoke() != -1) {
            smoke1.toggle();
        }else{
            smoke2.toggle();
        }

        if(existedPreference.getDrink() != -1) {
            drink1.toggle();
        }else{
            drink2.toggle();
        }


        if(existedPreference.getSurfing() == 1) {
            surfing.setChecked(true);
            surfing.setCheckMarkDrawable(R.drawable.checked);
        }
        if(existedPreference.getHiking() == 1) {
            hiking.setChecked(true);
            hiking.setCheckMarkDrawable(R.drawable.checked);
        }
        if(existedPreference.getSkiing() == 1) {
            skiing.setChecked(true) ;
            skiing.setCheckMarkDrawable(R.drawable.checked);
        }
        if(existedPreference.getGaming() == 1){
            gaming.setChecked(true);
            gaming.setCheckMarkDrawable(R.drawable.checked);
        }

        if(existedPreference.getBring() == 1){
            bring1.toggle();
        }
        else if(existedPreference.getBring() == -1) {
            bring2.toggle();
        }else{
            bring3.toggle();
        }

        if(existedPreference.getPet() == 1) {
            pet1.toggle();
        }else{
            pet2.toggle();
        }

        if(existedPreference.getParty() == 1){
            party1.toggle();
        }
        else if(existedPreference.getParty() == -1) {
            party2.toggle();
        }else{
            party3.toggle();
        }

        if (sleep_seekBar.getProgress() < 4) {
            int progress = sleep_seekBar.getProgress() + 9;
            sleep_seekbar_text.setText(progress + " PM");
        } else {
            int progress = sleep_seekBar.getProgress() - 3;
            sleep_seekbar_text.setText(progress + " AM");
        }
        smoke_seekbar_text.setText(smoke_seekBar.getProgress() + " days");
        drink_seekbar_text.setText(drink_seekBar.getProgress() + " days");
        clean_seekbar_text.setText(clean_seekBar.getProgress() + " times");


    }

    // set language variable based on what the user selected
    private void setLanguage(String languageSelected){
        switch(languageSelected) {
            case "Arabic": language = 0;
                break;
            case "Cantonese": language = 1;
                break;
            case "Chinese": language = 2;
                break;
            case "English": language = 3;
                break;
            case "Hindi": language = 4;
                break;
            case "Japanese": language = 5;
                break;
            case "Korean": language = 6;
                break;
            case "Portuguese": language = 7;
                break;
            case "Russian": language = 8;
                break;
            case "Spanish": language = 9;
                break;
            case "Vietnamese": language = 10;
                break;
            default: language = 11;
        }
    }
    public boolean getFinish(){
        return (smokeFinish && drinkFinish && bringFinish && petFinish && partyFinish);
    }
}
