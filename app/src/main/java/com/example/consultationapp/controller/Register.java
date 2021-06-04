package com.example.consultationapp.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.consultationapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {
    EditText emailAddress,password,confirm;
    TextView gotologin,createAccount;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        emailAddress = findViewById(R.id.username_input);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm_password);
        gotologin = findViewById(R.id.gotoLogin);
        createAccount = findViewById(R.id.createAccount);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailAddress.getText().toString();
                String pass = password.getText().toString();
                String confirmPass = confirm.getText().toString();
                if(!email.isEmpty() && !pass.isEmpty() && pass.equals(confirmPass)){
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(Register.this, "Account Created",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Register_User_Information.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, "Failed to create account",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    if(!password.equals(confirmPass)){
                        Toast.makeText(Register.this, "Confirm pass don't match password",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }

}