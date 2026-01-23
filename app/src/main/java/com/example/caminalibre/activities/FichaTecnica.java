package com.example.caminalibre.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminalibre.adapters.AdapterPuntos;
import com.example.caminalibre.Database.CreadorDB;
import com.example.caminalibre.R;
import com.example.caminalibre.modelo.Ruta;

import java.util.ArrayList;

public class FichaTecnica extends AppCompatActivity {
    Ruta ruta;
    Integer posicion;

    RecyclerView recyclerView;
    AdapterPuntos adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "antes", Toast.LENGTH_SHORT);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ficha_tecnica);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ejecutarrecyclerview();
        //ruta = (Ruta) getIntent().getSerializableExtra("ruta");
        //posicion = (Integer) getIntent().getSerializableExtra("posicion");

        Toast.makeText(this, "despues", Toast.LENGTH_SHORT);
        TextView titulo = (TextView) findViewById(R.id.FichaTecnicaTitulo);
        TextView distancia = (TextView) findViewById(R.id.FichaTecnicaDistancia);
        boolean hayDistancia = ruta.getDistancia() !=0;

        TextView dificultad = (TextView) findViewById(R.id.FichaTecnicaDificultad);
        boolean hayDificultad = ruta.getDificultad() !=0;

        TextView tiempoEstimado = (TextView) findViewById(R.id.FichaTecnicaTiempoEstimado);
        TextView latitud = (TextView) findViewById(R.id.FichaTecnicaLatitud);
        boolean haylatitud = ruta.getLatitud() !=0;
        TextView longitud = (TextView) findViewById(R.id.FichaTecnicaLongitud);
        boolean hayLongitud = ruta.getLongitud() !=0;

        titulo.setText(ruta.getNombreRuta());
        if (hayDistancia) {
            distancia.setText(String.valueOf(ruta.getDistancia()));
        }
        if (hayDificultad) {
            dificultad.setText(String.valueOf(ruta.getDificultad()));
        }
        if (hayDificultad && hayDistancia) {
            int tiempo;
            if (ruta.getDificultad() >= 4) {
                tiempo = (int) Math.round(ruta.getDistancia() / 3 * 60);
            } else {
                tiempo = (int) Math.round(ruta.getDistancia() / 4 * 60);
            }
            tiempoEstimado.setText(String.valueOf(tiempo) +" " + getString(R.string.FichaTecnicaMinutos));
        }
        if (haylatitud) {
            latitud.setText(String.valueOf(ruta.getLatitud()));
        }
        if (hayLongitud) {
            longitud.setText(String.valueOf(ruta.getLongitud()));
        }

        CheckBox favorite = (CheckBox) findViewById(R.id.FichaTecnicaFavoriaCheckBox);
        Toast.makeText(FichaTecnica.this, String.valueOf(ruta.isFavorita()), Toast.LENGTH_SHORT).show();
        favorite.setChecked(ruta.isFavorita());
        favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ruta.setFavorita(true);
                } else {
                    ruta.setFavorita(false);
                }
                Toast.makeText(FichaTecnica.this, String.valueOf(ruta.isFavorita()), Toast.LENGTH_SHORT).show();
                CreadorDB.getDatabase(FichaTecnica.this).actualizarRuta(ruta);
            }
        });

        ImageButton borrar = (ImageButton) findViewById(R.id.FichaTecnicaBorrarButton);
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreadorDB.getDatabase(FichaTecnica.this).borrarRuta(ruta, FichaTecnica.this);
            }
        });
    }

    private void ejecutarrecyclerview() {
        recyclerView = findViewById(R.id.recyclerViewPuntosInteres);
        ruta = (Ruta) getIntent().getSerializableExtra("ruta");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdapterPuntos(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        if (ruta !=null){
            long id = ruta.getId();
            CreadorDB.getDatabase(this).getPuntosDAO().getPuntosDeInteres(id)
                    .observe(this,puntos->{
                        adapter.setLista(puntos);
                    });
        }else{
            Toast.makeText(this, "Ruta no encontrada", Toast.LENGTH_SHORT).show();
        }


    }
}















