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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Has pulsado el punto de interes ", Toast.LENGTH_SHORT).show();
                int valor = holder.getBindingAdapterPosition();
                PuntoInteres punto = puntosInteres.get(valor);
                double latitud = punto.getLatitud();
                double longitud = punto.getLongitud();

                // 2. Crear el URI (la 'q' permite poner un marcador con nombre)
                String uri = "geo:" + latitud + "," + longitud + "?q=" + latitud + "," + longitud + "(" + punto.getNombre() + ")";
                android.content.Intent mapIntent = new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(uri));

                // 3. Intentar abrir siempre con la App de Google Maps
                mapIntent.setPackage("com.google.android.apps.maps");

                // 4. Verificar si hay una aplicación que pueda manejar el intent y lanzarla
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                } else {
                    // Si no tiene Google Maps, intentamos abrirlo de forma genérica (navegador u otros mapas)
                    android.content.Intent backupIntent = new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(uri));
                    context.startActivity(backupIntent);
                }

            }
        });



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
