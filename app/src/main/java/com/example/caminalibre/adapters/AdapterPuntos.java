package com.example.caminalibre.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.caminalibre.viewholders.PuntoViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminalibre.R;
import com.example.caminalibre.modelo.PuntoInteres;

import java.util.List;

public class AdapterPuntos extends RecyclerView.Adapter<PuntoViewHolder> {

    private List<PuntoInteres> puntosInteres;
    private Context context;

    public AdapterPuntos(List<PuntoInteres> puntosInteres, Context context) {
        this.puntosInteres = puntosInteres;
        this.context = context;
    }

    @NonNull
    @Override
    public PuntoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_punto_interes, parent, false);
        return new PuntoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PuntoViewHolder holder, int position) {
        PuntoInteres punto = puntosInteres.get(position);
        holder.nombre.setText(punto.getNombre());
        
        String coordenadas = "Lat: " + punto.getLatitud() + " | Lon: " + punto.getLongitud();
        holder.coordenadas.setText(coordenadas);
    }

    @Override
    public int getItemCount() {
        return puntosInteres.size();
    }

    // Método vital para que el LiveData de Room actualice la lista
    public void setLista(List<PuntoInteres> nuevaLista) {
        this.puntosInteres = nuevaLista;
        notifyDataSetChanged();
    }


}
