package com.seas.a10.bizijorge.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.beans.Estacion;
import com.seas.a10.bizijorge.fragments.ListadoEstaciones;

import java.util.ArrayList;
import java.util.Comparator;

//Clase que muestra un listado con las diferentes estaciones
public class ListadoEstacionesAdapter extends  RecyclerView.Adapter <ListadoEstacionesAdapter.ListadoEstacionesViewHolder>{

    //region variables
    private ArrayList<Estacion> lisadoEstaciones;
    private ArrayList<Estacion> lisadoEstacionesFilter;
    private Context context;

    //endregion

    //Contructor de la clase
    public ListadoEstacionesAdapter(ArrayList<Estacion> lisadoEstaciones, Context context) {
        this.lisadoEstaciones = lisadoEstaciones;
        this.context = context;
    }

    @NonNull
    @Override
    public ListadoEstacionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_estacion, parent, false);
        ListadoEstacionesViewHolder levh = new ListadoEstacionesViewHolder(itemView);

        return levh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListadoEstacionesViewHolder holder, int position) {
        final Estacion item = lisadoEstaciones.get(position);
        holder.bindEstacion(item);
        holder.tituloEstacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(context, "Pulsación", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lisadoEstaciones.size();
    }

    public void filterList(ArrayList<Estacion> filterList){
        lisadoEstaciones = filterList;
        notifyDataSetChanged();
    }

    public class ListadoEstacionesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tituloEstacion;
        private TextView bicisEstacion;
        private TextView anclajesEstacion;
        private ImageView iconoEstacionBicis;
        private ImageView iconoEstacionAnclajes;
        private ImageView favEstacion;

        public ListadoEstacionesViewHolder(View itemView){
            super(itemView);
            tituloEstacion = (TextView) itemView.findViewById(R.id.tituloEstacion);
            bicisEstacion = (TextView) itemView.findViewById(R.id.bicisEstacion);
            anclajesEstacion = (TextView) itemView.findViewById(R.id.anclajesEstacion);
            iconoEstacionBicis = (ImageView) itemView.findViewById(R.id.imagenEstacionBicis);
            iconoEstacionAnclajes = (ImageView) itemView.findViewById(R.id.imagenEstacionAnclajes);
            favEstacion = (ImageView) itemView.findViewById(R.id.imagenFav);
        }

        public void bindEstacion (Estacion c){

            tituloEstacion.setText(c.getTitle());
            bicisEstacion.setText("Bicis disponibles: " + c.getBicisDisponibles());
            anclajesEstacion.setText("Anclajes disponibles: " + c.getAnclajesDisponibles());

            if(c.getBicisDisponibles() <= 0){
                iconoEstacionBicis.setBackgroundResource(R.drawable.marcadorbicirojo);
            }else if(c.getBicisDisponibles() <= 4){
                iconoEstacionBicis.setBackgroundResource(R.drawable.marcadorbicinaranja);
            }else if(c.getBicisDisponibles() >= 5){
                iconoEstacionBicis.setBackgroundResource(R.drawable.marcadorbiciverde);
            }else{
                iconoEstacionBicis.setBackgroundResource(R.drawable.marcadorfueraservicio);
            }

            if(c.getAnclajesDisponibles() <= 0){
                iconoEstacionAnclajes.setBackgroundResource(R.drawable.marcadoranclajerojo);
            }else if(c.getAnclajesDisponibles() <= 4){
                iconoEstacionAnclajes.setBackgroundResource(R.drawable.marcadoranclajenaranja);
            }else if(c.getAnclajesDisponibles() >= 5){
                iconoEstacionAnclajes.setBackgroundResource(R.drawable.marcadoranclajeverde);
            }else{
                iconoEstacionAnclajes.setBackgroundResource(R.drawable.marcadorfueraservicio);
            }

            favEstacion.setBackgroundResource(R.drawable.notfav);


        }



        @Override
        public void onClick(View view) {

        }
    }




}
