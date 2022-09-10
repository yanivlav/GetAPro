package com.example.getapro.MyObjects;

public class User {
    private String userName;
    private String address;
    private String email;
    private String password;
    private long number;
    private String photoPath;
    private int resID;

    public User(String userName, String address, String email, String password, long number, String photoPath) {
        this.userName = userName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.number = number;
        this.photoPath = photoPath;
    }

    public User(String userName, String address, String email, String password, long number, int resID) {
        this.userName = userName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.number = number;
        this.resID = resID;
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
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
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
}



