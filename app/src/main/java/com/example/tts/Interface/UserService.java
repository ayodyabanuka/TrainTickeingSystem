package com.example.tts.Interface;


import com.example.tts.Model.LoginRequest;
import com.example.tts.Model.LoginResponse;
import com.example.tts.Model.RegisterRequest;
import com.example.tts.Model.Client;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @POST("client/login/")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

    @POST("client/")
    Call<Client> userRegister(@Body Client client);

    @PUT("client/{nic}")
    Call<Client> userUpdate(@Body Client client, @Path("nic")String nic);

    @GET("client/{nic}")
    Call<Client> GetUserByID(@Path("nic") String nic);

    @GET("client/deactivate/{nic}")
    Call<String> UserDeactivate(@Path("nic") String nic);

}