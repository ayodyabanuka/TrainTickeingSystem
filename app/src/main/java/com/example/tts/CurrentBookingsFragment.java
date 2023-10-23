package com.example.tts;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tts.Adapters.ReservationAdapter;
import com.example.tts.Database.MyDatabaseHelper;
import com.example.tts.Model.Reservation;
import com.example.tts.Utils.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CurrentBookingsFragment extends Fragment {

    String nicsqlite;
    private RecyclerView recyclerView;
    private ReservationAdapter adapter;

    MyDatabaseHelper myDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_current_bookings, container, false);

        recyclerView = view.findViewById(R.id.currentReservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        storeDataInArrays(getActivity());
        Call<List<Reservation>> call = ApiClient.getReservationService().ReservationCurrentGet(nicsqlite);

        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {

                    List<Reservation> itemList = response.body();

                    adapter = new ReservationAdapter(getActivity(),itemList);
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    void storeDataInArrays(Context context){
        myDB = new MyDatabaseHelper(context);
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(context, "Fail!", Toast.LENGTH_SHORT).show();
        }else{

            if(cursor.moveToFirst()){
                do{
                    nicsqlite = cursor.getString(1);
                }while(cursor.moveToNext());
            }

        }
    }

}