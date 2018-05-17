package com.teamxod.unilink;

public class HousePost {
    //item shown below the image
    private String room_type;

    private String room_title;

    private String room_price;

    private String room_location;

    private int imageResourceId;





    public HousePost(String p_room_type, String p_room_title, String p_room_price,
                String p_room_location, int p_imageResourceId)
    {
        room_type = p_room_type;
        room_title = p_room_title;
        room_price = p_room_price;
        room_location = p_room_location;
        imageResourceId = p_imageResourceId;

    }



    public String getRoom_type() {
        return room_type;
    }

    public String getRoom_title() {
        return room_title;
    }

    public String getRoom_price() {
        return room_price;
    }

    public String getRoom_location() {
        return room_location;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }


}
