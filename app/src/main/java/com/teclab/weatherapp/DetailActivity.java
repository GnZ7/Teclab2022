package com.teclab.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.teclab.weatherapp.modelo.MiTiempo;
import com.teclab.weatherapp.service.APIClient;
import com.teclab.weatherapp.service.WeatherAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailActivity extends AppCompatActivity {

    private TextView tvCiudadDetail;

    int count;


    @Override
    protected void onStart() {
        super.onStart();
        //Cargar el contador desde las shared preferences
        SharedPreferences sharedPrefs = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        count = sharedPrefs.getInt("counter", 0);
        if (count >= 5) {
            count = 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvCiudadDetail = findViewById(R.id.tv_ciudadDetail);

        //Visibilizar progressbar mientras carga. Invisibilizar UI.
        findViewById(R.id.loader).setVisibility(View.VISIBLE);
        findViewById(R.id.tempContainer).setVisibility(View.GONE);
        findViewById(R.id.cityContainer).setVisibility(View.GONE);
        findViewById(R.id.detallesContainer).setVisibility(View.GONE);

        Intent i = getIntent();
        String mensajeCiudad = i.getStringExtra("ciudadElegida");
        (tvCiudadDetail).setText(mensajeCiudad);


        getWeather(tvCiudadDetail.getText().toString().trim());


    }


    public void getWeather(String v) {
        WeatherAPI miAPI = APIClient.getAPIClient().create(WeatherAPI.class);
        //URL = "https://api.openweathermap.org/data/2.5/weather?q={city_name}&appid={api_key}&units{metric}&lang{sp}";

        String api_key = BuildConfig.API_KEY;
        String metric = "metric";
        String spa = "sp";
        Call<MiTiempo> miTiempoCall = miAPI.getweather(tvCiudadDetail.getText().toString().trim(),
                api_key, metric, spa);

        miTiempoCall.enqueue(new Callback<MiTiempo>() {


            @Override
            public void onResponse(@NonNull Call<MiTiempo> call, @NonNull Response<MiTiempo> response) {
                if (response.isSuccessful()) {
                    //Extraer los datos de la respuesta y asignarlos a variables String.
                    String temp;
                    if (response.body() != null) {
                        temp = response.body().getMain().getTemp().intValue() + "°C";
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
                        String timeZone = response.body().getTimezone().toString();
                        String code = response.body().getSys().getCountry();
                        String name = response.body().getName();

                        //Pasar las variables String a los Views del layout.
                        ((TextView) findViewById(R.id.temp)).setText(temp);
                        ((TextView) findViewById(R.id.pressure)).setText(press);
                        ((TextView) findViewById(R.id.temp_min)).setText(tempMin);
                        ((TextView) findViewById(R.id.temp_max)).setText(tempMax);
                        ((TextView) findViewById(R.id.temp_feels)).setText(tempFeels);
                        ((TextView) findViewById(R.id.status)).setText(status);
                        //Con Glide se obtiene y asigna la imagen del url
                        Glide.with(DetailActivity.this).load(statusIconUrl).into((ImageView) findViewById(R.id.statusIcon));
                        ((TextView) findViewById(R.id.humidity)).setText(humid);
                        ((TextView) findViewById(R.id.windspeed)).setText(wnd);
                        ((TextView) findViewById(R.id.sunrise)).setText(sunrise);
                        ((TextView) findViewById(R.id.sunset)).setText(sunset);
                        ((TextView) findViewById(R.id.timezone)).setText(timeZone + " UTC");
                        ((TextView) findViewById(R.id.timezone2)).setText(timeZone + " UTC");
                        tvCiudadDetail.setText(name + ", " + code);

                        //Guardar la cuidad buscada en los Shared Preferences luego pasarlos al listado de cuidades.
                        String responseCity = tvCiudadDetail.getText().toString();
                        savePreferences(count, responseCity);

                    }

                } else {
                    //Si falla la respuesta, se envía una alerta y se cierra la actividad, volviendo al MainActivity.
                    if (tvCiudadDetail.getText().toString().isEmpty()) {
                        Toast.makeText(DetailActivity.this, "No se pudo encontrar tu ubicación en este momento.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailActivity.this, "No se pudo encontrar la ciudad ingresada.", Toast.LENGTH_LONG).show();
                    }

                    finish();
                }
                //Invisibilizar progressbar ya que terminó la carga. Visibilizar UI.
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.tempContainer).setVisibility(View.VISIBLE);
                findViewById(R.id.cityContainer).setVisibility(View.VISIBLE);
                findViewById(R.id.detallesContainer).setVisibility(View.VISIBLE);


            }

            @Override
            public void onFailure(@NonNull Call<MiTiempo> call, @NonNull Throwable t) {
                Toast.makeText(DetailActivity.this, "Falló la conexión.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public void savePreferences(int count, String obtainedCity) {

        SharedPreferences sharedPrefs = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString("searchedCity_" + count, obtainedCity);
        editor.putInt("counter", count + 1);
        editor.apply();
        /*La variable count permite ir creando diferentes keys de manera incremental, para almacenar distintas cuidades.
            En el onStart luego se maneja que no se creen más de 5, para coincidir con el array del ListActivity.
         */
    }

}
