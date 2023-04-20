package com.example.journal.Model;

public class Post {
    String avatar;
    String username;

    public String getAvatar() {
        return avatar;
    }

    public Post(String avatar, String username, String caption) {
        this.avatar = avatar;
        this.username = username;
        this.caption = caption;
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
