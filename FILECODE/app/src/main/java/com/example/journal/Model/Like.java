package com.example.journal.Model;

public class Like {
    String idPost;
    String idUser;
    String fullname;
    String image;

    public Like(String idPost, String idUser, String fullname, String image) {
        this.idPost = idPost;
        this.idUser = idUser;
        this.fullname = fullname;
        this.image = image;
    }

    public Like() {
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
