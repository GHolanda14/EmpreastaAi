package com.example.emprestaai.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emprestaai.Adapter.PedidoAdapter;
import com.example.emprestaai.DAO.ObjetoDAO;
import com.example.emprestaai.DAO.PedidoDAO;
import com.example.emprestaai.DAO.UsuarioDAO;
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
    PedidoDAO pedidoDAO;
    ObjetoDAO objetoDAO;
    String idDonoAtual;
    int VISUALIZAR = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);

        Intent intent = getIntent();
        listaPedidos = (RecyclerView) findViewById(R.id.rvPedidos);
        tvListPedidosVazio = (TextView) findViewById(R.id.tvListPedidosVazio);
        listaPedidos.setHasFixedSize(true);

        idDonoAtual = intent.getStringExtra("idDonoAtual");
        layoutManager = new LinearLayoutManager(this);
        listaPedidos.setLayoutManager(layoutManager);
        pedidos = new ArrayList<Pedido>();

        pedidoDAO = new PedidoDAO(com.example.emprestaai.Activity.ListaPedidos.this);
        objetoDAO = new ObjetoDAO(com.example.emprestaai.Activity.ListaPedidos.this);
        Cursor cursor = pedidoDAO.buscarPedidos(idDonoAtual);

        if(cursor.getCount() != 0){
            while (cursor.moveToNext()) {
                Cursor cursor1 = objetoDAO.getObjeto(cursor.getInt(1));
                cursor1.moveToNext();

                UsuarioDAO usuarioDAO = new UsuarioDAO(ListaPedidos.this);
                Cursor cursor2 = usuarioDAO.getNome(cursor1.getInt(1));
                cursor2.moveToNext();
                String nomeDono = cursor2.getString(0);
                Objeto obj = new Objeto(Integer.toString(cursor1.getInt(0)),
                        nomeDono,
                        cursor1.getString(2),
                        cursor1.getString(3),
                        getImage(cursor1.getBlob(4)));
                Pedido pedido = new Pedido(Integer.toString(cursor.getInt(0)),
                        obj,cursor.getString(4),
                        cursor.getString(5),
                        idDonoAtual);
                pedidos.add(pedido);
            }
        }
        cursor.close();
        setResult(VISUALIZAR,intent);

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

    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}