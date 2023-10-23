package com.example.tts;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.tts.Database.MyDatabaseHelper;
import com.example.tts.Model.Client;
import com.example.tts.Model.Reservation;
import com.example.tts.Model.Train;
import com.example.tts.Utils.ApiClient;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditReservationActivity extends AppCompatActivity {

    Button dateedit,editReservation;
    List<String> codes = new ArrayList<String>();
    TextInputEditText seats;
    MyDatabaseHelper myDB;
 Client userdata;
    String nicsqlite;
    Train trainData ;
    int trainID;
    String dateString,seatsNumber,dateNumber,reservationId;

    Reservation reservationData;


    Calendar maxDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation);


        maxDate = Calendar.getInstance();
        maxDate.add(Calendar.DAY_OF_MONTH, 30);


        storeDataInArrays();


        Intent intent = getIntent();
        reservationId = intent.getStringExtra("reservationId");
        dateedit = findViewById(R.id.editdate);
        seats = findViewById(R.id.seats);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        editReservation = findViewById(R.id.editReservation);

        Call<Reservation> getReservationbyId = ApiClient.getReservationService().GetReservationbyId(reservationId);
        getReservationbyId.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                reservationData = response.body();
                dateedit.setText(reservationData.getDate());
                dateString = reservationData.getDate();

            }
            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
            }
        });

        editReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reservation reservation = new Reservation();
                reservation.setClient(reservationData.getClient());
                reservation.setTrain(trainData);
                reservation.setDate(dateString);
                reservation.setReservationId(Integer.valueOf(reservationId));

                Call<Reservation> Update = ApiClient.getReservationService().UpdateReservation( reservation,reservationId);
                Update.enqueue(new Callback<Reservation>() {
                    @Override
                    public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                        Toast.makeText(EditReservationActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditReservationActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onFailure(Call<Reservation> call, Throwable t) {
                    }
                });

            }
        });




        dateedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        Call<Client> call = ApiClient.getUserService().GetUserByID(nicsqlite);
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    userdata  = response.body();
                } else {
                    Toast.makeText(EditReservationActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Toast.makeText(EditReservationActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });




        Call<List<Train>> getTrains = ApiClient.getTrainService().TrainsGet();
        getTrains.enqueue(new Callback<List<Train>>() {
            @Override
            public void onResponse(Call<List<Train>> call, Response<List<Train>> response) {
                List<Train> trainList = response.body();
                for (int i = 0; i < trainList.size(); i++) {
                    codes.add(trainList.get(i).getName());
                }
                ArrayAdapter<String> adapterTime = new ArrayAdapter<>(EditReservationActivity.this, android.R.layout.simple_spinner_dropdown_item, codes);
                spinner.setAdapter(adapterTime);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        trainID = trainList.get(i).getTrainNo();
                        Call<Train> getTrainbyId = ApiClient.getTrainService().getTrainById(trainID);
                        getTrainbyId.enqueue(new Callback<Train>() {
                            @Override
                            public void onResponse(Call<Train> call, Response<Train> response) {
                                trainData = response.body();
                            }
                            @Override
                            public void onFailure(Call<Train> call, Throwable t) {
                            }
                        });
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

            }

            @Override
            public void onFailure(Call<List<Train>> call, Throwable t) {

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

                        dateedit.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        dateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }
                }, year, month, day);

        // Set the minimum date to today
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        datePickerDialog.show();
    }
    void storeDataInArrays(){
        myDB = new MyDatabaseHelper(getApplicationContext());
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "Fail!", Toast.LENGTH_SHORT).show();
        }else{

            if(cursor.moveToFirst()){
                do{
                    nicsqlite = cursor.getString(1);
                }while(cursor.moveToNext());
            }

        }
    }
}