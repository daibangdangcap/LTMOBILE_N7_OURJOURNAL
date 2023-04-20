package com.example.journal.Model;

public class FriendsList {
    String nameFriendItem_FriendsList;
    String avatar_FriendsList;

    public FriendsList(String nameFriendItem_FriendsList, String avatar_FriendsList) {
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
