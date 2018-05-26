package com.teamxod.unilink;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String picture;
    private String gender;
    private String yearGraduate;
    private String description;

    //temporary constructor
    User() {
    }

    User(String name, String picture, String gender, String yearGraduate,
         String description) {
        this.picture = picture;
        this.name = name;
        this.gender = gender;
        this.yearGraduate = yearGraduate;
        this.description = description;
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

    //FIXME
}
