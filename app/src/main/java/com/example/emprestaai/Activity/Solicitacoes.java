package com.example.emprestaai.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Solicitacoes extends AppCompatActivity implements SolicitacaoAdapter.PedidoClicado {
    TextView tvNome, tvPeriodo, tvStatus, tvLocalEncontro,tvSolicitante, tvSemSolicitacoes;
    ImageView dImagem;
    ArrayList<Pedido> solicitacoes;
    RecyclerView listaSolicitacoes;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;
    String donoAtual;
    int VISUALIZAR = 2, ACEITO = 3, RECUSADO = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacoes);

        donoAtual = getIntent().getStringExtra("donoAtual");
        inicializarComponentes();

        listaSolicitacoes.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listaSolicitacoes.setLayoutManager(layoutManager);
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
                                                                Objeto obj = new Objeto(snapshotObjetos.getId(),
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
        listaSolicitacoes = findViewById(R.id.rvSolicitacoes);
        tvSemSolicitacoes = findViewById(R.id.tvSemSolicitacoes);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onPedidoClicked(int posicao, ArrayList<Pedido> pedidos) {
        Intent intent = new Intent(Solicitacoes.this, VisualizarSolicitacao.class);
        Pedido ped = this.solicitacoes.get(posicao);
        intent.putExtra("solicitante",ped.getSolicitante());
        intent.putExtra("local",ped.getLocal());
        intent.putExtra("status",ped.getStatus());
        intent.putExtra("periodo",ped.getPeriodo());
        intent.putExtra("imagem",getBytes(ped.getObjeto().getImagem()));
        intent.putExtra("nome",ped.getObjeto().getNome());
        intent.putExtra("idObjeto",ped.getObjeto().getIdObjeto());
        intent.putExtra("idPedido",ped.getIdPedido());
        startActivityForResult(intent,VISUALIZAR);
    }
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            loadingData();
            int i = getIndexPedido(data.getStringExtra("idPedido"));
            Pedido p = new Pedido(solicitacoes.get(i).getIdPedido(),
                    solicitacoes.get(i).getObjeto(),
                    solicitacoes.get(i).getPeriodo(),
                    solicitacoes.get(i).getLocal(),
                    solicitacoes.get(i).getSolicitante(),
                    data.getStringExtra("status"));

            solicitacoes.set(i,p);
            adapter = new PedidoAdapter(Solicitacoes.this,solicitacoes);
            listaSolicitacoes.setAdapter(adapter);
            isListavazia();
            if(data.getStringExtra("status").equals(getString(R.string.hEmprestado))){
                Toast.makeText(Solicitacoes.this, "Objeto emprestado!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Solicitacoes.this, "Objeto recusado!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public int getIndexPedido(String idPedido){
        for(Pedido p : solicitacoes){
            if(p.getIdPedido().equals(idPedido)){
                return solicitacoes.indexOf(p);
            }
        }
        return 0;
    }

}