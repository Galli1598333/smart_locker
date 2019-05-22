package com.example.fpexample;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.MyViewHolder>{

    private Context mContext;
    private List<Booking> bookingList;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_card, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), CardActivity.class);
                mContext.startActivity(i);
            }
        });
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.park.setText(booking.getPark());
        holder.start.setText("Start: " + booking.getStartTime());
        holder.end.setText("End: " + booking.getEndTime());
    }



    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView park;
        public TextView start;
        public TextView end;

        public MyViewHolder(View view) {
            super(view);
            park = (TextView) view.findViewById(R.id.parkTV);
            start = (TextView) view.findViewById(R.id.startTV);
            end = (TextView) view.findViewById(R.id.endTV);
        }
    }

    public BookingsAdapter(List<Booking> list, Context mContext) {
        this.mContext = mContext;
        bookingList = list;
    }

}
