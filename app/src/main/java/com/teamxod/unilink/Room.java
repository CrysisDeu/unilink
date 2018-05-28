package com.teamxod.unilink;

class Room {

    private String roomType;

    private int price;

    public Room(String roomType, int price) {
        this.roomType = roomType;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getPrice() {
        return price;
    }
}
