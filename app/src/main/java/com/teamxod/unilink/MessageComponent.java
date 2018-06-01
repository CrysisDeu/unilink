package com.teamxod.unilink;

class MessageComponent {
    private String userPhoto;
    private String name;
    private String text;
    private String messagephoto;

    public MessageComponent() {
    }

    public MessageComponent(String userPhoto, String text, String name, String messagephoto) {
        this.userPhoto = userPhoto;
        this.text = text;
        this.name = name;
        this.messagephoto = messagephoto;
    }


    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String photoUrl) {
        this.userPhoto = photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getmessagePhoto() {
        return messagephoto;
    }

    public void setMessagephoto(String photoUrl) {
        this.messagephoto = photoUrl;
    }
}

