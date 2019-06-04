package com.example.fpexample;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Locker {

    private String lockName;
    private String user;
    private boolean available;
    private boolean open;

    public Locker(){
    }

    public Locker(String lockName, String user, boolean available, boolean open) {
        this.lockName = lockName;
        this.user = user;
        this.available = available;
        this.open = open;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isOpen() {
        return open;
    }

    public void setEmpty(boolean empty) {
        this.open = open;
    }
}
