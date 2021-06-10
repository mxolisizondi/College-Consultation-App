package com.example.consultationapp.model.adapter;

import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.consultationapp.model.ApointementInformation;
import com.example.consultationapp.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAppointmentsAdapter extends FirestoreRecyclerAdapter<ApointementInformation, StudentAppointmentsAdapter.StudentAppointmentsHolder> {
    StorageReference pathReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference lectRef;
    DocumentSnapshot documentSnapshot;
    final String doctorID = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

    public StudentAppointmentsAdapter(@NonNull FirestoreRecyclerOptions<ApointementInformation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull StudentAppointmentsHolder patientAppointmentsHolder, int position, @NonNull final ApointementInformation apointementInformation) {
        patientAppointmentsHolder.dateAppointement.setText(apointementInformation.getTime());
        patientAppointmentsHolder.studentName.setText(apointementInformation.getLecturerName());
        patientAppointmentsHolder.appointementType.setText(apointementInformation.getApointementType());
        patientAppointmentsHolder.type.setText(apointementInformation.getType());
        String doctorEmail = apointementInformation.getLecturerId();
        System.out.println(doctorEmail);
        Log.d("docotr email", doctorEmail);
        lectRef = db.collection("Lecturer").document("" + doctorEmail + "");
        /* Get the doctor's phone number */
        lectRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                patientAppointmentsHolder.phone.setText(document.getString("phoneNumber"));
                Log.d("telephone num", document.getString("phoneNumber"));
            }
        });


        //display profile image
        String imageId = apointementInformation.getLecturerId();
        pathReference = FirebaseStorage.getInstance().getReference().child("users/"+imageId+"/profile.jpg");
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .into(patientAppointmentsHolder.image);
                // profileImage.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        if (apointementInformation.getApointementType().equals("Consultation")) {
            //patientAppointmentsHolder.appointementType.setBackgroundColor((patientAppointmentsHolder.type.getContext().getResources().getColor(R.color.colorPrimaryDark)));
            patientAppointmentsHolder.appointementType.setBackground(patientAppointmentsHolder.appointementType.getContext().getResources().getDrawable(R.drawable.button_radius_primary_color));
        }
        if (apointementInformation.getType().equals("Accepted")) {
            patientAppointmentsHolder.type.setTextColor(Color.parseColor("#20bf6b"));
        } else if (apointementInformation.getType().equals("Checked")) {
            patientAppointmentsHolder.type.setTextColor(Color.parseColor("#8854d0"));
        } else {
            patientAppointmentsHolder.type.setTextColor(Color.parseColor("#eb3b5a"));
        }
    }

    @NonNull
    @Override
    public StudentAppointmentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_appointments_item, parent, false);
        return new StudentAppointmentsHolder(v);
    }


    class StudentAppointmentsHolder extends RecyclerView.ViewHolder {
        TextView dateAppointement;
        TextView studentName;
        TextView appointementType;
        TextView type;
        TextView phone;
        CircleImageView image;

        public StudentAppointmentsHolder(@NonNull View itemView) {
            super(itemView);
            dateAppointement = itemView.findViewById(R.id.appointement_date);
            studentName = itemView.findViewById(R.id.patient_name);
            appointementType = itemView.findViewById(R.id.appointement_type);
            type = itemView.findViewById(R.id.type);
            phone = itemView.findViewById(R.id.patient_phone);
            image = itemView.findViewById(R.id.patient_image);
        }
    }
}
