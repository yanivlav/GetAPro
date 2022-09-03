package com.example.getapro;

import java.io.Serializable;

public class Form implements Serializable {
    private String clientName;
    private String location;
    private String description;
    private long number;
    private String photoPath;
    private int photoPathInt;


    //ResIdImage
    public Form(String name, String location, int photoPathInt, long  number, String  desc) {
        this.clientName = name;
        this.location = location;
        this.photoPathInt = photoPathInt;
        this.number = number;
        this.description = desc;
    }

    //Gallery
    public Form(String name, String location, String photoPath, long  number, String  desc) {
        this.clientName = name;
        this.location = location;
        this.photoPath = photoPath;
        this.number = number;
        this.description = desc;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getPhotoPathInt() {
        return photoPathInt;
    }

    public void setPhotoPathInt(int photoPathInt) {
        this.photoPathInt = photoPathInt;
    }
}

