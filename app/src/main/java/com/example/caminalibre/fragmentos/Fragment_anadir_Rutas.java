package com.example.caminalibre.fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.caminalibre.Database.CreadorDB;
import com.example.caminalibre.R;
import com.example.caminalibre.modelo.Ruta;
import com.example.caminalibre.modelo.Tipo;

public class Fragment_anadir_Rutas extends Fragment {
    Button guardarButton;
    public Fragment_anadir_Rutas() {}
    public static Fragment_anadir_Rutas newInstance(String param1, String param2) {
        Fragment_anadir_Rutas fragment = new Fragment_anadir_Rutas();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anadir_rutas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        guardarButton = (Button) view.findViewById(R.id.AltaRutasGuardarButton);
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreRuta = ((EditText) view.findViewById(R.id.AltaRutasNombreEditText)).getText().toString();
                if (nombreRuta.isBlank()) {
                    Toast.makeText(getContext(), "No hay nombre de ruta", Toast.LENGTH_LONG).show();
                    return;
                }

                String localizacion = ((EditText) view.findViewById(R.id.AltasRutasLocalizacionEditText)).getText().toString();
                if (localizacion.isBlank()) {
                    Toast.makeText(getContext(), "No hay localización de ruta", Toast.LENGTH_LONG).show();
                    return;
                }

                RadioGroup group = view.findViewById(R.id.AltasRutasTipoRadioGroup);
                int checkedId = group.getCheckedRadioButtonId();
                if(checkedId == -1){
                    Toast.makeText(getContext(), "No hay tipo de ruta", Toast.LENGTH_LONG).show();
                    return;
                }

                RadioButton radioButton = view.findViewById(checkedId);
                Tipo tipo = Tipo.valueOf(radioButton.getText().toString());

                RatingBar ratingBar = view.findViewById(R.id.ratingBar3);
                float dificultad = ratingBar.getRating();

                EditText editTextDistancia = view.findViewById(R.id.AltaRutasDistanciaEditText);
                float distancia = 0.0f;
                try {
                    distancia = Float.parseFloat(editTextDistancia.getText().toString());
                }catch (NumberFormatException e){
                    Toast.makeText(getContext(), "Error en la distancia", Toast.LENGTH_LONG).show();
                    return;
                }

                String descripcion = ((EditText) view.findViewById(R.id.AltaRutasDescripcionEditText)).getText().toString();
                if (descripcion.isBlank()){
                    Toast.makeText(getContext(), "No hay descripcion de ruta", Toast.LENGTH_LONG).show();
                    return;
                }

                String notas = ((EditText) view.findViewById(R.id.AltaRutasNotasEditText)).getText().toString();
                if (notas.isBlank()){
                    Toast.makeText(getContext(), "No hay notas de ruta", Toast.LENGTH_LONG).show();
                    return;
                }

                Switch aSwitch = view.findViewById(R.id.switch1);
                boolean favorita = aSwitch.isChecked();

                double latitud = 0;
                String editTextLatitud = ((EditText) view.findViewById(R.id.AltaRutasLatitudEditText)).getText().toString();
                try {
                    latitud = Double.parseDouble(editTextLatitud);
                    if (latitud > 90 || latitud < -90) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "El formato de latitud es incorrecto", Toast.LENGTH_SHORT).show();
                    return;
                }

                double longitud = 0;
                String editTextLongitud = ((EditText) view.findViewById(R.id.AltaRutasLongitudEditText)).getText().toString();
                try {
                    longitud = Double.parseDouble(editTextLongitud);
                    if (longitud > 180 || longitud < -180) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "El formato de longitud es incorrecto", Toast.LENGTH_SHORT).show();
                    return;
                }



                Ruta ruta = new Ruta(nombreRuta, localizacion, tipo, dificultad, distancia, descripcion, notas, favorita);
                CreadorDB.getDatabase(getContext()).insertarRuta(ruta, getActivity());
            }
        });
    }
}