package com.example.consultationapp.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.consultationapp.R;
import com.example.consultationapp.model.Role;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText email,password;
    TextView createAccount,login;
    boolean valid = true;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        email = findViewById(R.id.username_input);
        password = findViewById(R.id.pass);
        login = findViewById(R.id.login);
        createAccount = findViewById(R.id.createAccount);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(email);
                checkField(password);

                if(valid){
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            checkUserRole(authResult.getUser().getUid());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

    }

    private void checkUserRole(String uid) {
        if(uid != null){
            DocumentReference df = fStore.collection("Users").document(uid);
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d("TAG", "onSuccess"+ documentSnapshot.getData());

                    if(documentSnapshot.getString("Role").equals(Role.Lecturer)){
                        System.out.println("Hey Im a Lecturer");
                        startActivity(new Intent(getApplicationContext(), LecturerMainActivity.class));//change back to Lecturer main activity

                    }
                    else if(documentSnapshot.getString("Role").equals(Role.Student)){
                        System.out.println("Hey Im Student");
                        startActivity(new Intent(getApplicationContext(), StudentMainActivity2.class));//change back to StudentMainActivity
                    }

                }
            });
        }

    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            checkUserRole(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        else {

        }
    }
}