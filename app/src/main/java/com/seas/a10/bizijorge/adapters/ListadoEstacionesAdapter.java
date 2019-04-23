package com.seas.a10.bizijorge.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.beans.Estacion;

import java.util.ArrayList;

//Clase que muestra un listado con las diferentes estaciones
public class ListadoEstacionesAdapter extends  RecyclerView.Adapter <ListadoEstacionesAdapter.ListadoEstacionesViewHolder>{

    //region variables
    private ArrayList<Estacion> lisadoEstaciones;
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


    public class ListadoEstacionesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tituloEstacion;
        private TextView bicisEstacion;
        private TextView anclajesEstacion;
        private ImageView iconoEstacion;
        private ImageView favEstacion;

        public ListadoEstacionesViewHolder(View itemView){
            super(itemView);
            tituloEstacion = (TextView) itemView.findViewById(R.id.tituloEstacion);
            bicisEstacion = (TextView) itemView.findViewById(R.id.bicisEstacion);
            anclajesEstacion = (TextView) itemView.findViewById(R.id.anclajesEstacion);
            iconoEstacion = (ImageView) itemView.findViewById(R.id.imagenEstacion);
            favEstacion = (ImageView) itemView.findViewById(R.id.imagenFav);
        }

        public void bindEstacion (Estacion c){

            tituloEstacion.setText(c.getTitle());
            bicisEstacion.setText("Bicis disponibles: " + c.getBicisDisponibles());
            anclajesEstacion.setText("Anclajes disponibles: " + c.getAnclajesDisponibles());



        }


        @Override
        public void onClick(View view) {

        }
    }




}
