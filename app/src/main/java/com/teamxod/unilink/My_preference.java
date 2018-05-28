package com.teamxod.unilink;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ArrayAdapter;
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


public class My_preference extends AppCompatActivity {

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
    private CardView save;

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

    // preference variables
    private int Bring;
    private int Pet;
    private int Smoke;
    private int Drink;
    private int Party;
    private int Sleep;
    private int Clean;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_preference);

        // firebase logic
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = mDatabase.child("preference");
        uid = mAuth.getCurrentUser().getUid();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    preference preference = dataSnapshot.getValue(preference.class);
                    System.out.println(preference.getBring());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        // if the user has finished the survey before, we will load the choices they made
        mDatabase.child("Preference").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // if the user is already in our database, we load the data they put in
                if(dataSnapshot.exists()){
                    existedPreference = dataSnapshot.getValue(preference.class);
                    sleep_seekBar.setProgress((1 - existedPreference.getSleepTime()) * 9);
                    clean_seekBar.setProgress((1 - existedPreference.getCleanTime()) * 7);
                    languageSpinner.setSelection(existedPreference.getLanguage());
                    System.out.println(languageSpinner.getSelectedItem().toString() + "kanwo");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        save = (CardView)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // map getProgress() to value between -1 and 1.
                Sleep = 1 - sleep_seekBar.getProgress() / 9;
                Clean = 1 - clean_seekBar.getProgress() / 7;
                if(Smoke == 1){
                    Smoke = -1 + smoke_seekBar.getProgress() / 7;
                }
                if(Drink == 1){
                    Drink = -1 + drink_seekBar.getProgress() / 7;
                }
                String languageSelected = languageSpinner.getSelectedItem().toString();
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
                // report data to the firebase
                newPreference();
            }
        });




        // UI logic
        mBackButton = findViewById(R.id.back_button);

        // seekbar for the sleep time
        sleep_seekBar = (SeekBar)findViewById(R.id.sleep_seekbar);
        sleep_seekBar.setMax(9);
        sleep_seekbar_text = (TextView)findViewById(R.id.sleep_seekbar_text);
        sleep_seekbar_text.setText("9PM - 6AM");
        sleep_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressV;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressV = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
             }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(progressV < 4) {
                    int time = progressV + 9;
                    sleep_seekbar_text.setText(time + " PM");
                    Toast.makeText(getApplicationContext(), "Setting to " + time + " AM", Toast.LENGTH_SHORT).show();
                }
                else{
                    int time = progressV - 3;
                    sleep_seekbar_text.setText(time + " AM");
                    Toast.makeText(getApplicationContext(),"Setting to " + time + " AM", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // seekbar for the clean times
        clean_seekBar = (SeekBar)findViewById(R.id.clean_seekbar);
        clean_seekBar.setMax(7);
        clean_seekbar_text = (TextView)findViewById(R.id.clean_seekbar_text);
        clean_seekbar_text.setText("0 times - 7 times");
        clean_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressV;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressV = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                clean_seekbar_text.setText("             " + progressV + " times");
                Toast.makeText(getApplicationContext(),"Setting to " + progressV + " times", Toast.LENGTH_SHORT).show();
            }
        });

        // button groups for yes and no
        bring = (SegmentedGroup)findViewById(R.id.bring_button);
        bring.setTintColor(Color.parseColor("#6D48E5"));

        pet = (SegmentedGroup)findViewById(R.id.pet_button);
        pet.setTintColor(Color.parseColor("#6D48E5"));

        // hobbies section
        surfing = (CheckedTextView)findViewById(R.id.surfing);
        surfing.setCheckMarkDrawable(R.drawable.unchecked);
        hiking = (CheckedTextView)findViewById(R.id.hiking);
        hiking.setCheckMarkDrawable(R.drawable.unchecked);
        skiing = (CheckedTextView)findViewById(R.id.skiing);
        skiing.setCheckMarkDrawable(R.drawable.unchecked);
        gaming = (CheckedTextView)findViewById(R.id.gaming);
        gaming.setCheckMarkDrawable(R.drawable.unchecked);


        surfing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!surfing.isChecked()) {
                    surfing.setChecked(true);
                    Surfing = 1;
                    surfing.setCheckMarkDrawable(R.drawable.checked);
                }else{
                    surfing.setChecked(false);
                    Surfing = -1;
                    surfing.setCheckMarkDrawable(R.drawable.unchecked);
                }

            }
        });

        hiking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hiking.isChecked()){
                    hiking.setCheckMarkDrawable(R.drawable.checked);
                    hiking.setChecked(true);
                    Hiking = 1;
                }else {
                    hiking.setCheckMarkDrawable(R.drawable.unchecked);
                    hiking.setChecked(false);
                    Hiking = -1;
                }
            }
        });

        skiing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!skiing.isChecked()){
                    skiing.setChecked(true);
                    skiing.setCheckMarkDrawable(R.drawable.checked);
                    Skiing = 1;
                }else {
                    skiing.setCheckMarkDrawable(R.drawable.unchecked);
                    skiing.setChecked(false);
                    Skiing = -1;
                }
            }
        });

        gaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!gaming.isChecked()){
                    gaming.setChecked(true);
                    gaming.setCheckMarkDrawable(R.drawable.checked);
                    Gaming = 1;
                }else {
                    gaming.setCheckMarkDrawable(R.drawable.unchecked);
                    gaming.setChecked(false);
                    Gaming = -1;
                }
            }
        });


        // smoke and drink section
        smoke = (SegmentedGroup)findViewById(R.id.smoke_button);
        smoke.setTintColor(Color.parseColor("#6D48E5"));
        drink = (SegmentedGroup)findViewById(R.id.drink_button);
        drink.setTintColor(Color.parseColor("#6D48E5"));
        // party
        party = (SegmentedGroup)findViewById(R.id.party_button);
        party.setTintColor(Color.parseColor("#6D48E5"));


        smoke_seekBar = (SeekBar)findViewById(R.id.smoke_seekbar);
        smoke_seekBar.setMax(7);
        smoke_seekbar_text = (TextView)findViewById(R.id.smoke_seekbar_text);
        smoke_seekbar_text.setText("0 days - 7 days");
        smoke_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressV;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressV = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                smoke_seekbar_text.setText(progressV + " days");
                Toast.makeText(getApplicationContext(), "Setting to " + progressV + " days", Toast.LENGTH_SHORT).show();
            }
        });

        drink_seekBar = (SeekBar)findViewById(R.id.drink_seekbar);
        drink_seekBar.setMax(7);
        drink_seekbar_text = (TextView)findViewById(R.id.drink_seekbar_text);
        drink_seekbar_text.setText("0 days - 7 days");
        drink_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressV;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressV = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                drink_seekbar_text.setText(progressV + " days");
                Toast.makeText(getApplicationContext(), "setting to " + progressV + " days", Toast.LENGTH_SHORT).show();
            }
        });

        // spinner for the language spinner
        languageSpinner = (Spinner)findViewById(R.id.languege_spinner);
        ArrayAdapter<CharSequence> languageAdaptor = ArrayAdapter.createFromResource(this,R.array.language_array, android.R.layout.simple_spinner_item);
        languageAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdaptor);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    // Button groups
        bring1 = (RadioButton)findViewById(R.id.bring_button_1);
        bring2 = (RadioButton)findViewById(R.id.bring_button_2);
        bring3 = (RadioButton)findViewById(R.id.bring_button_3);
        pet1 = (RadioButton)findViewById(R.id.pet_button_1);
        pet2 = (RadioButton)findViewById(R.id.pet_button_2);
        smoke1 = (RadioButton)findViewById(R.id.smoke_button_1);
        smoke2 = (RadioButton)findViewById(R.id.smoke_button_2);
        drink1 = (RadioButton)findViewById(R.id.drink_button_1);
        drink2 = (RadioButton)findViewById(R.id.drink_button_2);
        party1 = (RadioButton)findViewById(R.id.party_button_1);
        party2 = (RadioButton)findViewById(R.id.party_button_2);
        party3 = (RadioButton)findViewById(R.id.party_button_3);


    // Button onClick methods
    bring1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bring = 1;
        }
    });
    bring2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bring = -1;
        }
    });
    bring3.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bring = 0;
        }
    });

    pet1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Pet = 1;
        }
    });
    pet2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Pet = -1;
        }
    });

    smoke1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Smoke = 1;
        }
    });
    smoke2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Smoke = -1;
        }
    });

    drink1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Drink = 1;
        }
    });
    drink2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Drink = -1;
        }
    });

    party1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Party = 1;
        }
    });
    party2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Party = -1;
        }
    });
    party3.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Party = 0;
        }
    });
    }

    // report new preference to the firebase
    public void newPreference(){
        preference preference = new preference(Sleep, Clean, Bring, Pet, Surfing, Hiking,
                Skiing, Gaming, Smoke, Drink, Party, language);
        mDatabase.child("Preference").child(uid).setValue(preference);
    }
}
