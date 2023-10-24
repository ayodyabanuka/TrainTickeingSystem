package com.example.tts.Interface;


import com.example.tts.Model.Reservation;
import com.example.tts.Model.ReservationReqest;
import com.example.tts.Model.StationTrains;
import com.example.tts.Model.StationsList;
import com.example.tts.Model.Train;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TrainService {

    @GET("train")
    Call<List<Train>> TrainsGet();

    @GET("train/stationlist")
    Call <StationsList> TrainsList();

    @GET("train/{trainNo}")
    Call<Train>getTrainById(@Path("trainNo") int trainNo);

    @POST("train/filter")
    Call <List<Train>> GetTrainsByStations(@Body StationTrains stationTrains);
}
