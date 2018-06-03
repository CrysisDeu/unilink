package com.teamxod.unilink.user;

public class Preference {
    private double sleepTime;
    private double cleanTime;
    private int bring;
    private int pet;
    private int surfing;
    private int hiking;
    private int skiing;
    private int gaming;
    private double smoke;
    private double drink;
    private int party;
    private int language;

    // constructor

    public Preference() {
    }

    public Preference(double sleepTime, double cleanTime, int bring, int pet, int surfing,
                      int hiking, int skiing, int gaming, double smoke, double drink, int party, int language) {
        this.sleepTime = sleepTime;
        this.cleanTime = cleanTime;
        this.bring = bring;
        this.pet = pet;
        this.surfing = surfing;
        this.hiking = hiking;
        this.skiing = skiing;
        this.gaming = gaming;
        this.smoke = smoke;
        this.drink = drink;
        this.party = party;
        this.language = language;
    }

    // getter and setter
    public double getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(double sleepTime) {
        this.sleepTime = sleepTime;
    }

    public double getCleanTime() {
        return cleanTime;
    }

    public void setCleanTime(double cleanTime) {
        this.cleanTime = cleanTime;
    }

    public int getBring() {
        return bring;
    }

    public int getPet() {
        return pet;
    }

    public int getSurfing() {
        return surfing;
    }

    public int getHiking() {
        return hiking;
    }

    public int getSkiing() {
        return skiing;
    }

    public int getGaming() {
        return gaming;
    }

    public double getSmoke() {
        return smoke;
    }

    public void setSmoke(double smoke) {
        this.smoke = smoke;
    }

    public double getDrink() {
        return drink;
    }

    public void setDrink(double drink) {
        this.drink = drink;
    }

    public int getParty() {
        return party;
    }

    public int getLanguage() {
        return language;
    }

}
