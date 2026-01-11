package com.example.caminalibre;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AltaDeRutas extends AppCompatActivity {
    private View.OnClickListener guardarListener = new View.OnClickListener() {
        public void onClick(View v) {
            String nombreRuta = ((EditText) findViewById(R.id.AltaRutasNombreEditText)).getText().toString();
            if (nombreRuta.isEmpty()) {
                Toast.makeText(AltaDeRutas.this, "No hay nombre de ruta", Toast.LENGTH_LONG).show();
            }
            AltaDeRutas.this.finish();
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