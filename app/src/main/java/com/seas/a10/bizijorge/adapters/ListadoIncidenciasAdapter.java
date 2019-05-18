package com.seas.a10.bizijorge.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seas.a10.bizijorge.MenuActivity;
import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.beans.Incidencia;
import com.seas.a10.bizijorge.data.sData;
import com.seas.a10.bizijorge.fragments.fDetallesIncidencia;
import com.seas.a10.bizijorge.fragments.fMap;

import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class ListadoIncidenciasAdapter extends RecyclerView.Adapter <ListadoIncidenciasAdapter.ListadoIncidenciasViewHolder> {

    //region Variables
    ArrayList<Incidencia> listadoIncidencias;


    //endregion

    //region Constructores
    public ListadoIncidenciasAdapter(ArrayList<Incidencia> listadoIncidencias) {

        this.listadoIncidencias = listadoIncidencias;
    }
    public ListadoIncidenciasAdapter(){

    }
    //endregion

    //Instanciamos ViewHolder y le inyectamos la vista de las incidencias
    @NonNull
    @Override
    public ListadoIncidenciasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_incidencia, parent, false);
        ListadoIncidenciasViewHolder livh = new ListadoIncidenciasViewHolder(itemView);

        return livh;
    }

    //Bindeamos cada una de las incidencias que tenemos en la lista
    @Override
    public void onBindViewHolder(@NonNull ListadoIncidenciasViewHolder holder, int position) {
        final Incidencia item = listadoIncidencias.get(position);
        holder.bindIncidencia(item);

        holder.asuntoIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sData.setIncidenciaDetalles(item);

                ((MenuActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, new fDetallesIncidencia(item))
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listadoIncidencias.size();
    }


    //Clase ViewHolder del adaptador de listado de incidencias
    public class ListadoIncidenciasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView asuntoIncidencia;
        private TextView fechaIncidencia;
        private TextView emailIncidencia;
        private TextView idIncidencia;


        public ListadoIncidenciasViewHolder(View itemView) {
            super(itemView);
            asuntoIncidencia = (TextView) itemView.findViewById(R.id.tvAsuntoIncidencia);
            fechaIncidencia = (TextView) itemView.findViewById(R.id.tvFechaIncidencia);
            emailIncidencia = (TextView) itemView.findViewById(R.id.tvEmailIncidencia);
            idIncidencia = (TextView) itemView.findViewById(R.id.tvIncidenciaId);
        }

        public void bindIncidencia (Incidencia c){
            try {

                Date date = c.getFechaIncidencia();
                String year = (String) DateFormat.format("yyyy", date);
                String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
                String day = (String) DateFormat.format("dd",   date);
                String hour = (String) DateFormat.format("HH:mm",   date);

                asuntoIncidencia.setText(c.getAsuntoIncidencia());
                fechaIncidencia.setText( hour + " " + dayOfTheWeek + " " + day + " " + year );
                emailIncidencia.setText(c.getUserEmailIncidencia());
                idIncidencia.setText("ID: "+ c.getIdIncidencia());
            }catch (Exception ex){

            }
            Log.d(TAG, "Error al bindear los datos de la incidencia");
        }



        @Override
        public void onClick(View view) {

        }
    }




}
