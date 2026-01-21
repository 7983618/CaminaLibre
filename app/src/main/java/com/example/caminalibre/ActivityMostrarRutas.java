package com.example.caminalibre;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminalibre.modelo.Ruta;
import com.example.caminalibre.modelo.Tipo;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ActivityMostrarRutas extends AppCompatActivity {
    Spinner spinner;
    RecyclerView recyclerView;
    ArrayList<Ruta> rutas;

    AdapterReclyerView adapter;


    public ActivityResultLauncher<Intent> launcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mostrar_tareas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent intent = result.getData();
                            if (intent != null) {
                                Ruta ruta = (Ruta) intent.getSerializableExtra("ruta_devuelta");
                                Integer posicion = (Integer) intent.getSerializableExtra("posicion");
                                adapter.rutas.set(posicion.intValue(), ruta);
                                adapter.notifyItemChanged(posicion);

                                for (int i = 0; i < rutas.size(); i++) {
                                    if (rutas.get(i).getNombreRuta().equals(ruta.getNombreRuta())) {
                                        rutas.set(i, ruta);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
        );


        Intent intent = getIntent();
        rutas = (ArrayList<Ruta>) intent.getSerializableExtra("rutas");
        if (rutas == null || rutas.isEmpty()) {
            inicializarDatos();
        }

        recyclerView = findViewById(R.id.recyclerViewRutas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdapterReclyerView(rutas, this, launcher);

        recyclerView.setAdapter(adapter);
        spinner = findViewById(R.id.spinnerFiltroDificultad);
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("Todas");
        opciones.add("Facil");
        opciones.add("Media");
        opciones.add("Dificil");
        ArrayAdapter<String> adapterspinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterspinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String dificultad = spinner.getSelectedItem().toString();
                ArrayList<Ruta> tareasfiltro = new ArrayList<>(rutas.
                        stream().
                        filter(r -> dificultad.equals("Todas") ||
                                (dificultad.equals("Facil") && r.getDificultad() < 2)
                                || (dificultad.equals("Media") && r.getDificultad() >= 2 && r.getDificultad() < 4)
                                || (dificultad.equals("Dificil") && r.getDificultad() >= 4)).collect(Collectors.toList()));

               adapter.setRutas(tareasfiltro);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void inicializarDatos() {
        rutas = new ArrayList<>();
        // --- RUTAS CIRCULARES --- y longitud y latitud
        rutas.add(new Ruta("Anillo de Picos + Latitud y Longitud", "Asturias", Tipo.Circular, 5.0f, 115.0, "Travesía mítica por los Picos de Europa.", "Requiere equipo de alta montaña.", false, 86.34, 13.67));
        rutas.add(new Ruta("Laguna Negra", "Soria", Tipo.Circular, 2.0f, 4.5, "Paseo sencillo rodeando la laguna glacial.", "Muy concurrido en festivos.", true, 86.34, 13.67));
        rutas.add(new Ruta("Cañón del Río Lobos", "Burgos", Tipo.Circular, 3.0f, 12.0, "Ruta entre buitres y acantilados calizos.", "Llevar linterna para las cuevas.", true, 86.34, 13.67));

        // --- RUTAS LINEALES ---
        rutas.add(new Ruta("Vía Verde de la Jara", "Toledo", Tipo.Lineal, 1.0f, 52.0, "Antiguo trazado ferroviario ideal para bicis.", "Muchos túneles.", false));
        rutas.add(new Ruta("Ascensión al Teide", "Tenerife", Tipo.Lineal, 4.5f, 9.0, "Subida al pico más alto de España.", "Necesario permiso para la cima.", true));
        rutas.add(new Ruta("Camino de Santiago (Tramo)", "Navarra", Tipo.Lineal, 3.0f, 25.5, "Etapa clásica entre Roncesvalles y Zubiri.", "Suelo pedregoso en la bajada.", false));

        // --- RUTAS CORTAS Y SENCILLAS ---
        rutas.add(new Ruta("Bosque de Secuoyas", "Cantabria", Tipo.Circular, 1.0f, 1.5, "Paseo entre árboles gigantes.", "Aparcamiento limitado.", true));
        rutas.add(new Ruta("Chorro de Navafría", "Segovia", Tipo.Lineal, 2.0f, 3.0, "Camino a una cascada natural.", "Zona de picnic disponible.", false));

        // --- RUTAS DE GRAN DISTANCIA / DIFICULTAD ---
        rutas.add(new Ruta("GR-11 (Etapa 1)", "Pirineos", Tipo.Lineal, 5.0f, 30.2, "Inicio de la gran travesía pirenaica.", "Desnivel acumulado muy alto.", false));
        rutas.add(new Ruta("Ruta de los Volcanes", "La Palma", Tipo.Lineal, 4.0f, 17.5, "Caminata sobre paisajes lunares y lava.", "Sin puntos de agua en el camino.", true));

        // --- 40 RUTAS ADICIONALES ---
        rutas.add(new Ruta("Ruta del Cares", "León", Tipo.Lineal, 2.0f, 12.0, "La garganta divina entre León y Asturias.", "Vistas impresionantes del desfiladero.", true));
        rutas.add(new Ruta("Nacimiento del Río Mundo", "Albacete", Tipo.Circular, 1.5f, 4.0, "Espectacular cascada conocida como 'El Reventón'.", "Ideal para familias.", true));
        rutas.add(new Ruta("La Pedriza", "Madrid", Tipo.Circular, 3.5f, 15.0, "Formaciones graníticas singulares.", "Cuidado con el terreno resbaladizo.", false));
        rutas.add(new Ruta("Congost de Mont-rebei", "Lleida", Tipo.Lineal, 2.5f, 8.0, "Desfiladero excavado por el río Noguera Ribagorzana.", "Pasarelas colgadas en la roca.", true));
        rutas.add(new Ruta("Ruta de las Caras", "Cuenca", Tipo.Circular, 1.0f, 2.0, "Esculturas talladas en roca junto al pantano.", "Ruta artística y familiar.", false));
        rutas.add(new Ruta("Cabo de Gata (San José)", "Almería", Tipo.Lineal, 1.5f, 10.0, "Caminata entre playas vírgenes y acantilados.", "Llevar agua, no hay sombras.", true));
        rutas.add(new Ruta("Ruta del Flysch", "Gipuzkoa", Tipo.Lineal, 2.5f, 14.0, "Acantilados con capas geológicas milenarias.", "Mejor con marea baja.", false));
        rutas.add(new Ruta("Monasterio de Piedra", "Zaragoza", Tipo.Circular, 1.0f, 5.0, "Parque natural con cascadas y grutas.", "Entrada de pago al recinto.", true));
        rutas.add(new Ruta("Senda de Camille", "Huesca", Tipo.Circular, 5.0f, 120.0, "Travesía por los valles occidentales.", "Alojamiento en refugios.", false));
        rutas.add(new Ruta("Faro de Formentor", "Mallorca", Tipo.Lineal, 3.0f, 12.5, "Vistas panorámicas del Mediterráneo.", "Viento fuerte en la zona.", true));
        rutas.add(new Ruta("Hoces del Río Duratón", "Segovia", Tipo.Circular, 2.0f, 10.0, "Ermita de San Frutos y buitres leonados.", "Binoculares recomendados.", true));
        rutas.add(new Ruta("Muntanya de Montserrat", "Barcelona", Tipo.Circular, 3.5f, 11.0, "Macizo montañoso con formas singulares.", "Lugar de peregrinación.", false));
        rutas.add(new Ruta("Ruta de los Faros", "A Coruña", Tipo.Lineal, 4.5f, 200.0, "Gran recorrido por la Costa da Morte.", "Paisajes salvajes y atlánticos.", true));
        rutas.add(new Ruta("Aigüestortes y Estany de Sant Maurici", "Lleida", Tipo.Circular, 3.0f, 18.0, "Parque Nacional con cientos de lagos.", "Paisaje de alta montaña.", false));
        rutas.add(new Ruta("Cazorla (Río Borosa)", "Jaén", Tipo.Lineal, 2.5f, 22.0, "Ruta fluvial por excelencia en Andalucía.", "Túneles y cascadas.", true));
        rutas.add(new Ruta("Ruta de los Puentes Colgantes", "Valencia", Tipo.Circular, 2.0f, 7.5, "Paseo por el cañón del río Turia en Chulilla.", "Puentes de madera espectaculares.", true));
        rutas.add(new Ruta("Sierra de Gredos (Circo)", "Ávila", Tipo.Lineal, 3.5f, 14.0, "Ascensión a la Laguna Grande.", "Se suelen ver cabras montesas.", false));
        rutas.add(new Ruta("Bardenas Reales (Castildetierra)", "Navarra", Tipo.Circular, 1.5f, 6.0, "Paisaje semidesértico espectacular.", "Parece otro planeta.", true));
        rutas.add(new Ruta("Ruta de las Xanas", "Asturias", Tipo.Lineal, 2.0f, 8.0, "Pequeño Cares asturiano.", "Desfiladero muy fotogénico.", false));
        rutas.add(new Ruta("Dunas de Corralejo", "Fuerteventura", Tipo.Circular, 1.0f, 5.0, "Caminata sobre arena blanca frente al mar.", "Protegerse bien del sol.", false));
        rutas.add(new Ruta("Málaga (Torcal de Antequera)", "Málaga", Tipo.Circular, 1.5f, 4.5, "Laberinto natural de rocas calizas.", "Formas caprichosas de la erosión.", true));
        rutas.add(new Ruta("Teide (Pico Viejo)", "Tenerife", Tipo.Lineal, 4.5f, 15.0, "Alternativa salvaje a la cima principal.", "Terreno de ceniza volcánica.", false));
        rutas.add(new Ruta("Ruta del Agua", "Sevilla", Tipo.Circular, 1.5f, 12.0, "Ribera del río Huelva y antiguos molinos.", "Sombreada y agradable.", false));
        rutas.add(new Ruta("Serranía de Cuenca (Ciudad Encantada)", "Cuenca", Tipo.Circular, 1.0f, 3.0, "Formaciones kársticas famosas.", "Ideal para ir con niños.", true));
        rutas.add(new Ruta("Peñalara (Lagunas)", "Madrid", Tipo.Circular, 3.0f, 11.0, "Techo de la Sierra de Guadarrama.", "Paisaje glaciar cerca de la capital.", true));
        rutas.add(new Ruta("Cabo de Creus", "Girona", Tipo.Circular, 2.5f, 9.0, "Punto más oriental de la península.", "Inspiración para Salvador Dalí.", false));
        rutas.add(new Ruta("Senda del Arcediano", "León", Tipo.Lineal, 3.0f, 28.0, "Antigua vía romana por los Picos de Europa.", "Mucha historia en cada paso.", false));
        rutas.add(new Ruta("Barranco de Masca", "Tenerife", Tipo.Lineal, 4.0f, 10.0, "Descenso desde el caserío hasta la playa.", "Regreso en barco opcional.", true));
        rutas.add(new Ruta("Valle del Jerte (Los Pilones)", "Cáceres", Tipo.Lineal, 1.5f, 6.0, "Pozas naturales excavadas en granito.", "Famoso por los cerezos.", true));
        rutas.add(new Ruta("Ruta de los Monasterios", "La Rioja", Tipo.Circular, 2.0f, 15.0, "Cuna del castellano entre San Millán y Cañas.", "Visita cultural obligada.", false));
        rutas.add(new Ruta("Salinas de San Pedro", "Murcia", Tipo.Circular, 1.0f, 4.0, "Avistamiento de flamencos y aves.", "Llana y muy fácil.", true));
        rutas.add(new Ruta("San Juan de Gaztelugatxe", "Bizkaia", Tipo.Lineal, 2.0f, 3.0, "Escaleras hasta la ermita sobre el mar.", "Escenario de Juego de Tronos.", true));
        rutas.add(new Ruta("Ruta de los Volcanes (Garrotxa)", "Girona", Tipo.Circular, 2.0f, 12.0, "Hayedos sobre cráteres extintos.", "Espectacular en otoño.", false));
        rutas.add(new Ruta("Valles Pasiegos", "Cantabria", Tipo.Circular, 3.0f, 14.0, "Prados verdes infinitos y cabañas de piedra.", "Esencia pura de Cantabria.", true));
        rutas.add(new Ruta("Ribeira Sacra (Cañón del Sil)", "Ourense", Tipo.Lineal, 3.5f, 16.0, "Viñedos heroicos sobre el río.", "Vistas desde los miradores.", true));
        rutas.add(new Ruta("Sierra de Tramuntana (GR-221)", "Mallorca", Tipo.Lineal, 4.0f, 140.0, "Ruta de la Pedra en Sec.", "Patrimonio de la Humanidad.", false));
        rutas.add(new Ruta("Las Médulas", "León", Tipo.Circular, 1.5f, 7.0, "Antigua mina de oro romana a cielo abierto.", "Tierra roja y castaños centenarios.", true));
        rutas.add(new Ruta("Ancares (Cuiña)", "Lugo", Tipo.Lineal, 3.0f, 10.0, "Montañas indómitas entre Galicia y León.", "Zona de osos y urogallos.", false));
        rutas.add(new Ruta("Sierra de Urbasa", "Navarra", Tipo.Circular, 2.0f, 9.0, "Hayedo encantado y nacedero del Urederra.", "Aguas de color azul turquesa.", true));
        rutas.add(new Ruta("Valle de Ordesa (Cola de Caballo)", "Huesca", Tipo.Lineal, 3.0f, 17.5, "Clásica ascensión por el fondo del valle.", "Impresionantes paredes de piedra.", true));
    }

}