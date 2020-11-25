package com.example.keepfitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeScreen extends AppCompatActivity {

    Button btnLogout;
    Button btnBodyDetails;
    Button btnFitnessGoal;
    Button btnViewGoal;
    Button btnProgress;
    FirebaseAuth aFirebaseAuth;
    private FirebaseAuth.AuthStateListener aAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        btnLogout = findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent ToMainPage = new Intent(HomeScreen.this, MainActivity.class);
                startActivity(ToMainPage);
            }
        });

        btnBodyDetails = findViewById(R.id.bodydetails);
        btnBodyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToBodyPage = new Intent(HomeScreen.this, BodyDetails.class);
                startActivity(ToBodyPage);
            }
        });

        btnFitnessGoal = findViewById(R.id.fitnessgoals);
        btnFitnessGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToGoalPage = new Intent(HomeScreen.this, FitnessGoals.class);
                startActivity(ToGoalPage);
            }
        });

        btnViewGoal = findViewById(R.id.viewgoals);
        btnViewGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToVGoalsPage = new Intent(HomeScreen.this, CompletedGoals.class);
                startActivity(ToVGoalsPage);
            }
        });

        btnProgress = findViewById(R.id.viewprogress);
        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToProgressPage = new Intent(HomeScreen.this, Progress.class);
                startActivity(ToProgressPage);
            }
        });


    }
}