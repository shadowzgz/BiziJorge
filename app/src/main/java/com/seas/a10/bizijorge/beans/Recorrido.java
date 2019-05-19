package com.seas.a10.bizijorge.beans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Recorrido {

    //region Variables
    private int recorridoId;
    private int recorridoUser;
    private long recorridoTiempo;
    private double recorridoDistancia;
    private double recorridoCalorias;
    private Date recorridoFecha;
    private double recorridoCo2;

    private final static String RECORRIDOID = "RecorridoId";
    private final static String RECORRIDOUSER = "ID_USUARIO";
    private final static String RECORRIDOTIEMPO = "RecorridoTiempo";
    private final static String RECORRIDODISTANCIA = "RecorridoDistancia";
    private final static String RECORRIDOCALORIAS = "RecorridoCalorias";
    private final static String RECORRIDOFECHA = "RecorridoFecha";
    private final static String RECORRIDOCO2 = "RecorridoContaminacion";


    //endregion

    //region Constructores

    public Recorrido() {
    }

    public Recorrido(int recorridoId, int recorridoUser, long recorridoTiempo, double recorridoDistancia, double recorridoCalorias, Date recorridoFecha, double recorridoCo2) {
        this.recorridoId = recorridoId;
        this.recorridoUser = recorridoUser;
        this.recorridoTiempo = recorridoTiempo;
        this.recorridoDistancia = recorridoDistancia;
        this.recorridoCalorias = recorridoCalorias;
        this.recorridoFecha = recorridoFecha;
        this.recorridoCo2 = recorridoCo2;
    }

    //endregion

    //region getters y setters

    public int getRecorridoId() {
        return recorridoId;
    }
    public void setRecorridoId(int recorridoId) {
        this.recorridoId = recorridoId;
    }

    public int getRecorridoUser() {
        return recorridoUser;
    }
    public void setRecorridoUser(int recorridoUser) {
        this.recorridoUser = recorridoUser;
    }

    public long getRecorridoTiempo() {
        return recorridoTiempo;
    }
    public void setRecorridoTiempo(long recorridoTiempo) {
        this.recorridoTiempo = recorridoTiempo;
    }

    public double getRecorridoDistancia() {
        return recorridoDistancia;
    }
    public void setRecorridoDistancia(double recorridoDistancia) {
        this.recorridoDistancia = recorridoDistancia;
    }

    public double getRecorridoCalorias() {
        return recorridoCalorias;
    }
    public void setRecorridoCalorias(double recorridoCalorias) {
        this.recorridoCalorias = recorridoCalorias;
    }

    public Date getRecorridoFecha() {
        return recorridoFecha;
    }
    public void setRecorridoFecha(Date recorridoFecha) {
        this.recorridoFecha = recorridoFecha;
    }

    public double getRecorridoCo2() {
        return recorridoCo2;
    }
    public void setRecorridoCo2(double recorridoCo2) {
        this.recorridoCo2 = recorridoCo2;
    }

    //endregion

    /*Recibe un JSON y lo transforma a la entidad deseada*/
    public static ArrayList<Recorrido> getArrayListFromJSon(JSONArray datos){
        ArrayList<Recorrido> lista = null;

        //Creamos el formato con el que nos llegan las fechas
        SimpleDateFormat time =  new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        Recorrido recorrido = null;
        try {
            if(datos!=null && datos.length() > 0 ){
                lista = new ArrayList<Recorrido>();
            }
            for (int i = 0; i < datos.length(); i++) {
                JSONObject json_data = datos.getJSONObject(i);
                recorrido = new Recorrido();
                recorrido.setRecorridoId(json_data.getInt(RECORRIDOID));
                recorrido.setRecorridoUser(json_data.getInt(RECORRIDOUSER));
                recorrido.setRecorridoTiempo(json_data.getLong(RECORRIDOTIEMPO));
                recorrido.setRecorridoDistancia(json_data.getDouble(RECORRIDODISTANCIA));
                try{
                    recorrido.setRecorridoFecha(time.parse(json_data.getString(RECORRIDOFECHA)));
                }catch (Exception ex){
                    recorrido.setRecorridoFecha(null);
                }

                recorrido.setRecorridoCo2(json_data.getDouble(RECORRIDOCO2));


                lista.add(recorrido);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lista;

    }

}
