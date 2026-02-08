package com.example.caminalibre.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar$InspectionCompanion;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.caminalibre.Database.CreadorDB;
import com.example.caminalibre.R;
import com.example.caminalibre.fragmentos.FragmentAcercaDe;
import com.example.caminalibre.fragmentos.FragmentDetalleRuta;
import com.example.caminalibre.fragmentos.Fragment_anadir_Rutas;
import com.example.caminalibre.fragmentos.Fragment_mostra_rutas;
import com.example.caminalibre.modelo.PuntoInteres;
import com.example.caminalibre.modelo.Ruta;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ActivityPrincipal extends AppCompatActivity {

    Fragment_mostra_rutas mostraRutas = new Fragment_mostra_rutas();
    Fragment_anadir_Rutas anadirRutas = new Fragment_anadir_Rutas();
    FragmentAcercaDe acercaDe = new FragmentAcercaDe();


    androidx.appcompat.widget.Toolbar toolbar;

//    private DrawerLayout drawerLayout;
//    private NavigationView nv_side;
//    private ActionBarDrawerToggle toggle;

    public void cambiarNombreToolbar(String nombre) {
        toolbar.setTitle(nombre);
    }
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
            //insertarpuntosprueba();
//        drawerLayout = findViewById(R.id.main_drawerlayout);
//        nv_side = findViewById(R.id.nav_view);
//        toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeButtonEnabled(true);
//        }

//        nv_side.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                int id = menuItem.getItemId();
//                if (id == R.id.snv_logout) {
//                    finishAffinity();
//                } else if (id == R.id.snv_home) {
////                    loadFragment(fir);
//                }
//                // Añadir más casos según el menú side_tabs.xml si es necesario
//                drawerLayout.closeDrawers();
//                return true;
//            }
//        });

        //ESTO ERA PARA EL MENÚ PERO ESTÁ MATANDO LA PILA DE FRAGMENTOS
//        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                if (drawerLayout.isDrawerOpen(nv_side)) {
//                    drawerLayout.closeDrawers();
//                } else {
//                    finishAffinity();
//                }
//            }
//        });

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.añadirruta) {
                loadFragment(anadirRutas, true);
                return true;
            } else if (id == R.id.vermisrutas) {
                // 1. Miramos qué fragmento hay puesto ahora mismo
                Fragment actual = getSupportFragmentManager().findFragmentById(R.id.frame_container);

                // 2. Solo cargamos "Mostrar Rutas" si NO es el que ya estamos viendo
                if (!(actual instanceof Fragment_mostra_rutas)) {
                    loadFragment(new Fragment_mostra_rutas(), false);
                }
                return true;
            } else if (id == R.id.ayuda) {
                ayuda();
                return true;
            } else if (id == R.id.sobre) {
                loadFragment(acercaDe, true);
                return true;
            } else if (id == R.id.salir) {
                finishAffinity();
                return true;
            }
            return false;
        });

        // cargamos un framneto inicial
        if (savedInstanceState == null) {
            loadFragment(mostraRutas,false);
        }

    }
    public void loadFragment(Fragment fragment, boolean backStack) {
        FragmentManager fm = getSupportFragmentManager();
        if (!backStack) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        if (backStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.anadirrutaToolbar) {
            loadFragment(anadirRutas, true);
        } else if (id == R.id.action_settings) {
            Toast.makeText(this, "Se ha pulsado ajustes", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            // evento de búsqueda
            if (searchView != null) {
                searchView.setQueryHint("Escribe el nombre de la ruta...");                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (mostraRutas !=null && mostraRutas.isAdded()){
                            mostraRutas.filtrarRuta(newText);
                        }
                        return true;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false; // No necesitamos acción al pulsar Enter
                    }
                });
            }
        }
        return true;
    }
    private void ayuda() {
        String url = "https://www.senderismo.net/consejos";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void cargarFragmentoDetalleRuta(Ruta ruta) {
        FragmentDetalleRuta rutaFragment = new FragmentDetalleRuta();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ruta", ruta);
        rutaFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, rutaFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void insertarpuntosprueba() {
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