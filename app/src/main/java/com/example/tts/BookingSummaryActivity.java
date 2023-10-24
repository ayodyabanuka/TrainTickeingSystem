package com.example.tts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tts.Database.MyDatabaseHelper;
import com.example.tts.Model.Client;
import com.example.tts.Model.Reservation;
import com.example.tts.Model.Stations;
import com.example.tts.Model.Train;
import com.example.tts.Utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingSummaryActivity extends AppCompatActivity {

    TextView TrainName,StartStation,EndStation,starting,ending,date;
    Client client;
    Train train;

    Button Confirm,cancel;


    String usernic;

    String trainId,startStation,endStation,datestring;

    MyDatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        TrainName = findViewById(R.id.trainName);
        StartStation = findViewById(R.id.startStation);
        EndStation = findViewById(R.id.endStation);
        starting = findViewById(R.id.starting);
        ending = findViewById(R.id.ending);
        date = findViewById(R.id.date);
        Confirm = findViewById(R.id.bookingConfirm);
        cancel = findViewById(R.id.cancelConfirm);



        Intent intent = getIntent();
        trainId = intent.getStringExtra("trainId");
        startStation = intent.getStringExtra("startStation");
        endStation = intent.getStringExtra("endStation");
        datestring = intent.getStringExtra("date");
        storeDataInArrays();
        Call<Client> call = ApiClient.getUserService().GetUserByID(usernic);
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    client  = response.body();
                } else {
                    Toast.makeText(BookingSummaryActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Toast.makeText(BookingSummaryActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });


        Call<Train> getTrainbyId = ApiClient.getTrainService().getTrainById(Integer.valueOf(trainId));
        getTrainbyId.enqueue(new Callback<Train>() {
            @Override
            public void onResponse(Call<Train> call, Response<Train> response) {
//                train = response.body();
            }
            @Override
            public void onFailure(Call<Train> call, Throwable t) {
            }
        });



        TrainName.setText(train.getName().toString());
        StartStation.setText(startStation);
        EndStation.setText(endStation);
        date.setText(datestring);
        starting.setText(startStation);
        ending.setText(endStation);



        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Stations start = new Stations();
                Stations end = new Stations();
                start.setStation(startStation);
                end.setStation(endStation);
                start.setTime("00");
                end.setTime("00");

                Reservation reservation = new Reservation();
                reservation.setClient(client);
                reservation.setTrain(train);
                reservation.setDate(datestring);
                reservation.setStartStation(start);
                reservation.setEndStation(end);

                Call<Reservation> registerResponseCall = ApiClient.getReservationService().CreateReservation(reservation);
                registerResponseCall.enqueue(new Callback<Reservation>() {
                    @Override
                    public void onResponse(Call<Reservation> call, @NonNull Response<Reservation> response) {

                        if(response.isSuccessful()){
                            Toast.makeText(BookingSummaryActivity.this,"Successful", Toast.LENGTH_LONG).show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    startActivity(new Intent(BookingSummaryActivity.this,HomeActivity.class));
                                }
                            },700);

                        }else{
                            Toast.makeText(BookingSummaryActivity.this,"Login Failed", Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<Reservation> call, Throwable t) {
                        Toast.makeText(BookingSummaryActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                });
            }


        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelIntent = new Intent(BookingSummaryActivity.this, HomeActivity.class);
                startActivity(cancelIntent);

            }
        });






    }

    void storeDataInArrays(){
        myDB = new MyDatabaseHelper(getApplicationContext());
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){

        }else{

            if(cursor.moveToFirst()){
                do{
                    usernic = cursor.getString(1);
                }while(cursor.moveToNext());
            }

        }
    }
}