package com.seas.a10.bizijorge.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.seas.a10.bizijorge.LoginActivity;
import com.seas.a10.bizijorge.R;
import com.seas.a10.bizijorge.RegisterActivity;
import com.seas.a10.bizijorge.data.sData;
import com.seas.a10.bizijorge.utils.Post;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//Fragmento donde el usuario podrá mandar una incidencia a los administradores
public class fIncidencia extends Fragment {
    private EditText userEmailIncidencia;
    private EditText asuntoIncidencia;
    private EditText textoIncidencia;
    private Button btnSendIncidencia;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_f_incidencia, container, false);

        userEmailIncidencia = (EditText) v.findViewById(R.id.userEmailIncidencia);
        asuntoIncidencia = (EditText) v.findViewById(R.id.asuntoIncidencia);
        textoIncidencia = (EditText) v.findViewById(R.id.textoIncidencia);
        btnSendIncidencia = (Button) v.findViewById(R.id.btn_sendIncidencia);

//
        btnSendIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //Guardamos la fecha actual para adjuntar a la incidencia
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat time =  new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
                    String formattedDate = time.format(c);
                    //Si el cliente esta logueado y todos los campos están rellenados se desencadena el método
                    if (sData.getCliente() != null) {
                        if (TextUtils.isEmpty(userEmailIncidencia.getText().toString()) == false &&
                                TextUtils.isEmpty(asuntoIncidencia.getText().toString()) == false &&
                                TextUtils.isEmpty(textoIncidencia.getText().toString()) == false) {

                            HashMap<String, String> parametros = new HashMap<String, String>();
                            parametros.put("Action", "Incidencia.add");
                            parametros.put("asuntoIncidencia", asuntoIncidencia.getText().toString());
                            parametros.put("textoIncidencia", textoIncidencia.getText().toString());
                            parametros.put("idUsuario", "" + sData.getCliente().getIdUsuario());
                            parametros.put("fechaIncidencia", formattedDate);
                            parametros.put("userEmailIncidencia", userEmailIncidencia.getText().toString());

                            TareaSegundoPlano tarea = new TareaSegundoPlano(parametros);
                            tarea.execute("http://jgarcia.x10host.com/Controller.php");


                        } else {
                            Toast.makeText(getContext(), "Por favor" +
                                            ", rellene todos los camos antes de continuar.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(getContext(), "Debe estar registrado para mandar una incidencia.",
                                Toast.LENGTH_SHORT).show();
                    }
                }catch  (Exception ex){
                    Toast.makeText(getContext(), "Se ha producido un error.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }


    //final String username = "bizizaragozaapp@gmail.com";
    //final String password = "bizizaragozaapp1234";
    //Método que manda un correo para confirmar que se ha recibido correctamente la incidencia
    private void sendEmail(){

        final String username = "bizizaragozaapp@gmail.com";
        final String password = "bizizaragozaapp1234";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("bizizaragozaapp@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(userEmailIncidencia.getText().toString()));
            message.setSubject("Su incidencia se ha recibido correctamente.");
            message.setText("Desde el equipo de Bizi Zaragoza le comunicamos que:" + "\n\n" +
                   "Su incidencia se ha guardado correctamente en nuestra base de datos." +
                    " Lamentamos que haya tenido problemas con la aplicación." + " \n" +
                    "Un técnico del equipo se pondrá en contacto con usted en el menor tiempo posible" +
                    "\n" + "Gracias por la espera y por su comprensión. Atentamente, " + "\n\n" +
                    "Equipo de desarrollo de Bizi Zaragoza");


            Transport.send(message);

            System.out.println("Done");
            Toast.makeText(getContext(), "Se ha enviado un email de confirmación a " + userEmailIncidencia.getText().toString() + ".", Toast.LENGTH_SHORT).show();

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

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
                //Se manda el email de confirmación
                sendEmail();
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
            Toast.makeText(getContext(), "Incidencia guardada satisfactoriamente.", Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), "Le llegará un correo de confirmación", Toast.LENGTH_LONG).show();
        }
    }

}
