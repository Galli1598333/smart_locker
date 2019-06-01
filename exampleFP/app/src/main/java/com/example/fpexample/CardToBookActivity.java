package com.example.fpexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CardToBookActivity extends AppCompatActivity {

    private Button calButt;
    private TextView calView;

    private SwitchDateTimeDialogFragment dateTimeFragment;

    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";

    private static final String TAG = "Booking";
    private String parkName;

    // Firebase db
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_tobook_card);

        // DB
        db = FirebaseFirestore.getInstance();

        parkName = getIntent().getStringExtra("parkName");

        TextView parkLabel = (TextView) findViewById(R.id.idParkName);
        ImageView parkMap = (ImageView) findViewById(R.id.idParkImg);

        int img_source;

        parkLabel.setText(parkName);

        if(parkName.equals("Parco della Caffarella")){
            img_source = getResources().getIdentifier("@drawable/caffarellapark", null, this.getPackageName());
        }
        else if(parkName.equals("Villa Borghese")){
            img_source = getResources().getIdentifier("@drawable/villaborghese", null, this.getPackageName());
        }
        else{
            img_source = getResources().getIdentifier("@drawable/error", null, this.getPackageName());
        }

        parkMap.setImageResource(img_source);

        // CALENDAR WIDGET

        calView = (TextView) findViewById(R.id.idCalendarView);

        // Construct SwitchDateTimePicker
        dateTimeFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if(dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(android.R.string.ok),
                    getString(android.R.string.cancel),
                    getString(R.string.clean) // Optional
            );
        }

        // Optionally define a timezone
        dateTimeFragment.setTimeZone(TimeZone.getDefault());

        // Init format
        final SimpleDateFormat myDateFormat = new SimpleDateFormat("d MMM yyyy HH:mm", java.util.Locale.getDefault());
        // Assign unmodifiable values
        dateTimeFragment.set24HoursMode(true);
        dateTimeFragment.setHighlightAMPMSelection(false);
        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());

        // Define new day and month format
        try {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMMM dd", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

        // Set listener for date
        // Or use dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                //calView.setText(myDateFormat.format(date));
                addBooking(parkName, date);
                Intent i = new Intent(getApplicationContext(), BookedNearYouActivity.class);
                startActivity(i);
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Do nothing
            }

            @Override
            public void onNeutralButtonClick(Date date) {
                // Optional if neutral button does'nt exists
                calView.setText("");
            }
        });

        calButt = (Button) findViewById(R.id.idCalendarButton);

        calButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Re-init each time
                dateTimeFragment.startAtCalendarView();
                dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
            }
        });

    }

    private void addBooking(String park, Date date){
        String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        DateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        String strDate = dateFormat.format(date);
        Map<String, Object> booking = new HashMap<>();
        booking.put("user", user);
        booking.put("parkName", park);
        booking.put("date", strDate);

        db.collection("bookings").document(UUID.randomUUID().toString())
                .set(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

}
