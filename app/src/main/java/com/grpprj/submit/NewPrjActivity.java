package com.grpprj.submit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewPrjActivity extends AppCompatActivity {

    private static final String TAG = "NewPrjActivity";
    Button btnCreate;
    FirebaseFirestore db;

    TextInputLayout txtTopic;
    AutoCompleteTextView  txtLevel, txtSupervisor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_prj);

        txtTopic= (TextInputLayout) findViewById(R.id.txtTopic);
        txtLevel= (AutoCompleteTextView) findViewById(R.id.txtLevel);
        txtSupervisor= (AutoCompleteTextView)findViewById(R.id.txtSupervisor);
        btnCreate= (Button) findViewById(R.id.btnCreate);

        initSpinners(txtLevel, txtSupervisor);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String topic= txtTopic.getEditText().getText().toString().trim();
                String level= txtLevel.getText().toString().trim();
                String supervisor= txtSupervisor.getText().toString().trim();
                newProjectEntry(topic, level, supervisor);

            }
        });
    }

    private void newProjectEntry(String topic, String level, String supervisor) {

        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        String userID= firebaseUser.getUid();

        Map<String, Object> project = new HashMap<>();
        project.put("Topic", topic);
        project.put("Level", level);
        project.put("Supervisor", supervisor);
        project.put("ID", userID);

        DocumentReference userDocument = FirebaseFirestore.getInstance().collection("Projects").document(userID);
        DocumentReference supervisorDocument = FirebaseFirestore.getInstance().collection("Supervisors").document(supervisor);

        userDocument.set(project).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Project created in projects collection");
                        Toast.makeText(NewPrjActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewPrjActivity.this, ChaptersActivity.class));
                       /*supervisorDocument.collection("Projects").document("Student 1")
                                        .set(project).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "Project added to projects collection");
                                        Toast.makeText(NewPrjActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(NewPrjActivity.this, ChaptersActivity.class));
                                    }
                                });*/
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Project failed to create.");
                        Toast.makeText(NewPrjActivity.this, "Failed: Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initSpinners(AutoCompleteTextView txtLevel, AutoCompleteTextView txtSupervisor){
        String[] supervisors = getResources().getStringArray(R.array.lecturer_array);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.supervisor_dropdown,
                        supervisors);

        txtSupervisor.setAdapter(adapter);

        String[] level = getResources().getStringArray(R.array.level_array);

        ArrayAdapter<String> adapter2 =
                new ArrayAdapter<>(
                        this,
                        R.layout.supervisor_dropdown,
                        level);

        txtLevel.setAdapter(adapter2);

    }
}