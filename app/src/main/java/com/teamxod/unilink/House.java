package com.teamxod.unilink;

import java.util.ArrayList;
import java.util.List;

class House {

    public House(String postId, String posterId, String houseType, String title, String location,
                 String description,long startDate, String leasingLength,
                 List<String> pictures, List<Room> rooms, String tv, String ac, String bus,
                 String parking, String videoGame, String gym, String laundry, String pet) {

        this.postId = postId;
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
    }

    //private field
    private String postId;

    private String posterId;

    private String houseType;

    private String title;

    private String location;

    private String description;

    private long startDate;

    private String leasingLength;

    private List<String> pictures;

    private List<Room> rooms;

    private String tv;

    private String ac;

    private String bus;

    private String parking;

    private String videoGame;

    private String gym;

    public String getPostId() {
        return postId;
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

    public long getStartDate() {
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

    private String laundry;

    private String pet;

    //public constructor
    House() {
        this.title = "Costa Verde";
        this.location = "3465 Lebon Drive,San Diego";
        pictures = new ArrayList<>();
        pictures.add("http://www.xiugei.com/askimg/116615/tw1468549796178.png");
        pictures.add("http://img.wayes.cn/wayes_sys/imgdata/htmlimg/20160119/bj2016_01_19_09_56_36.jpg");
        pictures.add("http://www.sinaimg.cn/dy/slidenews/24_img/2015_27/66519_1233621_259753.jpg");
        pictures.add("http://www.sinaimg.cn/dy/slidenews/24_img/2015_27/66519_1233620_131928.jpg");
    }

}
