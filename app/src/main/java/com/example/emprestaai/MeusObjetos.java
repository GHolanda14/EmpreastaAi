package com.example.emprestaai;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MeusObjetos extends AppCompatActivity implements ObjetoAdapter.ItemClicado {
    ArrayList<Objeto> objetos;
    ArrayList<Pedido> meusPedidos;
    RecyclerView lista;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView tvObjeto;
    FloatingActionButton fabAdd, fabPesquisar, fabPedidos, fabSolicitacoes;
    int ADD = 1, VISUALIZAR=2, EXCLUIR = 3, EDITAR = 4, PEDIR = 5, SOLICITADO = 6;
    String donoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_objetos);

        Intent intent = getIntent();

        tvObjeto = (TextView) findViewById(R.id.tvObjeto);
        lista = (RecyclerView) findViewById(R.id.rvPedidos);
        fabAdd = (FloatingActionButton) findViewById(R.id.add);
        fabPesquisar = (FloatingActionButton) findViewById(R.id.pesquisar);
        fabPedidos = (FloatingActionButton) findViewById(R.id.meusPedidos);
        fabSolicitacoes = (FloatingActionButton) findViewById(R.id.solicitacoes);
        lista.setHasFixedSize(true);

        donoAtual = intent.getStringExtra("donoAtual");
        layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);
        //TODO: Conseguir a imagem aparecer aqui (Drawable.createFromPath(data.getStringExtra("url"))
        objetos = new ArrayList<Objeto>();
        adapter = new ObjetoAdapter(this,objetos);
        lista.setAdapter(adapter);

        meusPedidos = new ArrayList<Pedido>();
//        meusPedidos.fabAdd(new Pedido(new Objeto("Pedro",
//                "Escova",
//                "Testando aqui a funcionalidade",
//                "Solicitado", getDrawable(R.drawable.img)),
//                "Rua da pedra",
//                "25 Jun - 30 Jun"));

        isListavazia();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MeusObjetos.this, NovoObjeto.class);
                intent1.putExtra("donoAtual",donoAtual);
                startActivityForResult(intent1,ADD);
            }
        });

        //Todo: Solicitar ou recusar pedidos

        fabPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> donos = new ArrayList<>();
                ArrayList<String> nomes = new ArrayList<>();
                ArrayList<String> descricoes = new ArrayList<>();
                ArrayList<String> status = new ArrayList<>();
                for(Objeto obj : objetos){
                    donos.add(obj.getDono());
                    nomes.add(obj.getNome());
                    descricoes.add(obj.getDescricao());
                    status.add(obj.getStatus());
                }
                //Todo: Ver se vale a pena passar os objetos ou pelo menos alterar
                Intent intent1 = new Intent(MeusObjetos.this,com.example.emprestaai.PesquisarObjetos.class);
                intent1.putExtra("donoAtual",donoAtual);
                intent1.putStringArrayListExtra("donos",donos);
                intent1.putStringArrayListExtra("nomes",nomes);
                intent1.putStringArrayListExtra("descricoes",descricoes);
                intent1.putStringArrayListExtra("status",status);
                startActivityForResult(intent1,PEDIR);
            }
        });

        fabPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> donos = new ArrayList<>();
                ArrayList<String> nomes = new ArrayList<>();
                ArrayList<String> descricoes = new ArrayList<>();
                ArrayList<String> periodos = new ArrayList<>();
                ArrayList<String> locais = new ArrayList<>();
                ArrayList<String> status = new ArrayList<>();
                for(Pedido pedido : meusPedidos){
                    donos.add(pedido.getObjeto().getNome());
                    nomes.add(pedido.getObjeto().getNome());
                    descricoes.add(pedido.getObjeto().getDescricao());
                    periodos.add(pedido.getPeriodo());
                    locais.add(pedido.getLocal());
                    status.add(pedido.getObjeto().getStatus());
                }

                Intent intent1 = new Intent(MeusObjetos.this,com.example.emprestaai.ListaPedidos.class);
                intent1.putExtra("donoAtual",donoAtual);
                intent1.putStringArrayListExtra("donos",donos);
                intent1.putStringArrayListExtra("nomes",nomes);
                intent1.putStringArrayListExtra("descricoes",descricoes);
                intent1.putStringArrayListExtra("periodos",periodos);
                intent1.putStringArrayListExtra("status",status);
                intent1.putStringArrayListExtra("locais",locais);
                startActivityForResult(intent1,PEDIR);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD) {
            if (resultCode == RESULT_OK) {
                donoAtual = data.getStringExtra("donoAtual");
                Objeto obj = new Objeto(donoAtual,
                        data.getStringExtra("nome"),
                        data.getStringExtra("descricao"),
                        data.getStringExtra("status"),
                        getDrawable(R.drawable.img));
                objetos.add(obj);
                adapter.notifyItemInserted(objetos.size()-1);
                isListavazia();
            }
        }else if(requestCode == VISUALIZAR){
            if(resultCode == EXCLUIR){
                int posi = data.getIntExtra("posicao",0);
                objetos.remove(posi);
                adapter.notifyItemRemoved(posi);
                Toast.makeText(this, "Objeto excluído", Toast.LENGTH_SHORT).show();
                isListavazia();
            }else if(resultCode == EDITAR){
                donoAtual = data.getStringExtra("donoAtual");
                Objeto obj = new Objeto(donoAtual,
                        data.getStringExtra("nome"),
                        data.getStringExtra("descricao"),
                        data.getStringExtra("status"),
                        getDrawable(R.drawable.img));
                objetos.set(data.getIntExtra("posicao",0),obj);
                adapter.notifyItemChanged(data.getIntExtra("posicao",0));
            }
        }else if(requestCode == PEDIR){
            if (resultCode == SOLICITADO){
                donoAtual = data.getStringExtra("donoAtual");
                Objeto obj = new Objeto(data.getStringExtra("dono"),
                        data.getStringExtra("nome"),
                        data.getStringExtra("descricao"),
                        data.getStringExtra("status"),
                        getDrawable(R.drawable.img));
                Pedido pedido = new Pedido(obj,data.getStringExtra("local"),data.getStringExtra("periodo"));
                meusPedidos.add(pedido);
                Log.d("msg","Adicionei o pedido");
            }
        }
    }

    @Override
    public void onItemClicked(int posicao) {
        Intent intent = new Intent(MeusObjetos.this,com.example.emprestaai.VisualizarObjeto.class);
        Objeto obj = objetos.get(posicao);
        intent.putExtra("donoAtual",obj.getDono());
        intent.putExtra("nome",obj.getNome());
        intent.putExtra("descricao",obj.getDescricao());
        intent.putExtra("status",obj.getStatus());
        intent.putExtra("posicao",posicao);
        startActivityForResult(intent,VISUALIZAR);
    }

    public void isListavazia(){
        if (objetos.isEmpty()) {
            lista.setVisibility(View.GONE);
            tvObjeto.setVisibility(View.VISIBLE);
        } else {
            lista.setVisibility(View.VISIBLE);
            tvObjeto.setVisibility(View.GONE);
        }
    }
}