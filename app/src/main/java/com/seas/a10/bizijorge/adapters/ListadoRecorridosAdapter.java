package com.seas.a10.bizijorge.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.beans.Recorrido;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class ListadoRecorridosAdapter  extends RecyclerView.Adapter <ListadoRecorridosAdapter.ListadoRecorridoViewHolder>{

    //region Variables
    ArrayList<Recorrido> listadoRecorridos;
    //endregion

    //region Constructores
    public ListadoRecorridosAdapter(ArrayList<Recorrido> listadoRecorridos) {
        this.listadoRecorridos = listadoRecorridos;
    }

    @NonNull
    @Override
    public ListadoRecorridoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recorrido, parent, false);
        ListadoRecorridoViewHolder lrvh = new ListadoRecorridoViewHolder(itemView);
        return lrvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListadoRecorridoViewHolder holder, int position) {
        final Recorrido item = listadoRecorridos.get(position);
        holder.bindRecorrido(item);
    }

    @Override
    public int getItemCount() {
        return listadoRecorridos.size();
    }
    //endregion


    public class ListadoRecorridoViewHolder extends RecyclerView.ViewHolder implements View

            .OnClickListener{
        TextView tvRecorridoFecha;
        TextView tvRecorridoTiempo;
        TextView tvRecorridoDistancia;
        TextView tvRecorridoCo2;
        TextView tvRecorridoCalorias;


        public ListadoRecorridoViewHolder(View itemView) {
            super(itemView);
            tvRecorridoFecha = (TextView) itemView.findViewById(R.id.tvRecorridoFecha);
            tvRecorridoTiempo = (TextView) itemView.findViewById(R.id.tvRecorridoTiempo);
            tvRecorridoDistancia = (TextView) itemView.findViewById(R.id.tvRecorridoDistancia);
            tvRecorridoCo2 = (TextView) itemView.findViewById(R.id.tvRecorridoCo2);
            tvRecorridoCalorias = (TextView) itemView.findViewById(R.id.tvRecorridoCal);

        }

        public void bindRecorrido (Recorrido c){
            try {
                Date date = c.getRecorridoFecha();
                String year = (String) DateFormat.format("yyyy", date);
                String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
                String day = (String) DateFormat.format("dd", date);
                String hour = (String) DateFormat.format("HH:mm", date);
                tvRecorridoFecha.setText("Fecha: " + hour + " " + dayOfTheWeek + " " + day + " " + year);

                long millis = c.getRecorridoTiempo();
                long hours = TimeUnit.MILLISECONDS.toHours(millis);
                millis -= TimeUnit.HOURS.toMillis(hours);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
                millis -= TimeUnit.MINUTES.toMillis(minutes);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
                StringBuilder sb = new StringBuilder(64);
                sb.append(hours);
                sb.append(" Horas ");
                sb.append(minutes);
                sb.append(" Minutos ");
                sb.append(seconds);
                sb.append(" Segundos");
                tvRecorridoTiempo.setText("Tiempo de recorrido: " + sb);

                tvRecorridoDistancia.setText("Distancia recorrida: " + (int)c.getRecorridoDistancia() + " metros");
                tvRecorridoCo2.setText("Co2 no emitido: " + c.getRecorridoCo2() + " gramos");
                tvRecorridoCalorias.setText("Calorias consumidas: " + new DecimalFormat("##.##").format(c.getRecorridoCalorias()) + "calorías");

            }catch (Exception ex){
                Log.d(TAG, "bindRecorrido: Error al bindear los datos.");
            }
        }

        @Override
        public void onClick(View view) {

        }
    }
}
