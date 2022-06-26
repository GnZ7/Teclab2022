package com.teclab.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 666;
    private String inputCiudad;
    private String ubicacionGPS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pronosticoAct = findViewById(R.id.btn_weather_now);

        //Onclicklistener para darle funcionalidad a la lupa: Buscar la cuidad ingresada por el usuario.
        findViewById(R.id.ic_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputCiudad = ((EditText)findViewById(R.id.eT_ciudadActual)).getText().toString().trim();
                if (inputCiudad.isEmpty()){
                    Toast.makeText(MainActivity.this, "Por favor ingresá una cuidad", Toast.LENGTH_SHORT).show();
                }else{
                    launchDetail((inputCiudad));
                }

            }
        });


        pronosticoAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.loaderM).setVisibility(View.VISIBLE);
                updateLocation();

            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocation();

            } else {
                Toast.makeText(this, "Se requieren permisos para conocer tu ubicación.", Toast.LENGTH_SHORT).show();
            }
            findViewById(R.id.loaderM).setVisibility(View.GONE);
        }
    }

    private void updateLocation() {
        LocationManager locationManager = (LocationManager)MainActivity.this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                //Instanciar Geocoder para obtener la cuidad en base a la latitud y longitud.
                Geocoder geocoder = new Geocoder(MainActivity.this);
                try {

                    List<Address> direcciones = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                    String locality = direcciones.get(0).getLocality();
                    String countryCode = direcciones.get(0).getCountryCode();
                    ubicacionGPS = locality + ", " + countryCode;
                    launchDetail(ubicacionGPS);

                }catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Tu ubicación no esta disponible.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {}

        };
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,50000,locationListener);

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);
            }
        }

    }


    //Lanzar actividad ListActivity
    public void launchList (View v){
        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);
    }


    //Lanzar actividad DetailActivity
    public void launchDetail (String s){
        findViewById(R.id.loaderM).setVisibility(View.GONE);
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra("ciudadElegida", s);
        startActivity(i);
    }



}
