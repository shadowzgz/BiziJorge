package com.seas.a10.bizijorge.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.adapters.ListadoIncidenciasAdapter;
import com.seas.a10.bizijorge.beans.EstacionFavorita;
import com.seas.a10.bizijorge.beans.Incidencia;
import com.seas.a10.bizijorge.data.sData;
import com.seas.a10.bizijorge.utils.Post;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Fragmento con el que mostramos un listado con las incidencias de un usuario, o todas en caso de
 * ser el administrador
 */
public class ListadoIncidencias extends Fragment {

    //region variables
    ArrayList<Incidencia> listadoIncidencias;
    RecyclerView rvListadoIncidencias;
    private ListadoIncidenciasAdapter adapter;
    //endregion
    //region Constructores
    public ListadoIncidencias() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ListadoIncidencias(ArrayList<Incidencia> incidencias){
        listadoIncidencias = incidencias;
    }
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_listado_incidencias, container, false);
        rvListadoIncidencias = (RecyclerView) v.findViewById(R.id.rvListadoIncidencias);


        rvListadoIncidencias.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ListadoIncidenciasAdapter(listadoIncidencias);
        rvListadoIncidencias.setAdapter(adapter);
        return v;
    }



}
