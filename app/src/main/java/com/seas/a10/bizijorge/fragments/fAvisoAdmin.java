package com.seas.a10.bizijorge.fragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.seas.a10.bizijorge.MenuActivity;
import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.data.sData;
import com.seas.a10.bizijorge.utils.Post;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class fAvisoAdmin extends Fragment implements DatePickerDialog.OnDateSetListener {

    //region variables
    int diaSeleccionado;
    int mesSeleccionado;
    int añoSeleccionado;
    EditText etAvisoTexto;
    Button btnAvisoGuardar;
    TextView tvVolverListadoAvisos;
    TextView tvAvisoFechaCaducidad;
    Spinner avisoSpinner;
    //endregion

    //region constructores
    public fAvisoAdmin() {
        // Required empty public constructor
    }
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_f_aviso_admin, container, false);

        tvAvisoFechaCaducidad = v.findViewById(R.id.tvAvisoFechaCaducidad);
        btnAvisoGuardar = v.findViewById(R.id.btnGuardarAviso);
        tvAvisoFechaCaducidad.setText("Pulsar para seleccionar fecha");
        avisoSpinner = v.findViewById(R.id.spAvisoTipo);
        etAvisoTexto = v.findViewById(R.id.etAvisoTexto);
        tvVolverListadoAvisos = v.findViewById(R.id.tvVolverListadoAvisos);

        //Colocamos en lel spinner los tipos de avisos
        setSpinnerData();
        //Mostramos el calendario
        tvAvisoFechaCaducidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        tvVolverListadoAvisos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    ((MenuActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, new ListadoAvisos())
                            .commit();
                }catch (Exception ex){
                    Toast.makeText(getContext(), "Error al direccionar al listado de avisos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAvisoGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat time = new SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
                String formattedDate = time.format(c);
                String fechaCad = añoSeleccionado + "-" + mesSeleccionado + "-" + diaSeleccionado +
                        "T" + "12:00:00Z";
                 try{

                    HashMap<String, String> parametros = new HashMap<String, String>();
                    parametros.put("Action", "Aviso.add");
                    parametros.put("avisoTipo", avisoSpinner.getSelectedItem().toString());
                    parametros.put("avisoTexto", etAvisoTexto.getText().toString());
                    parametros.put("idUsuario", "" + sData.getCliente().getIdUsuario());
                    parametros.put("avisoFecha", formattedDate);
                    parametros.put("avisoFechaCaducidad", fechaCad);

                    TareaSegundoPlanoGuardarAviso tarea = new TareaSegundoPlanoGuardarAviso(parametros);
                     tarea.execute("http://jgarcia.x10host.com/Controller.php");

                }catch (Exception ex){
                    Toast.makeText(getContext(), "Error al recoger los datos para crear el aviso.", Toast.LENGTH_SHORT).show();
                }

                try{
                    ((MenuActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, new ListadoAvisos())
                            .commit();
                }catch (Exception ex){
                    Toast.makeText(getContext(), "Error al direccionar al listado de avisos", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return v;
    }



    //Ponemos los datos en el desplegable
    public void setSpinnerData(){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.avisoTipos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        avisoSpinner.setAdapter(adapter);

    }

    //recogemos los datos del calendario
    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                R.style.DialogTheme,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    //Colocamos la fecha seleccionada en el textView
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int mo = (int)month;
        mo = mo+1;

        diaSeleccionado = dayOfMonth;
        mesSeleccionado = mo;
        añoSeleccionado = year;

        String date = "Fecha seleccionada: " + dayOfMonth + "/" + mo + "/" + year;
        tvAvisoFechaCaducidad.setText(date);
    }

    //Hilo en segundo plano para guardar en la base de datos un aviso
    class TareaSegundoPlanoGuardarAviso extends AsyncTask<String, Integer, Boolean> {

        private ProgressDialog progressDialog = new ProgressDialog(getContext());
        private HashMap<String, String> parametros = null;


        public TareaSegundoPlanoGuardarAviso(HashMap<String, String> parametros) {
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
                    TareaSegundoPlanoGuardarAviso.this.cancel(true);
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
            // Toast.makeText(getContext(), "Aviso guardado satisfactoriamente.", Toast.LENGTH_SHORT).show();

        }
    }


}
