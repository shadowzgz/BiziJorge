package com.seas.a10.bizijorge.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.adapters.ListadoEstacionesAdapter;
import com.seas.a10.bizijorge.beans.Estacion;
import com.seas.a10.bizijorge.data.sData;

import java.util.ArrayList;
import java.util.Comparator;


public class ListadoEstaciones extends Fragment{


    RecyclerView rvPelicula;
    private ListadoEstacionesAdapter adapter;
    EditText search;

    public ListadoEstaciones() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_listado_estaciones, container, false);

        search = v.findViewById(R.id.searchEstacion);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });




        rvPelicula = (RecyclerView) v.findViewById(R.id.rvListadoEstaciones);
        rvPelicula.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ListadoEstacionesAdapter(sData.getListadoEstaciones(), getContext());
        rvPelicula.setAdapter(adapter);

        return v;
    }


    private void filter(String text){
        ArrayList<Estacion> listaFiltrada = new ArrayList<Estacion>();

        for(Estacion i : sData.getListadoEstaciones()){
            if(i.getTitle().toLowerCase().contains(text.toLowerCase())){
                listaFiltrada.add(i);
            }
        }
        adapter.filterList(listaFiltrada);
    }


}