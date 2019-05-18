package com.seas.a10.bizijorge.fragments;


import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.seas.a10.bizijorge.R;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class fRecorrido extends Fragment {

    //region Variables
    private Button btnStop;
    private Button btnEnd;
    private Button btnReset;
    private Chronometer crono  ;
    int isStoped = 0;
    int ini = 0;
    private long lastPause;

    TextView tvRecorridoTiempo;
    TextView tvRecorridoDistancia;
    TextView tvRecorridoContaminacion;
    TextView tvRecorridoCalorias;
    //endregion

    public fRecorrido() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_f_recorrido, container, false);

        btnStop = (Button) v.findViewById(R.id.btnStop);
        btnEnd = (Button) v.findViewById(R.id.btnEnd);
        btnReset = (Button) v.findViewById(R.id.btnReset);
        crono   = (Chronometer) v.findViewById(R.id.simpleChronometer);
        tvRecorridoTiempo = (TextView) v.findViewById(R.id.tvRecorridoTiempo);
        tvRecorridoDistancia = (TextView) v.findViewById(R.id.tvRecorridoDistancia);
        tvRecorridoContaminacion = (TextView) v.findViewById(R.id.tvRecorridoContaminacion);
        tvRecorridoCalorias = (TextView) v.findViewById(R.id.tvRecorridoCal);

        //Con este método configuramos el cronómetro añadiendo las horas
        crono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                cArg.setText(hh+":"+mm+":"+ss);
            }
        });
        crono.setBase(SystemClock.elapsedRealtime());


        //La primera vez iniciamos al pulsar, despues si pulsamos lo pausamos, y despues lo reanudamos volviendo a pulsar.
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ini == 0){
                    crono.setBase(SystemClock.elapsedRealtime());
                    crono.start();
                    ini = 1;

                }else{
                    if(isStoped == 1){
                        crono.setBase(crono.getBase() + SystemClock.elapsedRealtime() - lastPause);
                        crono.start();
                        isStoped = 0;
                    }else{
                        lastPause = SystemClock.elapsedRealtime();
                        crono.stop();

                        isStoped = 1;
                    }
                }

            }
        });

        //Reiniciamos el cronómetro dejando todos los valores por defecto
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crono.setBase(SystemClock.elapsedRealtime());
                ini = 0;
                isStoped = 0;
                crono.stop();
            }
        });


        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastPause = SystemClock.elapsedRealtime();
                crono.stop();
                isStoped = 1;

                try {


                    long millis = SystemClock.elapsedRealtime() - crono.getBase();
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


                    double dis = ((((double)millis / 1000.0)* 12.0)/ 3600.0)*1000.0;
                    //Teniendo en cuenta como velocidad media 12km/h
                    tvRecorridoDistancia.setText("Distancia recorrida: " + (int)dis + " metros");

                    //A partir de que un coche consume 17kg de co2 cada 100km --> 100m / 17g
                    int co = ((int)dis *17)/100;
                    tvRecorridoContaminacion.setText("CO2 no emitido: " + co + " gramos");

                    //Teniendo en cuenta que una persona de 65 kg gasta de media 6,4 calorías al minuto
                    double cal = (((double)millis/1000)*64)/1000;
                    tvRecorridoCalorias.setText("Calorías consumidas: " + new DecimalFormat("##.##").format(cal) + " calorías");



                }catch (Exception ex){
                    Toast.makeText(getContext(), "Error al guardar los datos del recorrido.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

}
