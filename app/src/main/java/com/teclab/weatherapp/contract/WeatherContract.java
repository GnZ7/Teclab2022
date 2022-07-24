package com.teclab.weatherapp.contract;

import java.util.List;

import retrofit2.Response;

public interface WeatherContract {
    interface View {
        String getCityInput();
        void loadDataToUI(List<String> responseArrayList);
        void showMessage(String msg);

        void savePreferences();

        void showProgressBar();
        void hideProgressBar();

        interface BackgroundAnimation{
            void startBackgroundAnimation();
        }
        void finishActivity();
    }

    interface Model{

        void getWeatherResponse(String cityName, ResponseListener responseListener);

    }

    interface Presenter{
        void getWeather(String cityName);
    }

    interface ResponseListener{
        void OnSuccess(Response<com.teclab.weatherapp.model.Model> response);
        void OnError(Response<com.teclab.weatherapp.model.Model> response);
        void OnFail(Throwable t);
    }


}
