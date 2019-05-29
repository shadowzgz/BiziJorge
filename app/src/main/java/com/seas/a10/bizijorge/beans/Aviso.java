package com.seas.a10.bizijorge.beans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//Clase de la entidad de avisos
public class Aviso {

    //region variables
    private int avisoId;
    private String avistoTipo;
    private String avisoTexto;
    private Date avisoFecha;
    private Date avisoFechaCaducidad;
    private int avisoUser;

    private final static String AVISOID = "AvisoId";
    private final static String AVISOTIPO = "AvisoTipo";
    private final static String AVISOTEXTO = "AvisoTexto";
    private final static String AVISOFECHA = "AvisoFecha";
    private final static String AVISOFECHACADUCIDAD = "AvisoFechaCaducidad";
    private final static String AVISOUSER = "ID_USUARIO";


    //endregion

    //region constructores

    public Aviso() {
    }

    public Aviso(int avisoId, String avistoTipo, String avisoTexto, Date avisoFecha, Date avisoFechaCaducidad, int avisoUser) {
        this.avisoId = avisoId;
        this.avistoTipo = avistoTipo;
        this.avisoTexto = avisoTexto;
        this.avisoFecha = avisoFecha;
        this.avisoFechaCaducidad = avisoFechaCaducidad;
        this.avisoUser = avisoUser;
    }

    //endregion

    //region getters y setters

    public int getAvisoId() {
        return avisoId;
    }
    public void setAvisoId(int avisoId) {
        this.avisoId = avisoId;
    }

    public String getAvistoTipo() {
        return avistoTipo;
    }
    public void setAvistoTipo(String avistoTipo) {
        this.avistoTipo = avistoTipo;
    }

    public String getAvisoTexto() {
        return avisoTexto;
    }
    public void setAvisoTexto(String avisoTexto) {
        this.avisoTexto = avisoTexto;
    }

    public Date getAvisoFecha() {
        return avisoFecha;
    }
    public void setAvisoFecha(Date avisoFecha) {
        this.avisoFecha = avisoFecha;
    }

    public Date getAvisoFechaCaducidad() {
        return avisoFechaCaducidad;
    }
    public void setAvisoFechaCaducidad(Date avisoFechaCaducidad) {
        this.avisoFechaCaducidad = avisoFechaCaducidad;
    }

    public int getAvisoUser() {
        return avisoUser;
    }
    public void setAvisoUser(int avisoUser) {
        this.avisoUser = avisoUser;
    }


    //endregion

    //Metodo que recoge los datos de un json y los transforma en la entidad Aviso
    public static ArrayList<Aviso> getArrayListFromJSon(JSONArray datos){
        ArrayList<Aviso> lista = null;

        //Creamos el formato con el que nos llegan las fechas
        SimpleDateFormat time =  new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        Aviso aviso = null;
        try {
            if(datos!=null && datos.length() > 0 ){
                lista = new ArrayList<Aviso>();
            }
            for (int i = 0; i < datos.length(); i++) {
                JSONObject json_data = datos.getJSONObject(i);
                aviso = new Aviso();
                aviso.setAvisoId(json_data.getInt(AVISOID));
                aviso.setAvistoTipo(json_data.getString(AVISOTIPO));
                aviso.setAvisoTexto(json_data.getString(AVISOTEXTO));
                try{
                    aviso.setAvisoFecha(time.parse(json_data.getString(AVISOFECHA)));
                    aviso.setAvisoFechaCaducidad(time.parse(json_data.getString(AVISOFECHACADUCIDAD)));
                }catch (Exception ex){
                    aviso.setAvisoFecha(null);
                    aviso.setAvisoFechaCaducidad(null);
                }
                aviso.setAvisoUser(json_data.getInt(AVISOUSER));


                lista.add(aviso);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lista;

    }


}
