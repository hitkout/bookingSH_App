package com.example.bookingshapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private ConstraintLayout constraint;
    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private TextView textViewDate;
    private TextView textViewInfoInSignUpPage;
    private List<String> user;
    private ListView usersInHall;
    private Button btnSignUp;
    private Button btnUnsubscribe;

    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference uidRef = rootRef.child("Users").child(uid);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        getChildrenFromDB();
        getChildrenCountFromDB();
        getIntentMain();
        btnClick();
    }

    private void init(){
        constraint = findViewById(R.id.constraintElementSignUp);
        textViewDate = findViewById(R.id.textViewDate);
        textViewInfoInSignUpPage = findViewById(R.id.textViewInfoInSignUpPage);
        user = new ArrayList<>();
        usersInHall = findViewById(R.id.usersInHall);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnUnsubscribe = findViewById(R.id.btnUnsubscribe);
    }

    @SuppressLint("SetTextI18n")
    private void getIntentMain(){
        Intent intent = getIntent();
        textViewDate.setText(intent.getStringExtra("getDateFromList")+" / "+intent.getStringExtra("getTimeFromList")+" / "+intent.getStringExtra("getOfficeFromList"));
    }

    private void getChildrenCountFromDB(){
        mDatabase.child("dates").child(getIntent().getStringExtra("getDateFromList"))
                .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textViewInfoInSignUpPage.setText(getIntent().getStringExtra("getOfficeFromList") + ": осталось " + (10 - snapshot.getChildrenCount()) + " мест!");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getChildrenFromDB(){
        mDatabase.child("dates").child(getIntent().getStringExtra("getDateFromList"))
                .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getChildren(snapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getChildren(DataSnapshot snapshot){
        List<String> listUsersInDB = new ArrayList<>();
        for (DataSnapshot dss : snapshot.getChildren()){
            String values = String.valueOf(dss.getValue());
            values = values.substring(1, values.length() - 1);
            values = values.replace("=", ": ");
            listUsersInDB.add(values);
        }
        usersInHall.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listUsersInDB));
    }

    private void btnClick(){
        uidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String id = dataSnapshot.child("id").getValue(String.class);
                List<String> polya = new ArrayList<>();
                polya.add("Имя");
                polya.add("Связь");
                user.add(name);
                user.add(email);

                btnSignUp.setOnClickListener(v -> mDatabase.child("dates").child(getIntent().getStringExtra("getDateFromList"))
                        .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() < 10){
                            addUsersInHall(polya, name, id);
                        }
                        else
                            Snackbar.make(constraint, "Максимальное число клиентов!", Snackbar.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                }));
                btnUnsubscribe.setOnClickListener(v -> deleteUsersInHall(name, id));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void addUsersInHall(List<String> polya, String name, String id){
        for (int i = 0; i < user.size(); i++){
            mDatabase.child("dates").child(getIntent().getStringExtra("getDateFromList"))
                    .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).child("Клиент " + name + " (" + id + ")").child(polya.get(i)).setValue(user.get(i));
        }
        usersInHall.refreshDrawableState();
    }

    private void deleteUsersInHall(String name, String id){
        mDatabase.child("dates").child(getIntent().getStringExtra("getDateFromList"))
                .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).child("Клиент " + name + " (" + id + ")").removeValue();
        usersInHall.refreshDrawableState();
    }
}