package com.seas.a10.bizijorge.beans;

import java.util.Date;

//Clase de las estaciones
public class Estacion {

    //region  Variables
    private int id;
    private String about;
    private String title;
    private String estado;
    private int bicisDisponibles;
    private int anclajesDisponibles;
    private Date lastUpdated;
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


    //endregion

}