package com.example.bookingshapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OfficeActivity extends AppCompatActivity {
    ListView listViewOffice;
    TextView textViewDate;
    TextView textViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office);
        listViewOffice = findViewById(R.id.listViewOffice);
        textViewDate = findViewById(R.id.textViewDate);
        textViewTime = findViewById(R.id.textViewTime);
        getIntentMain();
    }

    private void getIntentMain(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("dates").child(getIntent().getStringExtra("getDateFromList"));
        Intent intent = getIntent();
        textViewDate.setText(intent.getStringExtra("getDateFromList"));
        textViewTime.setText(intent.getStringExtra("getString"));
        List<String> listOffice = new ArrayList<>();
        byte count = 9;
        for (int i = 0; i < 7; i++, count++) {
            listOffice.add("Кабинет " + i);
            mDatabase.child(listOffice.get(i)).setValue(2);
        }
        listViewOffice.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOffice));
    }


}