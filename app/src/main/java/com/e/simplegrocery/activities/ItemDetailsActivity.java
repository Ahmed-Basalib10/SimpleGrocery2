package com.e.simplegrocery.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.e.simplegrocery.R;
import com.e.simplegrocery.databinding.ActivityItemDetailsBinding;
import com.e.simplegrocery.model.Cart;
import com.e.simplegrocery.utilities.Utility;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ItemDetailsActivity extends AppCompatActivity {
    private String name ,desc ,price,image,foodId, qt;
    private FirebaseUser user;
    private String currentUser;
    private DatabaseReference databaseReference;
    ActivityItemDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Item Details");

        user=FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            currentUser=user.getUid();
        }

        name=getIntent().getStringExtra("NAME");
        desc=getIntent().getStringExtra("DESC");
        price=getIntent().getStringExtra("PRICE");
        image=getIntent().getStringExtra("IMAGE");
        foodId=getIntent().getStringExtra("ID");

        qt = getIntent().getStringExtra("QT");


        initialize();
        setupWidgets();
    }

    private void initialize() {
        databaseReference= FirebaseDatabase.getInstance().getReference("cart");
        binding.add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart cart =new Cart(name,price,desc,image,binding.elegantNumberButton.getNumber()) ;
                databaseReference.child(currentUser).child(foodId).setValue(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Added to cart",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setupWidgets() {
        binding.textView.setText(Utility.capitalize(name));
        binding.textView1.setText("Rs. "+price);
        binding.desc.setText(desc);
        Glide.with(ItemDetailsActivity.this).load(image).into(binding.imageView2);
        if(qt != null){
            binding.elegantNumberButton.setNumber(qt);
        }
    }
}