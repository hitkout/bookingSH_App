package com.example.bookingshapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    Button btnSignUp;
    TextView textViewDate, textViewTime, textViewOffice;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private List<String> user;

    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference uidRef = rootRef.child("Users").child(uid);

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
        textViewDate = findViewById(R.id.textViewDate);

        user = new ArrayList<>();
    }

    @SuppressLint("SetTextI18n")
    private void getIntentMain(){
        Intent intent = getIntent();
        textViewDate.setText(intent.getStringExtra("getDateFromList")+" / "+intent.getStringExtra("getTimeFromList")+" / "+intent.getStringExtra("getOfficeFromList"));

    }

    private void btnSignUpClick(){
        //вывести имя зашедшего
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                List<String> polya = new ArrayList<>();
                polya.add("name");
                polya.add("email");
                user.add(name);
                user.add(email);
                btnSignUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < 2; i++){
                            int finalI = i;
                            mDatabase.child("dates").child(getIntent().getStringExtra("getDateFromList"))
                                    .child(getIntent().getStringExtra("getTimeFromList")).child(getIntent().getStringExtra("getOfficeFromList")).child("Клиент " + name).child(polya.get(finalI)).setValue(user.get(finalI));
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        uidRef.addValueEventListener(eventListener);

    }
}