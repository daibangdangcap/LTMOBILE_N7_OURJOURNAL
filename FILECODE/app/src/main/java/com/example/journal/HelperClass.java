package com.example.journal;

public class HelperClass {
    public String id,fullname, password, email, phone, gender,image;

    public HelperClass()
    {}
    public HelperClass(String id,String fullname, String password, String email, String phone, String  gender,String image) {
        this.id=id;
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.image=image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
