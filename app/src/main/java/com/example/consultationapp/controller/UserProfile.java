package com.example.consultationapp.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class UserProfile extends Fragment {
    EditText firstname,lastname,phone;
    TextView email,email2,role,fullnames,course,p_course;
    String uid;
    FirebaseAuth firebaseAuth;
    Button saveChanges, logout;
    CircleImageView profileImageView;
    ImageView editFirstname;

    FirebaseFirestore fStore;
    DocumentReference df;
    StorageReference storageReference;

    boolean valid = true;

    public UserProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_profile, container, false);
        firstname = view.findViewById(R.id.p_firstname);
        lastname = view.findViewById(R.id.p_lastname);
        fullnames = view.findViewById(R.id.fullnames);
        phone = view.findViewById(R.id.p_phone);
        email = view.findViewById(R.id.p_email);
        email2 = view.findViewById(R.id.p_email2);
        role = view.findViewById(R.id.role);
        saveChanges = view.findViewById(R.id.saveChanges);
        logout = view.findViewById(R.id.logout);
        profileImageView = view.findViewById(R.id.profileImageView);
        course = view.findViewById(R.id.course);
        p_course = view.findViewById(R.id.p_course);
        editFirstname = view.findViewById(R.id.editFirstname);
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        uid = firebaseAuth.getCurrentUser().getUid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        StorageReference profileRef = storageReference.child("users/"+firebaseAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
                System.out.println("hey");
            }
        });

        DocumentReference documentReference = fStore.collection("Users").document(uid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    firstname.setText(documentSnapshot.getString("Firstname"));
                    lastname.setText(documentSnapshot.getString("Lastname"));
                    phone.setText(documentSnapshot.getString("Phone"));
                    email.setText(firebaseAuth.getCurrentUser().getEmail());
                    email2.setText(firebaseAuth.getCurrentUser().getEmail());
                    course.setText(documentSnapshot.getString("Course"));
                    p_course.setText(documentSnapshot.getString("Course"));
                    role.setText(documentSnapshot.getString("Role"));
                    fullnames.setText(documentSnapshot.getString("Firstname")+" "+documentSnapshot.getString("Lastname"));
                }else {
                    Log.d("tag", "onEvent: Document do not exists");
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
                if(!valid){
                    Toast.makeText(getActivity(), "One or Many fields are empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    DocumentReference docRef = fStore.collection("Users").document(user.getUid());
                    Map<String,Object> edited = new HashMap<>();
                    edited.put("Firstname",firstname.getText().toString());
                    edited.put("Lastname",lastname.getText().toString());
                    edited.put("Phone",phone.getText().toString());
                    docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getActivity(), "Error profile not updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
            }
        });

        return view;
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // uplaod image to firebase storage
        final StorageReference fileRef = storageReference.child("users/"+firebaseAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImageView);
                        Toast.makeText(getActivity(), "Profile successful changed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed to upload image.", Toast.LENGTH_SHORT).show();
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