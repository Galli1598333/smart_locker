package com.example.fpexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BookedNearYouActivity extends AppCompatActivity {

    private SwitchDateTimeDialogFragment dateTimeFragment;

    private Button bookBtn;
    private TextView calView;

    private RecyclerView friendsRV;
    private FirestoreRecyclerAdapter bookedNearAdapter;

    private String parkName;
    private String user;

    // Firebase db
    private FirebaseFirestore db;

    private final String TAG = "NearYou";

    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_nearyou);

        parkName = getIntent().getStringExtra("parkName");
        user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        friendsRV = (RecyclerView) findViewById(R.id.idFriendsRV);
        friendsRV.setLayoutManager(new LinearLayoutManager(this));

        // DB
        db = FirebaseFirestore.getInstance();


        // Near bookings
        getNearBookings();

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
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
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

        bookBtn = (Button) findViewById(R.id.idBookBtn);

        bookBtn.setOnClickListener(new View.OnClickListener() {
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
        booking.put("park", park);
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

    private void addBookingDateString(String park, String date){
        Map<String, Object> booking = new HashMap<>();
        booking.put("user", user);
        booking.put("park", park);
        booking.put("date", date);

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

    private void getNearBookings(){
        Query query = db.collection("bookings").whereEqualTo("park", parkName);

        FirestoreRecyclerOptions<Booking> response = new FirestoreRecyclerOptions.Builder<Booking>()
                .setQuery(query, Booking.class)
                .build();

        bookedNearAdapter = new FirestoreRecyclerAdapter<Booking, NearBookingHolder>(response) {

            @NonNull
            @Override
            public NearBookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.nearyou_card, parent, false);
                return new NearBookingHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull NearBookingHolder nearBookingHolder, int i, @NonNull final Booking booking) {
                if(!user.equals(booking.getUser())){
                    nearBookingHolder.friendTV.setText(booking.getUser());
                    nearBookingHolder.parkTV.setText(booking.getPark());
                    nearBookingHolder.dateTV.setText(booking.getDate());
                    nearBookingHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addBookingDateString(booking.getPark(), booking.getDate());
                            Intent i = new Intent(v.getContext(), MainActivity.class);
                            v.getContext().startActivity(i);
                        }
                    });
                }
            }
        };
        bookedNearAdapter.notifyDataSetChanged();
        friendsRV.setAdapter(bookedNearAdapter);
    }

    public class NearBookingHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.friendTV)
        TextView friendTV;
        @BindView(R.id.parkTV)
        TextView parkTV;
        @BindView(R.id.dateTV)
        TextView dateTV;

        public NearBookingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bookedNearAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bookedNearAdapter.stopListening();
    }

}
