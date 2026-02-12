package com.example.caminalibre.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.caminalibre.viewholders.PuntoViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminalibre.R;
import com.example.caminalibre.modelo.PuntoInteres;

import java.util.List;

public class AdapterPuntos extends RecyclerView.Adapter<PuntoViewHolder> {

    private List<PuntoInteres> puntosInteres;

    public AdapterPuntos(List<PuntoInteres> puntosInteres) {
        this.puntosInteres = puntosInteres;
    }
    public void setLista(List<PuntoInteres> nuevaLista) {
        this.puntosInteres = nuevaLista;
        notifyDataSetChanged();
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
        holder.bind(punto);
    }

    @Override
    public int getItemCount() {
        return puntosInteres != null ? puntosInteres.size() : 0;
    }
}
