package com.teamxod.unilink;

import java.sql.Array;
import java.math.*;

public class Recommendation {
    private final int paramNumber = 13;
    int Array1[] = new int[paramNumber];
    int Array2[] = new int[paramNumber];
    /** get data from the database and put them into corresponding slots of arrays.
        Data is from the answer of users' preference survey.
        Data will be processed with coefficient into arrays based on importance.*/
    public void getData(){

    }

    /** Represent arrays as vectors in higher dimension vector space.
     *  Similarity is calculated based on the angle between these two vectors.
     *  Proper lengths of individual component are dealed in getData()
     */
    public double calculate(){
        // final result
        double score = 0;

        // intermediate values
        int innerProduct = 0;
        double lengthOne = 0;
        double lengthTwo = 0;

        for(int i = 0; i < paramNumber; i++) {
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
}
