package com.example.getapro.MyObjects;

public class User {
    private String userName;
    private String district;
    private String email;
    private String number;
    private String photoPath;
    private int resID;
    private String Uid;

    public User(String userName, String district, String email, String number, String photoPath, String uid) {
        this.userName = userName;
        this.district = district;
        this.email = email;
        this.number = number;
        this.photoPath = photoPath;
        Uid = uid;
    }

    public User(String userName, String district, String email, String number, int resID, String uid) {
        this.userName = userName;
        this.district = district;
        this.email = email;
        this.number = number;
        this.resID = resID;
        Uid = uid;
    }

    public User() {
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return district;
    }

    public void setAddress(String district) {
        this.district = district;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getResID() {
        return resID;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}