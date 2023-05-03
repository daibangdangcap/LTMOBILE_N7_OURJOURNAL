package com.example.journal.Model;

public class FriendsList {
    String nameFriendItem_FriendsList;
    String avatar_FriendsList;
    String id;

    public FriendsList(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FriendsList(String id, String nameFriendItem_FriendsList, String avatar_FriendsList) {
        this.id=id;
        this.nameFriendItem_FriendsList = nameFriendItem_FriendsList;
        this.avatar_FriendsList = avatar_FriendsList;
    }

    public FriendsList() {
    }

    public String getNameFriendItem_FriendsList() {
        return nameFriendItem_FriendsList;
    }

    public void setNameFriendItem_FriendsList(String nameFriendItem_FriendsList) {
        this.nameFriendItem_FriendsList = nameFriendItem_FriendsList;
    }

    public String getAvatar_FriendsList() {
        return avatar_FriendsList;
    }

    public void setAvatar_FriendsList(String avatar_FriendsList) {
        this.avatar_FriendsList = avatar_FriendsList;
    }
}
