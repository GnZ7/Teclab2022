package com.teclab.weatherapp.service;


import com.teclab.weatherapp.model.Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    //URL = "https://api.openweathermap.org/data/2.5/weather?q={city_name}&appid={api_key}&units={metric}&lang={sp}";

    @GET("weather")
    Call<Model> getWeatherData(@Query("q") String city, @Query("appid") String api_key,
                               @Query("units") String metric, @Query("lang") String spa);
}
