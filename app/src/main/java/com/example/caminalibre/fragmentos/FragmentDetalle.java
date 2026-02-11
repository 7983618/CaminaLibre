package com.example.caminalibre.fragmentos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminalibre.Database.CreadorDB;
import com.example.caminalibre.R;
import com.example.caminalibre.activities.ActivityPrincipal;
import com.example.caminalibre.adapters.AdapterPuntos;
import com.example.caminalibre.modelo.PuntoInteres;
import com.example.caminalibre.modelo.Ruta;
import com.example.caminalibre.servicios.MusicService;

import java.util.ArrayList;
import java.util.List;

public class FragmentDetalle extends Fragment {
    private Ruta ruta;
    private RecyclerView recyclerView;
    private AdapterPuntos adapter;
    private ActivityResultLauncher<String> launcherPermisos;

    public FragmentDetalle() {}

    @Override
    public void onCreate(Bundle savedInstanceState) { //INICIALIZAR DATOS
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ruta = (Ruta) getArguments().getSerializable("ruta");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { //INFLAR INTERFAZ
        return inflater.inflate(R.layout.fragment_detalle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { //LÓGICA
        super.onViewCreated(view, savedInstanceState);
        //CAMBIAR TITULO
        ((ActivityPrincipal) getActivity()).cambiarNombreToolbar(ruta.getNombreRuta());

        //OBTENER ELEMENTOS
        TextView titulo = view.findViewById(R.id.FichaTecnicaTitulo);
        TextView distancia = view.findViewById(R.id.FichaTecnicaDistancia);
        TextView dificultad = view.findViewById(R.id.FichaTecnicaDificultad);
        TextView tiempoEstimado = view.findViewById(R.id.FichaTecnicaTiempoEstimado);
        TextView latitud = view.findViewById(R.id.FichaTecnicaLatitud);
        TextView longitud = view.findViewById(R.id.FichaTecnicaLongitud);

        //CONFIGURACION FAVORITA
        CheckBox favorita = view.findViewById(R.id.FichaTecnicaFavoriaCheckBox);

        favorita.setChecked(ruta.isFavorita());
        favorita.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ruta.setFavorita(isChecked);
            CreadorDB.getDatabase(getContext()).actualizarRuta(ruta);
            Toast.makeText(getContext(), "Favorito: " + isChecked, Toast.LENGTH_SHORT).show();
        });

        //CONFIGURACIÓN BORRADO
        ImageButton borrar = getActivity().findViewById(R.id.FichaTecnicaBorrarButton);
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreadorDB.getDatabase(getContext()).borrarRuta(ruta, null);
                getParentFragmentManager().popBackStack();
            }
        });

        //CONFIGURACIÓN MÚSICA
        CheckBox musica = view.findViewById(R.id.FichaTecnicaMusicaCheckbox);
        musica.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // ENVIAR ORDEN DE PLAY AL SERVICIO
                Intent playIntent = new Intent(getContext(), MusicService.class);
                playIntent.setAction("PLAY");
                requireContext().startService(playIntent); // llamada a onStartCommand() MusicaService.java
                Toast.makeText(getContext(), "Reproduciendo audio...", Toast.LENGTH_SHORT).show();
            } else {
                // ENVIAR ORDEN DE STOP AL SERVICIO
                Intent stopIntent = new Intent(getContext(), MusicService.class);
                stopIntent.setAction("STOP");
                requireContext().startService(stopIntent);
            }
        });

        //CONFIGURACIÓN CAMARA/PERMISOS
        ImageButton imageButton = view.findViewById(R.id.FichaTecnicaImagenRutaImageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permitido = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
                if (permitido == PackageManager.PERMISSION_GRANTED) {
                    abrirCamara();
                } else {
                    launcherPermisos.launch(Manifest.permission.CAMERA);
                }
            }
        });

        launcherPermisos = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean o) {
                abrirCamara();
            }
        });

        //ESCRIBIR DATOS
        titulo.setText(ruta.getNombreRuta());
        distancia.setText(String.valueOf(ruta.getDistancia()));
        dificultad.setText(String.valueOf(ruta.getDificultad()));
        latitud.setText(String.valueOf(ruta.getLatitud()));
        longitud.setText(String.valueOf(ruta.getLongitud()));

        int tiempo = 0;
        if (ruta.getDificultad() >= 4) {
            tiempo = (int) Math.round(ruta.getDistancia() / 3 * 60);
        } else {
            tiempo = (int) Math.round(ruta.getDistancia() / 4 * 60);
        }
        tiempoEstimado.setText(tiempo + " " + getString(R.string.FichaTecnicaMinutos));

        if (ruta.getRutaImagen() != null) {
            imageButton.setImageURI(Uri.parse(ruta.getRutaImagen()));
        }

        ejecutarReciclerView(view);
    }
    private void abrirCamara() {
        // 1. Creamos la instancia del fragmento de la cámara pasando el ID de la ruta actual
        FragmentCamara fragmentCamara = FragmentCamara.newInstance(ruta.getId());

        // 2. Usamos el método de ActivityPrincipal para cargar el fragmento
        if (getActivity() instanceof ActivityPrincipal) {
            ((ActivityPrincipal) getActivity()).loadFragment(fragmentCamara, true);
        }
    }
    private void ejecutarReciclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewPuntosInteres);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AdapterPuntos(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        CreadorDB.getDatabase(getContext()).getPuntosDAO().getPuntosDeInteres(ruta.getId()).observe(getViewLifecycleOwner(), new Observer<List<PuntoInteres>>() {
            @Override
            public void onChanged(List<PuntoInteres> puntoInteres) {
                adapter.setLista(puntoInteres);
            }
        });
    }

    @Override
    public void onResume() { //RECARGA LA IMAGEN. (REMPLAZABLE POR LIVEDATA)
        super.onResume();
        CreadorDB.ejecutarhilo.execute(() -> {
            Ruta rutaActualizada = CreadorDB.getDatabase(getContext()).getDAO().read(ruta.getId());

            if (rutaActualizada != null && rutaActualizada.getRutaImagen() != null) {
                requireActivity().runOnUiThread(() -> {
                    ImageButton imageButton = getView().findViewById(R.id.FichaTecnicaImagenRutaImageButton);
                    imageButton.setImageURI(Uri.parse(rutaActualizada.getRutaImagen()));

                    this.ruta = rutaActualizada;
                });
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        // AQUÍ ESTÁ EL TRUCO:
        // isRemoving() es true si el usuario pulsó ATRÁS o cambió de sección.
        // getActivity().isFinishing() es true si la app se está cerrando del todo.
        if (isRemoving() || (getActivity() != null && getActivity().isFinishing())) {
            // ESCENARIO 1: El usuario CIERRA el detalle. Paramos música.
            Intent closeIntent = new Intent(getContext(), MusicService.class);
//            stopIntent.setAction("STOP");
            requireContext().stopService(closeIntent);
        } else {
            // ESCENARIO 2: El usuario solo ha MINIMIZADO la app (botón Home).
            // No enviamos STOP, por lo que la música SIGUE SONANDO como Spotify.
        }
    }
}