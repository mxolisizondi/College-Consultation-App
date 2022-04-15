package com.example.consultationapp.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.consultationapp.R;
import com.example.consultationapp.model.Role;
import com.example.consultationapp.model.fireStoreApi.LecturerHelper;
import com.example.consultationapp.model.fireStoreApi.StudentHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register_User_Information extends AppCompatActivity {
    EditText firstname,lastname,phone;
    Button registerBtn;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    boolean valid = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_information);

        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        phone = findViewById(R.id.contactNumber);
        registerBtn = findViewById(R.id.submit);



        Spinner rolers_spinner = (Spinner) findViewById(R.id.roles_spinner);
        ArrayAdapter<CharSequence> rolesAdapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        rolesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rolers_spinner.setAdapter(rolesAdapter);

        Spinner department_spinner = (Spinner) findViewById(R.id.departments_spinner);
        ArrayAdapter<CharSequence> deptAdapter = ArrayAdapter.createFromResource(this,
                R.array.departments_array, android.R.layout.simple_spinner_item);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department_spinner.setAdapter(deptAdapter);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            @Override
            public void onClick(View v) {
                checkField(firstname);
                checkField(lastname);
                checkField(phone);
                if(valid){
                    String getFirstname = firstname.getText().toString();
                    String getLastname = lastname.getText().toString();
                    String getPhone = phone.getText().toString();
                    DocumentReference df = fStore.collection("Users").document(user.getUid());
                    Map<String, Object> userInformation = new HashMap<>();
                    userInformation.put("Firstname", getFirstname);
                    userInformation.put("Lastname", getLastname);
                    userInformation.put("Phone", getPhone);
                    userInformation.put("Course", department_spinner.getSelectedItem().toString());
                    userInformation.put("Module", "");
                    //Role
                    userInformation.put("Role", rolers_spinner.getSelectedItem().toString());

                    df.set(userInformation); // add onSuccess
                    Toast.makeText(Register_User_Information.this, "Successful registered",
                            Toast.LENGTH_SHORT).show();
                    if(rolers_spinner.getSelectedItem().toString().equals(Role.Lecturer)){
                        LecturerHelper.addLecturer(getFirstname, getLastname,Role.Lecturer,getPhone,user.getEmail().toString(),department_spinner.getSelectedItem().toString(),"","");
                        startActivity(new Intent(getApplicationContext(), LecturerMainActivity.class));

                    }
                    else if(rolers_spinner.getSelectedItem().toString().equals(Role.Student)){
                        StudentHelper.addStudent(getFirstname, getLastname,Role.Student,getPhone,user.getEmail().toString(),"","","");
                        startActivity(new Intent(getApplicationContext(), StudentMainActivity2.class));
                    }
                }
                else {
                    Toast.makeText(Register_User_Information.this, "Please fill all required fields",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });




        rolers_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = rolers_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        department_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = department_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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