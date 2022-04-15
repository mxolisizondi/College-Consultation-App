package com.example.consultationapp.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.consultationapp.MyCalendarLecturer;
import com.example.consultationapp.R;
import com.example.consultationapp.model.Common.Common;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class LecturerMainActivity extends AppCompatActivity {
    private CardView myStudents;
    private CardView appointments;
    private CardView appointmentRequest;
    private CardView profileSettings;
    private CardView myCalendar;
    private CardView uploadVideo;
    TextView fullnames;
    CircleImageView profileImageView;
    FirebaseUser user;
    StorageReference storageReference;
    private CardView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_main);
        myStudents = findViewById(R.id.myStudents);
        appointments = findViewById(R.id.appointment);
        appointmentRequest = findViewById(R.id.appointmentRequest);
        profileSettings = findViewById(R.id.profileSetting);
        myCalendar = findViewById(R.id.myCalendar);
        uploadVideo = findViewById(R.id.uploadVideo);
        profileImageView = findViewById(R.id.profileImageView);
        fullnames = findViewById(R.id.d_fullname);
        logout = findViewById(R.id.d_logout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Common.currentLecturer = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            Common.CurrentUserType = "Lecturer";

            storageReference = FirebaseStorage.getInstance().getReference();

            StorageReference profileRef = storageReference.child("users/"+user.getEmail()+"/profile.jpg");
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profileImageView);
                    System.out.println("hey");
                }
            });

            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if(e == null){
                        if(documentSnapshot.exists()){
                            fullnames.setText(documentSnapshot.getString("Firstname")+" "+documentSnapshot.getString("Lastname"));
                        }else {
                            Log.d("tag", "onEvent: Document do not exists");
                        }
                    }
                }
            });
        }


        myStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyStudentsActivity.class));
            }
        });

        appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ConfirmedAppointmentActivity.class));
            }
        });

        appointmentRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LecturerAppointmentActivity.class));
            }
        });

        profileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LecturerProfileActivity.class));
            }
        });

        myCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyCalendarLecturer.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LecturerMainActivity.this, Login.class));
                finish();
            }
        });

        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UploadVideo.class));
            }
        });
    }
}