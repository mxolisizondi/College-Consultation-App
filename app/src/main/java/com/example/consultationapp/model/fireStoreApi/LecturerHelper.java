package com.example.consultationapp.model.fireStoreApi;

import com.example.consultationapp.model.Lecturer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LecturerHelper {
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static CollectionReference LecturerRef = db.collection("Lecturer");

    public static void addLecturer(String firstname, String lastname, String role, String phoneNumber, String email, String course, String module, String uid){
        Lecturer lecturer = new Lecturer(firstname, lastname, role, phoneNumber, email, course, module, uid);

        LecturerRef.document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).set(lecturer);

    }
}
