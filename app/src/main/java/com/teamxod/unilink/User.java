package com.teamxod.unilink;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class User {
    private String name;
    private String picture;
    private String gender;
    private String yearGraduate;
    private String description;
    private List<User> roommates;
    private List<String> favorite_houses;
    private List<String> my_house_posts;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    User(String name, String picture, String gender, String yearGraduate,
         String description,List<User> roommates, List<String> favorite_houses,
         List<String> my_house_posts) {

        this.picture = picture;
        this.name = name;
        this.gender = gender;
        this.yearGraduate = yearGraduate;
        this.description = description;
        this.roommates = roommates;
        this.favorite_houses = favorite_houses;
        this.my_house_posts = my_house_posts;
    }

    public List<User> getRoommates() {
        return roommates;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String photoUrl) {
        this.picture = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getYearGraduate() {
        return yearGraduate;
    }

    public void setYearGraduate(String yearGraduate) {
        this.yearGraduate = yearGraduate;
    }
    public String getDescription() {
        return this.description;
    }

    public List<String> getFavorite_houses() {
        return favorite_houses;
    }

    public List<String> getMy_house_posts() {
        return my_house_posts;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFavorite_houses(List<String> favorite_houses) {
        this.favorite_houses = favorite_houses;
    }

    public void setMy_house_posts(List<String> my_house_posts) {
        this.my_house_posts = my_house_posts;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("picture", picture);
        result.put("gender", gender);
        result.put("yearGraduate", yearGraduate);
        result.put("description", description);
        result.put("roommates", roommates);
        result.put("favorite_houses", favorite_houses);
        result.put("my_house_posts",my_house_posts);
        return result;
    }

    //FIXME
}
