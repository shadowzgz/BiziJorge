package com.seas.a10.bizijorge.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.data.sData;
import com.seas.a10.bizijorge.utils.Post;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class fRecorrido extends Fragment {

    //region Variables
    private Button btnStop;
    private Button btnEnd;
    private Button btnReset;
    Button btnGuardarRecorrido;
    private Chronometer crono  ;
    int isStoped = 0;
    int ini = 0;
    private long lastPause;
    private long tiempoRecorrido;
    private double distanciaRecorrida;
    private double contaminacion;
    private double calorias;
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
        btnGuardarRecorrido = (Button) v.findViewById(R.id.btnGuardarRecorrido);
        crono   = (Chronometer) v.findViewById(R.id.simpleChronometer);
        tvRecorridoTiempo = (TextView) v.findViewById(R.id.tvRecorridoTiempo);
        tvRecorridoDistancia = (TextView) v.findViewById(R.id.tvRecorridoDistancia);
        tvRecorridoContaminacion = (TextView) v.findViewById(R.id.tvRecorridoContaminacion);
        tvRecorridoCalorias = (TextView) v.findViewById(R.id.tvRecorridoCal);
        btnGuardarRecorrido.setVisibility(View.INVISIBLE);
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


        //region Botón Stop
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
        //endregion

        //region Botón Reset
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
        //endregion

        //region Botón End
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


                    distanciaRecorrida = ((((double)millis / 1000.0)* 12.0)/ 3600.0)*1000.0;
                    //Teniendo en cuenta como velocidad media 12km/h
                    tvRecorridoDistancia.setText("Distancia recorrida: " + (int)distanciaRecorrida + " metros");

                    //A partir de que un coche consume 17kg de co2 cada 100km --> 100m / 17g
                    contaminacion = ((int)distanciaRecorrida *17)/100;
                    tvRecorridoContaminacion.setText("CO2 no emitido: " + contaminacion + " gramos");

                    //Teniendo en cuenta que una persona de 65 kg gasta de media 6,4 calorías al minuto
                    calorias = (((double)millis/1000)*64)/1000;
                    tvRecorridoCalorias.setText("Calorías consumidas: " + new DecimalFormat("##.##").format(calorias) + " calorías");

                    tiempoRecorrido = millis;
                    btnGuardarRecorrido.setVisibility(View.VISIBLE);
                }catch (Exception ex){
                    Toast.makeText(getContext(), "Error al guardar los datos del recorrido.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //endregion

        btnGuardarRecorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat time = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
                    String formattedDate = time.format(c);

                    if (sData.getCliente() != null) {


                        HashMap<String, String> parametros = new HashMap<String, String>();
                        parametros.put("Action", "Recorrido.add");
                        parametros.put("ID_USUARIO", "" + sData.getCliente().getIdUsuario());
                        parametros.put("RecorridoTiempo", "" + tiempoRecorrido);
                        parametros.put("RecorridoDistancia", "" + distanciaRecorrida);
                        parametros.put("RecorridoContaminacion", "" + contaminacion);
                        parametros.put("RecorridoCalorias", "" + calorias);
                        parametros.put("RecorridoFecha", formattedDate);

                        TareaSegundoPlano tarea = new TareaSegundoPlano(parametros);
                        tarea.execute("http://jgarcia.x10host.com/Controller.php");
                    }else{
                        Toast.makeText(getContext(), "Debes estar registrado para guardar un recorrido.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(getContext(), "Se ha producido un error al guardar el recorrido.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return v;
    }

    //Hilo en segundo plano para guardar en la base de datos la incidencia
    class TareaSegundoPlano extends AsyncTask<String, Integer, Boolean> {

        private ProgressDialog progressDialog = new ProgressDialog(getContext());
        private HashMap<String, String> parametros = null;


        public TareaSegundoPlano(HashMap<String, String> parametros) {
            this.parametros = parametros;
        }

        /*
         * onPreExecute().
         *  Se ejecutará antes del código principal de nuestra tarea.
         * Se suele utilizar para preparar la ejecución de la tarea, inicializar la interfaz, etc.
         * */
        @Override
        protected void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Procesando...");
            progressDialog.setCancelable(true);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    TareaSegundoPlano.this.cancel(true);
                }
            });


        }

        /*
         * doInBackground().
         * Contendrá el código principal de nuestra tarea.
         * */
        @Override
        protected Boolean doInBackground(String... params) {
            String url_select = params[0];
            publishProgress(0);
            try {
                Post post = new Post();
                publishProgress(50);
                post.registerUser(parametros, url_select);
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                //messageUser = "Error al conectar con el servidor. ";
            }
            publishProgress(100);
            return true;
        }

        /* Acualizamos el progreso de la aplicación*/
        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            progressDialog.setProgress(progreso);
        }

        /*
         * onPostExecute().
         * Se ejecutará cuando finalice nuestra tarea, o dicho de otra forma,
         * tras la finalización del método doInBackground().
         * */
        @Override
        protected void onPostExecute(Boolean resp) {
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Toast.makeText(getContext(), "Recorrido guardado satisfactoriamente.", Toast.LENGTH_SHORT).show();

        }
    }


}
