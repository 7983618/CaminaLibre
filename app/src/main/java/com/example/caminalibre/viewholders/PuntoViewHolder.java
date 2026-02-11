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
    private final TextView nombre;
    private final TextView coordenadas;
    private final ImageView foto;

    public PuntoViewHolder(@NonNull View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.itemPuntoNombre);
        coordenadas = itemView.findViewById(R.id.itemPuntoCoordenadas);
        foto = itemView.findViewById(R.id.itemPuntoImagen);
    }


    public void bind(@NonNull PuntoInteres punto) {
        Context context = itemView.getContext();

        nombre.setText(punto.getNombre());
        String coordenadas1 = "Lat: " + punto.getLatitud() + " | Lon: " + punto.getLongitud();

        coordenadas.setText(coordenadas1);
        if (punto.getFoto() != null) {
            foto.setImageURI(Uri.parse(punto.getFoto()));
        }

        // Lógica del click para abrir mapas movida aquí
        itemView.setOnClickListener(v -> {
            abrirMapa(context, punto);
        });
    }

    private void abrirMapa(Context context, PuntoInteres punto) {
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