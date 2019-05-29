package com.seas.a10.bizijorge.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seas.a10.bizijorge.LoginActivity;
import com.seas.a10.bizijorge.MenuActivity;
import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.RegisterActivity;
import com.seas.a10.bizijorge.adapters.ListadoIncidenciasAdapter;
import com.seas.a10.bizijorge.adapters.ListadoMensajesAdapter;
import com.seas.a10.bizijorge.beans.Incidencia;
import com.seas.a10.bizijorge.beans.Mensaje;
import com.seas.a10.bizijorge.data.sData;
import com.seas.a10.bizijorge.utils.Post;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Fragmento en el que mostramos los detalles de una incidencia.
 */
public class fDetallesIncidencia extends Fragment {

    //region Variables
    Incidencia incidencia;
    TextView tvDetallesAsunto;
    TextView tvDetallesCorreo;
    TextView tvDetallesTexto;
    TextView tvDetallesFecha;
    EditText etNuevoMensaje;
    Button btnEliminarIncidencia;
    Button btnEnviarMensaje;
    RecyclerView rvMensajes;
    ListadoMensajesAdapter adapter;
    ArrayList<Mensaje> listadoMensajes;
    //endregion

    //region Constructores
    public fDetallesIncidencia() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public fDetallesIncidencia(Incidencia incidencia) {
        this.incidencia = incidencia;
    }

    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_f_detalles_incidencia, container, false);

        tvDetallesAsunto = (TextView) v.findViewById(R.id.tvDetallesAsunto);
        tvDetallesCorreo = (TextView) v.findViewById(R.id.tvDetallesCorreo);
        tvDetallesTexto = (TextView) v.findViewById(R.id.tvDetallesTexto);
        tvDetallesFecha = (TextView) v.findViewById(R.id.tvDetallesFecha);
        btnEliminarIncidencia = (Button) v.findViewById(R.id.btnEliminarIncidencia);
        btnEnviarMensaje = (Button) v.findViewById(R.id.btnDetallesMandarMensaje);
        etNuevoMensaje = (EditText) v.findViewById(R.id.textoIncidencia);
        rvMensajes = (RecyclerView) v.findViewById(R.id.rvListadoMensajes);

        //Si el admin está logueado se hace visible el botón
        if(sData.getCliente().getIdUsuario() == 10){
            btnEliminarIncidencia.setVisibility(View.VISIBLE);
        }

        tvDetallesTexto.setText("Texto: " + "\n" + incidencia.getAsuntoIncidencia());
        tvDetallesCorreo.setText("Usuario: " + incidencia.getUserEmailIncidencia());
        tvDetallesAsunto.setText("Asunto: " + incidencia.getTextoIncidencia());

        try {
            Date date = incidencia.getFechaIncidencia();
            String year = (String) DateFormat.format("yyyy", date);
            String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
            String day = (String) DateFormat.format("dd",   date);
            String hour = (String) DateFormat.format("HH:mm",   date);
            tvDetallesFecha.setText("Fecha: " + hour + " " + dayOfTheWeek + " " + day + " " + year );
        }catch (Exception ex){
            tvDetallesFecha.setText("Fecha: No se ha encontrado la fecha...");
        }

        btnEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat time =  new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
                    String formattedDate = time.format(c);

                    if (TextUtils.isEmpty(etNuevoMensaje.getText().toString()) == false) {

                        HashMap<String, String> parametros = new HashMap<String, String>();
                        parametros.put("Action", "Mensaje.add");
                        parametros.put("mensajeTexto", etNuevoMensaje.getText().toString());
                        parametros.put("mensajeFecha", formattedDate);
                        parametros.put("mensajeEmail", sData.getCliente().getEmail());
                        parametros.put("userId", "" + sData.getCliente().getIdUsuario());
                        parametros.put("incidenciaId", "" + incidencia.getIdIncidencia());

                        GuardarMensajeAsync tarea = new GuardarMensajeAsync(parametros);
                        tarea.execute("http://jgarcia.x10host.com/Controller.php");
                    } else {
                        Toast.makeText(getContext(), "Tienes que escribir algo en el mensaje.", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception ex){

                }

                try{
                    ((MenuActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, new fDetallesIncidencia(sData.getIncidenciaDetalles()))
                            .commit();

                }catch (Exception ex){
                    Log.d(TAG, "onClick: Error al tratar de actualizar el fragmento.");
                }
            }
        });

        //Eliminamos una incidencia al pulsar este botón.
        btnEliminarIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    HashMap<String, String> parametros = new HashMap<String, String>();
                    parametros.put("Action", "Incidencia.delete");
                    parametros.put("IncidenciaId", "" + incidencia.getIdIncidencia());

                    GuardarMensajeAsync tarea = new GuardarMensajeAsync(parametros);
                    tarea.execute("http://jgarcia.x10host.com/Controller.php");

                    //Creamos una nueva lista de incidencias donde eliminamos la que el usuario quiere quitar
                    ArrayList<Incidencia> lista = new ArrayList<Incidencia>();
                    for(Incidencia i : sData.getListadoIncidencias()){
                        if(i.getIdIncidencia() != incidencia.getIdIncidencia()){
                            lista.add(i);
                        }
                    }

                    //Establecemos la nueva lista con la incidencia eliminada en la clase estática
                    sData.setListadoIncidencias(lista);


                    ((MenuActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, new ListadoIncidencias(sData.getListadoIncidencias()))
                            .commit();
                }catch (Exception ex){
                    Toast.makeText(getContext(), "Error al eliminar la incidencia.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        //region instanciamos el recycler view
        try {
            sData.setListadoMensajes(null);
            getMensajes();
            if (listadoMensajes.size() > 0) {
                rvMensajes.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new ListadoMensajesAdapter(listadoMensajes);
                rvMensajes.setAdapter(adapter);
            }

        }catch (Exception ex){
            Log.d(TAG, "onCreateView: Error al cargar el recyclerView");
        }
        //endregion

        return v;
    }

    //Método con el que recibimos los mensajes de una incidencia
    public void getMensajes(){
        try {
            HashMap<String, String> parametros = new HashMap<String, String>();
            parametros.put("Action", "Mensaje.select");
            parametros.put("IncidenciaId", "" + sData.getIncidenciaDetalles().getIdIncidencia());
            TareaSegundoPlanoSelectMensajes tarea = new TareaSegundoPlanoSelectMensajes(parametros);
            tarea.execute("http://jgarcia.x10host.com/Controller.php").get();
        }catch (Exception ex){
            Log.d(TAG, "getMensajes: Error al realizar la petición.");
        }
        }

    //Hilo en segundo plano para realizar las llamadas a la base de datos
    class GuardarMensajeAsync extends AsyncTask<String, Integer, Boolean> {

        private ProgressDialog progressDialog = new ProgressDialog(getContext());
        private HashMap<String, String> parametros = null;


        public GuardarMensajeAsync(HashMap<String, String> parametros) {
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
                    GuardarMensajeAsync.this.cancel(true);
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
           // Toast.makeText(getContext(), "Mensaje enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    //Hilo en segundo plano que selecciona todos los mensajes de una incidencia
    class TareaSegundoPlanoSelectMensajes extends AsyncTask<String, Integer, Boolean> {

        private ProgressDialog progressDialog = new ProgressDialog(getContext());
        private HashMap<String, String> parametros = null;


        public TareaSegundoPlanoSelectMensajes (HashMap<String, String> parametros) {
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
                    fDetallesIncidencia.TareaSegundoPlanoSelectMensajes.this.cancel(true);
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
                    listadoMensajes = Mensaje.getArrayListFromJSon(result);
                    sData.setListadoMensajes(listadoMensajes);
                }else{
                    listadoMensajes = null;
                    sData.setListadoMensajes(null);
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
