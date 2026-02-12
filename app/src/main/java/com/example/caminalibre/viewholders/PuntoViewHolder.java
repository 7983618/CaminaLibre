package com.example.caminalibre.viewholders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminalibre.R;
import com.example.caminalibre.modelo.PuntoInteres;

public  class PuntoViewHolder extends RecyclerView.ViewHolder {
    private TextView nombre;
    private TextView coordenadas;
    private View viewActual;
    public PuntoViewHolder(@NonNull View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.itemPuntoNombre);
        coordenadas = itemView.findViewById(R.id.itemPuntoCoordenadas);
        viewActual = itemView;
    }
    public void bind(PuntoInteres punto) {
        nombre.setText(punto.getNombre());
        coordenadas.setText("Lat: " + punto.getLatitud() + " | Lon: " + punto.getLongitud());
        itemView.setOnClickListener(v -> {
            abrirMapa(punto);
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