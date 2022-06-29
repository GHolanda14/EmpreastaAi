package com.example.emprestaai.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emprestaai.Adapter.PedidoAdapter;
import com.example.emprestaai.DAO.PedidoDAO;
import com.example.emprestaai.Model.Objeto;
import com.example.emprestaai.Model.Pedido;
import com.example.emprestaai.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

public class ListaPedidos extends AppCompatActivity{
    ArrayList<Pedido> pedidos;
    RecyclerView listaPedidos;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView tvListPedidosVazio;
    PedidoDAO pedidoDAO;
    String donoAtual;
    int VISUALIZAR = 2;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);

        Intent intent = getIntent();
        donoAtual = intent.getStringExtra("donoAtual");

        listaPedidos = (RecyclerView) findViewById(R.id.rvPedidos);
        tvListPedidosVazio = (TextView) findViewById(R.id.tvListPedidosVazio);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listaPedidos.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listaPedidos.setLayoutManager(layoutManager);
        pedidos = new ArrayList<Pedido>();

        loadingData();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Pedidos")
                .whereEqualTo("solicitante",donoAtual)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> taskPedidos) {
                        if(taskPedidos.isSuccessful()){
                            if(taskPedidos.getResult().isEmpty()){
                                adapter = new PedidoAdapter(ListaPedidos.this,pedidos);
                                listaPedidos.setAdapter(adapter);
                                isListavazia();
                            }else{
                                for(DocumentSnapshot snapshotPedidos : taskPedidos.getResult()){
                                    db.collection("Objetos")
                                            .document(snapshotPedidos.get("idObjeto").toString())
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> taskObjetos) {
                                                    if(taskObjetos.isSuccessful()){
                                                        ImageLoader imageLoader = ImageLoader.getInstance();
                                                        DocumentSnapshot snapshotObjetos = taskObjetos.getResult();
                                                        imageLoader.loadImage(snapshotObjetos.getString("imagem"), new SimpleImageLoadingListener() {
                                                            @Override
                                                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                                                Objeto obj = new Objeto(snapshotPedidos.getId(),
                                                                        snapshotObjetos.getString("dono"),
                                                                        snapshotObjetos.getString("nome"),
                                                                        snapshotObjetos.getString("status"),
                                                                        loadedImage);
                                                                Pedido p = new Pedido(snapshotPedidos.getId(),
                                                                        obj,
                                                                        snapshotPedidos.getString("periodo"),
                                                                        snapshotPedidos.getString("local"),
                                                                        snapshotPedidos.getString("solicitante"),
                                                                        snapshotPedidos.getString("status"));
                                                                pedidos.add(p);
                                                                adapter = new PedidoAdapter(ListaPedidos.this,pedidos);
                                                                listaPedidos.setAdapter(adapter);
                                                                isListavazia();
                                                            }
                                                        });
                                                }
                                            };
                                });
                        }
                    }
                };

           setResult(VISUALIZAR,intent);
    }
                });
    }

    public void loadingData(){
        progressBar.setVisibility(View.VISIBLE);
        listaPedidos.setVisibility(View.GONE);
        tvListPedidosVazio.setVisibility(View.GONE);
    }

    public void isListavazia() {
        progressBar.setVisibility(View.GONE);
        if(pedidos.isEmpty()){
            tvListPedidosVazio.setVisibility(View.VISIBLE);
            listaPedidos.setVisibility(View.GONE);
        }else{
            tvListPedidosVazio.setVisibility(View.GONE);
            listaPedidos.setVisibility(View.VISIBLE);
        }
    }
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}