package com.example.emprestaai.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emprestaai.Adapter.PedidoAdapter;
import com.example.emprestaai.Adapter.SolicitacaoAdapter;
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

public class Solicitacoes extends AppCompatActivity{
    TextView tvNome, tvPeriodo, tvStatus, tvLocalEncontro,tvSolicitante, tvSemSolicitacoes;
    ImageView dImagem;
    ImageButton btnAceitar, btnRecusar;
    ArrayList<Pedido> solicitacoes;
    RecyclerView listaSolicitacoes;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;
    String donoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacoes);

        donoAtual = getIntent().getStringExtra("donoAtual");
        inicializarComponentes();
        solicitacoes = new ArrayList<Pedido>();

        loadingData();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Pedidos")
                .whereEqualTo("dono",donoAtual)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> taskPedidos) {
                        if(taskPedidos.isSuccessful()){
                            if(taskPedidos.getResult().isEmpty()){
                                adapter = new PedidoAdapter(Solicitacoes.this,solicitacoes);
                                listaSolicitacoes.setAdapter(adapter);
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
                                                                solicitacoes.add(p);
                                                                adapter = new SolicitacaoAdapter(Solicitacoes.this,solicitacoes);
                                                                listaSolicitacoes.setAdapter(adapter);
                                                                isListavazia();
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    }
                });
    }

    private void loadingData() {
        progressBar.setVisibility(View.VISIBLE);
        listaSolicitacoes.setVisibility(View.GONE);
        tvSemSolicitacoes.setVisibility(View.GONE);
    }

    public void isListavazia() {
        progressBar.setVisibility(View.GONE);
        if(solicitacoes.isEmpty()){
            tvSemSolicitacoes.setVisibility(View.VISIBLE);
            listaSolicitacoes.setVisibility(View.GONE);
        }else{
            tvSemSolicitacoes.setVisibility(View.GONE);
            listaSolicitacoes.setVisibility(View.VISIBLE);
        }
    }

    private void inicializarComponentes() {
        tvNome = findViewById(R.id.tvNome);
        tvPeriodo = findViewById(R.id.tvPeriodo);
        dImagem = findViewById(R.id.ivObjeto);
        tvStatus = findViewById(R.id.tvStatus);
        tvLocalEncontro = findViewById(R.id.tvLocalEncontro);
        tvSolicitante = findViewById(R.id.tvSolicitante);
        btnAceitar = findViewById(R.id.btnAceitar);
        btnRecusar = findViewById(R.id.btnRecusar);
        listaSolicitacoes = findViewById(R.id.rvSolicitacoes);
        tvSemSolicitacoes = findViewById(R.id.tvSemSolicitacoes);
        progressBar = findViewById(R.id.progressBar);

        listaSolicitacoes.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listaSolicitacoes.setLayoutManager(layoutManager);
    }
}