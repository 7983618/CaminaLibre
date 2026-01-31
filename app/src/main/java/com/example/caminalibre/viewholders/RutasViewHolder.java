package com.example.caminalibre.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

//        holder.tipo.setOnClickListener(new View.OnClickListener() {
//          @Override
//            public void onClick(View v) {
//                int pos = holder.getBindingAdapterPosition();
//                String nombre = rutas.get(pos).getNombreRuta();
//
//                // realizar intent para abrir galeria o camara para poner foto
//                Toast.makeText(context, "Has pulsado sobre la imagen de la ruta: " + nombre, Toast.LENGTH_SHORT).show();
//            }
//        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // codigo anterior
//                int valor = holder.getBindingAdapterPosition();
//                Toast.makeText(v.getContext(), "Haz pulsado sobre la ruta: " + rutas.get(valor).getNombreRuta(), Toast.LENGTH_SHORT).show();
//                Ruta ruta = rutas.get(valor);
//                // intent a la nueva actividad detalle
//                Intent intent = new Intent(context, FichaTecnica.class);
//                intent.putExtra("ruta", rutas.get(valor));
//                //intent.putExtra("posicion", Integer.valueOf(valor));
//                //launcher.launch(intent);
//                context.startActivity(intent);
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