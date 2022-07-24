package com.teclab.weatherapp.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.teclab.weatherapp.R;
import com.teclab.weatherapp.contract.WeatherContract;
import com.teclab.weatherapp.presenter.DetailActivityPresenter;

import java.util.List;


public class DetailActivity extends AppCompatActivity implements WeatherContract.View, WeatherContract.View.BackgroundAnimation {


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
        startBackgroundAnimation();
        new DetailActivityPresenter(this);

    }

    //--------- Metodos View Contract
    public String getCityInput() {
        Intent i = getIntent();
        String cityName = i.getStringExtra("ciudadElegida");
        if (cityName.isEmpty()) {
            Toast.makeText(DetailActivity.this, "No se pudo encontrar la ciudad ingresada.", Toast.LENGTH_LONG).show();
            finish();
        }
        return cityName;
    }

    @Override
    public void loadDataToUI(List<String> responseArrayList) {
        ((TextView) findViewById(R.id.temp)).setText(responseArrayList.get(0));
        ((TextView) findViewById(R.id.temp_min)).setText(responseArrayList.get(1));
        ((TextView) findViewById(R.id.temp_max)).setText(responseArrayList.get(2));
        ((TextView) findViewById(R.id.temp_feels)).setText(responseArrayList.get(3));
        ((TextView) findViewById(R.id.status)).setText(responseArrayList.get(4));
        Glide.with(this).load(responseArrayList.get(5)).into((ImageView) findViewById(R.id.statusIcon));
        ((TextView) findViewById(R.id.pressure)).setText(responseArrayList.get(6));
        ((TextView) findViewById(R.id.humidity)).setText(responseArrayList.get(7));
        ((TextView) findViewById(R.id.windspeed)).setText(responseArrayList.get(8));
        ((TextView) findViewById(R.id.sunrise)).setText(responseArrayList.get(9));
        ((TextView) findViewById(R.id.sunset)).setText(responseArrayList.get(10));
        ((TextView) findViewById(R.id.timezone)).setText(responseArrayList.get(11));
        ((TextView) findViewById(R.id.timezone2)).setText(responseArrayList.get(11));
        ((TextView) findViewById(R.id.tv_ciudadDetail)).setText(responseArrayList.get(13) + ", " + responseArrayList.get(12));

    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar() {
        //Visibilizar progressbar mientras carga. Invisibilizar UI.
        findViewById(R.id.loader).setVisibility(View.VISIBLE);
        findViewById(R.id.tempContainer).setVisibility(View.GONE);
        findViewById(R.id.cityContainer).setVisibility(View.GONE);
        findViewById(R.id.detallesContainer).setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {

        //Invisibilizar progressbar ya que terminó la carga. Visibilizar UI.
        findViewById(R.id.loader).setVisibility(View.GONE);
        findViewById(R.id.tempContainer).setVisibility(View.VISIBLE);
        findViewById(R.id.cityContainer).setVisibility(View.VISIBLE);
        findViewById(R.id.detallesContainer).setVisibility(View.VISIBLE);
    }

    @Override
    public void finishActivity() {
        finish();
    }


    public void savePreferences() {
        String responseCity = tvCiudadDetail.getText().toString();

        SharedPreferences sharedPrefs = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString("searchedCity_" + count, responseCity);
        editor.putInt("counter", count + 1);
        editor.apply();
        /*
        La variable count permite ir creando diferentes keys de manera incremental, para almacenar distintas cuidades.
            En el onStart luego se maneja que no se creen más de 5, para coincidir con el array del ListActivity.
        */
    }

    @Override
    public void startBackgroundAnimation() {
        ConstraintLayout constraintLayout = findViewById(R.id.constraint_layout2);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(100);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
    }
}
