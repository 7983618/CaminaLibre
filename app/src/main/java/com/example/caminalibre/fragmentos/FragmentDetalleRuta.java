package com.example.caminalibre.fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caminalibre.Database.CreadorDB;
import com.example.caminalibre.R;
import com.example.caminalibre.adapters.AdapterPuntos;
import com.example.caminalibre.modelo.Ruta;

import java.util.ArrayList;
import java.util.function.ToDoubleBiFunction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDetalleRuta#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDetalleRuta extends Fragment {
    private Ruta ruta;
    private RecyclerView recyclerView;
    private AdapterPuntos adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentDetalleRuta() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDetalleRuta.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDetalleRuta newInstance(String param1, String param2) {
        FragmentDetalleRuta fragment = new FragmentDetalleRuta();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            ruta = (Ruta) getArguments().getSerializable("ruta");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalle_ruta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Referenciamos las vistas usando 'view.findViewById'
        TextView titulo = view.findViewById(R.id.FichaTecnicaTitulo);
        TextView distancia = view.findViewById(R.id.FichaTecnicaDistancia);
        TextView dificultad = view.findViewById(R.id.FichaTecnicaDificultad);
        TextView tiempoEstimado = view.findViewById(R.id.FichaTecnicaTiempoEstimado);
        TextView latitud = view.findViewById(R.id.FichaTecnicaLatitud);
        TextView longitud = view.findViewById(R.id.FichaTecnicaLongitud);
        CheckBox favorite = view.findViewById(R.id.FichaTecnicaFavoriaCheckBox);

        if (ruta != null) {
            // Lógica de visualización de datos (tu lógica original)
            titulo.setText(ruta.getNombreRuta());
            distancia.setText(String.valueOf(ruta.getDistancia()));
            dificultad.setText(String.valueOf(ruta.getDificultad()));
            latitud.setText(String.valueOf(ruta.getLatitud()));
            longitud.setText(String.valueOf(ruta.getLongitud()));

            // Cálculo de tiempo estimado
            int tiempo = (ruta.getDificultad() >= 4) ?
                    (int) Math.round(ruta.getDistancia() / 3 * 60) :
                    (int) Math.round(ruta.getDistancia() / 4 * 60);
            tiempoEstimado.setText(tiempo + " " + getString(R.string.FichaTecnicaMinutos));

            // Configuración de Favoritos
            favorite.setChecked(ruta.isFavorita());
            favorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
                ruta.setFavorita(isChecked);
                CreadorDB.getDatabase(getContext()).actualizarRuta(ruta);
                Toast.makeText(getContext(), "Favorito: " + isChecked, Toast.LENGTH_SHORT).show();
            });

            ejecutarrecyclerview(view);
        }

    }
    private void ejecutarrecyclerview(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewPuntosInteres);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AdapterPuntos(new ArrayList<>(), getContext());
        recyclerView.setAdapter(adapter);

        // Acceso a base de datos con LiveData
        CreadorDB.getDatabase(getContext()).getPuntosDAO().getPuntosDeInteres(ruta.getId())
                .observe(getViewLifecycleOwner(), puntos -> {
                    adapter.setLista(puntos);
                });
    }
}