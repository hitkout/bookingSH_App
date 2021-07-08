package com.example.bookingshapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
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
        listTimes();
    }

    private void getIntentMain(){
        Intent intent = getIntent();
        textViewTimes.setText(intent.getStringExtra("getDateFromList"));
    }

    private void listTimes(){
        List<String> listTimes = new ArrayList<>();
        byte count = 9;
        for (int i = 0; i < 7; i++) {
            listTimes.add(count + ":00 - " + ++count + ":00");
            listTemp.add(listTimes.get(i));
        }
        listViewTimes.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTimes));
    }

    private void setOnClickList(){
        listViewTimes.setOnItemClickListener((parent, view, position, id) -> {
            String getTimeFromList = listTemp.get(position);
            Intent getIntent = getIntent();
            String getDateFromList = getIntent.getStringExtra("getDateFromList");
            Intent intent = new Intent(TimesActivity.this, OfficeActivity.class);
            intent.putExtra("getDateFromList", getDateFromList);
            intent.putExtra("getTimeFromList", getTimeFromList);
            startActivity(intent);
        });
    }
}