package com.seas.a10.bizijorge.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.beans.Estacion;

import java.util.ArrayList;

public class CustomWindowInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;
    private ArrayList<Estacion> listadoEstaciones;

    public CustomWindowInfoAdapter(Context context, ArrayList<Estacion> le) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.marker_info_window, null);
        listadoEstaciones = le;
    }

    private void setData(Marker marker, View view){
        try {


            int id = Integer.parseInt(marker.getTitle());
            Estacion estacion = null;
            for (Estacion i : listadoEstaciones) {
                if (i.getId() == id) {
                    estacion = i;
                }
            }
            TextView tittle = (TextView) view.findViewById(R.id.iwTittle);
            tittle.setText(estacion.getTitle());

            TextView bicisDisponiblesTitulo = (TextView) view.findViewById(R.id.txtBicisDisponiles);
            bicisDisponiblesTitulo.setText("Bicis disponibles: ");
            TextView bicisDisponibles = (TextView) view.findViewById(R.id.txtBicisDisponilesNum);
            bicisDisponibles.setText("" + estacion.getBicisDisponibles());

            TextView anclajesDisponiblesTitulo = (TextView) view.findViewById(R.id.txtAnclajesDisponiles);
            anclajesDisponiblesTitulo.setText("Anclajes disponibles: ");
            TextView anclajesDisponibles = (TextView) view.findViewById(R.id.txtAnclajesDisponiblesNum);
            anclajesDisponibles.setText("" + estacion.getAnclajesDisponibles());


        }catch (Exception ex){
            Log.e("Exception: %s", ex.getMessage());
        }



    }

    @Override
    public View getInfoWindow(Marker marker) {
        setData(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
