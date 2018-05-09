package com.teamxod.unilink;

import java.util.ArrayList;

public class User {
    private String userPhoto;
    private String name;
    private String gender;
    private int year_of_grad;
    private ArrayList <Integer> favorite_houses;
    private ArrayList <Integer> my_house_posts;
    private ArrayList <Integer> favorite_roommates;
    private ArrayList <Integer> my_roommate_posts;

    public User() {
    }

    public User(String userPhoto, String name, String gender, int year_of_grad,
                ArrayList <Integer> favorite_houses, ArrayList <Integer> my_house_posts,
                ArrayList <Integer>favorite_roommates, ArrayList <Integer> my_roommate_posts) {
        this.userPhoto = userPhoto;
        this.name = name;
        this.gender = gender;
        this.year_of_grad = year_of_grad;
        this.favorite_houses = favorite_houses;
        this.my_house_posts = my_house_posts;
        this.favorite_roommates = favorite_roommates;
        this. my_roommate_posts = my_roommate_posts;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String photoUrl) {
        this.userPhoto = photoUrl;
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

    public int getYear_of_grad() {
        return year_of_grad;
    }

    public void setYear_of_grad(int year_of_grad) {
        this.year_of_grad = year_of_grad;
    }

    //FIXME
}
