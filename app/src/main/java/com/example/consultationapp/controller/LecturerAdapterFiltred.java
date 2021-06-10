package com.example.consultationapp.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consultationapp.model.Lecturer;
import com.example.consultationapp.R;
import com.example.consultationapp.model.Common.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class LecturerAdapterFiltred extends RecyclerView.Adapter<LecturerAdapterFiltred.DoctoreHolder2> implements Filterable {
    public static boolean specialiteSearch = false;
    static String doc;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth;
    static CollectionReference addRequest = db.collection("Request");
    private List<Lecturer> mTubeList;
    private List<Lecturer> mTubeListFiltered;
    StorageReference pathReference ;


    public LecturerAdapterFiltred(List<Lecturer> tubeList){
        mTubeList = tubeList;
        mTubeListFiltered = tubeList;
    }

    @NonNull
    @Override
    public DoctoreHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecturer_item,
                parent, false);
        return new DoctoreHolder2(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctoreHolder2 doctoreHolder, int i) {
        final Lecturer lecturer = mTubeListFiltered.get(i);
        final TextView t = doctoreHolder.title ;
        doctoreHolder.title.setText(lecturer.getFirstname() +" "+ lecturer.getLastname());
        /// ajouter l'image
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        pathReference = FirebaseStorage.getInstance().getReference().child("users/"+lecturer.getEmail()+"/profile.jpg");
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(doctoreHolder.image);//Image location

                // profileImage.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        doctoreHolder.module.setText("Module : "+lecturer.getModule());//Change to get Module
        doctoreHolder.email.setText(lecturer.getEmail());
        final String idPat = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        final String idDoc = lecturer.getEmail();
        // doctoreHolder.image.setImageURI(Uri.parse("drawable-v24/ic_launcher_foreground.xml"));
        doctoreHolder.addDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> note = new HashMap<>();
                note.put("id_patient", idPat);
                note.put("id_doctor", idDoc);
                addRequest.document().set(note)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Snackbar.make(t, "Demande envoyée", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                doctoreHolder.addDoc.setVisibility(View.INVISIBLE);
            }
        });
        doctoreHolder.appointemenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doc= lecturer.getEmail();
                System.out.println("Doctors Email is "+lecturer.getEmail());
                Common.currentLecturer = lecturer.getEmail();
                Common.currentLecturerName = lecturer.getFirstname();
                Common.CurrentPhone = lecturer.getPhoneNumber();
                openPage(v.getContext());

            }
        });

    }

    @Override
    public int getItemCount() {
        return mTubeListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String pattern = constraint.toString().toLowerCase();
                if(pattern.isEmpty()){
                    mTubeListFiltered = mTubeList;
                } else {
                    List<Lecturer> filteredList = new ArrayList<>();
                    for(Lecturer tube: mTubeList){
                        if(specialiteSearch == false) {
                            if (tube.getFirstname().toLowerCase().contains(pattern) || tube.getLastname().toLowerCase().contains(pattern) || tube.getModule().toLowerCase().contains(pattern) || tube.getEmail().toLowerCase().contains(pattern)) {
                                filteredList.add(tube);
                            }
                        }
                        else{
                            if (tube.getModule().toLowerCase().contains(pattern) || tube.getEmail().toLowerCase().contains(pattern)) {//put getModule
                                filteredList.add(tube);
                            }
                        }
                    }
                    mTubeListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mTubeListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mTubeListFiltered = (ArrayList<Lecturer>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    class DoctoreHolder2 extends RecyclerView.ViewHolder {

        Button appointemenBtn;
        TextView title;
        TextView module,email;
        CircleImageView image;
        Button addDoc;
        Button load;
        public DoctoreHolder2(@NonNull View itemView) {
            super(itemView);
            addDoc = itemView.findViewById(R.id.addDocBtn);
            title = itemView.findViewById(R.id.doctor_view_title);
            module = itemView.findViewById(R.id.text_view_description);
            email = itemView.findViewById(R.id.text_view_email);
            image=itemView.findViewById(R.id.doctor_item_image);
            appointemenBtn=itemView.findViewById(R.id.appointemenBtn);
        }
    }
    private void openPage(Context wf){
        Intent i = new Intent(wf, TestActivity.class);
        wf.startActivity(i);
    }
}
