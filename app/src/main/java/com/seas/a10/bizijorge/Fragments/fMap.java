package com.seas.a10.bizijorge.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.seas.a10.bizijorge.R;

/**
 * Fragmento que contiene el mapa en el que se muestran todas las estaciones de Bizi Zaragoza
 *
 * https://www.zaragoza.es/sede/servicio/urbanismo-infraestructuras/estacion-bicicleta?rf=html&srsname=wgs84&start=0&point=-0.8774209607549572%2C41.65599799116259&distance=5000
 */
public class fMap extends Fragment {


    MapView mMapView;
    private GoogleMap googleMap;
//    SupportMapFragment supportMapFragment;
    public fMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_f_map, container, false);
//        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fMap);
//        if(supportMapFragment == null) {
//            FragmentManager fm = getFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            supportMapFragment = SupportMapFragment.newInstance();
//            ft.replace(R.id.fMap,supportMapFragment).commit();
//        }
//        supportMapFragment.getMapAsync(this);
        mMapView = v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

//        Cargamos el mapa en el momento.
        mMapView.onResume();

//



        return v;
    }


}
