package com.example.caminalibre.viewholders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminalibre.Database.CreadorDB;
import com.example.caminalibre.R;
import com.example.caminalibre.modelo.PuntoInteres;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public  class PuntoViewHolder extends RecyclerView.ViewHolder {
    private TextView nombre;
    private TextView coordenadas;
    private Button borrar;
    private View viewActual;
    private ImageView imageView;
    public PuntoViewHolder(@NonNull View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.itemPuntoNombre);
        coordenadas = itemView.findViewById(R.id.itemPuntoCoordenadas);
        borrar = itemView.findViewById(R.id.itemPuntoBorrar);
        imageView = itemView.findViewById(R.id.itemPuntoImagen);
        viewActual = itemView;

    }
    public void bind(PuntoInteres punto) {
        nombre.setText(punto.getNombre());
        coordenadas.setText("Lat: " + punto.getLatitud() + " | Lon: " + punto.getLongitud());
        imageView.setOnClickListener(v -> {
            abrirMapa(punto);
        });
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Opción moderna: Borrado directo con opción de deshacer (Undo)
                CreadorDB db = CreadorDB.getDatabase(viewActual.getContext());

                db.borrarPuntoInteres(punto);

                Snackbar.make(viewActual, "Punto eliminado "+punto.getNombre(), Snackbar.LENGTH_LONG)
                        .setAction("DESHACER", new View.OnClickListener() {
                            @Override
                            public void onClick(View undoView) {
                                // Si pulsa deshacer, lo volvemos a insertar
                                db.insertarPuntoInteres(punto, null);
                            }
                        })
                        .show();
            }
        });

    }

    private void abrirMapa(PuntoInteres punto) {
        Context context = viewActual.getContext();
        Toast.makeText(context, "Abriendo ubicación: " + punto.getNombre(), Toast.LENGTH_SHORT).show();
        String uri = "geo:" + punto.getLatitud() + "," + punto.getLongitud() + "?q=" + punto.getLatitud() + "," + punto.getLongitud() + "(" + punto.getNombre() + ")";

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        } else {
            // Intent genérico si no hay Google Maps
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
        }

    }
}