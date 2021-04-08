package com.e.simplegrocery.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.simplegrocery.R;
import com.facebook.shimmer.ShimmerFrameLayout;

public class CategoryAdapter extends RecyclerView.ViewHolder {
    public TextView cName;
    public ImageView imageView;
    public CategoryAdapter(@NonNull View itemView) {
        super(itemView);
        cName=itemView.findViewById(R.id.category_name);
        imageView = itemView.findViewById(R.id.category_photo);
    }
}
