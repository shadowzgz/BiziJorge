package com.seas.a10.bizijorge.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.beans.Mensaje;

import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class ListadoMensajesAdapter extends RecyclerView.Adapter <ListadoMensajesAdapter.ListadoMensajesViewHolder> {

    //region variables

    ArrayList<Mensaje> listadoMensajes;

    //endregion

    //region constructores

    public ListadoMensajesAdapter(ArrayList<Mensaje> listadoMensajes) {
        this.listadoMensajes = listadoMensajes;
    }

    //endregion

    //Instanciamos el ViewHolder y le inyectamos la vista delas incidencias
    @NonNull
    @Override
    public ListadoMensajesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mensaje, parent, false);
        ListadoMensajesViewHolder lmvh = new ListadoMensajesViewHolder(itemView);

        return lmvh;

    }

    //Bindeamos cada uno de los mensajes que tenemos en la lista
    @Override
    public void onBindViewHolder(@NonNull ListadoMensajesViewHolder holder, int position) {
        final Mensaje item = listadoMensajes.get(position);
        holder.bindMensaje(item);
    }

    @Override
    public int getItemCount() {
        return listadoMensajes.size();
    }

    public class ListadoMensajesViewHolder extends RecyclerView.ViewHolder implements View

        .OnClickListener{

    TextView tvMensajeEmail;
    TextView tvMensajeFecha;
    TextView tvMensajeTexto;

    public ListadoMensajesViewHolder(View itemView) {
        super(itemView);

        tvMensajeEmail = (TextView) itemView.findViewById(R.id.tvMensajeEmail);
        tvMensajeFecha = (TextView) itemView.findViewById(R.id.tvMensajeFecha);
        tvMensajeTexto = (TextView) itemView.findViewById(R.id.tvMensajeTexto);

    }

    public void bindMensaje (Mensaje c){

        try{
            Date date = c.getMensajeFecha();
            String year = (String) DateFormat.format("yyyy", date);
            String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
            String day = (String) DateFormat.format("dd",   date);
            String hour = (String) DateFormat.format("HH:mm",   date);

            tvMensajeEmail.setText(c.getMensajeEmail());
            tvMensajeFecha.setText(hour + " " + dayOfTheWeek + " " + day + " " + year);
            tvMensajeTexto.setText(c.getMensajeTexto());


        }catch (Exception ex){
            Log.d(TAG, "Error al bindear los datos del mensaje");
        }



    }

    @Override
    public void onClick(View view) {

    }
}


}
