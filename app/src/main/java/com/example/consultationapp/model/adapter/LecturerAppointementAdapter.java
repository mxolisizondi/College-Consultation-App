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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consultationapp.model.ApointementInformation;
import com.example.consultationapp.controller.ChatActivity;
import com.example.consultationapp.model.Lecturer;
import com.example.consultationapp.R;
import com.example.consultationapp.model.Student;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class LecturerAppointementAdapter extends FirestoreRecyclerAdapter<ApointementInformation, LecturerAppointementAdapter.MyDoctorAppointementHolder> {
    StorageReference pathReference ;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     * @param options
     */
    public LecturerAppointementAdapter(@NonNull FirestoreRecyclerOptions<ApointementInformation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyDoctorAppointementHolder myDoctorAppointementHolder, int position, @NonNull final ApointementInformation apointementInformation) {
        myDoctorAppointementHolder.dateAppointement.setText(apointementInformation.getTime());
        myDoctorAppointementHolder.studentName.setText(apointementInformation.getStudentName());
        myDoctorAppointementHolder.appointementType.setText(apointementInformation.getApointementType());
        myDoctorAppointementHolder.approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Patient ID "+apointementInformation.getStudentId());
                apointementInformation.setType("Accepted");
                FirebaseFirestore.getInstance().collection("Student").document(apointementInformation.getStudentId()).collection("calendar")
                        .document(apointementInformation.getTime().replace("/","_")).set(apointementInformation);
                FirebaseFirestore.getInstance().document(apointementInformation.getChemin()).update("type","Accepted");
                Toast.makeText(v.getContext(), "Appointment accepted", Toast.LENGTH_SHORT).show();
                FirebaseFirestore.getInstance().collection("Lecturer").document(apointementInformation.getLecturerId()).collection("calendar")
                        .document(apointementInformation.getTime().replace("/","_")).set(apointementInformation);

//////////// here add patient friend to doctor

                FirebaseFirestore.getInstance().document("Student/"+apointementInformation.getStudentId()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                FirebaseFirestore.getInstance().collection("Lecturer").document(apointementInformation.getLecturerId()+"")
                                        .collection("MyStudents").document(apointementInformation.getStudentId()).set(documentSnapshot.toObject(Student.class));
                            }
                        });
                FirebaseFirestore.getInstance().document("Lecturer/"+apointementInformation.getLecturerId()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                FirebaseFirestore.getInstance().collection("Student").document(apointementInformation.getStudentId()+"")
                                        .collection("MyLecturers").document(apointementInformation.getLecturerId()).set(documentSnapshot.toObject(Lecturer.class));
                            }
                        });


                getSnapshots().getSnapshot(position).getReference().delete();
            }
        });
        myDoctorAppointementHolder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apointementInformation.setType("Refused");
                FirebaseFirestore.getInstance().collection("Student").document(apointementInformation.getStudentId()).collection("calendar")
                        .document(apointementInformation.getTime().replace("/","_")).set(apointementInformation);
                FirebaseFirestore.getInstance().document(apointementInformation.getChemin()).delete();
                getSnapshots().getSnapshot(position).getReference().delete();
            }
        });

        pathReference = FirebaseStorage.getInstance().getReference().child("users/"+ apointementInformation.getStudentId()+"/profile.jpg"); //storage the image
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(myDoctorAppointementHolder.student_image);//Image location

                // profileImage.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    private void openPage(Context wf, Lecturer d){
        Intent i = new Intent(wf, ChatActivity.class);
        i.putExtra("key1",d.getEmail()+"_"+ FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());
        i.putExtra("key2",FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()+"_"+d.getEmail());
        wf.startActivity(i);
    }

    @NonNull
    @Override
    public MyDoctorAppointementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecturer_appointement_item, parent, false);
        return new MyDoctorAppointementHolder(v);
    }

    class MyDoctorAppointementHolder extends RecyclerView.ViewHolder{
        //Here we hold the MyDoctorAppointementItems
        TextView dateAppointement;
        TextView studentName;
        Button approveBtn;
        Button cancelBtn;
        TextView appointementType;
        ImageView student_image;
        public MyDoctorAppointementHolder(@NonNull View itemView) {
            super(itemView);
            dateAppointement = itemView.findViewById(R.id.appointement_date);
            studentName = itemView.findViewById(R.id.patient_name);
            approveBtn = itemView.findViewById(R.id.btn_accept);
            cancelBtn = itemView.findViewById(R.id.btn_decline);
            appointementType = itemView.findViewById(R.id.appointement_type);
            student_image = itemView.findViewById(R.id.patient_image);
        }
    }




}
