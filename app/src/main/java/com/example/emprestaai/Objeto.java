package com.example.emprestaai;

import android.graphics.drawable.Drawable;
import android.net.Uri;

public class Objeto {
    private String nome;
    private String descricao;
    private Drawable imagem;
    private boolean status;

    public Objeto(String nome,
                  String descricao,
                  boolean status,
                  Drawable imagem) {
        this.nome = nome;
        this.descricao = descricao;
        this.imagem = imagem;
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Drawable getImagem() {
        return imagem;
    }

    public void setImagem(Drawable imagem) {
        this.imagem = imagem;
    }

    public boolean getStatus(){return status;}

    public void setStatus(boolean status){ this.status = status;}
}
