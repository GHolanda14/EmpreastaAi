package com.example.emprestaai.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emprestaai.Adapter.PedidoAdapter;
import com.example.emprestaai.Model.Objeto;
import com.example.emprestaai.Model.Pedido;
import com.example.emprestaai.R;

import java.util.ArrayList;

public class ListaPedidos extends AppCompatActivity{
    ArrayList<Pedido> pedidos;
    RecyclerView listaPedidos;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView tvListPedidosVazio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);

        Intent intent = getIntent();
        listaPedidos = (RecyclerView) findViewById(R.id.rvPedidos);
        tvListPedidosVazio = (TextView) findViewById(R.id.tvListPedidosVazio);
        listaPedidos.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        listaPedidos.setLayoutManager(layoutManager);
        //TODO: Conseguir a imagem aparecer aqui (Drawable.createFromPath(data.getStringExtra("url"))
        pedidos = new ArrayList<Pedido>();
        ArrayList<String> donos = intent.getStringArrayListExtra("donos");
        ArrayList<String> nomes = intent.getStringArrayListExtra("nomes");
        ArrayList<String> periodos = intent.getStringArrayListExtra("periodos");
        ArrayList<String> locais = intent.getStringArrayListExtra("locais");
        ArrayList<String> status = intent.getStringArrayListExtra("status");

        for(int i = 0; i < nomes.size(); i++){
            Objeto obj = new Objeto("",donos.get(i),
                    nomes.get(i),
                    status.get(i),
                    null);
            pedidos.add(new Pedido(obj,locais.get(i),periodos.get(i)));
        }

        if(pedidos.isEmpty()){
            tvListPedidosVazio.setVisibility(View.VISIBLE);
            listaPedidos.setVisibility(View.GONE);
        }else{
            tvListPedidosVazio.setVisibility(View.GONE);
            listaPedidos.setVisibility(View.VISIBLE);
        }

        adapter = new PedidoAdapter(this,pedidos);
        listaPedidos.setAdapter(adapter);
    }
}