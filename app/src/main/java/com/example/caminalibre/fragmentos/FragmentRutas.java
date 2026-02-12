package com.example.caminalibre.fragmentos;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.caminalibre.Database.CreadorDB;
import com.example.caminalibre.R;
import com.example.caminalibre.activities.ActivityPrincipal;
import com.example.caminalibre.adapters.AdapterRutas;
import com.example.caminalibre.interfaces.OnRutaClickListener;
import com.example.caminalibre.modelo.Ruta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FragmentRutas extends Fragment implements OnRutaClickListener {
    private List<Ruta> rutas = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterRutas adapter;
    private Spinner spinner;

    public FragmentRutas() {}

    @Override
    public void onCreate(Bundle savedInstanceState) { //INICIALIZAR DATOS
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { //INFLAR INTERFAZ
        return inflater.inflate(R.layout.fragment_rutas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { //LÓGICA
        super.onViewCreated(view, savedInstanceState);

        // CONFIGURAR SPINNER
        spinner = view.findViewById(R.id.spinnerFiltroDificultad);

        ArrayList<String> opciones = new ArrayList<>(); // CARGAR OPCIONES
        opciones.add("Todas");
        opciones.add("Facil");
        opciones.add("Media");
        opciones.add("Dificil");
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // SELECCION OPCION
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtrarPorDificultad();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        // CONFIGURACIÓN RECICLERVIEW
        recyclerView = view.findViewById(R.id.recyclerViewRutas);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdapterRutas(rutas,this);
        recyclerView.setAdapter(adapter);

        // OBSERVAR BASE DE DATOS POR SI CAMBIA Y TENEMOS QUE VOLVER A FILTRAR
        CreadorDB.getDatabase(getContext()).getRutas().observe(getViewLifecycleOwner(), new Observer<List<Ruta>>() {
            @Override
            public void onChanged(List<Ruta> nuevasRutas) {
                rutas = nuevasRutas;
                filtrarPorDificultad();
            }
        });
    }
    //-----------------------------------------FILTROS-------------------------------------------------
    public void filtrarPorDificultad() {
        String dificultad = spinner.getSelectedItem().toString();
        List<Ruta> rutasFiltradas = rutas.stream()
                .filter(r -> dificultad.equals("Todas") ||
                        (dificultad.equals("Facil") && r.getDificultad() < 2) ||
                        (dificultad.equals("Media") && r.getDificultad() >= 2 && r.getDificultad() < 4) ||
                        (dificultad.equals("Dificil") && r.getDificultad() >= 4))
                .collect(Collectors.toList());
        adapter.setRutas(new ArrayList<>(rutasFiltradas));
    }
    public void filtrarPorNombre(String textoBuscar) {
        String dificultad = spinner.getSelectedItem().toString();
        List<Ruta> rutasFiltradas = rutas.stream()
                .filter(r -> {
                    // Filtro 1: El nombre coincide con lo que escribimos (Estilo Google)
                    boolean coincideNombre = r.getNombreRuta().toLowerCase()
                            .contains(textoBuscar.toLowerCase());

                    // Filtro 2: La dificultad coincide con el Spinner
                    boolean coincideDificultad = dificultad.equals("Todas") ||
                            (dificultad.equals("Facil") && r.getDificultad() < 2) ||
                            (dificultad.equals("Media") && r.getDificultad() >= 2 && r.getDificultad() < 4) ||
                            (dificultad.equals("Dificil") && r.getDificultad() >= 4);

                    return coincideNombre && coincideDificultad;
                })
                .collect(Collectors.toList());
        adapter.setRutas(new ArrayList<>(rutasFiltradas));
    }
    //------------------------------CARGA DE FRAGMENTOS CON INTERFAZ----------------------------------------
    @Override
    public void onRutaClick(int posicion) {
        Ruta ruta = rutas.get(posicion);
        if(getActivity() instanceof ActivityPrincipal){
            ((ActivityPrincipal) getActivity()).cargarFragmentoDetalle(ruta);
        }

    }
    //-----------------------------------REESTABLECER EL NOMBRE---------------------------------------------
    @Override
    public void onResume() {
        super.onResume();
        ((ActivityPrincipal) getActivity()).cambiarNombreToolbar(getString(R.string.app_name));
    }
}