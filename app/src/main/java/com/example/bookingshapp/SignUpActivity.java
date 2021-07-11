package com.example.bookingshapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private Button btnSignUp;
    private Button btnUnsubscribe;
    private TextView textViewDate;
    private TextView textViewInfoInSignUpPage;
    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private List<String> user;
    private ConstraintLayout constraint;

    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference uidRef = rootRef.child("Users").child(uid);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        getIntentMain();
        btnSignUpClick();
    }

    private void init(){
        btnSignUp = findViewById(R.id.btnSignUp);
        btnUnsubscribe = findViewById(R.id.btnUnsubscribe);
        textViewDate = findViewById(R.id.textViewDate);
        textViewInfoInSignUpPage = findViewById(R.id.textViewInfoInSignUpPage);
        user = new ArrayList<>();
        constraint = findViewById(R.id.constraintElementSignUp);
    }

    @SuppressLint("SetTextI18n")
    private void getIntentMain(){
        Intent intent = getIntent();
        textViewDate.setText(intent.getStringExtra("getDateFromList")+" / "+intent.getStringExtra("getTimeFromList")+" / "+intent.getStringExtra("getOfficeFromList"));
    }

    private void btnSignUpClick(){
        ValueEventListener eventListener = new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String id = dataSnapshot.child("id").getValue(String.class);
                List<String> polya = new ArrayList<>();
                polya.add("name");
                polya.add("email");
                user.add(name);
                user.add(email);
                mDatabase.child("dates").child(getIntent().getStringExtra("getDateFromList"))
                        .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        textViewInfoInSignUpPage.setText(getIntent().getStringExtra("getOfficeFromList") + ": осталось " + (10 - snapshot.getChildrenCount()) + " мест!");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                btnSignUp.setOnClickListener(v -> {
                    mDatabase.child("dates").child(getIntent().getStringExtra("getDateFromList"))
                            .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getChildrenCount() < 10){
                                for (int i = 0; i < user.size(); i++){
                                    mDatabase.child("dates").child(getIntent().getStringExtra("getDateFromList"))
                                            .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).child("Клиент " + name + " (" + id + ")").child(polya.get(i)).setValue(user.get(i));
                                }
                            }
                            else
                                Snackbar.make(constraint, "Максимальное число клиентов!", Snackbar.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                });

                btnUnsubscribe.setOnClickListener(v -> mDatabase.child("dates").child(getIntent().getStringExtra("getDateFromList"))
                        .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).child("Клиент " + name + " (" + id + ")").removeValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        uidRef.addValueEventListener(eventListener);

    }
}