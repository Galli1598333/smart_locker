package com.example.fpexample;

import android.content.Context;
import android.content.Intent;
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
    private View itemView;

    public ToBookAdapter(Context mContext, List<ToBook> toBookList) {
        this.mContext = mContext;
        this.toBookList = toBookList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tobook_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToBook toBook = toBookList.get(position);
        holder.parkName.setText(toBook.getParkName());
        //holder.parkAddress.setText("Park address: " + toBook.getParkAddress());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), CardToBookActivity.class);
                i.putExtra("parkAddress", toBook.getParkAddress());
                i.putExtra("parkName", toBook.getParkName());
                mContext.startActivity(i);
            }
        });
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
            //parkAddress = (TextView) view.findViewById(R.id.parkAddressTV);
        }
    }

}
