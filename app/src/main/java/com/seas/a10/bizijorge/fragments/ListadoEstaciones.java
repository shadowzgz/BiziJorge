package com.seas.a10.bizijorge.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.adapters.ListadoEstacionesAdapter;
import com.seas.a10.bizijorge.beans.Estacion;
import com.seas.a10.bizijorge.beans.EstacionFavorita;
import com.seas.a10.bizijorge.data.sData;
import com.seas.a10.bizijorge.utils.Post;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class ListadoEstaciones extends Fragment{

    //region variables
    RecyclerView rvPelicula;
    private ListadoEstacionesAdapter adapter;
    EditText search;
    ImageView imageLupa;
    private ArrayList<EstacionFavorita> listadoEstacionesFav;
    int favorito = 0;
    //endregion

    //region constructores
    public ListadoEstaciones() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ListadoEstaciones(int Favorito){
        favorito = Favorito;
    }

    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_listado_estaciones, container, false);

        //Filtramos el texto que se introduce en el textbox de búsqueda de estaciones
        search = v.findViewById(R.id.searchEstacion);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        if(sData.getCliente() != null) {
            try {
                recogerEstacionesFavoritas();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        imageLupa = (ImageView) v.findViewById(R.id.ivLupa);
        rvPelicula = (RecyclerView) v.findViewById(R.id.rvListadoEstaciones);
        rvPelicula.setLayoutManager(new LinearLayoutManager(getContext()));

        //Si hemos seleccionado en el menú el listado normal, pasaremos todas las estaciones.
        //Si hemos solicitado las estaciones favoritas pasaremos solo las mismas
        if(favorito == 1 && listadoEstacionesFav != null) {
            ArrayList<Estacion> listadoEstacionesTodo = new ArrayList<Estacion>();
            for(Estacion i : sData.getListadoEstaciones()){
                for(EstacionFavorita e : listadoEstacionesFav){
                    if(i.getId() == e.getIdEstacion()){
                        listadoEstacionesTodo.add(i);
                    }
                }

            }
            adapter = new ListadoEstacionesAdapter(listadoEstacionesTodo, getContext());
            rvPelicula.setAdapter(adapter);
        }else{

            adapter = new ListadoEstacionesAdapter(sData.getListadoEstaciones(), getContext());
            rvPelicula.setAdapter(adapter);
        }
        return v;
    }

    public void recogerEstacionesFavoritas() throws ExecutionException, InterruptedException {
        if(sData.getCliente() != null) {
            HashMap<String, String> parametros = new HashMap<String, String>();
            parametros.put("Action", "Estacionfav.select");
            parametros.put("ID_USUARIO", "" + sData.getCliente().getIdUsuario());
            TareaSegundoPlano tarea = new TareaSegundoPlano(parametros);
            tarea.execute("http://jgarcia.x10host.com/Controller.php").get();
        }
    }

    //Método que filtra las estaciones según el texto que se intruduzca en el textbox de búsca de estaciones
    private void filter(String text){
        ArrayList<Estacion> listaFiltrada = new ArrayList<Estacion>();

        for(Estacion i : sData.getListadoEstaciones()){
            if(i.getTitle().toLowerCase().contains(text.toLowerCase())){
                listaFiltrada.add(i);
            }
        }
        adapter.filterList(listaFiltrada);
    }

    //Recogemos la lista de estaciones favoritas del usuario en caso de estar logueado
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
                JSONArray result = post.getServerDataPost(parametros, url_select);

                    listadoEstacionesFav = EstacionFavorita.getArrayListFromJSon(result);
                    sData.setListadoEstacionesFavoritas(listadoEstacionesFav);

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