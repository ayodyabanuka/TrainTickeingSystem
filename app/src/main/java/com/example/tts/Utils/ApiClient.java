package com.example.tts.Utils;


import com.example.tts.Interface.ReservationsService;
import com.example.tts.Interface.TrainService;
import com.example.tts.Interface.UserService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit getRetrofit(){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://ead-web-main.azurewebsites.net/api/")
                .client(okHttpClient)
                .build();

        return retrofit;
    }


    public static UserService getUserService(){
        UserService userService = getRetrofit().create(UserService.class);

        return userService;
    }

    public static ReservationsService getReservationService(){
        ReservationsService reservationsService = getRetrofit().create(ReservationsService.class);

        return reservationsService;
    }
    public static TrainService getTrainService(){
        TrainService trainService = getRetrofit().create(TrainService.class);

        return trainService;
    }
}