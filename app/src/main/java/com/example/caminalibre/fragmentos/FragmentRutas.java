package com.example.caminalibre.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.caminalibre.adapters.AdapterReclyerView;
import com.example.caminalibre.interfaces.OnRutaClickListener;
import com.example.caminalibre.modelo.Ruta;
import com.example.caminalibre.modelo.Tipo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRutas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRutas extends Fragment implements OnRutaClickListener {

    private String textsearch = "";
    private Spinner spinner;
    private RecyclerView recyclerView;
    private List<Ruta> rutas = new ArrayList<>();
    private AdapterReclyerView adapter;
    private ActivityResultLauncher<Intent> launcher;
    public FragmentRutas() {}
    public static FragmentRutas newInstance(String param1, String param2) {
        FragmentRutas fragment = new FragmentRutas();
        Bundle args = new Bundle();
        /*DATOS*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /*DATOS*/
        }
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {}
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rutas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        // 1. Inicializar Vistas
        spinner = view.findViewById(R.id.spinnerFiltroDificultad);
        recyclerView = view.findViewById(R.id.recyclerViewRutas);

        // 2. Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdapterReclyerView(rutas,this);
        recyclerView.setAdapter(adapter);

        // 4. Observar cambios en la base de datos
        CreadorDB.getDatabase(getContext()).getRutas().observe(getViewLifecycleOwner(), new Observer<List<Ruta>>() {
            @Override
            public void onChanged(List<Ruta> nuevasRutas) {
                rutas = nuevasRutas;
                filtrarRuta();
            }
        });

        // 5. Configurar Spinner
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("Todas");
        opciones.add("Facil");
        opciones.add("Media");
        opciones.add("Dificil");
        ArrayAdapter<String> adapterspinner = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapterspinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtrarRuta();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    public void filtrarRuta() {
        if (spinner == null || adapter == null) return;

        String dificultad = spinner.getSelectedItem().toString();
        List<Ruta> rutasFiltradas = rutas.stream()
                .filter(r -> dificultad.equals("Todas") ||
                        (dificultad.equals("Facil") && r.getDificultad() < 2) ||
                        (dificultad.equals("Media") && r.getDificultad() >= 2 && r.getDificultad() < 4) ||
                        (dificultad.equals("Dificil") && r.getDificultad() >= 4))
                .collect(Collectors.toList());

        adapter.setRutas(new ArrayList<>(rutasFiltradas));
    }
    @Override
    public void onRutaClick(int posicion) {
        Ruta ruta = rutas.get(posicion);
        if(getActivity() instanceof ActivityPrincipal){
            ((ActivityPrincipal) getActivity()).cargarFragmentoDetalle(ruta);
        }

    }

    public void filtrarRuta(String textoBuscar){
        // Obtenemos la dificultad actual del Spinner
        String dificultad = (spinner != null) ? spinner.getSelectedItem().toString() : "Todas";

        if (rutas == null) return;

        List<Ruta> filtradas = rutas.stream()
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

        // Actualizamos el RecyclerView a través del adaptador
        if (adapter != null) {
            adapter.setRutas(new ArrayList<>(filtradas));
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityPrincipal) getActivity()).cambiarNombreToolbar(getString(R.string.app_name));
    }
}