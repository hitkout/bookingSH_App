package com.example.bookingshapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class OfficeActivity extends AppCompatActivity {
    private ConstraintLayout constraint;
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
        Snackbar.make(constraint, "Выберите зал", Snackbar.LENGTH_LONG).show();
    }

    private void init(){
        listViewOffice = findViewById(R.id.listViewOffice);
        textViewDate = findViewById(R.id.textViewDate);
        listTemp = new ArrayList<>();
        constraint = findViewById(R.id.constraintElementOffice);
        ImageView imageViewQuestion = findViewById(R.id.imageViewQuestion);
        imageViewQuestion.setOnClickListener(v -> showQuestion());
    }

    @SuppressLint("SetTextI18n")
    private void getIntentMain(){
        Intent intent = getIntent();
        textViewDate.setText(intent.getStringExtra("getDateFromList")+" / "+intent.getStringExtra("getTimeFromList"));
    }

    private void listOffice(){
        List<String> listOffice = new ArrayList<>();
        int numberOfPool = 1;
        listOffice.add("Кардиозона");
        listOffice.add("Силовая зона");
        listOffice.add("Зона свободных весов");
        listOffice.add("Зона растяжки");
        listOffice.add("Зона групповых программ");
        for (int i = listOffice.size(); i < 7; i++) {
            listOffice.add("Бассейн " + numberOfPool);
            numberOfPool++;
        }
        for (String i : listOffice){
            listTemp.add(String.valueOf(i));
        }
        listViewOffice.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOffice));
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