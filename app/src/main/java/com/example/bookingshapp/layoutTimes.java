package com.example.bookingshapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class layoutTimes extends AppCompatActivity {
    ListView listViewTimes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_times);
        init();
    }

    private void init(){
        listViewTimes = findViewById(R.id.listViewTimes);
    }

    private void getIntentMap(){
        Intent i = getIntent();
        if (i != null){
            
        }
    }
}
