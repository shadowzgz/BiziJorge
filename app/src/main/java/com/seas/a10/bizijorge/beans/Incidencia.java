package com.seas.a10.bizijorge.beans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//Entidad de incidencias
public class Incidencia {
    //Variables de la clase
    //region variables
    private int idIncidencia;
    private String asuntoIncidencia;
    private String textoIncidencia;
    private int idUsuario;
    private Date fechaIncidencia;
    private String userEmailIncidencia;

    private final static String IDINCIDENCIA = "IncidenciaId";
    private final static String ASUNTOINCIDENCIA = "IncidenciaAsunto";
    private final static String TEXTOINCIDENCIA = "IncidenciaTexto";
    private final static String IDUSUARIO = "ID_USUARIO";
    private final static String FECHAINCIDENCIA = "IncidenciaFecha";
    private final static String USEREMAILINCIDENCIA = "IncidenciaUserEmail";


    //endregion

    //Constructores de la clase
    //region constructor

    //Constructor vacío
    public Incidencia() {
    }

    public Incidencia(int idIncidencia, String asuntoIncidencia, String textoIncidencia, int idUsuario, Date fechaIncidencia, String userEmailIncidencia) {
        this.idIncidencia = idIncidencia;
        this.asuntoIncidencia = asuntoIncidencia;
        this.textoIncidencia = textoIncidencia;
        this.idUsuario = idUsuario;
        this.fechaIncidencia = fechaIncidencia;
        this.userEmailIncidencia = userEmailIncidencia;
    }

    //endregion

    //Métodos getters y setters de las variables
    //region getters y setters

    public int getIdIncidencia() {
        return idIncidencia;
    }
    public void setIdIncidencia(int idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public String getAsuntoIncidencia() {
        return asuntoIncidencia;
    }
    public void setAsuntoIncidencia(String asuntoIncidencia) {
        this.asuntoIncidencia = asuntoIncidencia;
    }

    public String getTextoIncidencia() {
        return textoIncidencia;
    }
    public void setTextoIncidencia(String textoIncidencia) {
        this.textoIncidencia = textoIncidencia;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaIncidencia() {
        return fechaIncidencia;
    }
    public void setFechaIncidencia(Date fechaIncidencia) {
        this.fechaIncidencia = fechaIncidencia;
    }

    public String getUserEmailIncidencia() {
        return userEmailIncidencia;
    }
    public void setUserEmailIncidencia(String userEmailIncidencia) {
        this.userEmailIncidencia = userEmailIncidencia;
    }

    //endregion

    /*Recibe un JSON y lo transforma a la entidad Incidencia*/
    public static ArrayList<Incidencia> getArrayListFromJSon(JSONArray datos){
        ArrayList<Incidencia> lista = null;

        //Creamos el formato con el que nos llegan las fechas
        SimpleDateFormat time =  new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        Incidencia incidencia = null;
        try {
            if(datos!=null && datos.length() > 0 ){
                lista = new ArrayList<Incidencia>();
            }
            for (int i = 0; i < datos.length(); i++) {
                JSONObject json_data = datos.getJSONObject(i);
                incidencia = new Incidencia();
                incidencia.setIdIncidencia(json_data.getInt(IDINCIDENCIA));
                incidencia.setAsuntoIncidencia(json_data.getString(ASUNTOINCIDENCIA));
                incidencia.setTextoIncidencia(json_data.getString(TEXTOINCIDENCIA));
                incidencia.setIdUsuario(json_data.getInt(IDUSUARIO));
                try{
                    incidencia.setFechaIncidencia(time.parse(json_data.getString(FECHAINCIDENCIA)));
                }catch (Exception ex){
                 incidencia.setFechaIncidencia(null);
                }
                //incidencia.setFechaIncidencia(time.parse(json_data.getString(FECHAINCIDENCIA)));
                incidencia.setUserEmailIncidencia(json_data.getString(USEREMAILINCIDENCIA));


                lista.add(incidencia);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lista;

    }


}
