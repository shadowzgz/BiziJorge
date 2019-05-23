package com.seas.a10.bizijorge;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.seas.a10.bizijorge.beans.Cliente;
import com.seas.a10.bizijorge.data.sData;
import com.seas.a10.bizijorge.fragments.ListadoAvisos;
import com.seas.a10.bizijorge.fragments.ListadoEstaciones;
import com.seas.a10.bizijorge.fragments.fAvisoAdmin;
import com.seas.a10.bizijorge.fragments.fIncidencia;
import com.seas.a10.bizijorge.fragments.fMap;
import com.seas.a10.bizijorge.fragments.fRecorrido;
import com.seas.a10.bizijorge.fragments.first;


public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //region Variables
    Cliente cliente = sData.getCliente();
    private static Context context;
    private NavigationView navigationView;
    TextView name = null;
    TextView email = null;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*En esta sección del create isntanciamos el botón flotante.*/
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Creamos el navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Establecemos el nombre y usuario en el menú
        View header = navigationView.getHeaderView(0);
        name = (TextView)header.findViewById(R.id.tvNavMenuUsername);       ;
        email = (TextView) header.findViewById(R.id.tvNavMenuEmail);
       setClientDataOnMenu();
       initFragmentMap();
    }

    public void initFragmentMap() {
        try {
            fMap map = new fMap();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_content, map).commit();
            setTitle("Mapa");

        } catch (Exception ex) {
            Log.e("Exception: %s", ex.getMessage());
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            fMap map = new fMap();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_content , map).commit();
            setTitle("Mapa");
        } else if (id == R.id.nav_gallery) {
            if(sData.getListadoEstaciones() != null) {
                ListadoEstaciones listadoEstaciones = new ListadoEstaciones();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_content, listadoEstaciones).commit();
                setTitle("Listado de estaciones");
            }else{
                Toast.makeText(getApplicationContext(), "Actualmente no hay estaciones disponibles. Disculpe las molestias.",
                        Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.nav_slideshow) {
            if(sData.getCliente() != null && sData.getListadoEstacionesFavoritas() != null) {
                ListadoEstaciones listadoEstaciones = new ListadoEstaciones(1);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_content, listadoEstaciones).commit();
                setTitle("Estaciones favoritas");
            }else{
                Toast.makeText(getApplicationContext(), "Debes estar registrado o tener estaciones favoritas.",
                        Toast.LENGTH_LONG).show();
            }
        }
             if (id == R.id.nav_manage) {
            fIncidencia fragmentoIncidencia = new fIncidencia();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_content, fragmentoIncidencia).commit();
            setTitle("Incidencia");
        } else if (id == R.id.nav_share) {
            Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intentRegister);
        } else if (id == R.id.nav_send) {
            Intent intentRegister = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intentRegister);
        } else if (id == R.id.nav_chrono) {
                 fRecorrido reco = new fRecorrido();
                 FragmentManager fragmentManager = getSupportFragmentManager();
                 fragmentManager.beginTransaction().replace(R.id.main_content , reco).commit();
                 setTitle("Recorrido");
             }else if (id == R.id.nav_log_out) {
                 if(cliente != null || sData.getCliente() != null) {
                     cliente = null;
                     sData.setCliente(null);
                     name.setText("Usuario sin identificar");
                     email.setText("");
                     Intent intentRegister = new Intent(getApplicationContext(), LoginActivity.class);
                     startActivity(intentRegister);
                 }else{
                     Toast.makeText(getApplicationContext(), "No hay ningún usuario.", Toast.LENGTH_SHORT).show();
                 }
             } else if (id == R.id.nav_aviso) {
        ListadoAvisos aviso = new ListadoAvisos();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content , aviso).commit();
        setTitle("Avisos");
    }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setClientDataOnMenu(){

        if(cliente != null){
            name.setText(cliente.getName().toString());
            email.setText(cliente.getEmail().toString());
        } else {
            name.setText("Usuario sin identificar");
            email.setText("");
        }


    }



}
