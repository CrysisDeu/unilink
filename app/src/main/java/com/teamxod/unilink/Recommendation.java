package com.teamxod.unilink;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.*;
import java.util.ArrayList;

/**
 * This class provides an algorithm to calculate the similarity between two given users.
 */
class Recommendation {

    private final int paramNumber = 12;
    private final double close = 0.2;   // decide if they are close for a parameter
    private final DatabaseReference mDatabase;
    private DatabaseReference user1Data;
    private DatabaseReference user2Data;
    private preference preference1;
    private preference preference2;
    private ArrayList<String> tagList;

    /**
     * Arrays are used to store users' parameter.
     * The order is : Bring, Pet, Smoke, Drink, Party, sleepTime, cleanTime, surfing, hiking,
     *                skiing, gaming, language
     */
    private double Array1[];
    private double Array2[];

    public Recommendation(String user1, String user2){
        Array1 = new double[paramNumber];
        Array2 = new double[paramNumber];
        tagList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user1Data = mDatabase.child("Preference").child("user1");
        user2Data = mDatabase.child("Preference").child("user2");
    }

    /** get data from the database and put them into corresponding slots of arrays.
        Data is from the answer of users' preference survey.
        Data will be processed with coefficient into arrays based on importance.*/
    private void getData(){
        user1Data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                preference1 = dataSnapshot.getValue(preference.class);
                Array1[0] = preference1.getBring();
                Array1[1] = preference1.getPet();
                Array1[2] = preference1.getSmoke();
                Array1[3] = preference1.getDrink();
                Array1[4] = preference1.getParty();
                Array1[5] = preference1.getSleepTime();
                Array1[6] = preference1.getCleanTime();
                Array1[7] = preference1.getSurfing();
                Array1[8] = preference1.getHiking();
                Array1[9] = preference1.getSkiing();
                Array1[10] = preference1.getGaming();
                Array1[11] = preference1.getLanguage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        user2Data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                preference2 = dataSnapshot.getValue(preference.class);
                Array2[0] = preference2.getBring();
                Array2[1] = preference2.getPet();
                Array2[2] = preference2.getSmoke();
                Array2[3] = preference2.getDrink();
                Array2[4] = preference2.getParty();
                Array2[5] = preference2.getSleepTime();
                Array2[6] = preference2.getCleanTime();
                Array2[7] = preference2.getSurfing();
                Array2[8] = preference2.getHiking();
                Array2[9] = preference2.getSkiing();
                Array2[10] = preference2.getGaming();
                Array2[11] = preference2.getLanguage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // if their languages are different, we reassign the languages to be -1 and 1.
        // This is because languages have no natural ordering.
        if(Array1[11] != Array2[11]) {
            Array1[11] = 1;
            Array2[11] = -1;
        }
    }

    /** Represent arrays as vectors in higher dimension vector space.
     *  Similarity is calculated based on the angle between these two vectors.
     *  Proper lengths of individual component are dealed in getData()
     */
    public double calculate(){
        getData();
        // final result
        double score = 0;

        // intermediate values
        int innerProduct = 0;
        double lengthOne = 0;
        double lengthTwo = 0;

        for(int i = 0; i < paramNumber; i++) {
            // add to Tags
            innerProduct += Array1[i]*Array2[i];
            lengthOne += Math.pow(Array1[i], 2);
            lengthTwo += Math.pow(Array2[i],2);
        }

        lengthOne = Math.pow(lengthOne, 0.5);
        lengthTwo = Math.pow(lengthTwo, 0.5);

        // find the cosine value and the corresponding angle
        score = innerProduct / (lengthOne * lengthTwo);
        score = Math.toDegrees(Math.acos(score));

        // de-uniform the distribution of all angles between 0 and 180. We choose log family.
        score += 1;
        double base = Math.pow(180, 1/100);
        score = Math.log(score) / Math.log(base);
        return score;
    }

    // implemented to only be used once!
    public ArrayList<String> getTagList() {
        getData();
        
        if(Array1[1] == 1 && Array2[1] == 1) {
            tagList.add("Love pets");
        }
        if(Array1[2] - Array2[2] < close) {
            tagList.add("Close in smoking habits");
        }
        if(Array1[3] - Array2[3] < close){
            tagList.add("Close in drinking habits");
        }
        if(Array1[5] - Array2[5] < close){
            tagList.add("Close in Sleep time");
        }
        if(Array1[7] == 1 && Array2[7] == 1){
            tagList.add("Love surfing");
        }
        if(Array1[8] - Array2[8] == 1){
            tagList.add("Love hiking");
        }
        if(Array1[9] - Array2[9] == 1){
            tagList.add("Love skiing");
        }
        if(Array1[10] - Array2[10] == 1){
            tagList.add("Love gaming");
        }
        if(Array1[11] - Array2[11] == 1){
            tagList.add("Same language");
        }
        return tagList;
    }
}
