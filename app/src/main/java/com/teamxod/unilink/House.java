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

    private ArrayList<String> housePictures;

    private ArrayList<Room> rooms;

    private boolean pet;


    //public constructor
    public House(String postId, String posterId, String houseType, String name, String houseDescription, ArrayList<String> housePictures, ArrayList<Room> rooms, boolean pet) {
        this.postId = postId;
        this.posterId = posterId;
        this.houseType = houseType;
        this.name = name;
        this.houseDescription = houseDescription;
        this.housePictures = housePictures;
        this.rooms = rooms;
        this.pet = pet;
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

    public ArrayList<String> getHousePictures() {
        return housePictures;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public boolean AllowPet() {
        return pet;
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

    public void addPicture(String newPicture) {
        housePictures.add(newPicture);
    }

    public void removePicture(String Picture) {
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

    public void setPet(boolean pet) {
        this.pet = pet;
    }

}
