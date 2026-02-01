package com.example.caminalibre.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminalibre.R;
import com.example.caminalibre.activities.FichaTecnica;
import com.example.caminalibre.interfaces.OnRutaClickListener;
import com.example.caminalibre.modelo.Ruta;
import com.example.caminalibre.viewholders.RutasViewHolder;

import java.util.List;

public class AdapterReclyerView extends RecyclerView.Adapter<RutasViewHolder> {

    List<Ruta> rutas;
    Context context;
    private OnRutaClickListener mListener;
    ActivityResultLauncher<Intent> launcher;
    private static final String codigoestrellellena = "\u2605";
    private static final String codigostrellanula = "\u2606";


    public AdapterReclyerView(List<Ruta> rutas, Context context, ActivityResultLauncher<Intent> launcher,OnRutaClickListener listener) {
        this.rutas = rutas;
        this.context = context;
        this.launcher = launcher;
        this.mListener = listener;
    }



    @NonNull
    @Override
    public RutasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ruta, parent, false);
        return new RutasViewHolder(view,mListener);
    }



    @Override
    public void onBindViewHolder(@NonNull RutasViewHolder holder, int position) {
        Ruta ruta = rutas.get(position);
        holder.nombreRuta.setText(ruta.getNombreRuta());
        holder.distancia.setText(ruta.getDistancia() + " km");
        holder.localizacion.setText(ruta.getLocalizacion());
        holder.estrellas.setText(establecerestrellas(ruta.getDificultad()));
        holder.tipo.setImageResource(R.drawable.fotoapp);

//        holder.tipo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int pos = holder.getBindingAdapterPosition();
//                String nombre = rutas.get(pos).getNombreRuta();
//
//                // realizar intent para abrir galeria o camara para poner foto
//                Toast.makeText(context, "Has pulsado sobre la imagen de la ruta: " + nombre, Toast.LENGTH_SHORT).show();
//            }
//        });
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int valor = holder.getBindingAdapterPosition();
//                Toast.makeText(v.getContext(), "Haz pulsado sobre la ruta: " + rutas.get(valor).getNombreRuta(), Toast.LENGTH_SHORT).show();
//                Ruta ruta = rutas.get(valor);
//                // intent a la nueva actividad detalle
//                Intent intent = new Intent(context, FichaTecnica.class);
//                intent.putExtra("ruta", rutas.get(valor));
//                //intent.putExtra("posicion", Integer.valueOf(valor));
//                //launcher.launch(intent);
//                context.startActivity(intent);
//
//            }
//        });
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
    public void setRutas(List<Ruta> rutas){
        this.rutas = rutas;
        notifyDataSetChanged();
    }
}
