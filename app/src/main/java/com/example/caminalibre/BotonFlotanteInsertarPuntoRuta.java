package com.example.caminalibre;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.caminalibre.Database.CreadorDB;
import com.example.caminalibre.modelo.PuntoInteres;
import com.example.caminalibre.modelo.Ruta;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class BotonFlotanteInsertarPuntoRuta extends BottomSheetDialogFragment {


    private Ruta ruta;
    private String pathFotoTemporal;

    public BotonFlotanteInsertarPuntoRuta() {
        // Required empty public constructor
    }

    public BotonFlotanteInsertarPuntoRuta(Ruta ruta) {
        this.ruta = ruta;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.pathFotoTemporal = getArguments().getString("FOTO_INICIAL");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.boton_flotante_insertar_punto, container, false);


    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView titulo = view.findViewById(R.id.titulopunto);
        titulo.setText("Añadir Punto de Interés para la Ruta "+ruta.getNombreRuta());
        ImageButton imageButton = view.findViewById(R.id.imgPuntoPreview);
        EditText editNombre = view.findViewById(R.id.editNombrePunto);
        EditText editLat = view.findViewById(R.id.editLatPunto);
        EditText editLon = view.findViewById(R.id.editLonPunto);

        // LÓGICA DEL BOTÓN GUARDAR
        MaterialButton boton = view.findViewById(R.id.botonguardapunto);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNombre.getText().toString().isBlank()){
                    editNombre.setError("Campo obligatorio");
                    return;
                }
                double latitud = editLat.getText().toString().isBlank() ? 0.0 : Double.parseDouble(editLat.getText().toString());
                double longitud = editLon.getText().toString().isBlank() ? 0.0 : Double.parseDouble(editLon.getText().toString());
                String nombre = editNombre.getText().toString();

                PuntoInteres puntoInteres = new PuntoInteres(nombre, latitud, longitud, null, ruta.getId());


                View principal = getActivity().findViewById(R.id.main);

                CreadorDB.getDatabase(getContext()).insertarPuntoInteres(puntoInteres, null);

                dismiss();

                if (principal != null) {
                    Snackbar.make(principal, "Punto guardado", Snackbar.LENGTH_LONG)
                            .setAction("DESHACER", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CreadorDB.getDatabase(getContext()).borrarPuntoInteres(puntoInteres);
                                }
                            })
                            .show();
                }


//                // 2. Usamos un Handler para esperar 2 segundos SIN bloquear la app
//                new android.os.Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // Este código se ejecutará después de 2000ms (2 segundos)
//                        if (isAdded()) { // Comprobamos que el fragmento sigue activo
//                            dismiss();
//                        }
//                    }
//                }, 2000);
            }
        });




    }



}
