package com.example.tts;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.tts.Adapters.ReservationAdapter;
import com.example.tts.Model.Reservation;
import com.example.tts.Model.Stations;
import com.example.tts.Utils.ApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentBookingSummaryActivity extends AppCompatActivity {

    TextView name,startStation,endStation,date,starting,ending,error;
    Button edit,delete;

    String trainId,datenumber,seatsNumber,reservationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_booking_summary);


        name = findViewById(R.id.trainName);
        startStation = findViewById(R.id.startStation);
        endStation = findViewById(R.id.endStation);
        starting = findViewById(R.id.starting);
        ending = findViewById(R.id.ending);
        date = findViewById(R.id.date);
        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);
        error = findViewById(R.id.errorMsg);

        error.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("TrainName"));
        date.setText(intent.getStringExtra("Date"));

        trainId = intent.getStringExtra("trainId");
        datenumber = intent.getStringExtra("date");
        reservationId = intent.getStringExtra("reservationId");


        Call<Reservation> call = ApiClient.getReservationService().GetReservationbyId(reservationId);

        call.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                if (response.isSuccessful()) {

                    Reservation reservation = response.body();
                    name.setText(reservation.getTrain().getName());
                    startStation.setText(reservation.getTrain().getStations().get(0).getStation());
                    endStation.setText(reservation.getTrain().getStations().get(1).getStation());
                    date.setText(reservation.getDate());
                    starting.setText(reservation.getTrain().getStations().get(0).getStation());
                    ending.setText(reservation.getTrain().getStations().get(1).getStation());


                } else {
                    Toast.makeText(CurrentBookingSummaryActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                Toast.makeText(CurrentBookingSummaryActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });


        checkDateGap(intent.getStringExtra("date").toString());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CurrentBookingSummaryActivity.this, EditReservationActivity.class);
                intent.putExtra("trainId",trainId);
                intent.putExtra("date",datenumber);
                intent.putExtra("seats",seatsNumber);
                intent.putExtra("reservationId",reservationId);
                startActivity(intent);
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<Reservation> DeleteReservation = ApiClient.getReservationService().DeleteReservation(reservationId);
                DeleteReservation.enqueue(new Callback<Reservation>() {
                    @Override
                    public void onResponse(Call<Reservation> call, Response<Reservation> response) {

                        Intent intent = new Intent(CurrentBookingSummaryActivity.this, HomeActivity.class);
                        startActivity(intent);
                        Toast.makeText(CurrentBookingSummaryActivity.this, "Delete Success!", Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onFailure(Call<Reservation> call, Throwable t) {
                    }
                });

            }
        });

    }

    private void checkDateGap(String apiDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentDate = new Date();
        try {
            Date dateFromApi = sdf.parse(apiDate);
            long differenceInMillis = dateFromApi.getTime() - currentDate.getTime();
            long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMillis);
            if (differenceInDays < 5) {
                edit.setVisibility(View.INVISIBLE);
                delete.setVisibility(View.INVISIBLE);
                error.setVisibility(View.VISIBLE);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}