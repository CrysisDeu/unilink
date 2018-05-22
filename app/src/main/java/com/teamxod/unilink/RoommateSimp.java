package com.teamxod.unilink;


public class RoommateSimp {
    private String rName;

    private String rPicture;

    private String rMajor;

    private int rScore;

    private int rYear;

    private String[] rProTag;

    private String[] rConTag;

    public RoommateSimp(String name, String pic, String major, int score, int year, String[] pro, String[] con){
        rName = name;
        rPicture = pic;
        rMajor = major;
        rScore = score;
        rYear = year;
        rProTag = pro;
        rConTag = con;
    }

    public String getrName() {
        return rName;
    }

    public String getrPicture() {
        return rPicture;
    }

    public String getrMajor() {
        return rMajor;
    }

    public int getrScore() {
        return rScore;
    }

    public int getrYear() {
        return rYear;
    }

    public String[] getrProTag() {
        return rProTag;
    }

    public String[] getrConTag() {
        return rConTag;
    }
}
