package com.example.bookingshapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapActivity extends AppCompatActivity {
    Button btnExit;
    private TextView textView;
    DatabaseReference db;
    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference uidRef = rootRef.child("Users").child(uid);
    private List<String> listTemp;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseDatabase addDateInDb;
    DatabaseReference dates;
    ListView listView;

    private void init(){
        btnExit = findViewById(R.id.buttonExit);
        textView = findViewById(R.id.textView);
        db = FirebaseDatabase.getInstance().getReference();
        listTemp = new ArrayList<>();
        listView = findViewById(R.id.listView);
        addDateInDb = FirebaseDatabase.getInstance();
        dates = addDateInDb.getReference("Dates");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        init();

        listDate();

        setOnClickList();

        //вывести имя зашедшего
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                textView.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        uidRef.addListenerForSingleValueEvent(eventListener);

        btnExit.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MapActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void listDate(){
        SimpleDateFormat curFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        SimpleDateFormat formatForDatabase = new SimpleDateFormat("dd MMMM, yyyy, EEEE", Locale.getDefault());
        Calendar date = new GregorianCalendar();
        List<String> dateStringList = new ArrayList<>();

        if (date.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY){
            while (date.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
                date.add(Calendar.DATE, -1);
            for (int i = 0; i < 7; i++) {
                dateStringList.add(curFormat.format(date.getTime()));
                mDatabase.child("dates").child(formatForDatabase.format(date.getTime())).setValue(1);
                listTemp.add(formatForDatabase.format(date.getTime()));
                date.roll(Calendar.DAY_OF_YEAR, true);
            }
        }
        else
            for (int i = 0; i < 7; i++) {
                dateStringList.add(curFormat.format(date.getTime()));
                mDatabase.child("dates").child(formatForDatabase.format(date.getTime())).setValue(1);
                listTemp.add(formatForDatabase.format(date.getTime()));
                date.roll(Calendar.DAY_OF_YEAR, true);
            }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dateStringList));
    }

    private void setOnClickList(){
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String getString = listTemp.get(position);
            Intent intent = new Intent(MapActivity.this, TimesActivity.class);
            intent.putExtra("getString", getString);
            startActivity(intent);
        });
    }

}