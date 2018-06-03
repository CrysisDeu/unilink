package com.teamxod.unilink.roommate;

import com.teamxod.unilink.user.Preference;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * This class provides an algorithm to calculate the similarity between two given users.
 */
class Recommendation {

    private final int paramNumber = 12;
    private final double close = 0.2;   // decide if they are close for a parameter
    private Preference preference1;
    private Preference preference2;

    private ArrayList<String> tagList;
    private double score;

    /**
     * Arrays are used to store users' parameter.
     * The order is : Bring, Pet, Smoke, Drink, Party, sleepTime, cleanTime, surfing, hiking,
     *                skiing, gaming, language
     */
    private double Array1[];
    private double Array2[];
    double innerProduct = 0;
    double lengthOne = 0;
    double lengthTwo = 0;

    Recommendation(Preference user1, Preference user2){
        Array1 = new double[paramNumber];
        Array2 = new double[paramNumber];
        tagList = new ArrayList<>();
        preference1 = user1;
        preference2 = user2;
        loadUserPreference();
    }

    /** get data from the database and put them into corresponding slots of arrays.
        Data is from the answer of users' Preference survey.
        Data will be processed with coefficient into arrays based on importance.*/
    public double getScore(){
        return score / 10;
    }

    public ArrayList<String> getTagList() {
        return tagList;
    }

    private void loadUserPreference() {

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
        for(int i = 0; i < paramNumber; i++) {
            lengthOne += Math.pow(Array1[i], 2);
        }

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

        // if their languages are different, we reassign the languages to be -1 and 1.
        // This is because languages have no natural ordering.
        if(Array1[11] != Array2[11]) {
            Array1[11] = 1;
            Array2[11] = -1;
        }

        calculate();
        setTagList();
    }

    /** Represent arrays as vectors in higher dimension vector space.
     *  Similarity is calculated based on the angle between these two vectors.
     *  Proper lengths of individual component are dealed in getData()
     */
    private void calculate(){
        for(int i = 0; i < paramNumber; i++) {
            innerProduct += Array1[i]*Array2[i];
            lengthOne += Math.pow(Array1[i], 2);
            lengthTwo += Math.pow(Array2[i],2);
        }
        // calculate the angle
        lengthOne = Math.pow(lengthOne, 0.5);
        lengthTwo = Math.pow(lengthTwo, 0.5);
        score = innerProduct / (lengthOne * lengthTwo);
        score = Math.toDegrees(Math.acos(score));
        // avoid score = 0
        score += 1;
        double base = Math.pow(181, 0.01);
        // de-uniform the score distribution
        score = Math.log(score) / Math.log(base);
        DecimalFormat round = new DecimalFormat(".#");
        round.setRoundingMode(RoundingMode.UP);
        score = Double.parseDouble(round.format(score));
    }

    // implemented to only be used once!
    private void setTagList() {
        
        if(Array1[1] == 1 && Array2[1] == 1) {
            tagList.add("Love pets");
        }
        if(Array1[2] > -1 && Array2[2] > -1) {
            tagList.add("smoke");
        }
        if(Array1[2] == -1 && Array2[2] == -1) {
            tagList.add("not smoke");
        }
        if(Array1[3] > -1 && Array2[3] > -1){
            tagList.add("drink");
        }
        if(Array1[3] == -1 && Array2[3] == -1){
            tagList.add("not drink");
        }
        if(Array1[5] - Array2[5] < close){
            tagList.add("similar sleep time");
        }
        if(Array1[7] == 1 && Array2[7] == 1){
            tagList.add("Surf");
        }
        if(Array1[8] == 1 && Array2[8] == 1){
            tagList.add("Hike");
        }
        if(Array1[9] == 1 && Array2[9] == 1){
            tagList.add("Ski");
        }
        if(Array1[10] == 1 && Array2[10] == 1){
            tagList.add("Game");
        }
        if(Array1[11] == Array2[11]){
            tagList.add("Same language");
        }
    }
}
