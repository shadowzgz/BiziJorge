package com.seas.a10.bizijorge.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.seas.a10.bizijorge.MenuActivity;
import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.adapters.ListadoRecorridosAdapter;
import com.seas.a10.bizijorge.beans.Incidencia;
import com.seas.a10.bizijorge.beans.Recorrido;
import com.seas.a10.bizijorge.data.sData;
import com.seas.a10.bizijorge.utils.Post;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class fRecorrido extends Fragment {

    //region Variables
    RecyclerView rvListadoRecorridos;
    private Button btnStop;
    private Button btnEnd;
    private Button btnReset;
    private Button btnSumatorio;
    ListadoRecorridosAdapter adapter;
    Button btnGuardarRecorrido;
    private Chronometer crono  ;
    int isStoped = 0;
    int ini = 0;
    int guardado = 0;
    private long lastPause;
    private long tiempoRecorrido;
    private double distanciaRecorrida;
    private double contaminacion;
    private double calorias;
    TextView tvRecorridoTiempo;
    TextView tvRecorridoDistancia;
    TextView tvRecorridoContaminacion;
    TextView tvRecorridoCalorias;
    ArrayList<Recorrido> listadoRecorridos = new ArrayList<Recorrido>();
    //endregion

    public fRecorrido() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_f_recorrido, container, false);

        rvListadoRecorridos = (RecyclerView) v.findViewById(R.id.rvListadoRecorridos);
        btnStop = (Button) v.findViewById(R.id.btnStop);
        btnEnd = (Button) v.findViewById(R.id.btnEnd);
        btnReset = (Button) v.findViewById(R.id.btnReset);
        btnGuardarRecorrido = (Button) v.findViewById(R.id.btnGuardarRecorrido);
        crono   = (Chronometer) v.findViewById(R.id.simpleChronometer);
        tvRecorridoTiempo = (TextView) v.findViewById(R.id.tvRecorridoTiempo);
        tvRecorridoDistancia = (TextView) v.findViewById(R.id.tvRecorridoDistancia);
        tvRecorridoContaminacion = (TextView) v.findViewById(R.id.tvRecorridoContaminacion);
        tvRecorridoCalorias = (TextView) v.findViewById(R.id.tvRecorridoCal);
        btnSumatorio = (Button) v.findViewById(R.id.btnSumatorioRecorridos);
        btnGuardarRecorrido.setVisibility(View.INVISIBLE);

        getRecorridos();

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

        //region Botón Guardar
        btnGuardarRecorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (guardado == 0) {
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
                            guardado = 1;


                        } else {
                            Toast.makeText(getContext(), "Debes estar registrado para guardar un recorrido.", Toast.LENGTH_SHORT).show();
                        }
                    }   else{
                        Toast.makeText(getContext(), "Ya se ha guardado en la base de datos.", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception ex){
                    Toast.makeText(getContext(), "Se ha producido un error al guardar el recorrido.", Toast.LENGTH_SHORT).show();
                }

                try{
                    ((MenuActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, new fRecorrido())
                            .commit();


                }catch (Exception ex){
                    Log.d(TAG, "onClick: Error al tratar de actualizar el fragmento.");
                }


            }
        });
        //endregion

        //region Botón sumatorio
        btnSumatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double distancia = 0;
                double co2 = 0;
                double cal = 0;
                long tiempo = 0;
                if(listadoRecorridos.size() > 0) {
                    for (Recorrido i : listadoRecorridos) {
                        distancia = distancia + i.getRecorridoDistancia();
                        co2 = co2 + i.getRecorridoCo2();
                        cal = cal + i.getRecorridoCalorias();
                        tiempo = tiempo + i.getRecorridoTiempo();
                    }

                    long hours = TimeUnit.MILLISECONDS.toHours(tiempo);
                    tiempo -= TimeUnit.HOURS.toMillis(hours);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(tiempo);
                    tiempo -= TimeUnit.MINUTES.toMillis(minutes);
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(tiempo);
                    StringBuilder sb = new StringBuilder(64);
                    sb.append(hours);
                    sb.append(" Horas ");
                    sb.append(minutes);
                    sb.append(" Minutos ");
                    sb.append(seconds);
                    sb.append(" Segundos");

                    Toast.makeText(getContext(), "Tiempo total: " + sb + "\n" +
                            "Distancia total: " + (int)distancia + " metros" + "\n" +
                            "Co2 no emitido total: " + co2 + " gramos" + "\n" +
                            "Calorías consumidas totales: " + new DecimalFormat("##.##").format(cal) + " calorías" , Toast.LENGTH_LONG).show();


                }else {
                    Toast.makeText(getContext(), "No tienes recorridos...", Toast.LENGTH_SHORT).show();

                }
            }
        });
        //endregion

        //Si tenemos algún recorrido en la lista, instanciamos el RecyclerView
        if(listadoRecorridos.size() > 0) {
            rvListadoRecorridos.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new ListadoRecorridosAdapter(listadoRecorridos);
            rvListadoRecorridos.setAdapter(adapter);
        }

        return v;
    }

    public void getRecorridos(){

        try{
            HashMap<String, String> parametros = new HashMap<String, String>();
            parametros.put("Action", "Recorrido.select");
            parametros.put("ID_USUARIO", "" + sData.getCliente().getIdUsuario());

            TareaSegundoPlanoSelectRecorridos tarea = new TareaSegundoPlanoSelectRecorridos(parametros);
            tarea.execute("http://jgarcia.x10host.com/Controller.php").get();
        }catch (Exception ex){
            Toast.makeText(getContext(), "Error al recibir los datos de los recorridos", Toast.LENGTH_SHORT).show();
        }

    }

    //Hilo en segundo plano para guardar en la base de datos del recorrido
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
            //Toast.makeText(getContext(), "Recorrido guardado satisfactoriamente.", Toast.LENGTH_SHORT).show();

        }
    }

    class TareaSegundoPlanoSelectRecorridos extends AsyncTask<String, Integer, Boolean> {

        private ProgressDialog progressDialog = new ProgressDialog(getContext());
        private HashMap<String, String> parametros = null;


        public TareaSegundoPlanoSelectRecorridos(HashMap<String, String> parametros) {
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
                    TareaSegundoPlanoSelectRecorridos.this.cancel(true);
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
                JSONArray result = post.getServerDataPost(parametros, url_select);
                if(result != null) {
                    listadoRecorridos = Recorrido.getArrayListFromJSon(result);
                    sData.setListadoRecorridos(listadoRecorridos);
                }else{
                    listadoRecorridos = null;
                    sData.setListadoRecorridos(null);
                }

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
            //Toast.makeText(RegisterActivity.getInstance().getBaseContext(), "Registro realizado satisfactoriamente", Toast.LENGTH_SHORT).show();
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }


}
