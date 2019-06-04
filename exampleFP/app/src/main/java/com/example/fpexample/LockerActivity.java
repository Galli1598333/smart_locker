package com.example.fpexample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LockerActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView lockerRV;
    private String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

    private final static String TAG = "LockerAct";
    private String park;
    private String date;

    private FirestoreRecyclerAdapter lockerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Remove action bar */
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_locker);

        db = FirebaseFirestore.getInstance();

        park = getIntent().getStringExtra("parkName");
        date = getIntent().getStringExtra("date");

        lockerRV = (RecyclerView) findViewById(R.id.lockerRV);
        lockerRV.setLayoutManager(new GridLayoutManager(this, 6));

        getLockers();
    }

    private void getLockers(){
        Query query = db.collection("parks/"+park.hashCode()+"/lockers");

        FirestoreRecyclerOptions<Locker> response = new FirestoreRecyclerOptions.Builder<Locker>()
                .setQuery(query, Locker.class)
                .build();

        lockerAdapter = new FirestoreRecyclerAdapter<Locker, LockerHolder>(response) {
            @NonNull
            @Override
            public LockerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.locker_item, parent, false);
                return new LockerHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull LockerHolder lockerHolder, int i, @NonNull final Locker locker) {
                String lockNum = locker.getLockName().substring(locker.getLockName().length()-1);
                lockerHolder.lockerTV.setText(lockNum);
                if(!locker.isAvailable()){
                    lockerHolder.lockerTV.setBackgroundColor(Color.parseColor("#ff0000"));
                }
                else{
                    lockerHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addBooking(user, park, date, locker.getLockName());
                            setLock(locker.getLockName(), user);
                            Intent i = new Intent(v.getContext(), MainActivity.class);
                            v.getContext().startActivity(i);
                        }
                    });
                }
            }
        };
        lockerAdapter.notifyDataSetChanged();
        lockerRV.setAdapter(lockerAdapter);
    }

    public class LockerHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.lockerTV)
        TextView lockerTV;

        public LockerHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    private void addBooking(String user, String park, String strDate, String lockName){

        String lockPark = park + lockName;
        int lockHash = lockPark.hashCode();

        String hc = user + " " + park + " " + strDate + " " + lockPark;
        int bookHash = hc.hashCode();

        Map<String, Object> booking = new HashMap<>();
        booking.put("user", user);
        booking.put("park", park);
        booking.put("date", strDate);
        booking.put("lockHash", Integer.toString(lockHash));
        //booking.put("empty", true);

        db.collection("bookings").document(Integer.toString(bookHash))
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

    private void setLock(String lockName, String user){
        Map<String, Object> lock = new HashMap<>();
        lock.put("user", user);
        lock.put("available", false);
        lock.put("open", false);

        String lockPark = park + lockName;
        int lockHash = lockPark.hashCode();

        db.collection("parks/"+park.hashCode()+"/lockers")
                .document(Integer.toString(lockHash))
                .set(lock, SetOptions.merge())
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

    @Override
    protected void onStart() {
        super.onStart();
        lockerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        lockerAdapter.stopListening();
    }
}
