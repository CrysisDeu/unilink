package com.teamxod.unilink;

public class preference {
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

    public preference(){
    }

    public preference(double sleepTime, double cleanTime, int bring, int pet, int surfing,
                      int hiking, int skiing, int gaming, double smoke, double drink, int party, int language)
    {
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

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public double getCleanTime() {
        return cleanTime;
    }

    public void setCleanTime(int cleanTime) {
        this.cleanTime = cleanTime;
    }

    public int getBring() {
        return bring;
    }

    public void setBring(int bring) {
        this.bring = bring;
    }

    public int getPet() {
        return pet;
    }

    public void setPet(int pet) {
        this.pet = pet;
    }

    public int getSurfing() {
        return surfing;
    }

    public void setSurfing(int surfing) {
        this.surfing = surfing;
    }

    public int getHiking() {
        return hiking;
    }

    public void setHiking(int hiking) {
        this.hiking = hiking;
    }

    public int getSkiing() {
        return skiing;
    }

    public void setSkiing(int skiing) {
        this.skiing = skiing;
    }

    public int getGaming() {
        return gaming;
    }

    public void setGaming(int gaming) {
        this.gaming = gaming;
    }

    public double getSmoke() {
        return smoke;
    }

    public void setSmoke(int smoke) {
        this.smoke = smoke;
    }

    public double getDrink() {
        return drink;
    }

    public void setDrink(int drink) {
        this.drink = drink;
    }

    public int getParty() {
        return party;
    }

    public void setParty(int party) {
        this.party = party;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }
}
