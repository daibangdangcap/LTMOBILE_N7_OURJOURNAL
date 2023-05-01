package com.example.journal;

public class HelperClass {
    public String fullname, password, email, phone, gender,image;

    public HelperClass()
    {}
    public HelperClass(String fullname, String password, String email, String phone, String  gender,String image) {
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.image=image;
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
