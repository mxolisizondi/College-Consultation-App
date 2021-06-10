package com.example.consultationapp.model.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consultationapp.controller.ChatActivity;
import com.example.consultationapp.model.Lecturer;
import com.example.consultationapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyLecturersAdapter extends FirestoreRecyclerAdapter<Lecturer, MyLecturersAdapter.MyLecturerAppointementHolder> {
    StorageReference pathReference ;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     * @param options
     */
    public MyLecturersAdapter(@NonNull FirestoreRecyclerOptions<Lecturer> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyLecturerAppointementHolder myLecturersHolder, int position, @NonNull final Lecturer lecturer) {
        myLecturersHolder.textViewTitle.setText(lecturer.getFirstname() + " "+ lecturer.getLastname());
        myLecturersHolder.textViewDescription.setText("Module : "+lecturer.getModule());//Get Module
        myLecturersHolder.sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(v.getContext(),lecturer);
            }
        });
        myLecturersHolder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(myLecturersHolder.sendMessageButton.getContext(),lecturer.getPhoneNumber());
            }
        });
        pathReference = FirebaseStorage.getInstance().getReference().child("users/"+ lecturer.getEmail()+"/profile.jpg"); //storage the image
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .into(myLecturersHolder.imageViewDoctor);//Image location

                // profileImage.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private void openPage(Context wf, String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        wf.startActivity(intent);
    }

    private void openPage(Context wf, Lecturer lecturer){
        Intent i = new Intent(wf, ChatActivity.class);
        i.putExtra("key1",lecturer.getEmail()+"_"+ FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());
        i.putExtra("key2",FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()+"_"+lecturer.getEmail());
        wf.startActivity(i);
    }

    @NonNull
    @Override
    public MyLecturerAppointementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_lecturer_item, parent, false);
        return new MyLecturerAppointementHolder(v);
    }

    class MyLecturerAppointementHolder extends RecyclerView.ViewHolder{
        //Here we hold the MyDoctorItems
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewStatus;
        CircleImageView imageViewDoctor;
        Button sendMessageButton;
        Button callBtn;
        Button contactButton;
        public MyLecturerAppointementHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.doctor_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewStatus = itemView.findViewById(R.id.onlineStatut);
            imageViewDoctor = itemView.findViewById(R.id.doctor_item_image);
            sendMessageButton = itemView.findViewById(R.id.voir_fiche_btn);
            callBtn = itemView.findViewById(R.id.callBtn);
            contactButton = itemView.findViewById(R.id.contact);
        }
    }




}
