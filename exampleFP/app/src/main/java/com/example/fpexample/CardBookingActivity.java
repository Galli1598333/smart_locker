package com.example.fpexample;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

public class CardBookingActivity extends AppCompatActivity {

    private Button authenticate;
    private Button leave;
    private Button delete;

    private TextView parkNameTV;
    private TextView lockNameTV;

    private String lockName;
    private String lockHash;
    private String parkName;
    private String date;
    private boolean lockState;

    private final static String TAG = "FPrintAct";

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

        db = FirebaseFirestore.getInstance();

        String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        setContentView(R.layout.activity_booking_card);

        parkNameTV = (TextView) findViewById(R.id.idParkName);
        lockNameTV = (TextView) findViewById(R.id.idLockName);

        parkName = getIntent().getStringExtra("park");
        lockHash = getIntent().getStringExtra("lockHash");
        lockName = getIntent().getStringExtra("lockName");
        lockState = getIntent().getBooleanExtra("lockState", true);
        date = getIntent().getStringExtra("date");

        Log.d(TAG, "LockName: " + lockName);
        Log.d(TAG, "LockState: " + lockState);

        String lockN = lockName.substring(0, lockName.length()-1);
        String lockID = lockName.substring(lockName.length()-1);

        parkNameTV.setText(parkName);
        lockNameTV.setText(lockN + " " + lockID);

        authenticate = (Button) findViewById(R.id.idAuthButt);
        leave = (Button) findViewById(R.id.idLeaveBtn);
        delete = (Button) findViewById(R.id.idDeleteBtn);

        final String bookID = user + " " + parkName + " " + date + " " + parkName + lockName;
        Log.d(TAG, "BookID: " + bookID);

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveLocker(bookID, parkName, lockHash);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirm delete !");
                builder.setMessage("You are about to delete your booking. Do you really want to proceed ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteBooking(bookID, parkName, lockHash);
                        Toast.makeText(getApplicationContext(), "You've choosen to delete you booking", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        getApplicationContext().startActivity(i);
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "You've changed your mind", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });

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

                setLockFull(parkName, lockHash);

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //TODO: Called when a biometric is valid but not recognized.
            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Put your finger over the sensor")
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
    }

    private void setLockFull(String parkName, String lockHash){
        Map<String, Object> lock = new HashMap<>();
        if(lockState == true) {
            lock.put("open", false);
            lockState = false;
        }
        else{
            lock.put("open", true);
            lockState = true;
        }

        db.collection("parks/"+parkName.hashCode()+"/lockers").document(lockHash)
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

    private void leaveLocker(String bookID, String parkName, String lockHash){
        Map<String, Object> lock = new HashMap<>();
        if(lockState == true) {
            lock.put("open", false);
        }
        lock.put("available", true);
        lock.put("user", "");
        lockState = false;

        db.collection("parks/"+parkName.hashCode()+"/lockers").document(lockHash)
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
        DateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        Date currentTime = Calendar.getInstance().getTime();
        String leaveTime = dateFormat.format(currentTime);
        Map<String, Object> booking = new HashMap<>();
        booking.put("active", false);
        booking.put("leave", leaveTime);

        db.collection("bookings").document(Integer.toString(bookID.hashCode()))
                .set(booking, SetOptions.merge())
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

        Intent i = new Intent(this, MainActivity.class);
        finish();
    }

    private void deleteBooking(String bookID, String parkName, String lockHash){

        Map<String, Object> lock = new HashMap<>();
        if(lockState == true) {
            lock.put("open", false);
        }
        lock.put("available", true);
        lock.put("user", "");
        lockState = false;

        db.collection("parks/"+parkName.hashCode()+"/lockers").document(lockHash)
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

        db.collection("bookings").document(Integer.toString(bookID.hashCode()))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

    }

}
