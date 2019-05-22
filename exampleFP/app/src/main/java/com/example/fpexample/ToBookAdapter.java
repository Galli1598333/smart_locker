package com.example.fpexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ToBookAdapter extends RecyclerView.Adapter<ToBookAdapter.MyViewHolder> {

    private Context mContext;
    private List<ToBook> toBookList;

    public ToBookAdapter(Context mContext, List<ToBook> toBookList) {
        this.mContext = mContext;
        this.toBookList = toBookList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tobook_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToBook toBook = toBookList.get(position);
        holder.parkName.setText("Park name: " + toBook.getParkName());
        holder.parkAddress.setText("Park address: " + toBook.getParkAddress());
    }

    @Override
    public int getItemCount() {
        return toBookList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView parkName;
        public TextView parkAddress;

        public MyViewHolder(View view) {
            super(view);
            parkName = (TextView) view.findViewById(R.id.parkNameTV);
            parkAddress = (TextView) view.findViewById(R.id.parkAddressTV);
        }
    }

}
