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

public class MainActivity extends AppCompatActivity {

    EditText emailID, password;
    Button btnSignUp;
    Button btnSignIn;
    FirebaseAuth aFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        aFirebaseAuth = FirebaseAuth.getInstance();
        emailID = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        btnSignIn = findViewById(R.id.button2);
        btnSignUp = findViewById(R.id.button);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(MainActivity.this, "Both fields are empty", Toast.LENGTH_SHORT).show();
                }
                else if (! email.isEmpty() && (! pass.isEmpty())){
                    aFirebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(MainActivity.this, HomeScreen.class));
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent i = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);
            }
        });

    }
}