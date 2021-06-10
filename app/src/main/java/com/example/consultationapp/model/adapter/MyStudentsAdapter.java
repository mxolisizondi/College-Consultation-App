package com.example.consultationapp.model.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consultationapp.controller.ChatActivity;
import com.example.consultationapp.R;
import com.example.consultationapp.model.Student;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyStudentsAdapter extends FirestoreRecyclerAdapter<Student, MyStudentsAdapter.MyStudentsHolder> {
    StorageReference pathReference ;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     * @param options
     */
    public MyStudentsAdapter(@NonNull FirestoreRecyclerOptions<Student> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull final MyStudentsHolder myStudentsHolder, int position, @NonNull final Student student) {
        myStudentsHolder.textViewTitle.setText(student.getFirstname());
        myStudentsHolder.textViewTelephone.setText("Tél : "+student.getPhoneNumber());
        myStudentsHolder.contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(v.getContext(),student);
            }
        });

        myStudentsHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openPatientMedicalFolder(v.getContext(),student);

            }
        });
        myStudentsHolder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(myStudentsHolder.contactButton.getContext(),student.getPhoneNumber());
            }
        });

        pathReference = FirebaseStorage.getInstance().getReference().child("users/"+ student.getEmail()+"/profile.jpg"); //storage the image
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(myStudentsHolder.imageViewPatient);//Image location

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

//    private void openPatientMedicalFolder(Context medicalFolder, Student patient){
//        Intent intent = new Intent(medicalFolder, DossierMedical.class);
//        intent.putExtra("patient_name", patient.getName());
//        intent.putExtra("patient_email",patient.getEmail());
//        intent.putExtra("patient_phone", patient.getTel());
//        medicalFolder.startActivity(intent);
//    }

    private void openPage(Context wf,Student student){
        Intent i = new Intent(wf, ChatActivity.class);
        i.putExtra("key1",student.getEmail()+"_"+ FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());
        i.putExtra("key2",FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()+"_"+student.getEmail());
        wf.startActivity(i);
    }
    @NonNull
    @Override
    public MyStudentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_student_item, parent, false);
        return new MyStudentsHolder(v);
    }

    class MyStudentsHolder extends RecyclerView.ViewHolder{
        //Here we hold the MyDoctorItems
        Button callBtn;
        TextView textViewTitle;
        TextView textViewTelephone;
        CircleImageView imageViewPatient;
        Button contactButton;
        RelativeLayout parentLayout;
        public MyStudentsHolder(@NonNull View itemView) {
            super(itemView);
            callBtn = itemView.findViewById(R.id.callBtn);
            textViewTitle = itemView.findViewById(R.id.patient_view_title);
            textViewTelephone = itemView.findViewById(R.id.text_view_telephone);
            imageViewPatient = itemView.findViewById(R.id.patient_item_image);
            contactButton = itemView.findViewById(R.id.contact);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }




}
