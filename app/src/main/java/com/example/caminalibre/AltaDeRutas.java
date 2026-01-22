package com.example.caminalibre;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.caminalibre.Database.CreadorDB;
import com.example.caminalibre.modelo.Ruta;
import com.example.caminalibre.modelo.Tipo;

public class AltaDeRutas extends AppCompatActivity {
    private View.OnClickListener guardarListener = new View.OnClickListener() {
        public void onClick(View v) {
            String nombreRuta = ((EditText) findViewById(R.id.AltaRutasNombreEditText)).getText().toString();
            if (nombreRuta.isBlank()) {
                Toast.makeText(AltaDeRutas.this, "No hay nombre de ruta", Toast.LENGTH_LONG).show();
                return;
            }
            String localizacion = ((EditText) findViewById(R.id.AltasRutasLocalizacionEditText)).getText().toString();
            if (localizacion.isBlank()) {
                Toast.makeText(AltaDeRutas.this, "No hay localización de ruta", Toast.LENGTH_LONG).show();
                return;
            }
            RadioGroup group = findViewById(R.id.AltasRutasTipoRadioGroup);
            int checkedId = group.getCheckedRadioButtonId();
            if(checkedId == -1){
                Toast.makeText(AltaDeRutas.this, "No hay tipo de ruta", Toast.LENGTH_LONG).show();
                return;
            }
            RadioButton radioButton = findViewById(checkedId);
            Tipo tipo = Tipo.valueOf(radioButton.getText().toString());

            RatingBar ratingBar = findViewById(R.id.ratingBar3);
            float dificultad = ratingBar.getRating();

            EditText editTextDistancia = findViewById(R.id.AltaRutasDistanciaEditText);
            float distancia = 0.0f;
            try {
                distancia = Float.parseFloat(editTextDistancia.getText().toString());
            }catch (NumberFormatException e){
                Toast.makeText(AltaDeRutas.this, "Error en la distancia", Toast.LENGTH_LONG).show();
                return;
            }
            String descripcion = ((EditText) findViewById(R.id.AltaRutasDescripcionEditText)).getText().toString();
            if (descripcion.isBlank()){
                Toast.makeText(AltaDeRutas.this, "No hay descripcion de ruta", Toast.LENGTH_LONG).show();
                return;
            }
            String notas = ((EditText) findViewById(R.id.AltaRutasNotasEditText)).getText().toString();
            if (notas.isBlank()){
                Toast.makeText(AltaDeRutas.this, "No hay notas de ruta", Toast.LENGTH_LONG).show();
                return;
            }
            Switch aSwitch = findViewById(R.id.switch1);
            boolean favorita = aSwitch.isChecked();

            double latitud = 0;
            String editTextLatitud = ((EditText) findViewById(R.id.AltaRutasLatitudEditText)).getText().toString();
            try {
                latitud = Double.parseDouble(editTextLatitud);
                if (latitud > 90 || latitud < -90) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(AltaDeRutas.this, "El formato de latitud es incorrecto", Toast.LENGTH_SHORT).show();
                return;
            }

            double longitud = 0;
            String editTextLongitud = ((EditText) findViewById(R.id.AltaRutasLongitudEditText)).getText().toString();
            try {
                longitud = Double.parseDouble(editTextLongitud);
                if (longitud > 180 || longitud < -180) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(AltaDeRutas.this, "El formato de longitud es incorrecto", Toast.LENGTH_SHORT).show();
                return;
            }



            Ruta ruta = new Ruta(nombreRuta, localizacion, tipo, dificultad, distancia, descripcion, notas, favorita);

            // Requisito entrega: Usar hilos para llamadas al DAORUTA
            CreadorDB.ejecutarhilo.execute(()->{
                CreadorDB db = CreadorDB.getDatabase(AltaDeRutas.this);
                // Guardamos en la base de datos
                db.getDAO().inserall(ruta);
                // Volvemos al hilo principal para el Toast y cerrar la pantalla
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AltaDeRutas.this, "Ruta guardada en la base de datos", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent();
                        intent.putExtra("ruta", ruta);
                        setResult(RESULT_OK, intent);
                        AltaDeRutas.this.finish();
                    }
                });
            });







        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alta_de_rutas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button guardar = (Button) findViewById(R.id.AltaRutasGuardarButton);
        guardar.setOnClickListener(guardarListener);
    }
}