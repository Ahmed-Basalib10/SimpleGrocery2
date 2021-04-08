package com.e.simplegrocery.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.e.simplegrocery.R;
import com.e.simplegrocery.activities.MainActivity2;
import com.e.simplegrocery.activities.OrdersActivity;
import com.e.simplegrocery.activities.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Utility {
    public static String capitalize(@NonNull String input) {

        String[] words = input.toLowerCase().split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            if (i > 0 && word.length() > 0) {
                builder.append(" ");
            }

            String cap = word.substring(0, 1).toUpperCase() + word.substring(1);
            builder.append(cap);
        }
        return builder.toString();
    }

    public static void enableNavigaton(final Context context , final Activity callingActivity , BottomNavigationView view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home1:
                        context.startActivity(new Intent(context, MainActivity2.class));
                        callingActivity.overridePendingTransition(0,0);
                        callingActivity.finish();
                        return true;
                    case R.id.orders:
                        context.startActivity(new Intent(context, OrdersActivity.class));
                        callingActivity.overridePendingTransition(0,0);
                        callingActivity.finish();
                        return true;
               /*  case R.id.favourite:
                     context.startActivity(new Intent(context,FavoriteSctivity.class));
                     callingActivity.overridePendingTransition(0,0);
                     callingActivity.finish();
                     return true;*/
                    case R.id.profile:
                        context.startActivity(new Intent(context, ProfileActivity.class));
                        callingActivity.overridePendingTransition(0,0);
                        callingActivity.finish();
                        return true;

                }

                return false;
            }
        });
    }
}
