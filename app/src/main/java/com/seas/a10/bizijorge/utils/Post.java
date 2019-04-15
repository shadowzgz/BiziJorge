package com.seas.a10.bizijorge.utils;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/*Clase que realiza las peticiones con la base de datos*/
public class Post {
    private InputStream is = null;
    private String respuesta = "";


    /*Crea la url de la peticón*/
    private String getEncodedData(Map<String,String> data) {
        // URL: Constante Config.URL
        // Parametros:
        //Action.xxxxxx
        //"EMAIL":"VALOR"
        //"PASS":"VALOR"
        StringBuilder sb = new StringBuilder();
        for(String key : data.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(data.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if(sb.length()>0)
                sb.append("&");

            sb.append(key + "=" + value);
        }
        return sb.toString();
    }

    /*Método que establece la conexión con la base de datos y lanza la petición*/
    private HttpURLConnection con = null;
    private void conectaPost(Map<String,String> dataToSend, String pagina) {
        OutputStreamWriter writer = null;
        try {

            // Preparo la cadena con las claves y valores que quiero que le llegue al Servidor
            // ?USUARIO=PEPE&CONTRASENA=1234&.........
            String encodedStr = getEncodedData(dataToSend);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // URL--> donde se encuentra el recurso Web
            URL url = new URL(pagina);

            // Petición HTTP al Servidor
            con = (HttpURLConnection) url.openConnection();

            // Petición al Servidor por método POST
            con.setRequestMethod("POST");

            // Habilitar la conexión para que nos permita enviar datos por POST
            con.setDoOutput(true);

            // Preparo el escritor (corriente de datos de salida) para adjuntar los
            // datos que quiero enviar al Servidor
            writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(encodedStr);

            // Cerramos el buffer de memoria que nos permite almacenar datos para enviar y recibir.
            // En este caso hemos escrito, ahora lo vacío porque después querré leer los datos que me envíe el
            // Servidor
            writer.flush();

            // Recupero un lector (corriente de datos de entrada), es decir, del Servidor hacia a mí.
            is= con.getInputStream();

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        } finally {
            try {
                if(writer!=null) {
                    writer.close();
                }

            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
        }

    }

    /*Método para realizar solicitudes get*/
    private void conectaGet(String pagina) {
        try {
//            // URL--> donde se encuentra el recurso Web
//            URL url = new URL(pagina);
//
//            // Petición HTTP al Servidor
//            con = (HttpURLConnection) url.openConnection();
//            //con.connect();
//            // Recupero un lector (corriente de datos de entrada), es decir, del Servidor hacia a mí.
//            is= con.getInputStream();

            URL url = new URL(pagina);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.connect();


            InputStream stream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        } finally {
            try {

            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
        }

    }


    /*Método que recibe la petición en JSON*/
    private JSONArray getRespuestaPostEnJson() {
        JSONArray jArray = null;
        try {
            if(is!=null){
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                respuesta = sb.toString();
                jArray = new JSONArray(respuesta);
            }
            Log.e("log_tag", "Cadena JSon " + respuesta);
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }finally {
            if(con!=null)
            {
                con.disconnect();
            }
            return jArray;
        }
    }

    public JSONArray getServerDataPost(Map<String,String> dataToSend, String URL) {
        conectaPost(dataToSend, URL);
        return 	getRespuestaPostEnJson();

    }

    public JSONArray getServerDataGet(String URL) {
        conectaGet(URL);
        return 	getRespuestaPostEnJson();

    }


    public void registerUser(Map<String,String> dataToSend, String URL){
        conectaPost(dataToSend, URL);
    }




}
