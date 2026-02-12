package com.example.caminalibre.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.caminalibre.Database.CreadorDB;
import com.example.caminalibre.R;
import com.example.caminalibre.fragmentos.FragmentAcercaDe;
import com.example.caminalibre.fragmentos.FragmentDetalle;
import com.example.caminalibre.fragmentos.FragmentAltas;
import com.example.caminalibre.fragmentos.FragmentRutas;
import com.example.caminalibre.modelo.PuntoInteres;
import com.example.caminalibre.modelo.Ruta;
import com.example.caminalibre.modelo.Tipo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class ActivityPrincipal extends AppCompatActivity {
    FragmentRutas fragmentRutas = new FragmentRutas();
    FragmentAltas fragmentAltas = new FragmentAltas();
    FragmentAcercaDe fragmentAcercaDe = new FragmentAcercaDe();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_drawerlayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //DATOS
        inicializarRutas();
        inicializarPuntosInteres();

        //CONFIGURACIÓN INICIAL TOOLBAR
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        //CONFIGURACIÓN BOTTOMNAVIGATIONVIEW
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> { //ITEM SELECCIONADO
            int id = item.getItemId();
            if (id == R.id.bottom_navigation_altas) {
                loadFragment(fragmentAltas, true);
                return true;
            } else if (id == R.id.bottom_navigation_rutas) {
                Fragment actual = getSupportFragmentManager().findFragmentById(R.id.frame_container);
                if (!(actual instanceof FragmentRutas)) {
                    loadFragment(new FragmentRutas(), false);
                }
                return true;
            } else if (id == R.id.bottom_navigation_ayuda) {
                ayuda();
                return true;
            } else if (id == R.id.bottom_navigation_acerca_de) {
                loadFragment(fragmentAcercaDe, true);
                return true;
            } else if (id == R.id.bottom_navigation_salir) {
                finishAffinity();
                return true;
            }
            return false;
        });

        // CARGAR FRAGMENTO INICIAL
        if (savedInstanceState == null) {
            loadFragment(fragmentRutas,false);
        }

    }

    //--------------------------LOGICA DE FRAGMENTOS------------------------------------
    public void loadFragment(Fragment fragment, boolean backStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (!backStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        if (backStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
    public void cargarFragmentoDetalle(Ruta ruta) {
        FragmentDetalle rutaFragment = new FragmentDetalle();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ruta", ruta);
        rutaFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, rutaFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void ayuda() {
        String url = "https://www.senderismo.net/consejos";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
    public void cambiarNombreToolbar(String nombre) {
        toolbar.setTitle(nombre);
    }
    //--------------------------LOGICA DE TOOLBAR---------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //HAMBURGUESA
        int id = item.getItemId();
        if (id == R.id.toolbar_altas) {
            loadFragment(fragmentAltas, true);
        } else if (id == R.id.toolbar_ajustes) {
            Toast.makeText(this, "Se ha pulsado ajustes", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //INFLADO Y LOGICA BUSCAR
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem itemBuscar = menu.findItem(R.id.toolbar_buscar);
        SearchView searchView = (SearchView) itemBuscar.getActionView();

        searchView.setQueryHint("Escribe el nombre de la ruta...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) { //TECLA POR TECLA
                if (fragmentRutas !=null && fragmentRutas.isAdded()){
                    fragmentRutas.filtrarPorNombre(newText);
                }
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) { //ENTER (NO USADO)
                return false;
            }
        });

        return true;
    }
    //--------------------------LOGICA DE DATOS---------------------------------------
    private void inicializarRutas() {
        List<Ruta> rutas = new ArrayList<>();
        rutas.add(new Ruta("Anillo de Picos", "Asturias", Tipo.Circular, 5.0f, 115.0, "...", "...", false, 43.2, -4.8));
        // --- RUTAS CIRCULARES --- y longitud y latitud
        rutas.add(new Ruta("Anillo de Picos + Latitud y Longitud", "Asturias", Tipo.Circular, 5.0f, 115.0, "Travesía mítica por los Picos de Europa.", "Requiere equipo de alta montaña.", false, 86.34, 13.67));
        rutas.add(new Ruta("Laguna Negra", "Soria", Tipo.Circular, 2.0f, 4.5, "Paseo sencillo rodeando la laguna glacial.", "Muy concurrido en festivos.", true, 41.99, -2.84));
        rutas.add(new Ruta("Cañón del Río Lobos", "Burgos", Tipo.Circular, 3.0f, 12.0, "Ruta entre buitres y acantilados calizos.", "Llevar linterna para las cuevas.", true, 41.75, -3.05));

        // --- RUTAS LINEALES ---
        rutas.add(new Ruta("Vía Verde de la Jara", "Toledo", Tipo.Lineal, 1.0f, 52.0, "Antiguo trazado ferroviario ideal para bicis.", "Muchos túneles.", false, 39.7126, -5.0232));
        rutas.add(new Ruta("Ascensión al Teide", "Tenerife", Tipo.Lineal, 4.5f, 9.0, "Subida al pico más alto de España.", "Necesario permiso para la cima.", true, 28.2723, -16.6425));
        rutas.add(new Ruta("Camino de Santiago (Tramo)", "Navarra", Tipo.Lineal, 3.0f, 25.5, "Etapa clásica entre Roncesvalles y Zubiri.", "Suelo pedregoso en la bajada.", false, 43.0092, -1.3158));

        // --- RUTAS CORTAS Y SENCILLAS ---
        rutas.add(new Ruta("Bosque de Secuoyas", "Cantabria", Tipo.Circular, 1.0f, 1.5, "Paseo entre árboles gigantes.", "Aparcamiento limitado.", true, 43.3214, -4.1883));
        rutas.add(new Ruta("Chorro de Navafría", "Segovia", Tipo.Lineal, 2.0f, 3.0, "Camino a una cascada natural.", "Zona de picnic disponible.", false, 41.0542, -3.8214));

        // --- RUTAS DE GRAN DISTANCIA / DIFICULTAD ---
        rutas.add(new Ruta("GR-11 (Etapa 1)", "Pirineos", Tipo.Lineal, 5.0f, 30.2, "Inicio de la gran travesía pirenaica.", "Desnivel acumulado muy alto.", false, 43.3551, -1.7911));
        rutas.add(new Ruta("Ruta de los Volcanes", "La Palma", Tipo.Lineal, 4.0f, 17.5, "Caminata sobre paisajes lunares y lava.", "Sin puntos de agua en el camino.", true, 28.5994, -17.8464));

        // --- 40 RUTAS ADICIONALES ---
        rutas.add(new Ruta("Ruta del Cares", "León", Tipo.Lineal, 2.0f, 12.0, "La garganta divina entre León y Asturias.", "Vistas impresionantes del desfiladero.", true, 43.2536, -4.8361));
        rutas.add(new Ruta("Nacimiento del Río Mundo", "Albacete", Tipo.Circular, 1.5f, 4.0, "Espectacular cascada conocida como 'El Reventón'.", "Ideal para familias.", true, 38.4522, -2.4371));
        rutas.add(new Ruta("La Pedriza", "Madrid", Tipo.Circular, 3.5f, 15.0, "Formaciones graníticas singulares.", "Cuidado con el terreno resbaladizo.", false, 40.7333, -3.8833));
        rutas.add(new Ruta("Congost de Mont-rebei", "Lleida", Tipo.Lineal, 2.5f, 8.0, "Desfiladero excavado por el río Noguera Ribagorzana.", "Pasarelas colgadas en la roca.", true, 42.0967, 0.6967));
        rutas.add(new Ruta("Ruta de las Caras", "Cuenca", Tipo.Circular, 1.0f, 2.0, "Esculturas talladas en roca junto al pantano.", "Ruta artística y familiar.", false, 40.4086, -2.7164));
        rutas.add(new Ruta("Cabo de Gata (San José)", "Almería", Tipo.Lineal, 1.5f, 10.0, "Caminata entre playas vírgenes y acantilados.", "Llevar agua, no hay sombras.", true, 36.7594, -2.1097));
        rutas.add(new Ruta("Ruta del Flysch", "Gipuzkoa", Tipo.Lineal, 2.5f, 14.0, "Acantilados con capas geológicas milenarias.", "Mejor con marea baja.", false, 43.3000, -2.2667));
        rutas.add(new Ruta("Monasterio de Piedra", "Zaragoza", Tipo.Circular, 1.0f, 5.0, "Parque natural con cascadas y grutas.", "Entrada de pago al recinto.", true, 41.1944, -1.7822));
        rutas.add(new Ruta("Senda de Camille", "Huesca", Tipo.Circular, 5.0f, 120.0, "Travesía por los valles occidentales.", "Alojamiento en refugios.", false, 42.8436, -0.6658));
        rutas.add(new Ruta("Faro de Formentor", "Mallorca", Tipo.Lineal, 3.0f, 12.5, "Vistas panorámicas del Mediterráneo.", "Viento fuerte en la zona.", true, 39.9614, 3.2114));
        rutas.add(new Ruta("Hoces del Río Duratón", "Segovia", Tipo.Circular, 2.0f, 10.0, "Ermita de San Frutos y buitres leonados.", "Binoculares recomendados.", true, 41.3283, -3.8789));
        rutas.add(new Ruta("Muntanya de Montserrat", "Barcelona", Tipo.Circular, 3.5f, 11.0, "Macizo montañoso con formas singulares.", "Lugar de peregrinación.", false, 41.5933, 1.8375));
        rutas.add(new Ruta("Ruta de los Faros", "A Coruña", Tipo.Lineal, 4.5f, 200.0, "Gran recorrido por la Costa da Morte.", "Paisajes salvajes y atlánticos.", true, 43.1114, -9.1922));
        rutas.add(new Ruta("Aigüestortes y Estany de Sant Maurici", "Lleida", Tipo.Circular, 3.0f, 18.0, "Parque Nacional con cientos de lagos.", "Paisaje de alta montaña.", false, 42.5806, 0.9856));
        rutas.add(new Ruta("Cazorla (Río Borosa)", "Jaén", Tipo.Lineal, 2.5f, 22.0, "Ruta fluvial por excelencia en Andalucía.", "Túneles y cascadas.", true, 38.0167, -2.8667));
        rutas.add(new Ruta("Ruta de los Puentes Colgantes", "Valencia", Tipo.Circular, 2.0f, 7.5, "Paseo por el cañón del río Turia en Chulilla.", "Puentes de madera espectaculares.", true, 39.6586, -0.8931));
        rutas.add(new Ruta("Sierra de Gredos (Circo)", "Ávila", Tipo.Lineal, 3.5f, 14.0, "Ascensión a la Laguna Grande.", "Se suelen ver cabras montesas.", false, 40.2500, -5.2500));
        rutas.add(new Ruta("Bardenas Reales (Castildetierra)", "Navarra", Tipo.Circular, 1.5f, 6.0, "Paisaje semidesértico espectacular.", "Parece otro planeta.", true, 42.1794, -1.5033));
        rutas.add(new Ruta("Ruta de las Xanas", "Asturias", Tipo.Lineal, 2.0f, 8.0, "Pequeño Cares asturiano.", "Desfiladero muy fotogénico.", false, 43.2758, -5.9758));
        rutas.add(new Ruta("Dunas de Corralejo", "Fuerteventura", Tipo.Circular, 1.0f, 5.0, "Caminata sobre arena blanca frente al mar.", "Protegerse bien del sol.", false, 28.6947, -13.8428));
        rutas.add(new Ruta("Málaga (Torcal de Antequera)", "Málaga", Tipo.Circular, 1.5f, 4.5, "Laberinto natural de rocas calizas.", "Formas caprichosas de la erosión.", true, 36.9531, -4.5442));
        rutas.add(new Ruta("Teide (Pico Viejo)", "Tenerife", Tipo.Lineal, 4.5f, 15.0, "Alternativa salvaje a la cima principal.", "Terreno de ceniza volcánica.", false, 28.2611, -16.6669));
        rutas.add(new Ruta("Ruta del Agua", "Sevilla", Tipo.Circular, 1.5f, 12.0, "Ribera del río Huelva y antiguos molinos.", "Sombreada y agradable.", false, 37.5275, -6.0425));
        rutas.add(new Ruta("Serranía de Cuenca (Ciudad Encantada)", "Cuenca", Tipo.Circular, 1.0f, 3.0, "Formaciones kársticas famosas.", "Ideal para ir con niños.", true, 40.2208, -2.0050));
        rutas.add(new Ruta("Peñalara (Lagunas)", "Madrid", Tipo.Circular, 3.0f, 11.0, "Techo de la Sierra de Guadarrama.", "Paisaje glaciar cerca de la capital.", true, 40.8500, -3.9500));
        rutas.add(new Ruta("Cabo de Creus", "Girona", Tipo.Circular, 2.5f, 9.0, "Punto más oriental de la península.", "Inspiración para Salvador Dalí.", false, 42.3189, 3.3158));
        rutas.add(new Ruta("Senda del Arcediano", "León", Tipo.Lineal, 3.0f, 28.0, "Antigua vía romana por los Picos de Europa.", "Mucha historia en cada paso.", false, 43.1500, -4.9500));
        rutas.add(new Ruta("Barranco de Masca", "Tenerife", Tipo.Lineal, 4.0f, 10.0, "Descenso desde el caserío hasta la playa.", "Regreso en barco opcional.", true, 28.3033, -16.8406));
        rutas.add(new Ruta("Valle del Jerte (Los Pilones)", "Cáceres", Tipo.Lineal, 1.5f, 6.0, "Pozas naturales excavadas en granito.", "Famoso por los cerezos.", true, 40.2150, -5.7667));
        rutas.add(new Ruta("Ruta de los Monasterios", "La Rioja", Tipo.Circular, 2.0f, 15.0, "Cuna del castellano entre San Millán y Cañas.", "Visita cultural obligada.", false, 42.3267, -2.8647));
        rutas.add(new Ruta("Salinas de San Pedro", "Murcia", Tipo.Circular, 1.0f, 4.0, "Avistamiento de flamencos y aves.", "Llana y muy fácil.", true, 37.8206, -0.7781));
        rutas.add(new Ruta("San Juan de Gaztelugatxe", "Bizkaia", Tipo.Lineal, 2.0f, 3.0, "Escaleras hasta la ermita sobre el mar.", "Escenario de Juego de Tronos.", true, 43.4472, -2.7850));
        rutas.add(new Ruta("Ruta de los Volcanes (Garrotxa)", "Girona", Tipo.Circular, 2.0f, 12.0, "Hayedos sobre cráteres extintos.", "Espectacular en otoño.", false, 42.1500, 2.5500));
        rutas.add(new Ruta("Valles Pasiegos", "Cantabria", Tipo.Circular, 3.0f, 14.0, "Prados verdes infinitos y cabañas de piedra.", "Esencia pura de Cantabria.", true, 43.2000, -3.8333));
        rutas.add(new Ruta("Ribeira Sacra (Cañón del Sil)", "Ourense", Tipo.Lineal, 3.5f, 16.0, "Viñedos heroicos sobre el río.", "Vistas desde los miradores.", true, 42.3667, -7.5000));
        rutas.add(new Ruta("Sierra de Tramuntana (GR-221)", "Mallorca", Tipo.Lineal, 4.0f, 140.0, "Ruta de la Pedra en Sec.", "Patrimonio de la Humanidad.", false, 39.7500, 2.7500));
        rutas.add(new Ruta("Las Médulas", "León", Tipo.Circular, 1.5f, 7.0, "Antigua mina de oro romana a cielo abierto.", "Tierra roja y castaños centenarios.", true, 42.4586, -6.7644));
        rutas.add(new Ruta("Ancares (Cuiña)", "Lugo", Tipo.Lineal, 3.0f, 10.0, "Montañas indómitas entre Galicia y León.", "Zona de osos y urogallos.", false, 42.8500, -6.8333));
        rutas.add(new Ruta("Sierra de Urbasa", "Navarra", Tipo.Circular, 2.0f, 9.0, "Hayedo encantado y nacedero del Urederra.", "Aguas de color azul turquesa.", true, 42.8333, -2.1500));
        rutas.add(new Ruta("Valle de Ordesa (Cola de Caballo)", "Huesca", Tipo.Lineal, 3.0f, 17.5, "Clásica ascensión por el fondo del valle.", "Impresionantes paredes de piedra.", true, 42.6667, -0.0500));
        CreadorDB.getDatabase(this).insertarLista(rutas);
    }
    private void inicializarPuntosInteres() {
        CreadorDB.ejecutarhilo.execute(() -> {
            // 1. Obtener todas las rutas que ya están en la BD
            // Nota: Usamos una consulta directa al DAO fuera de LiveData para este proceso masivo
            List<Ruta> todasLasRutas = CreadorDB.getDatabase(this).getDAO().readAllSync();

            List<PuntoInteres> puntosNuevos = new ArrayList<>();

            // 2. Por cada ruta, crear 5 puntos de interés
            for (Ruta r : todasLasRutas) {
                long idRuta = r.getId();
                for (int i = 1; i <= 5; i++) {
                    puntosNuevos.add(new PuntoInteres(
                            "Punto " + i + " de " + r.getNombreRuta(), // Nombre
                            r.getLatitud() + (i * 0.001),             // Latitud simulada
                            r.getLongitud() + (i * 0.001),            // Longitud simulada
                            "foto_demo_" + i,                         // Foto demo
                            idRuta                                     // ID de la ruta vinculada
                    ));
                }
            }

            // 3. Insertar todos los puntos de golpe en la base de datos
            CreadorDB.getDatabase(this).getPuntosDAO().insertAll(puntosNuevos);
        });
    }

}