package com.e.simplegrocery.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.e.simplegrocery.Adapter.OrderAdapter;
import com.e.simplegrocery.R;
import com.e.simplegrocery.databinding.ActivityProfileBinding;
import com.e.simplegrocery.model.Order;
import com.e.simplegrocery.model.User;
import com.e.simplegrocery.utilities.Utility;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding profileBinding;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(profileBinding.getRoot());
        setUpNavigiation();
        initialize();
    }

    private void initialize() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user != null){
            currentUser = user.getUid();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUser);
        loadUserDetails();

        profileBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
                finishAffinity();
            }
        });
    }

    private void loadUserDetails() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class) ;
                if(user != null){
                    profileBinding.name.setText(Utility.capitalize(user.getName()));
                    profileBinding.email.setText(user.getMail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setUpNavigiation() {
        BottomNavigationView bottomNavigationView =findViewById(R.id.nav_view);
        Utility.enableNavigaton(ProfileActivity.this,this,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem =menu.getItem(2);
        menuItem.setChecked(true);
    }
}