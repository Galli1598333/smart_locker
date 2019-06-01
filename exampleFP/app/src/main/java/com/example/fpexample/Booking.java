package com.example.fpexample;


import com.google.firebase.Timestamp;

public class Booking {

    private String user;
    private String park;
    private String date;

    public Booking(String user, String park, String date) {
        this.user = user;
        this.park = park;
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPark() {
        return park;
    }

    public void setPark(String park) {
        this.park = park;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
