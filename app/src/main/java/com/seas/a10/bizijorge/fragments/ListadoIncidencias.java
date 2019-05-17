package com.seas.a10.bizijorge.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.adapters.ListadoIncidenciasAdapter;
import com.seas.a10.bizijorge.beans.EstacionFavorita;
import com.seas.a10.bizijorge.beans.Incidencia;
import com.seas.a10.bizijorge.data.sData;
import com.seas.a10.bizijorge.utils.Post;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListadoIncidencias extends Fragment {

    //region variables
    ArrayList<Incidencia> listadoIncidencias;
    RecyclerView rvListadoIncidencias;
    private ListadoIncidenciasAdapter adapter;
    //endregion
    //region Constructores
    public ListadoIncidencias() {
        // Required empty public constructor
    }
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_listado_incidencias, container, false);
        rvListadoIncidencias = (RecyclerView) v.findViewById(R.id.rvListadoIncidencias);

        try {
            recogerIncidencias();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        rvListadoIncidencias.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ListadoIncidenciasAdapter(listadoIncidencias);
        rvListadoIncidencias.setAdapter(adapter);
        return v;
    }

    public void recogerIncidencias() throws ExecutionException, InterruptedException {

        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put("Action", "Incidencia.select");
        TareaSegundoPlano tarea = new TareaSegundoPlano(parametros);
        tarea.execute("http://jgarcia.x10host.com/Controller.php").get();

    }

    //Hilo en segundo plano que trae las incidencias de la base de datos
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
                    ListadoIncidencias.TareaSegundoPlano.this.cancel(true);
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

                listadoIncidencias = Incidencia.getArrayListFromJSon(result);
                sData.setListadoIncidencias(listadoIncidencias);

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
