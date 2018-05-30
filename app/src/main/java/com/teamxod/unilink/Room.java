package com.teamxod.unilink;

class Room {

    private String roomType;

    private int price;

    Room(String roomType, int price) {
        this.roomType = roomType;
        this.price = price;
    }

    Room(){
       //empty constructor for firebaase
    }
    
    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
