package com.example.getapro.MyObjects;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Form implements Serializable, Parcelable {//extends User implements Serializable {
    private String description;
    private String issueImage;
    private int issueImageResID;
    private String selectedSpetzUid;
    private String categoty;
    private String address;
    private String spetzPhone;
    private String spetzName;
    private String usersPhone;
    private String usersName;

//    public Form(String description, int imgResID, String selectedSpetzEmail, String categoty, String address) {
//        super();
//        this.description = description;
//        this.issueImageResID = imgResID;
//        this.selectedSpetzUid = selectedSpetzEmail;
//        this.categoty = categoty;
//        this.address = address;
////        this.spetzName = spetzName;
////        this.spetzPhone = spetzPhone;
////        this.usersName = usersName;
////        this.usersPhone = usersPhone;
//    }

    public Form(String description, String issueImage, String selectedSpetzEmail, String categoty, String address,String spetzPhone, String spetzName, String usersPhone, String usersName) {
        this.description = description;
        this.issueImage = issueImage;
        this.selectedSpetzUid = selectedSpetzEmail;
        this.categoty = categoty;
        this.address = address;
        this.spetzName = spetzName;
        this.spetzPhone = spetzPhone;
        this.usersName = usersName;
        this.usersPhone = usersPhone;
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

    public String getSpetzPhone() {
        return spetzPhone;
    }

    public void setSpetzPhone(String spetzPhone) {
        this.spetzPhone = spetzPhone;
    }

    public String getSpetzName() {
        return spetzName;
    }

    public void setSpetzName(String spetzName) {
        this.spetzName = spetzName;
    }

    public String getUsersPhone() {
        return usersPhone;
    }

    public void setUsersPhone(String usersPhone) {
        this.usersPhone = usersPhone;
    }

    public String getUsersName() {
        return usersName;
    }

    public void setUsersName(String usersName) {
        this.usersName = usersName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}