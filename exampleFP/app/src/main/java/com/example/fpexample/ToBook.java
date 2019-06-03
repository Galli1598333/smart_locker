package com.example.fpexample;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ToBook {

    private String parkName;
    private String parkAddress;

    public ToBook(){ }

    public ToBook(String parkName, String parkAddress) {
        this.parkName = parkName;
        this.parkAddress = parkAddress;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getParkAddress() {
        return parkAddress;
    }

    public void setParkAddress(String parkAddress) {
        this.parkAddress = parkAddress;
    }

}
