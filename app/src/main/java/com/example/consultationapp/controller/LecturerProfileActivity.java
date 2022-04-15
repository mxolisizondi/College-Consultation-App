package com.example.consultationapp.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.consultationapp.R;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class LecturerProfileActivity extends AppCompatActivity {
    EditText firstname,lastname,phone,module;
    TextView email,email2,role,fullnames,course,p_course;
    String uid;
    Button saveChanges, logout;
    CircleImageView profileImageView;
    ImageView editFirstname;
    FirebaseUser user;
    FirebaseFirestore fStore;
    DocumentReference df;
    StorageReference storageReference;

    boolean valid = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_profile);

        firstname = findViewById(R.id.p_firstname);
        lastname = findViewById(R.id.p_lastname);
        fullnames = findViewById(R.id.fullnames);
        phone = findViewById(R.id.p_phone);
        email = findViewById(R.id.p_email);
        email2 = findViewById(R.id.p_email2);
        role = findViewById(R.id.role);
        saveChanges = findViewById(R.id.saveChanges);
        logout = findViewById(R.id.logout);
        profileImageView = findViewById(R.id.profileImageView);
        course = findViewById(R.id.course);
        p_course = findViewById(R.id.p_course);
        module = findViewById(R.id.p_module);
        editFirstname = findViewById(R.id.editFirstname);
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //uid = firebaseAuth.getCurrentUser().getUid();
        user = FirebaseAuth.getInstance().getCurrentUser();

        StorageReference profileRef = storageReference.child("users/"+user.getEmail()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
                System.out.println("hey");
            }
        });

        DocumentReference documentReference = fStore.collection("Users").document(user.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e == null){
                    if(documentSnapshot.exists()){
                        firstname.setText(documentSnapshot.getString("Firstname"));
                        lastname.setText(documentSnapshot.getString("Lastname"));
                        phone.setText(documentSnapshot.getString("Phone"));
                        email.setText(user.getEmail());
                        email2.setText(user.getEmail());
                        course.setText(documentSnapshot.getString("Course"));
                        p_course.setText(documentSnapshot.getString("Course"));
                        module.setText(documentSnapshot.getString("Module"));
                        role.setText(documentSnapshot.getString("Role"));
                        fullnames.setText(documentSnapshot.getString("Firstname")+" "+documentSnapshot.getString("Lastname"));
                    }else {
                        Log.d("tag", "onEvent: Document do not exists");
                    }
                }
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });

        editFirstname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstname.setEnabled(true);
                firstname.setFocusable(true);
                System.out.println("Hey");
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(firstname);
                checkField(lastname);
                checkField(phone);
                if(!valid){
                    Toast.makeText(LecturerProfileActivity.this, "One or Many fields are empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    DocumentReference docRef = fStore.collection("Users").document(user.getUid());
                    Map<String,Object> edited = new HashMap<>();
                    edited.put("Firstname",firstname.getText().toString());
                    edited.put("Lastname",lastname.getText().toString());
                    edited.put("Phone",phone.getText().toString());
                    edited.put("Module",module.getText().toString());
                    docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Toast.makeText(LecturerProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            //Toast.makeText(LecturerProfileActivity.this, "Error profile not updated", Toast.LENGTH_SHORT).show();
                        }
                    });

                    docRef = fStore.collection("Lecturer").document(user.getEmail());
                    edited = new HashMap<>();
                    edited.put("firstname",firstname.getText().toString());
                    edited.put("lastname",lastname.getText().toString());
                    edited.put("phoneNumber",phone.getText().toString());
                    edited.put("module",module.getText().toString());
                    docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(LecturerProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(LecturerProfileActivity.this, "Error profile not updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LecturerProfileActivity.this, Login.class));
                finish();
            }
        });
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // uplaod image to firebase storage
        final StorageReference fileRef = storageReference.child("users/"+user.getEmail()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImageView);
                        Toast.makeText(LecturerProfileActivity.this, "Profile successful changed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LecturerProfileActivity.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == RESULT_OK){
                Uri imageUri = data.getData();
                //profileImage.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);
            }
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
}