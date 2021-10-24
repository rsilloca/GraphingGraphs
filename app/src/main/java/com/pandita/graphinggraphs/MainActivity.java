package com.pandita.graphinggraphs;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Button salir, continuar, aceptar;
    AlertDialog dialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        toolbar.setTitle("Graphing Graphs");
        transaction.replace(R.id.managerFragments, new MainFragment());
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View miView = getLayoutInflater().inflate(R.layout.alert_finish_app, null);
            salir = miView.findViewById(R.id.salir);
            salir.setOnClickListener(this);
            continuar = miView.findViewById(R.id.continuar);
            continuar.setOnClickListener(this);
            builder.setView(miView);
            dialog = builder.create();
            dialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View miView = getLayoutInflater().inflate(R.layout.alert_help_information, null);
            aceptar = miView.findViewById(R.id.aceptar);
            aceptar.setOnClickListener(this);
            builder.setView(miView);
            dialog = builder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (id == R.id.nav_home) {
            toolbar.setTitle("Graphing Graphs");
            transaction.replace(R.id.managerFragments, new MainFragment());
        } else if (id == R.id.nav_insert_matrix) {
            toolbar.setTitle("Generar Grafo");
            transaction.replace(R.id.managerFragments, new InsertMatrix());
        } else if (id == R.id.nav_draw_graph) {
            toolbar.setTitle("Generar Matriz");
            transaction.replace(R.id.managerFragments, new DrawGraph());
        } else if (id == R.id.nav_dijkstra) {
            toolbar.setTitle("Algoritmo de Dijkstra");
            transaction.replace(R.id.managerFragments, new DijkstraAlgorithm());
        } else if (id == R.id.nav_arbol_generador) {
            toolbar.setTitle("Arboles Generadores");
            transaction.replace(R.id.managerFragments, new GenerateTree());
        } else if (id == R.id.nav_arbol_generador_minimal) {
            toolbar.setTitle("Arboles Minimales");
            transaction.replace(R.id.managerFragments, new GenerateMinimalTree());
        } else if (id == R.id.nav_settings) {
            // En proceso
        } else if (id == R.id.nav_share) {
            // En proceso
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        transaction.commit();
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == salir){
            super.finish();
        } else if (v == continuar){
            dialog.cancel();
        } else if (v == aceptar) {
            dialog.cancel();
        }
    }
}

