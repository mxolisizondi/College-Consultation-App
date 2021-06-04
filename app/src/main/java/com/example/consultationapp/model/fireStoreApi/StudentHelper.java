package com.example.consultationapp.model.fireStoreApi;

import com.example.consultationapp.model.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentHelper {
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static CollectionReference StudentRef = db.collection("Student");

    public static void addStudent(String firstname, String lastname, String role, String phoneNumber, String email, String course, String url, String uid){
        Student student = new Student(firstname, lastname, role, phoneNumber, email, course, url, uid);
        System.out.println("Create object student");
        StudentRef.document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).set(student);
    }
}
