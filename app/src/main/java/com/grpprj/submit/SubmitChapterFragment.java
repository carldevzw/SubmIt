package com.grpprj.submit;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class SubmitChapterFragment extends Fragment {

    private static final String TAG = "SubmitChapterFragment";
    View view;
    StorageReference storageReference;
    FirebaseFirestore db;
    Uri docUri;
    private ProgressDialog progressDialog;
    TextInputLayout txtChaptNum, txtChaptNam;

    private ActivityResultLauncher<String> selectDoc;

    Button btnSelect, btnSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_submit, container, false);

                selectDoc = registerForActivityResult(
                        new ActivityResultContracts.GetContent(),
                        new ActivityResultCallback<Uri>() {
                            @Override
                            public void onActivityResult(Uri result) {
                                docUri = result;
                                Toast.makeText(getContext(), "Document Selected.", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

        btnSelect= view.findViewById(R.id.btnSelectDoc);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDoc.launch("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            }
        });

        btnSubmit= view.findViewById(R.id.btnAdd);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDocument(docUri);
            }
        });

        return view;

    }

    private void uploadDocument(Uri docUrihere) {

        progressDialog= new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading chapter...");
        progressDialog.show();

        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        String userID= firebaseUser.getUid();

        txtChaptNum= (TextInputLayout) getView().findViewById(R.id.txt_chptNum);
        txtChaptNam= (TextInputLayout) getView().findViewById(R.id.txt_chptName);

        storageReference= FirebaseStorage.getInstance().getReference("Projects/Chapter" + txtChaptNum.getEditText().getText().toString().trim());

        storageReference.putFile(docUrihere)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    String docURL= task.getResult().toString();
                                    String chaptName= txtChaptNam.getEditText().getText().toString().trim();
                                    String chaptNum= txtChaptNum.getEditText().getText().toString().trim();

                                    Map<String, Object> projectDoc = new HashMap<>();
                                    projectDoc.put("Name", chaptName);
                                    projectDoc.put("Number", chaptNum);
                                    projectDoc.put("URL", docURL);
                                    projectDoc.put("Reviewed", false);

                                    DocumentReference userDocument = FirebaseFirestore.getInstance().collection("Projects")
                                            .document(userID);


                                    DocumentReference projectsDocument = FirebaseFirestore.getInstance().collection("Supervisors").document("Mr C. Zano")
                                            .collection("Projects").document("Student 1");

                                    // Add a new document with a generated ID
                                    userDocument.collection("Chapters").document(chaptNum)
                                            .set(projectDoc)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG, "Chapter added.");

                                                    if (progressDialog.isShowing()) {
                                                        progressDialog.dismiss();
                                                        txtChaptNam.getEditText().setText(null);
                                                        txtChaptNum.getEditText().setText(null);
                                                        Toast.makeText(getContext(), "Chapter Submitted", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    if(progressDialog.isShowing()){
                                                        progressDialog.dismiss();
                                                        Log.d(TAG, "Failed to add chapter." + e);
                                                    }
                                                }
                                            });
                                }else{
                                    Toast.makeText(getContext(), "Failed to collect URL", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Document upload failed: ", e);
                        Toast.makeText(getContext(), "Chapter upload failed", Toast.LENGTH_LONG).show();
                    }
                });
        
    }

}