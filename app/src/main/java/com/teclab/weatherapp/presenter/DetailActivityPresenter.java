package com.teclab.weatherapp.presenter;

import com.teclab.weatherapp.contract.WeatherContract;
import com.teclab.weatherapp.model.Model;
import com.teclab.weatherapp.model.WeatherResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class DetailActivityPresenter implements WeatherContract.Presenter, WeatherContract.ResponseListener {


    private final WeatherContract.View dView;
    private final WeatherContract.Model dModel;

    //------------------- Constructor
    public DetailActivityPresenter(WeatherContract.View view) {
        dView = view;
        dModel = new WeatherResponseModel();
        getWeather(dView.getCityInput());

    }

    //--------------------- Metodos Presenter Contract
    @Override
    public void getWeather(String cityName) {
        dView.showProgressBar();
        dModel.getWeatherResponse(cityName, this);

    }


    //-------------- Response Listener

    @Override
    public void OnSuccess(Response<Model> response) {
        //paso la respuesta a un arraylist.
        List<String> responseArrayList = new ArrayList<>();
        if (response.body() != null) {
            String temp = response.body().getMain().getTemp().intValue() + "°C";
            String tempMin = "Mín: " + response.body().getMain().getTemp_min().intValue() + "°C";
            String tempMax = "Máx: " + response.body().getMain().getTemp_max().intValue() + "°C";
            String tempFeels = "S.T: " + response.body().getMain().getFeels_like().intValue() + "°C";
            String status = response.body().getWeather().get(0).getDescription();
            String statusIconStr = response.body().getWeather().get(0).getIcon();
            String statusIconUrl = "http://openweathermap.org/img/w/" + statusIconStr + ".png";
            String press = response.body().getMain().getPressure().toString() + " hPa";
            String humid = response.body().getMain().getHumidity().toString() + "%";
            String wnd = response.body().getWind().getSpeed().toString() + " m/s";
            String sunrise = response.body().getSys().getSunrise();
            String sunset = response.body().getSys().getSunset();
            String timeZone = response.body().getTimezone().toString() + " UTC";
            String code = response.body().getSys().getCountry();
            String name = response.body().getName();

            responseArrayList.add(temp);
            responseArrayList.add(tempMin);
            responseArrayList.add(tempMax);
            responseArrayList.add(tempFeels);
            responseArrayList.add(status);
            responseArrayList.add(statusIconUrl);
            responseArrayList.add(press);
            responseArrayList.add(humid);
            responseArrayList.add(wnd);
            responseArrayList.add(sunrise);
            responseArrayList.add(sunset);
            responseArrayList.add(timeZone);
            responseArrayList.add(code);
            responseArrayList.add(name);


            //Paso el arraylist al metodo para cargar la UI
            dView.loadDataToUI(responseArrayList);

            dView.hideProgressBar();
            //Guardar la cuidad en los Shared Preferences para luego pasarlos al ListActivity.
            dView.savePreferences();
        } else {
            OnError(response);
        }
    }

    @Override
    public void OnError(Response<Model> response) {
        dView.hideProgressBar();
        dView.showMessage("Oops! Algo salió mal.");
        dView.finishActivity();
    }

    @Override
    public void OnFail(Throwable t) {
        dView.hideProgressBar();
        dView.showMessage(t.getMessage());
    }
}
