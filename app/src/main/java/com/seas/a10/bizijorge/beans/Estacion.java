package com.seas.a10.bizijorge.beans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//Clase de las estaciones jsonArray --> id,title,estado,bicisDisponibles,anclajesDisponibles,lastUpdated,geometry
public class Estacion {

    //region  Variables
    private int id;
    private String title;
    private String estado;
    private int bicisDisponibles;
    private int anclajesDisponibles;
    private Date lastUpdated;
    private String estacionLat;
    private String estacionLong;
    private ArrayList<String> coordenadas;



    private String about;
    private String description;
    private String icon;

    private final static String ID = "id";
    private final static String ABOUT = "about";
    private final static String TITLE = "title";
    private final static String ESTADO = "estado";
    private final static String BICISDISPONIBLES = "bicisDisponibles";
    private final static String ANCLAJESDISPONIBLES = "anclajesDisponibles";
    private final static String LASTUPDATED = "lastUpdated";
    private final static String DESCRIPTION = "description";
    private final static String ICON = "icon";
    private final static String ESTACIONLAT =  "estacionLat";
    private final static String ESTACIONLONG = "geometry";
    //endregion

    //region Constructores
    //Constructo vacío de la clase
    public Estacion(){

    }

    //Contructor de la clase
    public Estacion(int id, String about, String title, String estado, int bicisDisponibles,
                    int anclajesDisponibles, Date lastUpdated, String description, String icon) {
        this.id = id;
        this.about = about;
        this.title = title;
        this.estado = estado;
        this.bicisDisponibles = bicisDisponibles;
        this.anclajesDisponibles = anclajesDisponibles;
        this.lastUpdated = lastUpdated;
        this.description = description;
        this.icon = icon;
    }
    //endregion

    //region Getters y setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getAbout() {
        return about;
    }
    public void setAbout(String about) {
        this.about = about;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getBicisDisponibles() {
        return bicisDisponibles;
    }
    public void setBicisDisponibles(int bicisDisponibles) {
        this.bicisDisponibles = bicisDisponibles;
    }

    public int getAnclajesDisponibles() {
        return anclajesDisponibles;
    }
    public void setAnclajesDisponibles(int anclajesDisponibles) {
        this.anclajesDisponibles = anclajesDisponibles;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getEstacionLat() {
        return estacionLat;
    }
    public void setEstacionLat(String estacionLat) {
        this.estacionLat = estacionLat;
    }

    public String getEstacionLong() {
        return estacionLong;
    }
    public void setEstacionLong(String estacionLong) {
        this.estacionLong = estacionLong;
    }

    public ArrayList<String> getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(ArrayList<String> coordenadas) {
        this.coordenadas = coordenadas;
    }

    //endregion

    public static ArrayList<Estacion> getArrayListFromJSon(JSONArray datos){
        ArrayList<Estacion> lista = null;
        Estacion estacion = null;
        //extraemos del cada esatcion del json un nuevo array con las coordenadas

        //Array de las coordenadas, viene el tipo de dato y las coordenadas en si mismas
        JSONObject coordArray;
        //Almacenamos en este objeto las coordenadas
        JSONArray coordObj = null;

        try {
            if(datos!=null && datos.length() > 0 ){
                lista = new ArrayList<Estacion>();
            }
            for (int i = 0; i < datos.length(); i++) {
                JSONObject json_data = datos.getJSONObject(i);
                estacion = new Estacion();
                estacion.setId(json_data.getInt(ID));
                estacion.setTitle(json_data.getString(TITLE));
                estacion.setEstado(json_data.getString(ESTADO));
                estacion.setBicisDisponibles(json_data.getInt(BICISDISPONIBLES));
                estacion.setAnclajesDisponibles(json_data.getInt(ANCLAJESDISPONIBLES));

                //Recogemos el array de las coordenadas de la estacion
                coordArray = new JSONObject();
                coordArray = json_data.getJSONObject("geometry");
                //Guardamos las coordenadas en este objeto, y despues las setteamos
                coordObj = new JSONArray();
                coordObj = coordArray.getJSONArray("coordinates");
                estacion.setEstacionLong(coordObj.get(0).toString());
                estacion.setEstacionLat(coordObj.get(1).toString());

                lista.add(estacion);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return lista;
    }

}
