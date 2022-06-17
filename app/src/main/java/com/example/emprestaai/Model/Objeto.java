package com.example.emprestaai.Model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Objeto implements Serializable {
    /*dono na verdade é o nome do Dono
     */
    private String idObjeto;
    private String dono;
    private String nome;
    private Bitmap imagem;
    private String status;

    public Objeto(String idObjeto,
                  String dono,
                  String nome,
                  String status,
                  Bitmap imagem) {
        this.idObjeto = idObjeto;
        this.dono = dono;
        this.nome = nome;
        this.imagem = imagem;
        this.status = status;
    }

    public String getIdObjeto() { return idObjeto; }

    public void setIdObjeto(String idObjeto) {this.idObjeto = idObjeto;}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Bitmap getImagem() {
        return imagem;
    }

    public void setImagem(Bitmap imagem) {
        this.imagem = imagem;
    }

    public String getStatus(){return status;}

    public void setStatus(String status){ this.status = status;}

    public String getDono() {
        return dono;
    }

    public void setDono(String dono) {
        this.dono = dono;
    }
}
