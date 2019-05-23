package com.example.fpexample;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;

import android.app.ActionBar;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeTV;

    // Auth book page
    private Button authenticate;
    private TextView booking;
    private RecyclerView bookedRV;
    private BookingsAdapter bookingAdapter;
    private List<Booking> bookingList;

    // To Book Page
    private TextView toBookTV;
    private TextView parkToBookTV;
    private RecyclerView toBookRV;
    private ToBookAdapter toBookAdapter;
    private List<ToBook> toBookList;

    // Profile page
    private TextView profileTV;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        welcomeTV.setVisibility(View.VISIBLE);
                        booking.setVisibility(View.VISIBLE);
                        bookedRV.setVisibility(View.VISIBLE);
                        toBookTV.setVisibility(View.GONE);
                        parkToBookTV.setVisibility(View.GONE);
                        toBookRV.setVisibility(View.GONE);
                        profileTV.setVisibility(View.GONE);
                        return true;
                    case R.id.navigation_account:
                        welcomeTV.setVisibility(View.GONE);
                        booking.setVisibility(View.GONE);
                        bookedRV.setVisibility(View.GONE);
                        toBookTV.setVisibility(View.GONE);
                        parkToBookTV.setVisibility(View.GONE);
                        toBookRV.setVisibility(View.GONE);
                        profileTV.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.navigation_book:
                        welcomeTV.setVisibility(View.GONE);
                        booking.setVisibility(View.GONE);
                        bookedRV.setVisibility(View.GONE);
                        toBookTV.setVisibility(View.VISIBLE);
                        parkToBookTV.setVisibility(View.VISIBLE);
                        toBookRV.setVisibility(View.VISIBLE);
                        profileTV.setVisibility(View.GONE);
                        return true;
                    case R.id.navigation_settings:
                        welcomeTV.setVisibility(View.GONE);
                        booking.setVisibility(View.GONE);
                        bookedRV.setVisibility(View.GONE);
                        toBookTV.setVisibility(View.GONE);
                        parkToBookTV.setVisibility(View.GONE);
                        toBookRV.setVisibility(View.GONE);
                        profileTV.setVisibility(View.GONE);
                        return true;
                }
                return false;
            }
        };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // GENERAL SETTINGS
        /* Remove action bar */
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // END GENERAL SETTINGS

        welcomeTV = (TextView) findViewById(R.id.welcomeView);

        // HOME PAGE

        booking = (TextView) findViewById(R.id.booking_label);

        bookedRV = (RecyclerView) findViewById(R.id.bookedRV);
        bookedRV.setLayoutManager(new LinearLayoutManager(this));

        bookingList = new ArrayList<Booking>();
        bookingList.add(new Booking("Parco della Caffarella", "05:00 pm", "06:45 pm"));
        bookingList.add(new Booking("Villa Borghese", "07:00 am", "08:00 am"));

        bookingAdapter = new BookingsAdapter(bookingList, this);
        bookedRV.setAdapter(bookingAdapter);

        // END HOME PAGE

        // START BOOK PAGE

        parkToBookTV = (TextView) findViewById(R.id.parkToBook_label);
        toBookTV = (TextView) findViewById(R.id.bookView);

        toBookRV = (RecyclerView) findViewById(R.id.toBookRV);
        toBookRV.setLayoutManager(new LinearLayoutManager(this));

        toBookList = new ArrayList<ToBook>();
        toBookList.add(new ToBook("Parco della Caffarella", "Via Latina"));
        toBookList.add(new ToBook("Villa Borghese", "Viale Pietro Canonica"));

        toBookAdapter = new ToBookAdapter(this, toBookList);
        toBookRV.setAdapter(toBookAdapter);

        // END BOOK PAGE

        // START ACCOUNT PAGE

        profileTV = (TextView) findViewById(R.id.idProfileHome);

        // END ACCOUNT PAGE


    }
}
