package com.example.journal.Model;

public class Post {
    String avatar;
    String username;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String image;

    public String getAvatar() {
        return avatar;
    }

    public Post(String avatar, String username, String caption,String image) {
        this.avatar = avatar;
        this.username = username;
        this.caption = caption;
        this.image=image;
    }

    public Post() {
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    String caption;

}
