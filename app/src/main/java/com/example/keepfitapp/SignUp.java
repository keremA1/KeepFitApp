package com.example.keepfitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    EditText emailID, password;
    Button btnSignIn;
    TextView tvBack;
    FirebaseAuth aFirebaseAuth;
    private FirebaseAuth.AuthStateListener aAuthStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        aFirebaseAuth = FirebaseAuth.getInstance();
        emailID = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        btnSignIn = findViewById(R.id.button);
        tvBack = findViewById(R.id.textView2);

        aAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser aFirebaseUser = aFirebaseAuth.getCurrentUser();
                if (aFirebaseUser != null) {
                    Toast.makeText(SignUp.this, "You have successfully logged in", Toast.LENGTH_SHORT).show();
                    Intent b = new Intent (SignUp.this, HomeScreen.class);
                    startActivity(b);
                }
                else {
                    Toast.makeText(SignUp.this, "Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailID.getText().toString();
                String pass = password.getText().toString();
                if (pass.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if(email.isEmpty()){
                    emailID.setError("Please enter an email address");
                    emailID.requestFocus();
                }
                else if (email.isEmpty() && (pass.isEmpty())){
                    Toast.makeText(SignUp.this, "Both fields are empty", Toast.LENGTH_SHORT).show();
                }
                else if (! email.isEmpty() && (! pass.isEmpty())){
                    aFirebaseAuth.signInWithEmailAndPassword(email, pass). addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignUp.this, "Error has accured, please try again", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent ToHomePage = new Intent (SignUp.this, HomeScreen.class);
                                startActivity(ToHomePage);

                            }
                        }
                    });
                }
                else {
                    Toast.makeText(SignUp.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BackPage = new Intent(SignUp.this, MainActivity.class);
                startActivity(BackPage);
            }
        });

    }
}