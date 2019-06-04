package com.example.fpexample;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookedNearYouAdapter extends RecyclerView.Adapter<BookedNearYouAdapter.MyViewHolder> {

    private List<Booking> bList;

    // Firebase db
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final String TAG = "NearYou";

    public BookedNearYouAdapter(List<Booking> list) {
        bList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearyou_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Booking b = bList.get(position);
        holder.friendTV.setText(b.getUser());
        holder.parkTV.setText(b.getPark());
        holder.dateTV.setText(b.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), LockerActivity.class); // Go to lockerActivity
                i.putExtra("parkName", b.getPark());
                i.putExtra("date", b.getDate());
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView friendTV;
        public TextView parkTV;
        public TextView dateTV;

        public MyViewHolder(View view){
            super(view);
            friendTV = (TextView) view.findViewById(R.id.friendTV);
            parkTV = (TextView) view.findViewById(R.id.parkTV);
            dateTV = (TextView) view.findViewById(R.id.dateTV);
        }

    }

}


