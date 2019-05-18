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

import com.seas.a10.bizijorge.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fRecorrido extends Fragment {

    //region Variables
    private Button btnStop;
    private Chronometer crono  ;
    int isStoped = 0;
    int ini = 0;
    private long lastPause;
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
        crono   = (Chronometer) v.findViewById(R.id.simpleChronometer);

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

        return v;
    }

}
