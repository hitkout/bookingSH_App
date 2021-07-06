package com.example.bookingshapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimesActivity extends AppCompatActivity {
    TextView textViewTimes;
    ListView listViewTimes;
    private List<String> listTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times);
        textViewTimes = findViewById(R.id.textViewTimes);
        listViewTimes = findViewById(R.id.listViewTimes);
        listTemp = new ArrayList<>();
        getIntentMain();
        setOnClickList();
    }

    private void getIntentMain(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("dates").child(getIntent().getStringExtra("getString"));
        Intent intent = getIntent();
        List<String> listTimes = new ArrayList<>();
        textViewTimes.setText(intent.getStringExtra("getString"));
        byte count = 9;
        for (int i = 0; i < 7; i++, count++) {
            listTimes.add(count + ":00");
            listTemp.add(listTimes.get(i));
            mDatabase.child(listTimes.get(i)).setValue(2);
        }
        listViewTimes.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTimes));
    }

    private void setOnClickList(){
        listViewTimes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getString = listTemp.get(position);
                Intent intent = new Intent(TimesActivity.this, OfficeActivity.class);
                intent.putExtra("getString", getString);
                startActivity(intent);
            }
        });
    }
}