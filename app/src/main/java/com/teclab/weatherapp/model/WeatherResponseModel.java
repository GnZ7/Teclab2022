package com.teclab.weatherapp.model;

import androidx.annotation.NonNull;

import com.teclab.weatherapp.BuildConfig;
import com.teclab.weatherapp.contract.WeatherContract;
import com.teclab.weatherapp.service.WeatherAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherResponseModel implements WeatherContract.Model {
    String api_key = BuildConfig.API_KEY;
    String metric = "metric";
    String spa = "sp";



    @Override
    public void getWeatherResponse(String cityName, WeatherContract.ResponseListener responseListener) {
        try {
            retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            WeatherAPI myAPI = retrofit.create(WeatherAPI.class);

            Call<Model> miTiempoCall = myAPI.getWeatherData(cityName, api_key, metric, spa);

            miTiempoCall.enqueue(new Callback<Model>() {
                @Override
                public void onResponse(@NonNull Call<Model> call, @NonNull Response<Model> response) {

                    if (response.isSuccessful()) {
                        responseListener.OnSuccess(response);
                    } else {
                        responseListener.OnError(response);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Model> call, @NonNull Throwable t) {

                    responseListener.OnFail(t);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

