package com.example.caminalibre.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminalibre.R;

public  class PuntoViewHolder extends RecyclerView.ViewHolder {
    public TextView nombre, coordenadas;
    public ImageView foto;

    public PuntoViewHolder(@NonNull View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.itemPuntoNombre);
        coordenadas = itemView.findViewById(R.id.itemPuntoCoordenadas);
        foto = itemView.findViewById(R.id.itemPuntoImagen);
    }
}