package com.example.caminalibre.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminalibre.R;
import com.example.caminalibre.interfaces.OnRutaClickListener;

public  class RutasViewHolder extends RecyclerView.ViewHolder {
    public TextView nombreRuta;
    public TextView distancia;
   public TextView localizacion;
    public TextView estrellas;
   public ImageView tipo;
    public RutasViewHolder(@NonNull View itemView, OnRutaClickListener listener) {
        super(itemView);
        nombreRuta = itemView.findViewById(R.id.itemRutaNombre);
        distancia = itemView.findViewById(R.id.itemRutaDistancia);
        localizacion = itemView.findViewById(R.id.itemRutaLocalizacion);
        tipo = itemView.findViewById(R.id.itemRutaTipo);
        estrellas = itemView.findViewById(R.id.itemRutaEstrellas);

        tipo.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View v) {

                if (listener !=null){
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onFotoClick(position);
                    }
                }
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // codigo anterior
                if (listener !=null){
                    int position = getBindingAdapterPosition();
                    // posicion valida
                    if (position != RecyclerView.NO_POSITION){
                        listener.onRutaClick(position);
                    }
                }
            }
        });


    }
}