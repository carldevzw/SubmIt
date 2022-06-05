package com.grpprj.submit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import adapters.ChaptersAdapter;
import models.ChaptersModel;

public class ViewChaptersFragment extends Fragment implements ChaptersAdapter.OnProjectListener {

    private static final String TAG = "ViewChaptersFragment";
    View view;
    RecyclerView rvChaptersList;
    private ArrayList<ChaptersModel> chaptersModelArrayList;
    private ChaptersAdapter chaptersAdapter;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_view_chapters, container, false);

        initRecyclerView(view);
        return view;
    }

    public void initRecyclerView(View view){

        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        String userID= firebaseUser.getUid();

        rvChaptersList= view.findViewById(R.id.rvChaptersList);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity());

        rvChaptersList.setLayoutManager(linearLayoutManager);

        chaptersModelArrayList = new ArrayList<>();

        db= FirebaseFirestore.getInstance();

        CollectionReference chaptersCollection= db.collection("Projects").document(userID).collection("Chapters");


        chaptersCollection
                .orderBy("Number", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.w(TAG, "Database exception", error);
                        }else {
                            assert value != null;
                            for(DocumentChange dc: value.getDocumentChanges()){
                                if(dc.getType()== DocumentChange.Type.ADDED){
                                    chaptersModelArrayList.add(dc.getDocument().toObject(ChaptersModel.class));
                                }
                                chaptersAdapter= new ChaptersAdapter(getActivity(), chaptersModelArrayList, ViewChaptersFragment.this);
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
}