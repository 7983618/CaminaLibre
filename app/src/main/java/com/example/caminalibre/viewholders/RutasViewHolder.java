package com.example.caminalibre.viewholders;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminalibre.R;
import com.example.caminalibre.interfaces.OnRutaClickListener;
import com.example.caminalibre.modelo.Ruta;

public  class RutasViewHolder extends RecyclerView.ViewHolder {
    private static final String ESTRELLA_LLENA = "\u2605"; // ★
    private static final String ESTRELLA_VACIA = "\u2606"; // ☆
    private final TextView nombreRuta;
    private final TextView distancia;
    private final TextView localizacion;
    private final TextView estrellas;
    private final ImageView tipo;
    public RutasViewHolder(@NonNull View itemView, OnRutaClickListener listener) {
        super(itemView);
        nombreRuta = itemView.findViewById(R.id.itemRutaNombre);
        distancia = itemView.findViewById(R.id.itemRutaDistancia);
        localizacion = itemView.findViewById(R.id.itemRutaLocalizacion);
        tipo = itemView.findViewById(R.id.itemRutaTipo);
        estrellas = itemView.findViewById(R.id.itemRutaEstrellas);

        itemView.setOnClickListener(v -> {
            if (listener == null) return;
            int position = getBindingAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;
            listener.onRutaClick(position);
        });


    }

    public void bind(Ruta ruta) {
        nombreRuta.setText(ruta.getNombreRuta());
        distancia.setText(ruta.getDistancia() + " km");
        localizacion.setText(ruta.getLocalizacion());
        estrellas.setText(generarEstrellas(ruta.getDificultad()));

        if (ruta.getRutaImagen() != null) {
            tipo.setImageURI(Uri.parse(ruta.getRutaImagen()));
        }
    }
    private String generarEstrellas(float dificultad) {
        StringBuilder sb = new StringBuilder(5);
        int redondeado = Math.round(dificultad);
        for (int i = 0; i < 5; i++) {
            sb.append(i < redondeado ? ESTRELLA_LLENA : ESTRELLA_VACIA);
        }
        return sb.toString();
    }







}