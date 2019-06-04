package com.example.fpexample;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.io.IOException;
import java.io.Serializable;
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

public class CardToBookActivity extends AppCompatActivity {

    private Button selBtn;
    private TextView calView;

    private SwitchDateTimeDialogFragment dateTimeFragment;

    private MapView mapView;
    private GoogleMap map;

    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";

    private static final String TAG = "Booking";
    private String parkName;
    private String parkAddress;
    private String user;

    // Firebase db
    private FirebaseFirestore db;

    //Near
    private List<Booking> nearYou;

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
        parkAddress = getIntent().getStringExtra("parkAddress");
        user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        nearYou = new ArrayList<Booking>();
        getNearBookings();

        final double[] coordinates = getCoordinates(parkAddress);

        TextView parkLabel = (TextView) findViewById(R.id.idParkName);
        parkLabel.setText(parkName);

        mapView = (MapView) findViewById(R.id.idMapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coordinates[0], coordinates[1]), 17));
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(coordinates[0], coordinates[1]))
                        .title("Marker"));
            }
        });

        mapView.onResume();

        selBtn = (Button) findViewById(R.id.idSelButton);

        selBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BookedNearYouActivity.class);
                i.putExtra("parkName", parkName);
                i.putExtra("nearYou", (Serializable) nearYou);
                startActivity(i);
            }
        });

    }

    private void getNearBookings(){
        db.collection("bookings")
                .whereEqualTo("park", parkName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            if(!doc.get("user").equals(user)){
                                nearYou.add(doc.toObject(Booking.class));
                            }
                        }
                    }
                });
    }

    private double[] getCoordinates(String address){
        double[] coordinates = new double[2];
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocationName(address, 5);
            if (addresses.size() > 0) {
                Double lat = (double) (addresses.get(0).getLatitude());
                Double lon = (double) (addresses.get(0).getLongitude());
                coordinates[0] = lat;
                coordinates[1] = lon;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return coordinates;
    }

}
