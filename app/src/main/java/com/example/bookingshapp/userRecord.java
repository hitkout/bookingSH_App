package com.example.bookingshapp;

import androidx.activity.contextaware.OnContextAvailableListener;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class userRecord extends AppCompatActivity {
    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    TextView textView;
    TextView textViewUser;

    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference uidRef = rootRef.child("Users").child(uid);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_record);
        init();
        getRecordsFromDB();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                textViewUser.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        uidRef.addListenerForSingleValueEvent(eventListener);
    }

    private void init(){
        textView = findViewById(R.id.textViewRecord);
        textViewUser = findViewById(R.id.textViewUser);
        ImageView imageViewQuestion = findViewById(R.id.imageViewQuestion);
        imageViewQuestion.setOnClickListener(v -> showQuestion());
    }

    private void getRecordsFromDB(){
        uidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String id = dataSnapshot.child("id").getValue(String.class);
                assert id != null;
                mDatabase.child("records").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        getRecords(snapshot);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void getRecords(DataSnapshot snapshot){
        String listRecordInDB = "Здесь пока нет записей";
        for (DataSnapshot dss : snapshot.getChildren()){
            String values = String.valueOf(dss.getValue());
            values = values.replace("}, ", "\n\n");
            values = values.replace("{", "");
            values = values.replace("}", "");
            values = values.replace("=", " / ");
            values = values.replace(" / true", "");

            listRecordInDB = values;
        }
        textView.setText(listRecordInDB);
    }

    private void showQuestion() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setMessage("Справка");

        LayoutInflater inflater = LayoutInflater.from(this);
        View questionInWindow = inflater.inflate(R.layout.question_window, null);
        dialog.setView(questionInWindow);

        dialog.setNegativeButton("Закрыть", (dialogInterface, which) -> dialogInterface.dismiss());

        dialog.show();
    }

//    private void deleteOldRecords(){
//        uidRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String id = dataSnapshot.child("id").getValue(String.class);
//                String name = dataSnapshot.child("name").getValue(String.class);
//                boolean check = false;
//                int getMilliFromDB = 0;
//                mDatabase.child("records").child(id).child(name).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        try {
//                            getDate(snapshot, check, getMilliFromDB);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                if (check){
//                    mDatabase.child("records").child(id).child(name).child(String.valueOf(getMilliFromDB)).removeValue();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void getDate(DataSnapshot snapshot, boolean check, int getMilliFromDB) throws ParseException {
//        String myDate = getIntent().getStringExtra("getMilli");
//        SimpleDateFormat formatForDatabase = new SimpleDateFormat("dd MMMM, yyyy, EEEE", Locale.getDefault());
//        Date date = formatForDatabase.parse(myDate);
//        String nowDate = formatForDatabase.format(date.getTime());
//        long millis = date.getTime();
//        for (DataSnapshot dss : snapshot.getChildren()){
//            if (Long.parseLong(nowDate) > millis) {
//                getMilliFromDB = Integer.parseInt(String.valueOf(dss.getValue()));
//                check = true;
//                return;
//            }
//        }
//    }
}