package com.seas.a10.bizijorge.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.beans.Aviso;

import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class ListadoAvisosAdapter extends RecyclerView.Adapter <ListadoAvisosAdapter.ListadoAvisosViewHolder> {
    //region variables
    ArrayList<Aviso> listadoAvisos;
    //endregion

    //region constructores
    //constructor vacío
    public ListadoAvisosAdapter() {
    }


    public ListadoAvisosAdapter(ArrayList<Aviso> listadoAvisos) {
        this.listadoAvisos = listadoAvisos;
    }

    //endregion


    @NonNull
    @Override
    public ListadoAvisosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aviso, parent, false);
        ListadoAvisosViewHolder lavh = new ListadoAvisosViewHolder(itemView);

        return lavh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListadoAvisosViewHolder holder, int position) {
        final Aviso item = listadoAvisos.get(position);
        holder.bindAviso(item);
    }

    @Override
    public int getItemCount() {
        return listadoAvisos.size();
    }



    public class ListadoAvisosViewHolder extends RecyclerView.ViewHolder implements View

            .OnClickListener{

        TextView tvAvisoTitulo;
        TextView tvAvisoTexto;
        TextView tvAvisoFecha;
        ImageView imageAviso;

        public ListadoAvisosViewHolder(View itemView) {
            super(itemView);

            tvAvisoTitulo = (TextView) itemView.findViewById(R.id.tvAvisoItemTitulo);
            tvAvisoTexto = (TextView) itemView.findViewById(R.id.tvAvisoItemTexto);
            tvAvisoFecha = (TextView) itemView.findViewById(R.id.tvAvisoItemFecha);
            imageAviso = (ImageView) itemView.findViewById(R.id.imgAviso);
        }

        public void bindAviso(Aviso c){
            Date date = c.getAvisoFecha();
            
            try {
                String year = (String) DateFormat.format("yyyy", date);
                String month = (String) DateFormat.format("MM", date);
                //String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
                String day = (String) DateFormat.format("dd", date);
                String hour = (String) DateFormat.format("HH:mm", date);

                tvAvisoTitulo.setText("Aviso por " + c.getAvistoTipo().toLowerCase());
                tvAvisoTexto.setText(c.getAvisoTexto());
                tvAvisoFecha.setText(hour + " " + day + "/" + month + "/" + year);
                imageAviso.setBackgroundResource(R.drawable.aviso);
            }catch (Exception ex){
                Log.d(TAG, "bindAviso: Error al bindear los datos del aviso");
            }

        }

        @Override
        public void onClick(View view) {

        }
    }

}
