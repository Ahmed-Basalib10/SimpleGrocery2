package com.e.simplegrocery.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.simplegrocery.R;

public class OrderAdapter extends RecyclerView.ViewHolder {
    public TextView id, date, state, total;
    public OrderAdapter(@NonNull View itemView) {
        super(itemView);
        id = itemView.findViewById(R.id.orderid);
        date = itemView.findViewById(R.id.date);
        state = itemView.findViewById(R.id.state);
        total = itemView.findViewById(R.id.total);
    }
}
