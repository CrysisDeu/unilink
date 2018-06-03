package com.teamxod.unilink.house;

import com.teamxod.unilink.roommate.Room;

import java.util.List;

public class House {


    public House(String posterId, String houseType, String title, String location,
                 String description, String startDate, String leasingLength,
                 List<String> pictures, List<Room> rooms, String tv, String ac, String bus,
                 String parking, String videoGame, String gym, String laundry, String pet,
                 String numBedroom, String numBathroom) {

        this.posterId = posterId;
        this.houseType = houseType;
        this.title = title;
        this.location = location;
        this.description = description;
        this.startDate = startDate;
        this.leasingLength = leasingLength;
        this.pictures = pictures;
        this.rooms = rooms;
        this.tv = tv;
        this.ac = ac;
        this.bus = bus;
        this.parking = parking;
        this.videoGame = videoGame;
        this.gym = gym;
        this.laundry = laundry;
        this.pet = pet;
        this.numBedroom = numBedroom;
        this.numBathroom = numBathroom;
    }

    public House() {
        // Default constructor required for calls to DataSnapshot.getValue(House.class)
    }
    //private field

    private String numBedroom;

    private String numBathroom;

    private String posterId;

    private String houseType;

    private String title;

    private String location;

    private String description;

    private String startDate;

    private String leasingLength;

    private List<String> pictures;

    private List<Room> rooms;

    private String tv;

    private String ac;

    private String bus;

    private String parking;

    private String videoGame;

    private String gym;

    private String laundry;

    private String pet;

    public String getNumBedroom() {
        return numBedroom;
    }

    public String getNumBathroom() {
        return numBathroom;
    }

    public String getPosterId() {
        return posterId;
    }

    public String getHouseType() {
        return houseType;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getLeasingLength() {
        return leasingLength;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public String getTv() {
        return tv;
    }

    public String getAc() {
        return ac;
    }

    public String getBus() {
        return bus;
    }

    public String getParking() {
        return parking;
    }

    public String getVideoGame() {
        return videoGame;
    }

    public String getGym() {
        return gym;
    }

    public String getLaundry() {
        return laundry;
    }

    public String getPet() {
        return pet;
    }


}
