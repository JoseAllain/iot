package com.example.iotintromisin.interfaces;

import com.example.iotintromisin.model.Activity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ActividadApi {
    @GET("/api/activities")
    Call<List<Activity>> obtenerActividades();
}
