package com.grpprj.submit;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import adapters.ChaptersAdapter;
import models.ChaptersModel;

public class ProjectChaptersActivity extends AppCompatActivity  implements ChaptersAdapter.OnProjectListener {

    private static final String TAG = "ProjectChaptersActivity";
    RecyclerView rvChaptersList;
    private ArrayList<ChaptersModel> chaptersModelArrayList;

    ChaptersAdapter chaptersAdapter;
    FirebaseStorage storage;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_chapters);

        initRecyclerView();
    }

    public void initRecyclerView() {

        Intent intent = getIntent();
        String ID = intent.getStringExtra("ID");

        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String userID = firebaseUser.getUid();

        rvChaptersList = findViewById(R.id.rvChaptersList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProjectChaptersActivity.this);

        rvChaptersList.setLayoutManager(linearLayoutManager);

        chaptersModelArrayList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        CollectionReference chaptersReference = FirebaseFirestore.getInstance().collection("Projects").document(ID).collection("Chapters");

        chaptersReference.orderBy("Number").addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Database exception", error);
                } else {
                    assert value != null;
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            chaptersModelArrayList.add(dc.getDocument().toObject(ChaptersModel.class));
                        }
                        chaptersAdapter = new ChaptersAdapter(ProjectChaptersActivity.this, chaptersModelArrayList, ProjectChaptersActivity.this);
                        rvChaptersList.setAdapter(chaptersAdapter);
                        chaptersAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    @Override
    public void onProjectClick(int position) {
        chaptersAdapter.downloadFile(position);
    }


        /*File rootPath = new File(Environment.getExternalStorageDirectory(), "Documents");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath,"Chapter 1");

        currentDocument.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ",";local tem file created  created " +localFile.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ",";local tem file not created  created " +exception.toString());
            }
        });*/

}