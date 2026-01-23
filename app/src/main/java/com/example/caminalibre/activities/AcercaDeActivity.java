package com.example.caminalibre.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caminalibre.R;

public class AcercaDeActivity extends AppCompatActivity {

    // comentario ma
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        Button btnSoporte = findViewById(R.id.btnSoporte);
        btnSoporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); 
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"soporte@caminalibre.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Soporte Técnico - CaminaLibre");
                
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }
}