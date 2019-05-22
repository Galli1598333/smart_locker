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

    private Button authenticate;
    private TextView booking;

    private RecyclerView bookedRV;
    private BookingsAdapter mAdapter;
    private List<Booking> mBookingList;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        booking.setVisibility(View.VISIBLE);
                        authenticate.setVisibility(View.GONE);
                        bookedRV.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.navigation_account:
                        booking.setVisibility(View.GONE);
                        authenticate.setVisibility(View.GONE);
                        bookedRV.setVisibility(View.GONE);
                        return true;
                    case R.id.navigation_book:
                        booking.setVisibility(View.GONE);
                        authenticate.setVisibility(View.VISIBLE);
                        bookedRV.setVisibility(View.GONE);
                        return true;
                    case R.id.navigation_settings:
                        booking.setVisibility(View.GONE);
                        authenticate.setVisibility(View.GONE);
                        bookedRV.setVisibility(View.GONE);
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

        // HOME PAGE

        booking = (TextView) findViewById(R.id.booking_label);

        bookedRV = (RecyclerView) findViewById(R.id.bookedRV);
        bookedRV.setLayoutManager(new LinearLayoutManager(this));

        mBookingList = new ArrayList<Booking>();
        mBookingList.add(new Booking("Parco della Caffarella", "05:00 pm", "06:45 pm"));
        mBookingList.add(new Booking("Villa Borghese", "07:00 am", "08:00 am"));

        mAdapter = new BookingsAdapter(mBookingList, this);
        bookedRV.setAdapter(mAdapter);


        // END HOME PAGE


        // BOOK PAGE

        authenticate = (Button) findViewById(R.id.authenticateButton);

        Executor executor = Executors.newSingleThreadExecutor();

        FragmentActivity activity = this;

        final BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    // user clicked negative button
                } else {
                    //TODO: Called when an unrecoverable error has been encountered and the operation is complete.
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //TODO: Called when a biometric is recognized.
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //TODO: Called when a biometric is valid but not recognized.
            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Set the title to display.")
                .setSubtitle("Set the subtitle to display.")
                .setDescription("Set the description to display")
                .setNegativeButtonText("Negative Button")
                .build();

        authenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
        // END BOOK PAGE

    }
}
