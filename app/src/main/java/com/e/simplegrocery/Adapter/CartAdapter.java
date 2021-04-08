package com.e.simplegrocery.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.simplegrocery.R;

public class CartAdapter extends RecyclerView.ViewHolder {
    public TextView textView ,textView1 ,textView2;
    public ImageView imageView;
    public CartAdapter(@NonNull View itemView) {
        super(itemView);
        textView=itemView.findViewById(R.id.name);
        textView1=itemView.findViewById(R.id.rating);
        textView2 = itemView.findViewById(R.id.qt);
        imageView = itemView.findViewById(R.id.image1);
    }
}
