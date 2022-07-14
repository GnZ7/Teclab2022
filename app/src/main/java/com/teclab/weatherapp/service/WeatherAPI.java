package com.teclab.weatherapp.service;

import com.teclab.weatherapp.modelo.MiTiempo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("weather")
    Call<MiTiempo> getweather(@Query("q") String mensajeCiudad, @Query("appid") String api_key,
                              @Query("units") String metric, @Query("lang") String spa);
}
