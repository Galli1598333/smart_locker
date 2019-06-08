package com.example.fpexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    private String park, dateS, locker, leaveTime;

    private String durationS;

    private TextView parkTV;
    private TextView dateTV;
    private TextView lockerTV;
    private TextView durationTV;

    private final static String TAG = "profAct";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Remove action bar */
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_profile_card);

        try {
            park = getIntent().getStringExtra("park");
            dateS = getIntent().getStringExtra("date");
            locker = getIntent().getStringExtra("locker");
            leaveTime = getIntent().getStringExtra("leaveTime");

            parkTV = (TextView) findViewById(R.id.idParkTxt);
            parkTV.setText(park);

            dateTV = (TextView) findViewById(R.id.idDateTxt);
            dateTV.setText(dateS);

            lockerTV = (TextView) findViewById(R.id.idLockerTxt);
            lockerTV.setText(locker);

            DateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");

            Date leaveDate = dateFormat.parse(leaveTime);
            Date bookingDate = dateFormat.parse(dateS);
            durationS = getDifference(leaveDate, bookingDate);

            durationTV = (TextView) findViewById(R.id.idDurationTxt);
            durationTV.setText(durationS);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private String getDifference(Date leave, Date booking){
        long difference = leave.getTime() - booking.getTime();

        Log.d(TAG, "startDate : " + leave);
        Log.d(TAG, "endDate : "+ booking);
        Log.d(TAG, "different : " + difference);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = difference / daysInMilli;
        difference = difference % daysInMilli;

        long elapsedHours = difference / hoursInMilli;
        difference = difference % hoursInMilli;

        long elapsedMinutes = difference / minutesInMilli;
        difference = difference % minutesInMilli;

        long elapsedSeconds = difference / secondsInMilli;

        String diff = elapsedHours + "hours, " + elapsedMinutes + "minutes";
        return diff;
    }


}
