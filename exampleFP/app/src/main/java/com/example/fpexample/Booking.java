package com.example.fpexample;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Booking implements Serializable {

    private String user;
    private String park;
    private String date;
    private boolean empty;
    private String lockHash;

    public Booking(){ }

    public Booking(String user, String park, String date, boolean empty, String lockHash) {
        this.user = user;
        this.park = park;
        this.date = date;
        this.empty = empty;
        this.lockHash = lockHash;
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

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public String getLockHash() {
        return lockHash;
    }

    public void setLockHash(String lockHash) {
        this.lockHash = lockHash;
    }
}
