package com.teamxod.unilink;

import java.util.ArrayList;

public class House {

    //private field
    private String postId;

    private String posterId;

    private String houseType;

    private String name;

    private String location;

    private String houseDescription;

    private ArrayList<Integer> housePictures;

    private ArrayList<Room> rooms;

    private boolean tv;

    private boolean wifi;

    private boolean bus;

    private boolean gym;

    private boolean ac;

    private boolean game;

    public boolean hasWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean hasBus() {
        return bus;
    }

    public void setBus(boolean bus) {
        this.bus = bus;
    }

    public boolean hasGym() {
        return gym;
    }

    public void setGym(boolean gym) {
        this.gym = gym;
    }

    public boolean hasAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }

    public boolean hasGame() {
        return game;
    }

    public void setGame(boolean game) {
        this.game = game;
    }


    //public constructor
    public House() {
        this.name = "Costa Verde";
        this.location = "3465 Lebon Drive";
    }


    //get methods
    public String getPostId() {
        return postId;
    }

    public String getPosterId() {
        return posterId;
    }

    public String getHouseType() {
        return houseType;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return houseDescription;
    }

    public ArrayList<Integer> getHousePictures() {
        return housePictures;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public boolean hasTv() {
        return tv;
    }


    //set&edit methods
    public void setHouseType(String newHouseType) {
        houseType = newHouseType;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setLocation(String newLocation) {
        location = newLocation;
    }

    public void addPicture(Integer newPicture) {
        housePictures.add(newPicture);
    }

    public void removePicture(Integer Picture) {
        housePictures.remove(Picture);
    }

    public void setHouseDescription(String newDescription) {
        houseDescription = newDescription;
    }

    public void addRoom(Room newRoom) {
        rooms.add(newRoom);
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    public void setTv(boolean tv) {
        this.tv = tv;
    }

}
