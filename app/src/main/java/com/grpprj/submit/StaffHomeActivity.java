package com.grpprj.submit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import adapters.ChaptersAdapter;
import adapters.ProjectsAdapter;
import models.ChaptersModel;
import models.ProjectsModel;

public class StaffHomeActivity extends AppCompatActivity implements ProjectsAdapter.OnChapterListener {

    private static final String TAG = "StaffHomeActivity";

    RecyclerView rvProjectsList;
    private ArrayList<ProjectsModel> projectsModelArrayList;
    private ProjectsAdapter projectsAdapter;
    String studentID;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);

        initRecyclerView();
    }

    public void initRecyclerView(){

        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        String userID= firebaseUser.getUid();

        rvProjectsList= findViewById(R.id.rvProjectsList);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(StaffHomeActivity.this);

        rvProjectsList.setLayoutManager(linearLayoutManager);

        projectsModelArrayList = new ArrayList<>();

        db= FirebaseFirestore.getInstance();

        DocumentReference supervisorReference= FirebaseFirestore.getInstance().collection("Supervisors").document(userID);

        supervisorReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String supervisor= documentSnapshot.getString("Name");

                CollectionReference projectsCollection= db.collection("Projects");

                projectsCollection
                        .whereEqualTo("Supervisor", supervisor)
                        .orderBy("Level", Query.Direction.ASCENDING)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                if(error != null){
                                    Log.w(TAG, "Database exception", error);
                                }else {
                                    assert value != null;
                                    for(DocumentChange dc: value.getDocumentChanges()){
                                        if(dc.getType()== DocumentChange.Type.ADDED){
                                            projectsModelArrayList.add(dc.getDocument().toObject(ProjectsModel.class));
                                        }
                                        projectsAdapter= new ProjectsAdapter(StaffHomeActivity.this, projectsModelArrayList, StaffHomeActivity.this);
                                        rvProjectsList.setAdapter(projectsAdapter);
                                        projectsAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onChapterClick(int position) {
        String ID= projectsAdapter.getStudentID(position);
        startActivity(new Intent(StaffHomeActivity.this, ProjectChaptersActivity.class).putExtra("ID", ID));
    }
}