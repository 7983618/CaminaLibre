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
import androidx.fragment.app.FragmentTransaction;

import com.example.caminalibre.R;
import com.example.caminalibre.fragmentos.FragmentAcercaDe;
import com.example.caminalibre.fragmentos.FragmentDetalle;
import com.example.caminalibre.fragmentos.FragmentAltas;
import com.example.caminalibre.fragmentos.FragmentRutas;
import com.example.caminalibre.modelo.Ruta;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityPrincipal extends AppCompatActivity {

    FragmentRutas mostraRutas = new FragmentRutas();
    FragmentAltas anadirRutas = new FragmentAltas();
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
                loadFragment(mostraRutas, false);
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
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
        FragmentDetalle rutaFragment = new FragmentDetalle();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ruta", ruta);
        rutaFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, rutaFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}