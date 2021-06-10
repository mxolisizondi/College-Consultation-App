package com.example.consultationapp.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.consultationapp.R;
import com.example.consultationapp.Showvideo;
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

public class StudentMainActivity2 extends AppCompatActivity {
    private CardView myLecturers;
    private CardView myAppointments;
    private CardView bookAppointment;
    private CardView profileSettings;
    private CardView watchVideos;
    private CardView logout;
    TextView fullnames;
    CircleImageView profileImageView;
    FirebaseUser user;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main2);
        myLecturers = findViewById(R.id.myStudents);
        myAppointments = findViewById(R.id.appointment);
        bookAppointment = findViewById(R.id.appointmentRequest);
        profileSettings = findViewById(R.id.profileSetting);
        profileImageView = findViewById(R.id.profileImageView);
        fullnames = findViewById(R.id.d_fullname);
        watchVideos = findViewById(R.id.uploadVideo);
        logout = findViewById(R.id.d_logout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            System.out.println("Not empty");
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

        myLecturers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyLecturersActivity.class));//change to my lecturers
            }
        });

        myAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), StudentAppointmentActivity.class));
            }
        });

        bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), StudentSearchLecturerActivity.class));
            }
        });

        profileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), StudentProfile.class));
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        watchVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Showvideo.class));
            }
        });
        Common.CurrentUserid= FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        FirebaseFirestore.getInstance().collection("Users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Common.CurrentUserName = documentSnapshot.getString("Firstname");
            }
        });
    }
}