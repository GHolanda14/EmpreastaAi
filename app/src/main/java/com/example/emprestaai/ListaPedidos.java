package com.example.emprestaai;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListaPedidos extends AppCompatActivity{
    ArrayList<Pedido> pedidos;
    RecyclerView listaPedidos;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);

        Intent intent = getIntent();
        listaPedidos = (RecyclerView) findViewById(R.id.rvPedidos);
        listaPedidos.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        listaPedidos.setLayoutManager(layoutManager);
        //TODO: Conseguir a imagem aparecer aqui (Drawable.createFromPath(data.getStringExtra("url"))
        pedidos = new ArrayList<Pedido>();
        ArrayList<String> donos = intent.getStringArrayListExtra("donos");
        ArrayList<String> nomes = intent.getStringArrayListExtra("nomes");
        ArrayList<String> periodos = intent.getStringArrayListExtra("periodos");
        ArrayList<String> descricoes = intent.getStringArrayListExtra("descricoes");
        ArrayList<String> locais = intent.getStringArrayListExtra("locais");
        ArrayList<String> status = intent.getStringArrayListExtra("status");

        for(int i = 0; i < nomes.size(); i++){
            Objeto obj = new Objeto(donos.get(i),
                    nomes.get(i),
                    descricoes.get(i),
                    status.get(i),
                    getDrawable(R.drawable.img));
            pedidos.add(new Pedido(obj,periodos.get(i),locais.get(i)));
        }

        adapter = new PedidoAdapter(this,pedidos);
        listaPedidos.setAdapter(adapter);
    }
}