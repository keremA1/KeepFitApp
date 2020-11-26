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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class BodyDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private Button btnBack;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser aUser;
    private String userID;
    private ProgressDialog load;
    private String key = "";
    private String sAge;
    private String sWeight;
    private String sHeight;
    private String sBmi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_details);

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
        reference = FirebaseDatabase.getInstance().getReference().child("details").child(userID);
        floatingActionButton = findViewById(R.id.fBTN);
        floatingActionButton.setOnClickListener((v) -> {addDetails();});

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToHomePage = new Intent(BodyDetails.this, HomeScreen.class);
                startActivity(ToHomePage);
            }
        });


    }

    private void addDetails() {
        AlertDialog.Builder aDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View aView = inflater.inflate(R.layout.input_details, null);
        aDialog.setView(aView);
        AlertDialog dialog = aDialog.create();
        dialog.show();

        final EditText age = aView.findViewById(R.id.age);
        final EditText weight = aView.findViewById(R.id.weight);
        final EditText height = aView.findViewById(R.id.height);
        final EditText bmi = aView.findViewById(R.id.bmi);

        Button cancel = aView.findViewById(R.id.cancelBTN);
        cancel.setOnClickListener((v -> {dialog.dismiss();}));
        Button save = aView.findViewById(R.id.saveBTN);
        save.setOnClickListener((v -> {
            String mAge = age.getText().toString().trim();
            String mHeight = height.getText().toString().trim();
            String mWeight = weight.getText().toString().trim();
            String mBMI = bmi.getText().toString().trim();

            String id = reference.push().getKey();
            String date = DateFormat.getDateInstance().format(new Date());


            if (TextUtils.isEmpty(mAge)){
                age.setError("Please enter your age");
                return; }

            if (TextUtils.isEmpty(mWeight)){
                weight.setError("Please enter your height");
                return; }

            if (TextUtils.isEmpty(mHeight)){
                height.setError("Please enter a description");
                return; }

            if (TextUtils.isEmpty(mBMI)){
                bmi.setError("Please enter your BMI");
                return; }


            else {
                load.setMessage("Adding your details");
                load.setCanceledOnTouchOutside(false);
                load.show();

                aModel model = new aModel(mAge, mWeight, mHeight, mBMI, id, date);
                reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(BodyDetails.this, "Your details has been added", Toast.LENGTH_SHORT).show();
                            load.dismiss();
                        }
                        else {
                            String error = task.getException().toString();
                            Toast.makeText(BodyDetails.this, "Failed to add:" + error, Toast.LENGTH_SHORT).show();
                            load.dismiss();
                        }
                    }
                });
            }
            dialog.dismiss();

        }));

        dialog.show();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setAge(String age) {
            TextView ageTV = mView.findViewById(R.id.tvAge);
            ageTV.setText(age);
        }

        public void setWeight (String weight){
            TextView weightTV = mView.findViewById(R.id.tvWeight);
            weightTV.setText(weight);
        }

        public void setHeight (String height){
            TextView heightTV = mView.findViewById(R.id.tvHeight);
            heightTV.setText(height);
        }

        public void setBMI (String bmi){
            TextView bmiTV = mView.findViewById(R.id.tvBMI);
            bmiTV.setText(bmi);
        }

        public void setDate (String date){
            TextView dateTV = mView.findViewById(R.id.tvDate);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<aModel> options = new FirebaseRecyclerOptions.Builder<aModel>()
        .setQuery(reference, aModel.class).build();

        FirebaseRecyclerAdapter<aModel, MyViewHolder> adapter = new FirebaseRecyclerAdapter<aModel, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull aModel model) {
                holder.setDate(model.getDate());
                holder.setAge(model.getAge());
                holder.setWeight(model.getWeight());
                holder.setHeight(model.getHeight());
                holder.setBMI(model.getBmi());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        key = getRef(position).getKey();
                        sAge = model.getAge();
                        sWeight = model.getWeight();
                        sHeight = model.getHeight();
                        sBmi = model.getBmi();

                        updateDetails();

                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_details, parent, false);
                return new MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void updateDetails() {

        AlertDialog.Builder aDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update_details, null);
        aDialog.setView(view);

        AlertDialog dialog = aDialog.create();

        EditText aAge = view.findViewById(R.id.etAge);
        EditText aWeight = view.findViewById(R.id.etWeight);
        EditText aHeight = view.findViewById(R.id.etHeight);
        EditText aBMI = view.findViewById(R.id.etBMI);

        aAge.setText(sAge);
        aAge.setSelection(sAge.length());

        aWeight.setText(sWeight);
        aWeight.setSelection(sWeight.length());

        aHeight.setText(sHeight);
        aHeight.setSelection(sHeight.length());

        aBMI.setText(sBmi);
        aBMI.setSelection(sBmi.length());

        Button dltBtn = view.findViewById(R.id.btnDelete);
        Button updateBtn = view.findViewById(R.id.btnUpdate);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sAge = aAge.getText().toString().trim();
                sWeight = aWeight.getText().toString().trim();
                sHeight = aHeight.getText().toString().trim();
                sBmi = aBMI.getText().toString().trim();

                String date = DateFormat.getDateInstance().format(new Date());
                aModel model = new aModel(sAge, sWeight, sHeight, sBmi, key, date);

                reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(BodyDetails.this, "Your details have been updated", Toast.LENGTH_SHORT).show();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(BodyDetails.this, "Update has failed" + error, Toast.LENGTH_SHORT).show();
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
                        if (task.isSuccessful()) {
                            Toast.makeText(BodyDetails.this, "Your details has been deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(BodyDetails.this, "Delete has failed" + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();

            }
        });


        dialog.show();
    }
}

