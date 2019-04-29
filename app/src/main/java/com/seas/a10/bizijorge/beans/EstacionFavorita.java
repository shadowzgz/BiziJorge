package com.seas.a10.bizijorge.beans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class EstacionFavorita {

    //region Variables
    private int idEstacion;
    private int idUsuario;
    private String estacionTitle;

    private static final String IDESTACION = "idEstacion";
    private static final String ID_USUARIO = "ID_USUARIO";
    private static final String ESTACIONTITLE = "estacionTitle";
    //endregion

    //region Constructor
    public EstacionFavorita(){

    }

    public EstacionFavorita(int idEstacion, int idUsuario, String estacionTitle) {
        this.idEstacion = idEstacion;
        this.idUsuario = idUsuario;
        this.estacionTitle = estacionTitle;
    }
    //endregion

    //region getters y setters

    public int getIdEstacion() {
        return idEstacion;
    }
    public void setIdEstacion(int idEstacion) {
        this.idEstacion = idEstacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEstacionTitle() {
        return estacionTitle;
    }
    public void setEstacionTitle(String estacionTitle) {
        this.estacionTitle = estacionTitle;
    }


    //endregiones


    public static ArrayList<EstacionFavorita> getArrayListFromJSon(JSONArray datos){
        ArrayList<EstacionFavorita> lista = null;
        EstacionFavorita estacionFavorita = null;
        try {
            if(datos!=null && datos.length() > 0 ){
                lista = new ArrayList<EstacionFavorita>();
            }
            for (int i = 0; i < datos.length(); i++) {
                JSONObject json_data = datos.getJSONObject(i);
                estacionFavorita = new EstacionFavorita();
                estacionFavorita.setIdUsuario(json_data.getInt(ID_USUARIO));
                estacionFavorita.setIdEstacion(json_data.getInt(IDESTACION));


                lista.add(estacionFavorita);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lista;

    }


}
