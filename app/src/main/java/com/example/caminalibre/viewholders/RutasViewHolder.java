package com.example.caminalibre.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminalibre.R;

public  class RutasViewHolder extends RecyclerView.ViewHolder {
    public TextView nombreRuta;
    public TextView distancia;
   public TextView localizacion;
    public TextView estrellas;
   public ImageView tipo;
    public RutasViewHolder(@NonNull View itemView) {
        super(itemView);
        nombreRuta = itemView.findViewById(R.id.itemRutaNombre);
        distancia = itemView.findViewById(R.id.itemRutaDistancia);
        localizacion = itemView.findViewById(R.id.itemRutaLocalizacion);
        tipo = itemView.findViewById(R.id.itemRutaTipo);
        estrellas = itemView.findViewById(R.id.itemRutaEstrellas);
    }
}