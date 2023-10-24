package com.example.tts;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tts.Model.StationTrains;
import com.example.tts.Model.StationsList;
import com.example.tts.Model.Train;
import com.example.tts.Utils.ApiClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationActivity extends AppCompatActivity {

    Button date,next;
    int trainID;
    String dateString,startStation,endStation,trains;
    Calendar maxDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        maxDate = Calendar.getInstance();
        maxDate.add(Calendar.DAY_OF_MONTH, 30);

        Spinner start = (Spinner) findViewById(R.id.startStation);
        Spinner end = (Spinner) findViewById(R.id.endStation);
        Spinner trainspinners = (Spinner) findViewById(R.id.trainsspinner);
        date = findViewById(R.id.date);
        next = findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReservationActivity.this, BookingSummaryActivity.class);
                intent.putExtra("trainId",String.valueOf(trainID));
                intent.putExtra("startStation",startStation);
                intent.putExtra("endStation",endStation);
                intent.putExtra("date",dateString);
                startActivity(intent);
            }
        });


        // perform click event on edit text
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        Call<StationsList> getTrains = ApiClient.getTrainService().TrainsList();
        getTrains.enqueue(new Callback<StationsList>() {
            @Override
            public void onResponse(Call<StationsList> call, Response<StationsList> response) {
                if (response.isSuccessful()) {
                    StationsList stationResponse = response.body();
                    List<String> startstations = stationResponse.getStations();

                    // Now, populate the Spinner with the stations list
                    ArrayAdapter<String> adapterstart = new ArrayAdapter<>(ReservationActivity.this, android.R.layout.simple_spinner_item, startstations);
                    adapterstart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    start.setAdapter(adapterstart);
                    start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            startStation = startstations.get(position);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Handle no selection if needed
                        }
                    });


                    StationsList endResponse = response.body();
                    List<String> endstations = endResponse.getStations();

                    // Now, populate the Spinner with the stations list
                    ArrayAdapter<String> adapterend = new ArrayAdapter<>(ReservationActivity.this, android.R.layout.simple_spinner_item, endstations);
                    adapterend.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    end.setAdapter(adapterend);
                    end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            endStation = endstations.get(position);

                            StationTrains stationTrains = new StationTrains();
                            stationTrains.setDate(dateString);
                            stationTrains.setStartStation(startStation);
                            stationTrains.setEndStation(endStation);

                            Call<List<Train>> call = ApiClient.getTrainService().GetTrainsByStations(stationTrains);

                            call.enqueue(new Callback<List<Train>>() {
                                @Override
                                public void onResponse(Call<List<Train>> call, Response<List<Train>> response) {
                                    if (response.isSuccessful()) {
                                        List<Train> trainList = response.body();

                                        // Populate Spinner with train names
                                        List<String> trainNames = new ArrayList<>();
                                        for (Train train : trainList) {
                                            trainNames.add(train.getName());
                                        }

                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ReservationActivity.this, android.R.layout.simple_spinner_item, trainNames);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        trainspinners.setAdapter(adapter);
                                        trainspinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                trainID = trainList.get(position).getTrainNo();

                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {
                                                // Handle no selection if needed
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Train>> call, Throwable t) {
                                    // Handle failure (e.g., show an error message)
                                }
                            });


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Handle no selection if needed
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<StationsList> call, Throwable t) {
                // Handle failure (e.g., show an error message)
            }
        });

    }
    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        dateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }
                }, year, month, day);

        // Set the minimum date to today
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        datePickerDialog.show();
    }

}