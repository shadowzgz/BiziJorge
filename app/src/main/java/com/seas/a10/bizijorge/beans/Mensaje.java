package com.seas.a10.bizijorge.beans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Mensaje {
    //region Variables
        private int mensajeId;
        private String mensajeTexto;
        private Date mensajeFecha;
        private String mensajeEmail;
        private int userId;
        private int incidenciaId;

        private final static String MENSAJEID = "MensajeId";
        private final static String MENSAJETEXTO = "MensajeTexto";
        private final static String MENSAJEFECHA = "MensajeFecha";
        private final static String MENSAJEEMAIL = "UsuarioEmail";
        private final static String USERID = "ID_USUARIO";
        private final static String INCIDENCIAID = "IncidenciaId";
    //endregion


    //region Constructores
    //constructor vacío
    public Mensaje() {
    }

    public Mensaje(int mensajeId, String mensajeTexto, Date mensajeFecha, String mensajeEmail, int userId, int incidenciaId) {
        this.mensajeId = mensajeId;
        this.mensajeTexto = mensajeTexto;
        this.mensajeFecha = mensajeFecha;
        this.mensajeEmail = mensajeEmail;
        this.userId = userId;
        this.incidenciaId = incidenciaId;
    }

    //endregion


    //region Getters y setters

    public int getMensajeId() {
        return mensajeId;
    }
    public void setMensajeId(int mensajeId) {
        this.mensajeId = mensajeId;
    }

    public String getMensajeTexto() {
        return mensajeTexto;
    }
    public void setMensajeTexto(String mensajeTexto) {
        this.mensajeTexto = mensajeTexto;
    }

    public Date getMensajeFecha() {
        return mensajeFecha;
    }
    public void setMensajeFecha(Date mensajeFecha) {
        this.mensajeFecha = mensajeFecha;
    }

    public String getMensajeEmail() {
        return mensajeEmail;
    }
    public void setMensajeEmail(String mensajeEmail) {
        this.mensajeEmail = mensajeEmail;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIncidenciaId() {
        return incidenciaId;
    }
    public void setIncidenciaId(int incidenciaId) {
        this.incidenciaId = incidenciaId;
    }


    //endregion


    /*Recibe un JSON y lo transforma a la entidad Mensaje*/
    public static ArrayList<Mensaje> getArrayListFromJSon(JSONArray datos){
        ArrayList<Mensaje> lista = null;

        //Creamos el formato con el que nos llegan las fechas
        SimpleDateFormat time =  new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        Mensaje mensaje = null;
        try {
            if(datos!=null && datos.length() > 0 ){
                lista = new ArrayList<Mensaje>();
            }
            for (int i = 0; i < datos.length(); i++) {
                JSONObject json_data = datos.getJSONObject(i);
                mensaje = new Mensaje();
                mensaje.setMensajeId(json_data.getInt(MENSAJEID));
                mensaje.setMensajeTexto(json_data.getString(MENSAJETEXTO));

                try{
                    mensaje.setMensajeFecha(time.parse(json_data.getString(MENSAJEFECHA)));
                }catch (Exception ex){
                    mensaje.setMensajeFecha(null);
                }
                mensaje.setMensajeEmail(json_data.getString(MENSAJEEMAIL));

                mensaje.setUserId(json_data.getInt(USERID));
                mensaje.setIncidenciaId(json_data.getInt(INCIDENCIAID));

                lista.add(mensaje);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lista;

    }


}
