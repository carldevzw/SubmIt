package com.grpprj.submit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LauncherActivity extends AppCompatActivity {

    private static final String TAG = "LauncherActivity";
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    TextInputLayout txtUsername, txtPassword;
    TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        txtPassword = (TextInputLayout) findViewById(R.id.txtPassword);
        txtUsername = (TextInputLayout) findViewById(R.id.txtUsername);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);

        Button btnSubmit= (Button) findViewById(R.id.btnSubmit);

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LauncherActivity.this, SignUpActivity.class));
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username= txtUsername.getEditText().getText().toString().trim();
                String Password= txtPassword.getEditText().getText().toString().trim();
                loginUser(Username, Password);

            }
        });
    }

    private void loginUser(String username, String password) {

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userOrAdminLogin(username);
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            txtUsername.getEditText().setText(null);
                            txtPassword.getEditText().setText(null);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Incorrect Password or Username!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    
    private void userOrAdminLogin(String username) {
        if(username.contains("busestaff.ac.zw")){
            Toast.makeText(getApplicationContext(), "Welcome Admin", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LauncherActivity.this, StaffHomeActivity.class));
        }else{

            db = FirebaseFirestore.getInstance();
            FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

            String userID= firebaseUser.getUid();

            DocumentReference userDocument = FirebaseFirestore.getInstance().collection("Projects").document(userID);

            userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "User Login Success", Toast.LENGTH_SHORT).show();
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            startActivity(new Intent(LauncherActivity.this, ChaptersActivity.class));
                            Log.d(TAG, "Document exists!");
                        } else {
                            startActivity(new Intent(LauncherActivity.this, NewPrjActivity.class));
                            Log.d(TAG, "Document does not exist!");
                        }
                    } else {
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                }
            });
        }
    }
}