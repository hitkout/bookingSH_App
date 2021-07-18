package com.example.bookingshapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class userRecord extends AppCompatActivity {
    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    //ListView listViewRecord;
    TextView textView;

    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference uidRef = rootRef.child("Users").child(uid);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_record);
        //listViewRecord = findViewById(R.id.listViewRecord);
        textView = findViewById(R.id.textViewRecord);
        getRecordsFromDB();
    }

    private void getRecordsFromDB(){
        uidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String id = dataSnapshot.child("id").getValue(String.class);
                String name = dataSnapshot.child("name").getValue(String.class);
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
        String listRecordInDB = "";
        for (DataSnapshot dss : snapshot.getChildren()){
            String values = String.valueOf(dss.getValue());
            values = values.replace("}, ", "\n\n");
            values = values.replace("{", "");
            values = values.replace("}", "");
            values = values.replace("=", " / ");

            listRecordInDB = values;
        }
        textView.setText(listRecordInDB);
        //listViewRecord.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listRecordInDB));
    }
}