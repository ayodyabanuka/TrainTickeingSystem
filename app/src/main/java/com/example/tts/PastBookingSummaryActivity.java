package com.example.tts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PastBookingSummaryActivity extends AppCompatActivity {

    TextView name,startStation,endStation,date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_booking_summary);

        name = findViewById(R.id.trainName);
        startStation = findViewById(R.id.startStation);
        endStation = findViewById(R.id.endStation);
        date = findViewById(R.id.date);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("TrainName"));
        startStation.setText(intent.getStringExtra("StartStation"));
        endStation.setText(intent.getStringExtra("EndStation"));
        date.setText(intent.getStringExtra("Date"));
    }
}