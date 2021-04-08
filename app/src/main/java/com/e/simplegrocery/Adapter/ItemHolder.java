package com.e.simplegrocery.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.simplegrocery.R;
import com.facebook.shimmer.ShimmerFrameLayout;

public class ItemHolder extends RecyclerView.ViewHolder {
    public TextView textView ,textView1 ;
    public ImageView imageView;
    public ItemHolder(@NonNull View itemView) {
        super(itemView);
        textView=itemView.findViewById(R.id.name);
        textView1=itemView.findViewById(R.id.rating);
        imageView = itemView.findViewById(R.id.image1);
    }
}
