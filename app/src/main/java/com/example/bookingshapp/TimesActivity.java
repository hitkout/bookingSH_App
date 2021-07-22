package com.example.bookingshapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class TimesActivity extends AppCompatActivity {
    TextView textViewTimes;
    ListView listViewTimes;
    TextView textViewBackList;
    private List<String> listTemp;
    private ConstraintLayout constraint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times);
        init();
        getIntentMain();
        setOnClickList();
        listTimes();
        Snackbar.make(constraint, "Выберите время", Snackbar.LENGTH_LONG).show();
    }

    private void init(){
        textViewTimes = findViewById(R.id.textViewTimes);
        listViewTimes = findViewById(R.id.listViewTimes);
        listTemp = new ArrayList<>();
        constraint = findViewById(R.id.constraintElementTimes);
        textViewBackList = findViewById(R.id.textViewBackList);
        ImageView imageViewQuestion = findViewById(R.id.imageViewQuestion);
        imageViewQuestion.setOnClickListener(v -> showQuestion());
    }

    private void getIntentMain(){
        Intent intent = getIntent();
        textViewTimes.setText(intent.getStringExtra("getDateFromList"));
    }

    private void listTimes(){
        SimpleDateFormat formatForDatabase = new SimpleDateFormat("dd MMMM, yyyy, EEEE", Locale.getDefault());
        String dat = getIntent().getStringExtra("getDateFromList");
        Calendar date = new GregorianCalendar();
        String nowDate = formatForDatabase.format(date.getTime());

        List<String> listTimes = new ArrayList<>();
        int count = 9;
        if (dat.equals(nowDate)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (LocalDateTime.now().getHour() > count) {
                    count = LocalDateTime.now().getHour() + 1;
                    for (int i = 0; count <= 17; i++) {
                        listTimes.add(count + ":00 - " + ++count + ":00");
                        listTemp.add(listTimes.get(i));
                    }
                }
            }
        }
        else {
                for (int i = 0; i < 9; i++) {
                    listTimes.add(count + ":00 - " + ++count + ":00");
                    listTemp.add(listTimes.get(i));
                }
            }
        if (listTimes.isEmpty())
            textViewBackList.setText("На сегодня нет занятий");
        listViewTimes.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTimes));
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