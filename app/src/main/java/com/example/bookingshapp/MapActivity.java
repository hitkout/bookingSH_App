package com.example.bookingshapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookingshapp.Models.User;
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
import java.util.Objects;

public class MapActivity extends AppCompatActivity {

    Button btnExit;
    private TextView textView;
    DatabaseReference db;
    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference uidRef = rootRef.child("Users").child(uid);
    private List<User> listTemp;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    FirebaseDatabase addDateInDb;
    DatabaseReference dates;
    ListAdapter mAdapter;

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

        //вывести имя зашедшего
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                textView.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
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

        SimpleDateFormat curFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        GregorianCalendar date = new GregorianCalendar();
        String[] dateStringArray = new String[7];

        if (date.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY){
            date.add(Calendar.DATE, date.get(Calendar.DAY_OF_WEEK) - date.get(Calendar.MONDAY));
            for (int i = 0; i < 7; i++) {
                dateStringArray[i] = curFormat.format(date.getTime());
                mDatabase.child("dates").child(dateStringArray[i]).setValue(2);
                date.roll(Calendar.DAY_OF_YEAR, true);
            }
        }
        else
            for (int i = 0; i < 7; i++) {
                dateStringArray[i] = curFormat.format(date.getTime());
                mDatabase.child("dates").child(dateStringArray[i]).setValue(2);
                date.roll(Calendar.DAY_OF_YEAR, true);
            }
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dateStringArray));
    }

    private void setOnClickList(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = listTemp.get(position);
                Intent i = new Intent(MapActivity.this, layoutTimes.class);
                i.putExtra("userName", user.getName());
            }
        });
    }

}