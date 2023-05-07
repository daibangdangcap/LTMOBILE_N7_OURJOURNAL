package com.example.journal.Model;

public class Comment {
    String idComment;
    String idUserName;
    String fullname;
    String image;
    String commentContent;

    public Comment() {
    }

    public Comment(String idComment, String idUserName, String fullname, String image, String commentContent) {
        this.idComment = idComment;
        this.idUserName = idUserName;
        this.fullname = fullname;
        this.image = image;
        this.commentContent = commentContent;
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public String getIdUserName() {
        return idUserName;
    }

    public void setIdUserName(String idUserName) {
        this.idUserName = idUserName;
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

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
