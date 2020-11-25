package com.example.keepfitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;

public class FitnessGoals extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private Button btnBack;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser aUser;
    private String userID;
    private ProgressDialog load;
    private String key = "";
    private String task;
    private String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_goals);

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        firebaseAuth = FirebaseAuth.getInstance();
        load = new ProgressDialog(this);
        aUser = firebaseAuth.getCurrentUser();
        userID = aUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("goal").child(userID);
        floatingActionButton = findViewById(R.id.fBTN);
        floatingActionButton.setOnClickListener((v) -> {addGoal();});
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToHomePage = new Intent(FitnessGoals.this, HomeScreen.class);
                startActivity(ToHomePage);
            }
        });


    }

    private void addGoal() {
        AlertDialog.Builder aDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View aView = inflater.inflate(R.layout.input_goals, null);
        aDialog.setView(aView);
        AlertDialog dialog = aDialog.create();
        dialog.show();

        final EditText goal = aView.findViewById(R.id.goal);
        final EditText description = aView.findViewById(R.id.description);
        Button cancel = aView.findViewById(R.id.cancelBTN);
        cancel.setOnClickListener((v -> {dialog.dismiss();}));
        Button save = aView.findViewById(R.id.saveBTN);
        save.setOnClickListener((v -> {
            String aGoal = goal.getText().toString().trim();
            String aDescription = description.getText().toString().trim();
            String id = reference.push().getKey();
            String date = DateFormat.getDateInstance().format(new Date());


            if (TextUtils.isEmpty(aGoal)){
                goal.setError("Please enter a goal");
                return; }

            if (TextUtils.isEmpty(aDescription)){
                description.setError("Please enter a description");
                return;

            }
            else {
                load.setMessage("Adding your goal");
                load.setCanceledOnTouchOutside(false);
                load.show();

                Model model = new Model(aGoal, aDescription, id, date);
                reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(FitnessGoals.this, "Your goal has been added", Toast.LENGTH_SHORT).show();
                            load.dismiss();
                        }
                        else {
                            String error = task.getException().toString();
                            Toast.makeText(FitnessGoals.this, "Failed to add:" + error, Toast.LENGTH_SHORT).show();
                            load.dismiss();
                        }
                    }
                });
            }
            dialog.dismiss();

        }));

        dialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions
                .Builder<Model>().setQuery(reference, Model.class).build();
        FirebaseRecyclerAdapter<Model, goalViewer> adapter = new FirebaseRecyclerAdapter<Model, goalViewer>(options) {
            @Override
            protected void onBindViewHolder(@NonNull goalViewer holder, int position, @NonNull Model model) {
                holder.setDate(model.getDate());
                holder.setGoal(model.getTask());
                holder.setDescription(model.getDescription());

                holder.aView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        key = getRef(position).getKey();
                        task = model.getTask();
                        description = model.getDescription();

                        updateGoal();
                    }
                });
            }

            @NonNull
            @Override
            public goalViewer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_goal, parent, false);
                return new goalViewer(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class goalViewer extends RecyclerView.ViewHolder{
        View aView;


        public goalViewer(@NonNull View itemView) {
            super(itemView);
            aView = itemView;
        }
        public void setGoal(String goal){
            TextView goalTV = aView.findViewById(R.id.tvGoal);
            goalTV.setText(goal);
        }
        public void setDescription(String description){
            TextView desTV = aView.findViewById(R.id.tvDescription);
            desTV.setText(description);
        }
        public void setDate(String date){
            TextView dateTV = aView.findViewById(R.id.tvDate);

        }
    }
    private void updateGoal(){

        AlertDialog.Builder aDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update_goals, null);
        aDialog.setView(view);

        AlertDialog dialog = aDialog.create();

        EditText aGoal = view.findViewById(R.id.etGoal);
        EditText aDescription = view.findViewById(R.id.etDescription);

        aGoal.setText(task);
        aGoal.setSelection(task.length());

        aDescription.setText(description);
        aDescription.setSelection(description.length());

        Button dltBtn = view.findViewById(R.id.btnDelete);
        Button updateBtn = view.findViewById(R.id.btnUpdate);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = aGoal.getText().toString().trim();
                description = aDescription.getText().toString().trim();

                String date = DateFormat.getDateInstance().format(new Date());
                Model model = new Model(task, description, key, date);

                reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(FitnessGoals.this, "Your goal has been updated", Toast.LENGTH_SHORT).show();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(FitnessGoals.this, "Update has failed"+error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.dismiss();
            }
        });

        dltBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(FitnessGoals.this, "Your goal has been deleted", Toast.LENGTH_SHORT).show();
                        } else{
                            String error = task.getException().toString();
                            Toast.makeText(FitnessGoals.this, "Delete has failed"+error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();

            }
        });


        dialog.show();

    }
}