package com.example.caminalibre.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.caminalibre.R;
import com.example.caminalibre.fragmentos.FragmentDetalleRuta;
import com.example.caminalibre.fragmentos.Fragment_mostra_rutas;
import com.example.caminalibre.modelo.Ruta;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class ActivityPrincipal extends AppCompatActivity {

    Fragment_mostra_rutas mostraRutas = new Fragment_mostra_rutas();

    private DrawerLayout drawerLayout;
    private NavigationView nv_side;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_drawerlayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        drawerLayout = findViewById(R.id.main_drawerlayout);
//        nv_side = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

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


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.añadirruta) {
//                loadFragment(añadirRuta);
                return true;
            } else if (id == R.id.vermisrutas) {
                loadFragment(mostraRutas);
                return true;
            } else if (id == R.id.ayuda) {
                ayuda();
                return true;
            } else if (id == R.id.sobre) {
//                loadFragment(sobre);
                return true;
            } else if (id == R.id.salir) {
                finishAffinity();
                return true;
            }
            return false;
        });

        // cargamos un framneto inicial
        if (savedInstanceState == null) {
            loadFragment(mostraRutas);
        }

    }
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Añade aquí el manejo de tus nuevos botones
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Ajustes seleccionados", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_search) {
            Toast.makeText(this, "Buscando...", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
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
}