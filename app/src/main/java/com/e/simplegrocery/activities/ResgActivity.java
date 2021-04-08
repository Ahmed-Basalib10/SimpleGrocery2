package com.e.simplegrocery.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.e.simplegrocery.R;
import com.e.simplegrocery.databinding.ActivityResgBinding;
import com.e.simplegrocery.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResgActivity extends AppCompatActivity {
    ActivityResgBinding resgBinding;
    private String name , email , password;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resgBinding = ActivityResgBinding.inflate(getLayoutInflater());
        setContentView(resgBinding.getRoot());

        database = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user != null){
            currentUser = user.getUid();
        }

        resgBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = resgBinding.textInputLayout.getEditText().getText().toString();
                email = resgBinding.textInputLayout1.getEditText().getText().toString();
                password = resgBinding.textInputLayout2.getEditText().getText().toString();
                if(name.isEmpty()){
                    resgBinding.textInputEdittext.setError("Name can not be empty");
                }else if(email.isEmpty()){
                    resgBinding.textInputEdittext1.setError("Email can not be empty");
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    resgBinding.textInputEdittext1.setError("Email not failed");
                }else if(password.isEmpty()){
                    resgBinding.textInputEdittext2.setError("Password can not be empty");
                }else if(password.length() < 6){
                    resgBinding.textInputEdittext2.setError("Password can not be less than 6");
                }else{
                    resgBinding.textInputEdittext.setError(null);
                    resgBinding.textInputEdittext1.setError(null);
                    resgBinding.textInputEdittext2.setError(null);

                    register(email,password,name);
                }
            }
        });
    }

    private void register(String email, String password, String name) {
        resgBinding.progress.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
             User user = new User(name,email);
             database.child(auth.getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void aVoid) {
                     Toast.makeText(ResgActivity.this, "User has been registered successfully", Toast.LENGTH_SHORT).show();
                     resgBinding.progress.setVisibility(View.GONE);
                     startActivity(new Intent(ResgActivity.this,MainActivity2.class));
                     finishAffinity();
                 }
             });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ResgActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}