package com.example.bookingshapp;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class OfficeActivity extends AppCompatActivity {
    private ListView listViewOffice;
    private TextView textViewDate;
    private List<String> listTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office);
        init();
        getIntentMain();
        setOnClickList();
        listOffice();
    }

    private void init(){
        listViewOffice = findViewById(R.id.listViewOffice);
        textViewDate = findViewById(R.id.textViewDate);
        listTemp = new ArrayList<>();
    }

    @SuppressLint("SetTextI18n")
    private void getIntentMain(){
        Intent intent = getIntent();
        textViewDate.setText(intent.getStringExtra("getDateFromList")+" / "+intent.getStringExtra("getTimeFromList"));
    }

    private void listOffice(){
        List<String> listOffice = new ArrayList<>();
        int numberOfCabinet = 1;
        for (int i = 0; i < 7; i++) {
            listOffice.add("Зал " + numberOfCabinet);
            numberOfCabinet++;
            listTemp.add(listOffice.get(i));
        }
        listViewOffice.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOffice));
    }

    private void setOnClickList(){
        listViewOffice.setOnItemClickListener((parent, view, position, id) -> {
            String getOfficeFromList = listTemp.get(position);
            Intent getIntent = getIntent();
            String getDateFromList = getIntent.getStringExtra("getDateFromList");
            String getTimeFromList = getIntent.getStringExtra("getTimeFromList");
            Intent intent = new Intent(OfficeActivity.this, SignUpActivity.class);
            intent.putExtra("getDateFromList", getDateFromList);
            intent.putExtra("getTimeFromList", getTimeFromList);
            intent.putExtra("getOfficeFromList", getOfficeFromList);
            startActivity(intent);
        });
    }

}