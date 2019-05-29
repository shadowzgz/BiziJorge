package com.seas.a10.bizijorge;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seas.a10.bizijorge.utils.Post;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;


//Clase con la que implementamos la funcionalidad de registro de usuario
public class RegisterActivity extends AppCompatActivity {
    private static RegisterActivity registerActivity;

    private EditText _nameText;
    private EditText _emailText;
    private EditText _passwordText;
    private Button _signupButton;
    private TextView _loginLink;
    /*Constructor de la clase*/
    public static RegisterActivity getInstance(){
        return registerActivity;
    }



    /*Método que se ejecuta al iniciar el activity*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _nameText = (EditText) findViewById(R.id.input_name);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);

        registerActivity = this;


        //Botón de registro
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //signup();
                if(TextUtils.isEmpty(_nameText.getText().toString()) == false && TextUtils.isEmpty(_passwordText.getText().toString()) == false &&
                        TextUtils.isEmpty(_emailText.getText().toString()) == false) {
                    HashMap<String, String> parametros = new HashMap<String, String>();
                    parametros.put("Action", "User.add");
                    parametros.put("USER", _nameText.getText().toString());
                    parametros.put("EMAIL", _emailText.getText().toString());
                    parametros.put("PASS", _passwordText.getText().toString());

                    RegisterActivity.TareaSegundoPlano tarea = new RegisterActivity.TareaSegundoPlano(parametros);
                    tarea.execute("http://jgarcia.x10host.com/Controller.php");
                }else{
                    Toast.makeText(RegisterActivity.getInstance().getBaseContext(), "Por favor" +
                            ", rellene todos los camos antes de continuar.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Abrimos la ventana de login
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentLogin);

            }
        });
    }

    //Hilo en segundo plano para realiza    r las llamadas a la base de datos
    class TareaSegundoPlano extends AsyncTask<String, Integer, Boolean> {

        private ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
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
                    RegisterActivity.TareaSegundoPlano.this.cancel(true);
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
            Toast.makeText(RegisterActivity.getInstance().getBaseContext(), "Registro realizado satisfactoriamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

}
