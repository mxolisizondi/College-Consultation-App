package com.example.consultationapp.model.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consultationapp.model.ApointementInformation;
import com.example.consultationapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfirmedAppointmentsAdapter extends FirestoreRecyclerAdapter<ApointementInformation, ConfirmedAppointmentsAdapter.ConfirmedAppointmentsHolder> {
    StorageReference pathReference ;
    public ConfirmedAppointmentsAdapter(@NonNull FirestoreRecyclerOptions<ApointementInformation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ConfirmedAppointmentsHolder confirmedAppointmentsHolder, int position, @NonNull final ApointementInformation apointementInformation) {
        confirmedAppointmentsHolder.dateAppointement.setText(apointementInformation.getTime());
        confirmedAppointmentsHolder.studentName.setText(apointementInformation.getStudentName());
        confirmedAppointmentsHolder.appointementType.setText(apointementInformation.getApointementType());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String imageId = apointementInformation.getStudentId(); //add a title image
        System.out.println("Patient/Student is "+imageId);

        pathReference = FirebaseStorage.getInstance().getReference().child("users/"+user.getEmail()+"/profile.jpg"); //storage the image
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .into(confirmedAppointmentsHolder.studentImage);//Image location

                // profileImage.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    @NonNull
    @Override
    public ConfirmedAppointmentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirmed_appointements_item, parent, false);
        return new ConfirmedAppointmentsHolder(v);
    }

    class ConfirmedAppointmentsHolder extends RecyclerView.ViewHolder{
        TextView dateAppointement;
        TextView studentName;
        TextView appointementType;
        CircleImageView studentImage;
        public ConfirmedAppointmentsHolder(@NonNull View itemView) {
            super(itemView);
            dateAppointement = itemView.findViewById(R.id.appointement_date);
            studentName = itemView.findViewById(R.id.patient_name);
            appointementType = itemView.findViewById(R.id.appointement_type);
            studentImage = itemView.findViewById(R.id.patient_image);
        }
    }
}
