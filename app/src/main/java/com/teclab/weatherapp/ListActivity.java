package com.teclab.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends AppCompatActivity {

    private String mensajeCiudad;

    //Array de string que llenará al listview.
    private final String[] ciudadesArray = {"Buenos Aires, AR", "Tokio, JP", "Bangkok, TH",
            "Maldivas, MV", "Santa Cruz, AR"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //Vinculo el listView del layout con mi variable local.
        ListView cities_listView = findViewById(R.id.cities_listView);

        //Adapter para aplicar mi layout personalizado al listView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_ciudades, ciudadesArray);
        cities_listView.setAdapter(adapter);

        //Metodo para que al clickear un elemento del listView tome el string del array de ciudades en esa posicion.
        // y luego lo pase como mensaje a la actividad Detail.
        cities_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mensajeCiudad = ciudadesArray[position];
                launchDetail(mensajeCiudad);
            }
        });
    }
        //Metodo para lanzar Detail y pasarle el mensaje.
        private void launchDetail (String s){
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra("ciudadElegida", s);
        startActivity(i);
        }
}