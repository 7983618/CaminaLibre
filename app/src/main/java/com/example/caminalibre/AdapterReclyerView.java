package com.example.caminalibre;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminalibre.modelo.Ruta;

import java.util.ArrayList;

public class AdapterReclyerView extends RecyclerView.Adapter<AdapterReclyerView.ViewHolder> {

    ArrayList<Ruta> rutas;
    private static final String codigoestrellellena = "\u2605";
    private static final String codigostrellanula = "\u2606";

    public AdapterReclyerView(ArrayList<Ruta> rutas) {
        this.rutas = rutas;
    }
    @NonNull
    @Override
    public AdapterReclyerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ruta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterReclyerView.ViewHolder holder, int position) {
        Ruta ruta = rutas.get(position);
        holder.nombreRuta.setText(ruta.getNombreRuta());
        holder.distancia.setText(ruta.getDistancia() + " km");
        holder.localizacion.setText(ruta.getLocalizacion());
        holder.estrellas.setText(establecerestrellas(ruta.getDificultad()));
        holder.tipo.setImageResource(R.drawable.fotoapp);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Haz pulsado sobre la ruta: " + rutas.get(position).getNombreRuta(), Toast.LENGTH_SHORT).show();
                Ruta ruta = rutas.get(position);
                // intent a la nueva actividad detalle
                Intent intent = new Intent();

                intent.putExtra("ruta", rutas.get(position));

            }
        });
    }

    private String establecerestrellas(float dificultad) {
        String estrellas = "";
        int redondeado = Math.round(dificultad);
        for (int i = 0; i < 5; i++) {
            if (redondeado>i){
                estrellas+=codigoestrellellena;
            }else {
                estrellas+=codigostrellanula;
            }
        }
        return estrellas;
    }

    @Override
    public int getItemCount() {
        return rutas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreRuta;
        TextView distancia;
        TextView localizacion;
        TextView estrellas;
        ImageView tipo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreRuta = itemView.findViewById(R.id.itemRutaNombre);
            distancia = itemView.findViewById(R.id.itemRutaDistancia);
            localizacion = itemView.findViewById(R.id.itemRutaLocalizacion);
            tipo = itemView.findViewById(R.id.itemRutaTipo);
            estrellas = itemView.findViewById(R.id.itemRutaEstrellas);
        }
    }
    public void setRutas(ArrayList<Ruta> rutas){
        this.rutas = rutas;
        notifyDataSetChanged();
    }
}
