package com.example.getapro.MyObjects;

import java.io.Serializable;

public class Form extends User implements Serializable {
    private String description;
    private String issueImage;
    private int issueImageResID;

    public Form(String userName, String address, String email, String number, String photoPath, String description, String issueImage) {
        super(userName, address, email, number, photoPath);
        this.description = description;
        this.issueImage = issueImage;
    }

    public Form(String userName, String address, String email, String number, int resID, String description, int issueImageResID) {
        super(userName, address, email, number, resID);
        this.description = description;
        this.issueImageResID = issueImageResID;
    }

    public Form(String description, int imgResID) {
        super();
        this.description = description;
        this.issueImageResID = imgResID;
    }

    public Form() {

    }




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

}