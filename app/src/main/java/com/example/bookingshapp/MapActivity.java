package com.example.bookingshapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    private ImageView btnExit;
    private TextView textViewName;
    private final String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference uidRef = rootRef.child("Users").child(uid);
    private List<String> listTemp;
    private ListView listView;

    private void init(){
        btnExit = findViewById(R.id.imageViewExitButton);
        textViewName = findViewById(R.id.textViewName);
        listTemp = new ArrayList<>();
        listView = findViewById(R.id.listView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        init();
        listDate();
        setOnClickList();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                textViewName.setText(name);
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

//    private void listDate() {
//
//        SimpleDateFormat curFormat = new SimpleDateFormat("EEEE, dd MMMM, yyyy", Locale.getDefault());
//        SimpleDateFormat formatForDatabase = new SimpleDateFormat("dd MMMM, yyyy, EEEE", Locale.getDefault());
//        Calendar date = new GregorianCalendar();
//        List<String> dateStringList = new ArrayList<>();
//
//        if (date.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY){
//            while (date.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
//                date.add(Calendar.DATE, -1);
//            for (int i = 0; i < 7; i++) {
//                dateStringList.add(curFormat.format(date.getTime()));
//                listTemp.add(formatForDatabase.format(date.getTime()));
//                date.roll(Calendar.DAY_OF_YEAR, true);
//            }
//        }
//        else
//            for (int i = 0; i < 7; i++) {
//                dateStringList.add(curFormat.format(date.getTime()));
//                listTemp.add(formatForDatabase.format(date.getTime()));
//                date.roll(Calendar.DAY_OF_YEAR, true);
//            }
//        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dateStringList));
//    }

    private void listDate() {
        SimpleDateFormat curFormat = new SimpleDateFormat("EEEE, dd MMMM, yyyy", Locale.getDefault());
        SimpleDateFormat formatForDatabase = new SimpleDateFormat("dd MMMM, yyyy, EEEE", Locale.getDefault());
        Calendar date = new GregorianCalendar();
        List<String> dateStringList = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            dateStringList.add(curFormat.format(date.getTime()));
            listTemp.add(formatForDatabase.format(date.getTime()));
            date.roll(Calendar.DAY_OF_YEAR, true);
        }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dateStringList));
    }

    private void setOnClickList(){
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String getDateFromList = listTemp.get(position);
            Intent intent = new Intent(MapActivity.this, TimesActivity.class);
            intent.putExtra("getDateFromList", getDateFromList);
            startActivity(intent);
        });
    }

}