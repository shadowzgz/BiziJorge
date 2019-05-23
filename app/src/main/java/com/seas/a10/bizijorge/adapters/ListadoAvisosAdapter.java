package com.seas.a10.bizijorge.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seas.a10.bizijorge.MenuActivity;
import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.beans.Aviso;
import com.seas.a10.bizijorge.data.sData;
import com.seas.a10.bizijorge.fragments.ListadoAvisos;
import com.seas.a10.bizijorge.fragments.fDetallesIncidencia;
import com.seas.a10.bizijorge.utils.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class ListadoAvisosAdapter extends RecyclerView.Adapter <ListadoAvisosAdapter.ListadoAvisosViewHolder> {
    //region variables
    Context context;
    ArrayList<Aviso> listadoAvisos;
    //endregion

    //region constructores
    //constructor vacío
    public ListadoAvisosAdapter() {
    }


    public ListadoAvisosAdapter(Context context, ArrayList<Aviso> listadoAvisos) {
        this.listadoAvisos = listadoAvisos;
        this.context = context;
    }

    //endregion


    @NonNull
    @Override
    public ListadoAvisosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aviso, parent, false);
        ListadoAvisosViewHolder lavh = new ListadoAvisosViewHolder(itemView);

        return lavh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListadoAvisosViewHolder holder, int position) {
        final Aviso item = listadoAvisos.get(position);
        holder.bindAviso(item);

        if(sData.getCliente() != null){
            if(sData.getCliente().getIdUsuario() == 10){
                holder.imageAviso.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            HashMap<String, String> parametros = new HashMap<String, String>();
                            parametros.put("Action", "Aviso.delete");
                            parametros.put("AvisoId", "" + item.getAvisoId());

                            EliminarAvisoAsync tarea = new EliminarAvisoAsync(parametros);
                            tarea.execute("http://jgarcia.x10host.com/Controller.php");
                        }catch (Exception ex){
                            Toast.makeText(context, "Error al borrar la incidencia.", Toast.LENGTH_SHORT).show();
                        }

                        try{
                            ((MenuActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.main_content, new ListadoAvisos())
                                    .commit();
                        }catch (Exception ex){
                            Toast.makeText(context, "Error al recargar la página.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return listadoAvisos.size();
    }



    public class ListadoAvisosViewHolder extends RecyclerView.ViewHolder implements View

            .OnClickListener{

        TextView tvAvisoTitulo;
        TextView tvAvisoTexto;
        TextView tvAvisoFecha;
        ImageView imageAviso;

        public ListadoAvisosViewHolder(View itemView) {
            super(itemView);

            tvAvisoTitulo = (TextView) itemView.findViewById(R.id.tvAvisoItemTitulo);
            tvAvisoTexto = (TextView) itemView.findViewById(R.id.tvAvisoItemTexto);
            tvAvisoFecha = (TextView) itemView.findViewById(R.id.tvAvisoItemFecha);
            imageAviso = (ImageView) itemView.findViewById(R.id.imgAviso);
        }

        public void bindAviso(Aviso c){
            Date date = c.getAvisoFecha();
            
            try {
                String year = (String) DateFormat.format("yyyy", date);
                String month = (String) DateFormat.format("MM", date);
                //String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
                String day = (String) DateFormat.format("dd", date);
                String hour = (String) DateFormat.format("HH:mm", date);

                tvAvisoTitulo.setText("Aviso por " + c.getAvistoTipo().toLowerCase());
                tvAvisoTexto.setText(c.getAvisoTexto());
                tvAvisoFecha.setText(hour + " " + day + "/" + month + "/" + year);
                imageAviso.setBackgroundResource(R.drawable.aviso);
            }catch (Exception ex){
                Log.d(TAG, "bindAviso: Error al bindear los datos del aviso");
            }

        }

        @Override
        public void onClick(View view) {

        }
    }

    //Hilo en segundo plano para realiza    r las llamadas a la base de datos
    class EliminarAvisoAsync extends AsyncTask<String, Integer, Boolean> {

        private ProgressDialog progressDialog = new ProgressDialog(context);
        private HashMap<String, String> parametros = null;


        public EliminarAvisoAsync(HashMap<String, String> parametros) {
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
                    EliminarAvisoAsync.this.cancel(true);
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

}
