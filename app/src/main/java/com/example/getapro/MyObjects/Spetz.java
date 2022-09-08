package com.example.getapro.MyObjects;

import android.os.Parcel;
import android.os.Parcelable;

public class Spetz extends User implements Parcelable {
    private String occupation;

    public Spetz(String userName, String address, String email, String password, long number, int resID, String occupation) {
        super(userName, address, email, password, number, resID);
        this.occupation = occupation;
    }

    public Spetz(String userName, String address, String email, String password, long number, String photoPath, String occupation) {
        super(userName, address, email, password, number, photoPath);
        this.occupation = occupation;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
