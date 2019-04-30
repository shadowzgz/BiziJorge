package com.seas.a10.bizijorge.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seas.a10.bizijorge.LoginActivity;
import com.seas.a10.bizijorge.MenuActivity;
import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.RegisterActivity;
import com.seas.a10.bizijorge.beans.Estacion;
import com.seas.a10.bizijorge.beans.EstacionFavorita;
import com.seas.a10.bizijorge.data.sData;
import com.seas.a10.bizijorge.fragments.ListadoEstaciones;
import com.seas.a10.bizijorge.fragments.fMap;
import com.seas.a10.bizijorge.utils.Post;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

//Clase que muestra un listado con las diferentes estaciones
public class ListadoEstacionesAdapter extends  RecyclerView.Adapter <ListadoEstacionesAdapter.ListadoEstacionesViewHolder>{

    //region variables
    private ArrayList<Estacion> lisadoEstaciones;
    private ArrayList<Estacion> lisadoEstacionesFilter;
    private Context context;
    private int estacionSelect = 0;
    ListadoEstaciones listadoEstacionesFragmento;
    ArrayList<EstacionFavorita> listadoEstacionesFav;
    fMap fragmentoMapa;

    //endregion

    //Contructor de la clase
    public ListadoEstacionesAdapter(ArrayList<Estacion> lisadoEstaciones, Context context) {
        this.lisadoEstaciones = lisadoEstaciones;
        this.context = context;

    }


    @NonNull
    @Override
    public ListadoEstacionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_estacion, parent, false);
        ListadoEstacionesViewHolder levh = new ListadoEstacionesViewHolder(itemView);

        return levh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListadoEstacionesViewHolder holder, int position) {
        final Estacion item = lisadoEstaciones.get(position);
        holder.bindEstacion(item);

        //region Listener del botón de favoritos
        //Pulsamos para añadir a favoritos. En caso de ya ser favorito, se elimina de la base de datos
        //Después se actualiza la lista de estaciones favoritas con la base de datos.
        //Por último se notifica al adaptador que se han realizado cambios.

        holder.favEstacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean esFavorito = false;
                if(sData.getCliente() != null) {


                    try {
                        //Buscamos si la estacion se encuentra en el listado de estaciones favoritas
                        if(sData.getListadoEstacionesFavoritas() != null) {
                            for (EstacionFavorita i : sData.getListadoEstacionesFavoritas()) {
                                if (item.getId() == i.getIdEstacion()) {
                                    esFavorito = true;
                                }
                            }
                        }
                        //En caso de no estar en el listado se guarda en la base de datos
                        if (sData.getCliente() != null && esFavorito == false) {
                            HashMap<String, String> parametros = new HashMap<String, String>();
                            parametros.put("Action", "Estacionfav.add");
                            parametros.put("ID_USUARIO", "" + sData.getCliente().getIdUsuario());
                            parametros.put("idEstacion", "" + item.getId());
                            TareaSegundoPlano tarea = new TareaSegundoPlano(parametros);
                            tarea.execute("http://jgarcia.x10host.com/Controller.php");
                            Toast.makeText(context, "Se ha guardado correctamente en favoritos", Toast.LENGTH_SHORT).show();


                            //Si es favorita se elimina de la base de datos
                        } else {
                            HashMap<String, String> parametros = new HashMap<String, String>();
                            parametros.put("Action", "Estacionfav.delete");
                            parametros.put("idEstacion", "" + item.getId());
                            TareaSegundoPlano tarea = new TareaSegundoPlano(parametros);
                            tarea.execute("http://jgarcia.x10host.com/Controller.php");
                            Toast.makeText(context, "Se ha borrado correctamente de favoritos", Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception ex) {

                    }

                    //Se actualiza la lista de estaciones favoritas en caso de que el usuario este logueado
                    if (sData.getCliente() != null) {
                        HashMap<String, String> parametros = new HashMap<String, String>();
                        parametros.put("Action", "Estacionfav.select");
                        parametros.put("ID_USUARIO", "" + sData.getCliente().getIdUsuario());
                        TareaSegundoPlanoSelect tarea = new TareaSegundoPlanoSelect(parametros);
                        try {
                            tarea.execute("http://jgarcia.x10host.com/Controller.php").get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(context, "Debe estar registrado para usar esta función...", Toast.LENGTH_SHORT).show();
                }


            }
        });


        //endregion

        holder.tituloEstacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                fragmentoMapa = fragmentoMapa.
//                GrouponData.setPeliculaSeleccionada(item);
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragmentoPeliculaDetails).addToBackStack(null).commit();
                ((MenuActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, new fMap(item.getId()))
                        .commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return lisadoEstaciones.size();
    }

    public void filterList(ArrayList<Estacion> filterList){
        lisadoEstaciones = filterList;
        notifyDataSetChanged();
    }

    public class ListadoEstacionesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tituloEstacion;
        private TextView bicisEstacion;
        private TextView anclajesEstacion;
        private ImageView iconoEstacionBicis;
        private ImageView iconoEstacionAnclajes;
        private ImageView favEstacion;

        public ListadoEstacionesViewHolder(View itemView){
            super(itemView);
            tituloEstacion = (TextView) itemView.findViewById(R.id.tituloEstacion);
            bicisEstacion = (TextView) itemView.findViewById(R.id.bicisEstacion);
            anclajesEstacion = (TextView) itemView.findViewById(R.id.anclajesEstacion);
            iconoEstacionBicis = (ImageView) itemView.findViewById(R.id.imagenEstacionBicis);
            iconoEstacionAnclajes = (ImageView) itemView.findViewById(R.id.imagenEstacionAnclajes);
            favEstacion = (ImageView) itemView.findViewById(R.id.imagenFav);
        }

        public void bindEstacion (Estacion c){

            tituloEstacion.setText(c.getTitle());
            bicisEstacion.setText("Bicis disponibles: " + c.getBicisDisponibles());
            anclajesEstacion.setText("Anclajes disponibles: " + c.getAnclajesDisponibles());

            if(c.getBicisDisponibles() <= 0){
                iconoEstacionBicis.setBackgroundResource(R.drawable.marcadorbicirojo);
            }else if(c.getBicisDisponibles() <= 4){
                iconoEstacionBicis.setBackgroundResource(R.drawable.marcadorbicinaranja);
            }else if(c.getBicisDisponibles() >= 5){
                iconoEstacionBicis.setBackgroundResource(R.drawable.marcadorbiciverde);
            }else{
                iconoEstacionBicis.setBackgroundResource(R.drawable.marcadorfueraservicio);
            }

            if(c.getAnclajesDisponibles() <= 0){
                iconoEstacionAnclajes.setBackgroundResource(R.drawable.marcadoranclajerojo);
            }else if(c.getAnclajesDisponibles() <= 4){
                iconoEstacionAnclajes.setBackgroundResource(R.drawable.marcadoranclajenaranja);
            }else if(c.getAnclajesDisponibles() >= 5){
                iconoEstacionAnclajes.setBackgroundResource(R.drawable.marcadoranclajeverde);
            }else{
                iconoEstacionAnclajes.setBackgroundResource(R.drawable.marcadorfueraservicio);
            }

            //Si existe un usuario registrado, y tenemos su lista de estaciones favoritas, las
            //mostramos en el listado cambiando el icono de favorito
            favEstacion.setBackgroundResource(R.drawable.notfav);
            if(sData.getListadoEstacionesFavoritas() != null && sData.getCliente() != null) {
                if(sData.getListadoEstacionesFavoritas().size() > 0) {
                    for (EstacionFavorita i : sData.getListadoEstacionesFavoritas()) {
                        if (i.getIdEstacion() == c.getId()) {
                            favEstacion.setBackgroundResource(R.drawable.truefav);
                        }

                    }
                }
            }
        }
        @Override
        public void onClick(View view) {

        }
    }

    //Hilo en segundo plano que se encarga de guardar en la base de datos la estación que seleccionamos
    //como favorita
    class TareaSegundoPlano extends AsyncTask<String, Integer, Boolean> {

        private ProgressDialog progressDialog = new ProgressDialog(context);
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
            //Toast.makeText(RegisterActivity.getInstance().getBaseContext(), "Registro realizado satisfactoriamente", Toast.LENGTH_SHORT).show();
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    //Hilo en segundo plano que se encarga de actualizar la lista en la base de datos.
    class TareaSegundoPlanoSelect extends AsyncTask<String, Integer, Boolean> {

        private ProgressDialog progressDialog = new ProgressDialog(context);
        private HashMap<String, String> parametros = null;


        public TareaSegundoPlanoSelect(HashMap<String, String> parametros) {
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
                    TareaSegundoPlanoSelect.this.cancel(true);
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
                //Si no recibe ningun dato lo podenos to do a null, si no hay errores
                if(result != null) {
                    listadoEstacionesFav = EstacionFavorita.getArrayListFromJSon(result);
                    sData.setListadoEstacionesFavoritas(listadoEstacionesFav);
                }else{
                    listadoEstacionesFav = null;
                    sData.setListadoEstacionesFavoritas(null);
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
