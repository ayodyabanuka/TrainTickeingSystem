package com.example.tts.Interface;

import com.example.tts.Model.Reservation;
import com.example.tts.Model.StationTrains;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReservationsService {

    @GET("reservation/current/{nic}")
    Call<List<Reservation>> ReservationCurrentGet(@Path("nic") String nic);

    @GET("reservation/past/{nic}")
    Call<List<Reservation>> ReservationPastGet(@Path("nic") String nic);

    @POST("reservation")
    Call<Reservation> CreateReservation(@Body Reservation reservation);
    
    @DELETE("reservation/{reservationId}")
    Call<Reservation> DeleteReservation(@Path("reservationId") String reservationId);

    @GET("reservation/{reservationId}")
    Call<Reservation> GetReservationbyId(@Path("reservationId") String reservationId);

    @PUT("reservation/{reservationId}")
    Call<Reservation> UpdateReservation(@Body Reservation reservation,@Path("reservationId")String reservationId);
}
