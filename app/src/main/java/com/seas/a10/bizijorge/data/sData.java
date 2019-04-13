package com.seas.a10.bizijorge.data;

import com.seas.a10.bizijorge.beans.Cliente;

public class sData {

    private static Cliente  cliente;


    //region Getters y setters

    public static Cliente getCliente() {
        return cliente;
    }
    public static void setCliente(Cliente cliente) {
        sData.cliente = cliente;
    }


    //endregion

}
