package com.example.emprestaai.Model;

import java.io.Serializable;

public class Pedido implements Serializable {
    String idPedido;
    Objeto objeto;
    String local;
    String periodo;
    String solicitante;

    public Pedido(String idPedido, Objeto objeto, String periodo,String local, String solicitante) {
        this.idPedido = idPedido;
        this.objeto = objeto;
        this.local = local;
        this.periodo = periodo;
        this.solicitante = solicitante;
    }

    public Objeto getObjeto() {
        return objeto;
    }

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
    public String getIdPedido() {
        return idPedido;
    }

    public void setId(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

}
