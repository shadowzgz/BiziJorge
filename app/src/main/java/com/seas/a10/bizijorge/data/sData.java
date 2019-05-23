package com.seas.a10.bizijorge.data;

import com.seas.a10.bizijorge.beans.Aviso;
import com.seas.a10.bizijorge.beans.Cliente;
import com.seas.a10.bizijorge.beans.Estacion;
import com.seas.a10.bizijorge.beans.EstacionFavorita;
import com.seas.a10.bizijorge.beans.Incidencia;
import com.seas.a10.bizijorge.beans.Mensaje;
import com.seas.a10.bizijorge.beans.Recorrido;

import java.util.ArrayList;

public class sData {

    private static Cliente  cliente;
    private static boolean ubiGuaranteed;
    private static ArrayList<Estacion> listadoEstaciones;
    private static ArrayList<EstacionFavorita> listadoEstacionesFavoritas;
    private static ArrayList<Incidencia> listadoIncidencias;
    private static ArrayList<Mensaje>   listadoMensajes;
    private static Incidencia incidenciaDetalles;
    private static ArrayList<Recorrido> listadoRecorridos;
    private static ArrayList<Aviso> listadoAvisos;
    private static ArrayList<Aviso> listadoAviososNoCaducados;

    //region Getters y setters

    public static Cliente getCliente() {
        return cliente;
    }
    public static void setCliente(Cliente cliente) {
        sData.cliente = cliente;
    }

    public static ArrayList<Estacion> getListadoEstaciones() {
        return listadoEstaciones;
    }
    public static void setListadoEstaciones(ArrayList<Estacion> listadoEstaciones) {
        sData.listadoEstaciones = listadoEstaciones;
    }

    public static ArrayList<EstacionFavorita> getListadoEstacionesFavoritas() {
        return listadoEstacionesFavoritas;
    }
    public static void setListadoEstacionesFavoritas(ArrayList<EstacionFavorita> listadoEstacionesFavoritas) {
        sData.listadoEstacionesFavoritas = listadoEstacionesFavoritas;
    }

    public static ArrayList<Incidencia> getListadoIncidencias() {
        return listadoIncidencias;
    }
    public static void setListadoIncidencias(ArrayList<Incidencia> listadoIncidencias) {
        sData.listadoIncidencias = listadoIncidencias;
    }

    public static ArrayList<Mensaje> getListadoMensajes() {
        return listadoMensajes;
    }
    public static void setListadoMensajes(ArrayList<Mensaje> listadoMensajes) {
        sData.listadoMensajes = listadoMensajes;
    }

    public static Incidencia getIncidenciaDetalles() {
        return incidenciaDetalles;
    }
    public static void setIncidenciaDetalles(Incidencia incidenciaDetalles) {
        sData.incidenciaDetalles = incidenciaDetalles;
    }

    public static ArrayList<Recorrido> getListadoRecorridos() {
        return listadoRecorridos;
    }
    public static void setListadoRecorridos(ArrayList<Recorrido> listadoRecorridos) {
        sData.listadoRecorridos = listadoRecorridos;
    }

    public static ArrayList<Aviso> getListadoAvisos() {
        return listadoAvisos;
    }
    public static void setListadoAvisos(ArrayList<Aviso> listadoAvisos) {
        sData.listadoAvisos = listadoAvisos;
    }

    public static ArrayList<Aviso> getListadoAviososNoCaducados() {
        return listadoAviososNoCaducados;
    }
    public static void setListadoAviososNoCaducados(ArrayList<Aviso> listadoAviososNoCaducados) {
        sData.listadoAviososNoCaducados = listadoAviososNoCaducados;
    }
    //endregion

}
