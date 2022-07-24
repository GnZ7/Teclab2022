package com.teclab.weatherapp.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.teclab.weatherapp.R;
import com.teclab.weatherapp.contract.WeatherContract;

import java.util.Arrays;

public class ListActivity extends AppCompatActivity implements WeatherContract.View.BackgroundAnimation {

    private String mensajeCiudad;

    //Array de string que llenará al listview.
    private final String[] citiesArray = {"Buenos Aires, AR", "Tokio, JP", "Bangkok, TH",
            "Maldives, MV", "Santa Cruz, AR"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ListView cities_listView = findViewById(R.id.cities_listView);

        startBackgroundAnimation();
        loadSharedPreferences();
        setArrayAdapter(cities_listView);


        cities_listView.setOnItemClickListener((parent, view, position, id) -> {

            mensajeCiudad = citiesArray[position];  //Al clickear un elemento del listView toma el string en esa posicion del array.
            launchDetail(mensajeCiudad);
        });
    }

    private void setArrayAdapter(ListView listView) {
        //Adapter para aplicar mi layout personalizado al listView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_ciudades, citiesArray);
        listView.setAdapter(adapter);
    }

    //Metodo para lanzar Detail y pasarle el mensaje.
    private void launchDetail(String s) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra("ciudadElegida", s);
        startActivity(i);
    }

    @Override
    public void startBackgroundAnimation() {
        ConstraintLayout constraintLayout = findViewById(R.id.constraint_layout1);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(100);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
    }

    private void loadSharedPreferences() {
        SharedPreferences sharedPrefs = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        //Recuperar las ciudades guardadas y, si no son null y no están en el array (para no repetir), agregarlas al listado
        for (int i = 0; i < citiesArray.length; i++) {

            String searchedCity = sharedPrefs.getString("searchedCity_" + i, "");
            if (!searchedCity.equals("") && !Arrays.asList(citiesArray).contains(searchedCity)) {
                citiesArray[i] = searchedCity;
            }

        }
        Arrays.sort(citiesArray);
    }
}