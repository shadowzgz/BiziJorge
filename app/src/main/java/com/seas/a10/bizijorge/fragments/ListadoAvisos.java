package com.seas.a10.bizijorge.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.seas.a10.bizijorge.MenuActivity;
import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.adapters.ListadoAvisosAdapter;
import com.seas.a10.bizijorge.beans.Aviso;
import com.seas.a10.bizijorge.beans.Recorrido;
import com.seas.a10.bizijorge.data.sData;
import com.seas.a10.bizijorge.utils.Post;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListadoAvisos extends Fragment {

    //region variables
    ArrayList<Aviso> listadoAvisos;
    RecyclerView rvListadoAvisos;
    Button btnCrearAviso;
    private ListadoAvisosAdapter adapter;
    ArrayList<Aviso> listadoAvisosFiltrados = new ArrayList<Aviso>();

    //endregion

    //region Constructores
    public ListadoAvisos() {
        // Required empty public constructor
    }
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_listado_avisos, container, false);

        rvListadoAvisos = (RecyclerView) v.findViewById(R.id.rvListadoAvisos);
        btnCrearAviso = (Button) v.findViewById(R.id.btnCrearAviso);

        if(sData.getCliente() != null){
            if(sData.getCliente().getIdUsuario() == 10){
                btnCrearAviso.setVisibility(View.VISIBLE);
            }
        }

        getListadoAvisos();

        //Botón con el que accedemos a la página de creación de incidencias
        btnCrearAviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ((MenuActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, new fAvisoAdmin())
                            .commit();
                }catch (Exception ex){
                    Toast.makeText(getContext(), "Error al acceder a la página.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        try {
            filtrarAvisosCaducados();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //si existe algun aviso sin caducar cargamos el recycler view
        if(listadoAvisosFiltrados.size()>0) {
            rvListadoAvisos.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new ListadoAvisosAdapter(listadoAvisosFiltrados);
            rvListadoAvisos.setAdapter(adapter);
        }
        return v;
    }

    //Filtramos los avisos caducados para no mostrarlos
    public void filtrarAvisosCaducados() throws ParseException {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat time = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        String formattedDate = time.format(c);
        try {
            //Si la fecha del aviso no ha caducado, lo incluimos en los avisos filtrados
            for (Aviso i : listadoAvisos) {
                //Si la fecha de caducidad viene después de la fecha actual, significa que aún no ha caducado
                if (i.getAvisoFechaCaducidad().after(c)) {
                    listadoAvisosFiltrados.add(i);
                }

            }
            sData.setListadoAviososNoCaducados(listadoAvisosFiltrados);
        }catch (Exception ex){
            Log.d(TAG, "filtrarAvisosCaducados: " + ex);
        }
    }

    //Método que recoje los avisos de la base de datos
    public void getListadoAvisos(){
        try{
            HashMap<String, String> parametros = new HashMap<String, String>();
            parametros.put("Action", "Aviso.select");

            TareaSegundoPlanoSelectAvisos tarea = new TareaSegundoPlanoSelectAvisos(parametros);
            tarea.execute("http://jgarcia.x10host.com/Controller.php").get();
        }catch (Exception ex){
            Toast.makeText(getContext(), "Error al recibir los datos de los avisos", Toast.LENGTH_SHORT).show();
        }

    }

    class TareaSegundoPlanoSelectAvisos extends AsyncTask<String, Integer, Boolean> {

        private ProgressDialog progressDialog = new ProgressDialog(getContext());
        private HashMap<String, String> parametros = null;


        public TareaSegundoPlanoSelectAvisos(HashMap<String, String> parametros) {
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
                    TareaSegundoPlanoSelectAvisos.this.cancel(true);
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
                    listadoAvisos = Aviso.getArrayListFromJSon(result);
                    sData.setListadoAvisos(listadoAvisos);
                }else{
                    listadoAvisos = null;
                    sData.setListadoAvisos(null);
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
