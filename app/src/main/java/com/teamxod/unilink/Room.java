package com.teamxod.unilink;

import java.util.ArrayList;

public class Room {
    private String roomId;

    private String roomType;

    private ArrayList<String> roomImages;

    private int price;

    private String startDate;

    private int duration;

    private String roomDescription;

    private boolean furniture;

    private boolean walkInCloset;

    private boolean individualBathroom;

    private int water;

    private int electricity;

    private int internet;


    //setter method
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public void setFurniture(boolean furniture) {
        this.furniture = furniture;
    }

    public void setWalkInCloset(boolean walkInCloset) {
        this.walkInCloset = walkInCloset;
    }

    public void setIndividualBathroom(boolean individualBathroom) {
        this.individualBathroom = individualBathroom;
    }

    public void addPicture(String newPicture) {
        roomImages.add(newPicture);
    }

    public void removePicture(String Picture) {
        roomImages.remove(Picture);
    }

    public void setWater(int water) {
        this.water = water;
    }

    public void setElectricity(int electricity) {
        this.electricity = electricity;
    }

    public void setInternet(int internet) {
        this.internet = internet;
    }


    //public constructor
    public Room(String roomType, ArrayList<String> roomImages, int price, String roomId, String startDate, int duration, String roomDescription, boolean furniture, boolean walkInCloset, boolean individualBathroom, int water, int electricity, int internet) {
        this.roomType = roomType;
        this.roomImages = roomImages;
        this.price = price;
        this.roomId = roomId;
        this.startDate = startDate;
        this.duration = duration;
        this.roomDescription = roomDescription;
        this.furniture = furniture;
        this.walkInCloset = walkInCloset;
        this.individualBathroom = individualBathroom;
        this.water = water;
        this.electricity = electricity;
        this.internet = internet;
    }

    //getter method
    public String getRoomId() {
        return roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public ArrayList<String> getRoomImages() {
        return roomImages;
    }

    public int getPrice() {
        return price;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getDuration() {
        return duration;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public boolean hasFurniture() {
        return furniture;
    }

    public boolean hasWalkInCloset() {
        return walkInCloset;
    }

    public boolean hasIndividualBathroom() {
        return individualBathroom;
    }

    public int getWater() {
        return water;
    }

    public int getElectricity() {
        return electricity;
    }

    public int getInternet() {
        return internet;
    }
}
