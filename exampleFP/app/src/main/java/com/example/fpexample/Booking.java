package com.example.fpexample;

public class Booking {

    private String park;
    private String startTime;
    private String endTime;

    public Booking(String park, String start, String end) {
        this.park = park;
        this.startTime = start;
        this.endTime = end;
    }

    public String getPark() {
        return park;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setPark(String park) {
        this.park = park;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
