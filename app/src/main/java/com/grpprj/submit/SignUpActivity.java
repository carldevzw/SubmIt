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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    TextInputLayout txtRegNumber, txtPassword, txtConPassword, txtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtPassword = (TextInputLayout) findViewById(R.id.txtPassword);
        txtConPassword = (TextInputLayout) findViewById(R.id.txtConPassword);
        txtUsername = (TextInputLayout) findViewById(R.id.txtUsername);
        txtRegNumber = (TextInputLayout) findViewById(R.id.txtRegNum);

        Button btnSubmit= (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username= txtUsername.getEditText().getText().toString().trim();
                String regNum= txtRegNumber.getEditText().getText().toString().trim();
                String password= txtConPassword.getEditText().getText().toString().trim();

                if(valUsername() && valPassword() && valPasswordMatch()){
                mAuth= FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            newUserSign(username, regNum, password);
                            Toast.makeText(getApplicationContext(), "Signup Successful", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignUpActivity.this, LauncherActivity.class));

                            txtUsername.getEditText().setText(null);
                            txtRegNumber.getEditText().setText(null);
                            txtConPassword.getEditText().setText(null);
                            txtPassword.getEditText().setText(null);

                        }else{
                            Toast.makeText(getApplicationContext(), "Signup Failed. Try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else{
                Toast.makeText(SignUpActivity.this, "Signup Error", Toast.LENGTH_SHORT).show();
            }
        }
    });
}

    public void newUserSign(String uName, String regNum, String password){

        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        String userID= firebaseUser.getUid();

        Map<String, Object> user = new HashMap<>();
        user.put("Username", uName);
        user.put("Reg Number", regNum);
        user.put("Password", password);

        db.collection("Students").document(userID)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Account Created");
                        Toast.makeText(SignUpActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Sign up exception", e);
                        Toast.makeText(getApplicationContext(), "Error, try again." + e, Toast.LENGTH_LONG).show();
                    }
                });
    }

    public boolean valUsername(){

        TextInputLayout txtUsername=  (TextInputLayout) findViewById(R.id.txtUsername);
        String username= txtUsername.getEditText().getText().toString().trim();
        String checkSpace= "^(.+)@(.+)$";

        if(username.isEmpty()){
            txtUsername.setError("Required");
            return false;
        }else if(!username.matches(checkSpace)){
            txtUsername.setError("Remove whitespaces");
            return false;
        }
        else{
            txtUsername.setError(null);
            txtUsername.setErrorEnabled(false);
            return true;
        }
    }
    public boolean valPassword() {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        TextInputLayout txtPassword= (TextInputLayout) findViewById(R.id.txtPassword);


        String password = txtPassword.getEditText().getText().toString().trim();

        if (!password.matches(PASSWORD_PATTERN)) {
            txtPassword.setError("Invalid Password");
            return false;
        } else {
            return true;

        }
    }
    public boolean valPasswordMatch(){
        TextInputLayout txtConPassword= (TextInputLayout) findViewById(R.id.txtConPassword);
        TextInputLayout txtPassword= (TextInputLayout) findViewById(R.id.txtPassword);

        String password= txtPassword.getEditText().getText().toString().trim();
        String password2= txtConPassword.getEditText().getText().toString().trim();

        if(password.isEmpty()){
            txtConPassword.setError("Required");
            return false;
        }else if(!password2.equals(password)){
            txtConPassword.setError("Passwords do not match");
            return false;
        }
        else{
            txtConPassword.setError(null);
            txtConPassword.setErrorEnabled(false);
            return true;
        }
    }
}