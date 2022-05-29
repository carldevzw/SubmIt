package com.grpprj.submit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LauncherActivity extends AppCompatActivity {

    private static final String TAG = "LauncherActivity";
    FirebaseAuth firebaseAuth;
    TextInputLayout txtUsername, txtPassword;
    TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        txtPassword = (TextInputLayout) findViewById(R.id.txtPassword);
        txtUsername = (TextInputLayout) findViewById(R.id.txtUsername);
        Button btnSubmit= (Button) findViewById(R.id.btnSubmit);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);

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
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Incorrect Password or Username!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    
    private void userOrAdminLogin(String username) {
        if(username.contains("busestaff.com")){
            Toast.makeText(getApplicationContext(), "Welcome Admin", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LauncherActivity.this, StaffHomeActivity.class));
        }else{
            Toast.makeText(getApplicationContext(), "User Login Success", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LauncherActivity.this, NewPrjActivity.class));
        }
    }
}