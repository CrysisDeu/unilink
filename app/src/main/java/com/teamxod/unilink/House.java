package com.teamxod.unilink;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import java.net.URL;
import java.util.ArrayList;

public class House {

    //private field
    private String postId;

    private String posterId;

    private String houseType;

    private String name;

    private String location;

    private String houseDescription;

    private ArrayList<URL> housePictures;

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
    House() {
        this.name = "Costa Verde";
        this.location = "3465 Lebon Drive,San Diego";
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

    public ArrayList<URL> getHousePictures() {
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

    public void addPicture(URL newPicture) {
        housePictures.add(newPicture);
    }

    public void removePicture(URL Picture) {
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
