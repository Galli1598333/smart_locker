package com.example.fpexample;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView usernameTV;

    // Auth book page
    private String user;
    private TextView booking;
    private RecyclerView bookedRV;
    private FirestoreRecyclerAdapter bookingAdapter;
    private String lockName;
    private boolean lockState;

    private FirebaseAuth mAuth;

    // To Book Page
    private TextView toBookTV;
    private TextView parkToBookTV;
    private RecyclerView toBookRV;
    private FirestoreRecyclerAdapter toBookAdapter;

    // Profile page
    private TextView profileTV;
    private RecyclerView profileRV;
    private FirestoreRecyclerAdapter profileAdapter;

    // Settings page
    private TextView settingsTV;
    private Button signOutBtn;

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
                        profileRV.setVisibility(View.GONE);
                        settingsTV.setVisibility(View.GONE);
                        signOutBtn.setVisibility(View.GONE);
                        return true;
                    case R.id.navigation_account:
                        usernameTV.setVisibility(View.GONE);
                        booking.setVisibility(View.GONE);
                        bookedRV.setVisibility(View.GONE);
                        toBookTV.setVisibility(View.GONE);
                        parkToBookTV.setVisibility(View.GONE);
                        toBookRV.setVisibility(View.GONE);
                        profileTV.setVisibility(View.VISIBLE);
                        profileRV.setVisibility(View.VISIBLE);
                        settingsTV.setVisibility(View.GONE);
                        signOutBtn.setVisibility(View.GONE);
                        return true;
                    case R.id.navigation_book:
                        usernameTV.setVisibility(View.GONE);
                        booking.setVisibility(View.GONE);
                        bookedRV.setVisibility(View.GONE);
                        toBookTV.setVisibility(View.VISIBLE);
                        parkToBookTV.setVisibility(View.VISIBLE);
                        toBookRV.setVisibility(View.VISIBLE);
                        profileTV.setVisibility(View.GONE);
                        profileRV.setVisibility(View.GONE);
                        settingsTV.setVisibility(View.GONE);
                        signOutBtn.setVisibility(View.GONE);
                        return true;
                    case R.id.navigation_settings:
                        usernameTV.setVisibility(View.GONE);
                        booking.setVisibility(View.GONE);
                        bookedRV.setVisibility(View.GONE);
                        toBookTV.setVisibility(View.GONE);
                        parkToBookTV.setVisibility(View.GONE);
                        toBookRV.setVisibility(View.GONE);
                        profileRV.setVisibility(View.GONE);
                        profileTV.setVisibility(View.GONE);
                        settingsTV.setVisibility(View.VISIBLE);
                        signOutBtn.setVisibility(View.VISIBLE);
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

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // END GENERAL SETTINGS

        // DB
        db = FirebaseFirestore.getInstance();

        // HOME PAGE

        usernameTV = (TextView) findViewById(R.id.usernameView);

        user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        Log.d(TAG, "username: " + user);

        usernameTV.setText("Welcome back, " + user);

        booking = (TextView) findViewById(R.id.booking_label);

        bookedRV = (RecyclerView) findViewById(R.id.bookedRV);
        bookedRV.setLayoutManager(new LinearLayoutManager(this));

        getUserActiveBookings();

        // END HOME PAGE

        // START BOOK PAGE

        parkToBookTV = (TextView) findViewById(R.id.parkToBook_label);
        toBookTV = (TextView) findViewById(R.id.bookView);

        toBookRV = (RecyclerView) findViewById(R.id.toBookRV);
        toBookRV.setLayoutManager(new LinearLayoutManager(this));

        getAllParks();

        // END BOOK PAGE

        // START ACCOUNT PAGE

        profileTV = (TextView) findViewById(R.id.idProfileHome);

        profileRV = (RecyclerView) findViewById(R.id.profileRV);
        profileRV.setLayoutManager(new LinearLayoutManager(this));

        getUserDisabledBookings();

        // END ACCOUNT PAGE

        // START SETTINGS PAGE

        settingsTV = (TextView) findViewById(R.id.idSettingsTV);
        signOutBtn = (Button) findViewById(R.id.buttonFacebookSignout);

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        // END SETTINGS PAGE

    }

    private void getAllParks(){
        Query query = db.collection("parks");

        FirestoreRecyclerOptions<ToBook> response = new FirestoreRecyclerOptions.Builder<ToBook>()
                .setQuery(query, ToBook.class)
                .build();

        toBookAdapter = new FirestoreRecyclerAdapter<ToBook, ToBookHolder>(response) {

            @NonNull
            @Override
            public ToBookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tobook_card, parent, false);
                return new ToBookHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ToBookHolder toBookHolder, int i, @NonNull final ToBook toBook) {
                toBookHolder.parkName.setText(toBook.getParkName());
                toBookHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), CardToBookActivity.class);
                        i.putExtra("parkAddress", toBook.getParkAddress());
                        i.putExtra("parkName", toBook.getParkName());
                        v.getContext().startActivity(i);
                    }
                });
            }
        };
        toBookAdapter.notifyDataSetChanged();
        toBookRV.setAdapter(toBookAdapter);
    }

    private void getUserDisabledBookings(){
        Query query = db.collection("bookings")
                .whereEqualTo("user", user)
                .whereEqualTo("active", false);

        FirestoreRecyclerOptions<Booking> response = new FirestoreRecyclerOptions.Builder<Booking>()
                .setQuery(query, Booking.class)
                .build();

        profileAdapter = new FirestoreRecyclerAdapter<Booking, BookingDisabledHolder>(response) {

            @NonNull
            @Override
            public BookingDisabledHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.profile_card, parent, false);
                return new BookingDisabledHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull BookingDisabledHolder bookingDisabledHolder, int i, @NonNull Booking booking) {
                bookingDisabledHolder.parkBD.setText(booking.getPark());
                bookingDisabledHolder.dateBD.setText(booking.getDate());
            }
        };
        profileAdapter.notifyDataSetChanged();
        profileRV.setAdapter(profileAdapter);
    }

    private void getUserActiveBookings(){
        Query query = db.collection("bookings")
                .whereEqualTo("user", user)
                .whereEqualTo("active", true);

        FirestoreRecyclerOptions<Booking> response = new FirestoreRecyclerOptions.Builder<Booking>()
                .setQuery(query, Booking.class)
                .build();

        bookingAdapter = new FirestoreRecyclerAdapter<Booking, BookingActiveHolder>(response) {

            @NonNull
            @Override
            public BookingActiveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.booking_card, parent, false);
                return new BookingActiveHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull BookingActiveHolder bookingHolder, int i, @NonNull final Booking booking) {
                getLockInfo(booking.getPark(), booking.getLockHash());
                bookingHolder.parkB.setText(booking.getPark());
                bookingHolder.dateB.setText(booking.getDate());
                bookingHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), CardBookingActivity.class);
                        i.putExtra("park", booking.getPark());
                        i.putExtra("date", booking.getDate());
                        i.putExtra("lockHash", booking.getLockHash());
                        i.putExtra("lockName", lockName);
                        i.putExtra("lockState", lockState);
                        v.getContext().startActivity(i);
                    }
                });
            }
        };
        bookingAdapter.notifyDataSetChanged();
        bookedRV.setAdapter(bookingAdapter);
    }

    public class BookingActiveHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.parkTV)
        TextView parkB;
        @BindView(R.id.dateTV)
        TextView dateB;

        public BookingActiveHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public class BookingDisabledHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.parkTV1)
        TextView parkBD;
        @BindView(R.id.dateTV1)
        TextView dateBD;

        public BookingDisabledHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public class ToBookHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.parkNameTV)
        TextView parkName;

        public ToBookHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        bookingAdapter.startListening();
        toBookAdapter.startListening();
        profileAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bookingAdapter.stopListening();
        toBookAdapter.stopListening();
        profileAdapter.stopListening();
    }

    private void getLockInfo(String parkName, String lockHash){
        DocumentReference docRef = db.collection("parks/"+parkName.hashCode()+"/lockers").document(lockHash);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Locker lock = documentSnapshot.toObject(Locker.class);
                lockName = lock.getLockName();
                Log.d(TAG, "LockName: " + lockName);
                lockState = lock.isOpen();
                Log.d(TAG, "LockState: " + lockState);
            }
        });
    }

}
