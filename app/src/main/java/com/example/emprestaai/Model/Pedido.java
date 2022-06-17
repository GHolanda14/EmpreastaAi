package com.example.emprestaai.Model;

import java.io.Serializable;

public class Pedido implements Serializable {
    Objeto objeto;
    String local;
    String periodo;

    public Pedido(Objeto objeto, String local, String periodo) {
        this.objeto = objeto;
        this.local = local;
        this.periodo = periodo;
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
}
