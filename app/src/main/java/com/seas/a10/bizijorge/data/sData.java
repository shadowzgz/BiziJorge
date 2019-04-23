package com.seas.a10.bizijorge.data;

import com.seas.a10.bizijorge.beans.Cliente;
import com.seas.a10.bizijorge.beans.Estacion;

import java.util.ArrayList;

public class sData {

    private static Cliente  cliente;
    private static boolean ubiGuaranteed;
    private static ArrayList<Estacion> listadoEstaciones;


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

//endregion

}
