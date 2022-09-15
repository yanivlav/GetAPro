package com.example.getapro.MyObjects;

import java.io.Serializable;

public class Form implements Serializable {//extends User implements Serializable {
    private String description;
    private String issueImage;
    private int issueImageResID;
    private String selectedSpetzUid;
    private String categoty;
    private String address;

    public Form(String description, int imgResID, String selectedSpetzEmail, String categoty, String address) {
        super();
        this.description = description;
        this.issueImageResID = imgResID;
        this.selectedSpetzUid = selectedSpetzEmail;
        this.categoty = categoty;
        this.address = address;
    }

    public Form(String description, String issueImage, String selectedSpetzEmail, String categoty, String address) {
        this.description = description;
        this.issueImage = issueImage;
        this.selectedSpetzUid = selectedSpetzEmail;
        this.categoty = categoty;
        this.address = address;
    }

    public Form() {}


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIssueImage() {
        return issueImage;
    }

    public void setIssueImage(String issueImage) {
        this.issueImage = issueImage;
    }

    public int getIssueImageResID() {
        return issueImageResID;
    }

    public void setIssueImageResID(int issueImageResID) {
        this.issueImageResID = issueImageResID;
    }

    public String getSelectedSpetzUid() {
        return selectedSpetzUid;
    }
    public void setSelectedSpetzUid(String selectedSpetzUid) {
        this.selectedSpetzUid = selectedSpetzUid;
    }

    public String getCategoty() {
        return categoty;
    }

    public void setCategoty(String categoty) {
        this.categoty = categoty;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}