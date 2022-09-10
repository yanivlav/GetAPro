package com.example.getapro.MyObjects;

import android.os.Parcel;
import android.os.Parcelable;

public class Spetz extends User implements Parcelable {
    private String occupation;

    private String district;

    public Spetz(String userName, String district, String email, String number, int resID, String occupation) {
        super(userName, district, email, number, resID);
        this.occupation = occupation;
        this.district =  district;
    }

    public Spetz(String userName, String district, String email, String number, String photoPath, String occupation) {
        super(userName, district, email, number, photoPath);
        this.occupation = occupation;
        this.district =  district;
    }

    public Spetz() {

    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getDistrict() {return district;}

    public void setDistrict(String district) {this.district = district;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}