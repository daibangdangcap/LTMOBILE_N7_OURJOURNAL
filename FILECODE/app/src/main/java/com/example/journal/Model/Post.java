package com.example.journal.Model;

import java.util.ArrayList;

public class Post {
    private String postKey;
    private String Caption;
    //private ArrayList<String> Photos;
    private String Image;
    private String userId;
    private String userName;
    private String location = null;
    //private ArrayList<String> userLiked;
    //private ArrayList<String> userComment;
    private Object timeStamp;

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Post(String caption, String Image, String userId, String userName, String location) {

        this.Caption = caption;
        this.Image = Image;
        this.userId = userId;
        this.userName = userName;
        this.location = "";
    }

    public Post() {
    }



}
