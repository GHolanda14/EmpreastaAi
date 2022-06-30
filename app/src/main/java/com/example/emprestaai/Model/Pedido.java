package com.example.emprestaai.Model;

public class Pedido{
    String idPedido;
    Objeto objeto;
    String local;
    String periodo;
    String solicitante;
    String status;

    public Pedido(String idPedido, Objeto objeto, String periodo,String local, String solicitante, String status) {
        this.idPedido = idPedido;
        this.objeto = objeto;
        this.local = local;
        this.periodo = periodo;
        this.solicitante = solicitante;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status){ this.status = status;}
}
