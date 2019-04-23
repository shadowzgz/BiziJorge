package com.seas.a10.bizijorge.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.adapters.ListadoEstacionesAdapter;
import com.seas.a10.bizijorge.data.sData;


public class ListadoEstaciones extends Fragment {


    RecyclerView rvPelicula;


    public ListadoEstaciones() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_listado_estaciones, container, false);

        rvPelicula = (RecyclerView) v.findViewById(R.id.rvListadoEstaciones);
        rvPelicula.setLayoutManager(new LinearLayoutManager(getContext()));

        ListadoEstacionesAdapter adapter = new ListadoEstacionesAdapter(sData.getListadoEstaciones(), getContext());
        rvPelicula.setAdapter(adapter);

        return v;
    }





}