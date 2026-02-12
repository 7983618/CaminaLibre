package com.example.caminalibre.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminalibre.R;
import com.example.caminalibre.interfaces.OnRutaClickListener;
import com.example.caminalibre.modelo.Ruta;
import com.example.caminalibre.viewholders.RutasViewHolder;

import java.util.List;

public class AdapterRutas extends RecyclerView.Adapter<RutasViewHolder> {
    List<Ruta> rutas;
    private OnRutaClickListener listener;
    public AdapterRutas(List<Ruta> rutas, OnRutaClickListener listener) {
        this.rutas = rutas;
        this.listener = listener;
    }
    public void setRutas(List<Ruta> rutas){
        this.rutas = rutas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RutasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ruta, parent, false);
        return new RutasViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RutasViewHolder holder, int position) {
        Ruta ruta = rutas.get(position);
        holder.bind(ruta);
    }

    @Override
    public int getItemCount() {
        return rutas.size();
    }
}
