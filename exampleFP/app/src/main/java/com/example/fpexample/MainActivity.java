package com.example.fpexample;

import androidx.annotation.NonNull;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView usernameTV;

    // Auth book page
    private String user;
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

    // Settings page
    private TextView settingsTV;

    // Firebase db
    private FirebaseFirestore db;

    private final static String TAG = "MainAct";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        usernameTV.setVisibility(View.VISIBLE);
                        booking.setVisibility(View.VISIBLE);
                        bookedRV.setVisibility(View.VISIBLE);
                        toBookTV.setVisibility(View.GONE);
                        parkToBookTV.setVisibility(View.GONE);
                        toBookRV.setVisibility(View.GONE);
                        profileTV.setVisibility(View.GONE);
                        settingsTV.setVisibility(View.GONE);
                        return true;
                    case R.id.navigation_account:
                        usernameTV.setVisibility(View.GONE);
                        booking.setVisibility(View.GONE);
                        bookedRV.setVisibility(View.GONE);
                        toBookTV.setVisibility(View.GONE);
                        parkToBookTV.setVisibility(View.GONE);
                        toBookRV.setVisibility(View.GONE);
                        profileTV.setVisibility(View.VISIBLE);
                        settingsTV.setVisibility(View.GONE);
                        return true;
                    case R.id.navigation_book:
                        usernameTV.setVisibility(View.GONE);
                        booking.setVisibility(View.GONE);
                        bookedRV.setVisibility(View.GONE);
                        toBookTV.setVisibility(View.VISIBLE);
                        parkToBookTV.setVisibility(View.VISIBLE);
                        toBookRV.setVisibility(View.VISIBLE);
                        profileTV.setVisibility(View.GONE);
                        settingsTV.setVisibility(View.GONE);
                        return true;
                    case R.id.navigation_settings:
                        usernameTV.setVisibility(View.GONE);
                        booking.setVisibility(View.GONE);
                        bookedRV.setVisibility(View.GONE);
                        toBookTV.setVisibility(View.GONE);
                        parkToBookTV.setVisibility(View.GONE);
                        toBookRV.setVisibility(View.GONE);
                        profileTV.setVisibility(View.GONE);
                        settingsTV.setVisibility(View.VISIBLE);
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

        // DB
        db = FirebaseFirestore.getInstance();

        // HOME PAGE

        usernameTV = (TextView) findViewById(R.id.usernameView);

        user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        usernameTV.setText("Welcome back, " + user);

        booking = (TextView) findViewById(R.id.booking_label);

        bookedRV = (RecyclerView) findViewById(R.id.bookedRV);
        bookedRV.setLayoutManager(new LinearLayoutManager(this));

        bookingList = getUserBookings();

        if(bookingList != null) {
            bookingAdapter = new BookingsAdapter(bookingList, this);
            bookedRV.setAdapter(bookingAdapter);
        }

        // END HOME PAGE

        // START BOOK PAGE

        parkToBookTV = (TextView) findViewById(R.id.parkToBook_label);
        toBookTV = (TextView) findViewById(R.id.bookView);

        toBookRV = (RecyclerView) findViewById(R.id.toBookRV);
        toBookRV.setLayoutManager(new LinearLayoutManager(this));

        toBookList = getAllParks();

        toBookAdapter = new ToBookAdapter(this, toBookList);
        toBookRV.setAdapter(toBookAdapter);

        // END BOOK PAGE

        // START ACCOUNT PAGE

        profileTV = (TextView) findViewById(R.id.idProfileHome);

        // END ACCOUNT PAGE

        // START SETTINGS PAGE

        settingsTV = (TextView) findViewById(R.id.idSettingsTV);

        // END SETTINGS PAGE


    }

    private List<ToBook> getAllParks(){
        final List<ToBook> parkList = new ArrayList<ToBook>();
        db.collection("parks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                parkList.add(new ToBook(document.getString("parkName"), document.getString("parkAddress")));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
        return parkList;
    }

    private List<Booking> getUserBookings(){
        final List<Booking> userBookings = new ArrayList<Booking>();
        db.collection("bookings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //if(document != null) {
                                if (document.getString("user").equals(user)) {
                                    userBookings.add(new Booking(document.getString("user"), document.getString("parkName"), document.getString("date")));
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                //}
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
        return userBookings;
    }

}
