package com.seas.a10.bizijorge.beans;

import java.util.Date;

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


}
