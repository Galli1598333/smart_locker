package com.example.fpexample;

public class ToBook {

    private String parkName;
    private String parkAddress;

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
