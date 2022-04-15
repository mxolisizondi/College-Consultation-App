package com.example.consultationapp.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.example.consultationapp.model.Lecturer;
import com.example.consultationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class StudentSearchLecturerActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference doctorRef = db.collection("Lecturer");

    private LecturerAdapterFiltred adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_search_lecturer);
        configureToolbar();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.serachPatRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Query query = doctorRef.orderBy("firstname", Query.Direction.DESCENDING);


        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                adapter = new LecturerAdapterFiltred(task.getResult().toObjects(Lecturer.class));
                recyclerView.setAdapter(adapter);
            }
        });
        //FirestoreRecyclerOptions<Doctor> options = new FirestoreRecyclerOptions.Builder<Doctor>()
        //  .setQuery(query, Doctor.class)
        //  .build();

        //adapter = new DoctoreAdapter(options);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_bar, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        Drawable r= getResources().getDrawable(R.drawable.ic_local_hospital_black_24dp);
        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" Specialite" );
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //menu.findItem(R.id.empty).setTitle(sb);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint(Html.fromHtml("<font color = #000000>" + "Search Lecturer" + "</font>"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                LecturerAdapterFiltred.specialiteSearch = false;
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //3 - Handle actions on menu items
        switch (item.getItemId()) {
            case R.id.option_all:
                LecturerAdapterFiltred.specialiteSearch = true;
                adapter.getFilter().filter("");
                return true;
            case R.id.option_general:
                LecturerAdapterFiltred.specialiteSearch = true;
                adapter.getFilter().filter("Advanced Diploma");
                return true;
            case R.id.option_Dentiste:
                LecturerAdapterFiltred.specialiteSearch = true;
                adapter.getFilter().filter("National Diploma");
                return true;
            case R.id.option_Ophtalmologue:
                LecturerAdapterFiltred.specialiteSearch = true;
                adapter.getFilter().filter("IT");
                return true;
            case R.id.option_ORL:
                LecturerAdapterFiltred.specialiteSearch = true;
                adapter.getFilter().filter("Markerting");
                return true;
            case R.id.option_Pédiatre:
                LecturerAdapterFiltred.specialiteSearch = true;
                adapter.getFilter().filter("HR");
                return true;
            case R.id.option_Radiologue:
                LecturerAdapterFiltred.specialiteSearch = true;
                adapter.getFilter().filter("Engineering");
                return true;
            case R.id.option_Rhumatologue:
                LecturerAdapterFiltred.specialiteSearch = true;
                adapter.getFilter().filter("Accounting");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void configureToolbar(){
        // Get the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("Lecturers list");
        // Sets the Toolbar
        setSupportActionBar(toolbar);
    }
}