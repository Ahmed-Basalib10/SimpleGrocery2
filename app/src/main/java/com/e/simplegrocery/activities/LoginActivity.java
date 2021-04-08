package com.e.simplegrocery.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.e.simplegrocery.R;
import com.e.simplegrocery.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private String  email , password;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        binding.donthave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ResgActivity.class));
            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.textInputLayout.getEditText().getText().toString();
                password = binding.textInputLayout1.getEditText().getText().toString();
                if(email.isEmpty()){
                    binding.textInputEdittext.setError("Email can not be empty");
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    binding.textInputEdittext.setError("Email not failed");
                }else if(password.isEmpty()){
                    binding.textInputEdittext1.setError("Password can not be empty");
                }else if(password.length() < 6){
                    binding.textInputEdittext1.setError("Password can not be less than 6");
                }else{
                    binding.textInputEdittext.setError(null);
                    binding.textInputEdittext1.setError(null);

                    login(email,password);
                }
            }
        });
    }

    private void login(String email, String password) {
        binding.progress.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(LoginActivity.this,MainActivity2.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser() != null){
           startActivity(new Intent(LoginActivity.this,MainActivity2.class));
           finish();
        }
    }
}