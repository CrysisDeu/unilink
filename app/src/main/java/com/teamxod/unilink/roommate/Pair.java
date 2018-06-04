package com.teamxod.unilink.roommate;

public class Pair {
    private int key;
    private double value;

    public Pair(int key, double value) {
        this.key = key;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public int getKey() {
        return key;
    }
}
