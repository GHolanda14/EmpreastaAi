package com.example.emprestaai;

public class Objeto {
    private String nome;
    private String descricao;
    private int imagem;
    private int id;
    private boolean status;

    public Objeto(String nome, String descricao, int imagem, int id) {
        this.nome = nome;
        this.descricao = descricao;
        this.imagem = imagem;
        this.id = id;
        this.status = true;
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

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getStatus(){return status;}

    public void setStatus(boolean status){ this.status = status;}

    @Override
    public String toString() {
        return "Objeto{" +
                "nome='" + nome + '\'' +
                ", id=" + id +
                '}';
    }
}
