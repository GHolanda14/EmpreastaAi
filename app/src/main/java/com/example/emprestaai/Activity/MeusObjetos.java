package com.example.emprestaai.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emprestaai.Adapter.ObjetoAdapter;
import com.example.emprestaai.DAO.ObjetoDAO;
import com.example.emprestaai.Model.Objeto;
import com.example.emprestaai.Model.Pedido;
import com.example.emprestaai.R;
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
    String donoAtual, idObjeto,idDonoAtual;;
    ObjetoDAO objetoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_objetos);

        Intent intent = getIntent();

        tvObjeto = (TextView) findViewById(R.id.tvObjeto);
        tvObjeto.setVisibility(View.GONE);
        lista = (RecyclerView) findViewById(R.id.rvPedidos);
        fabAdd = (FloatingActionButton) findViewById(R.id.add);
        fabPesquisar = (FloatingActionButton) findViewById(R.id.pesquisar);
        fabPedidos = (FloatingActionButton) findViewById(R.id.meusPedidos);
        fabSolicitacoes = (FloatingActionButton) findViewById(R.id.solicitacoes);
        lista.setHasFixedSize(true);

        donoAtual = intent.getStringExtra("donoAtual");
        idDonoAtual = intent.getStringExtra("idDonoAtual");
        layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);

        objetos = new ArrayList<Objeto>();
        objetoDAO = new ObjetoDAO(com.example.emprestaai.Activity.MeusObjetos.this);
        Cursor cursor = objetoDAO.procurarObjetosDono(idDonoAtual);

        if(cursor.getCount() == 0){
            isListavazia();
        }else{
            while(cursor.moveToNext()){
                Objeto obj = new Objeto(Integer.toString(cursor.getInt(0)),
                        donoAtual,
                        cursor.getString(2),
                        cursor.getString(3),
                        null);
                objetos.add(obj);
            }
        }
        cursor.close();

        adapter = new ObjetoAdapter(this,objetos);
        lista.setAdapter(adapter);

        meusPedidos = new ArrayList<Pedido>();

        //Todo: Solicitar ou recusar pedidos

        fabPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> donos = new ArrayList<>();
                ArrayList<String> nomes = new ArrayList<>();
                ArrayList<String> status = new ArrayList<>();
                for(Objeto obj : objetos){
                    donos.add(obj.getDono());
                    nomes.add(obj.getNome());
                    status.add(obj.getStatus());
                }
                //Todo: Ver se vale a pena passar os objetos ou pelo menos alterar
                Intent intent1 = new Intent(MeusObjetos.this, PesquisarObjetos.class);
                intent1.putExtra("donoAtual",donoAtual);
                intent1.putExtra("idObjeto",idObjeto);
                intent1.putExtra("idDonoAtual",idDonoAtual);
                intent1.putStringArrayListExtra("donos",donos);
                intent1.putStringArrayListExtra("nomes",nomes);
                intent1.putStringArrayListExtra("status",status);
                startActivityForResult(intent1,PEDIR);
            }
        });

        fabPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> donos = new ArrayList<>();
                ArrayList<String> nomes = new ArrayList<>();
                ArrayList<String> periodos = new ArrayList<>();
                ArrayList<String> locais = new ArrayList<>();
                ArrayList<String> status = new ArrayList<>();
                for(Pedido pedido : meusPedidos){
                    donos.add(pedido.getObjeto().getDono());
                    nomes.add(pedido.getObjeto().getNome());
                    periodos.add(pedido.getPeriodo());
                    locais.add(pedido.getLocal());
                    status.add(pedido.getObjeto().getStatus());
                }

                Intent intent1 = new Intent(MeusObjetos.this, ListaPedidos.class);
                intent1.putExtra("donoAtual",donoAtual);
                intent1.putStringArrayListExtra("donos",donos);
                intent1.putStringArrayListExtra("nomes",nomes);
                intent1.putStringArrayListExtra("periodos",periodos);
                intent1.putStringArrayListExtra("status",status);
                intent1.putStringArrayListExtra("locais",locais);
                startActivityForResult(intent1,PEDIR);
            }
        });

        fabSolicitacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MeusObjetos.this, Solicitacoes.class);
                startActivity(intent1);
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MeusObjetos.this, NovoObjeto.class);
                intent1.putExtra("donoAtual",donoAtual);
                intent1.putExtra("idDonoAtual",idDonoAtual);
                startActivityForResult(intent1,ADD);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD) {
            if (resultCode == RESULT_OK) {
                donoAtual = data.getStringExtra("donoAtual");
                idDonoAtual = data.getStringExtra("idDonoAtual");

                idObjeto = objetoDAO.addObjeto(idDonoAtual,
                        data.getStringExtra("nome"),
                        data.getStringExtra("status"));
                if(!idObjeto.equals("-1")) {
                    Objeto obj = new Objeto(idObjeto,
                            donoAtual,
                            data.getStringExtra("nome"),
                            data.getStringExtra("status"),
                            null);
                    objetos.add(obj);
                    adapter.notifyItemInserted(objetos.size() - 1);
                    isListavazia();
                }
            }
        }else if(requestCode == VISUALIZAR){
            if(resultCode == EXCLUIR){
                donoAtual = data.getStringExtra("donoAtual");
                idObjeto = data.getStringExtra("idObjeto");
                idDonoAtual = data.getStringExtra("idDonoAtual");
                int posi = data.getIntExtra("posicao",0);
                objetos.remove(posi);
                adapter.notifyItemRemoved(posi);
                Toast.makeText(this, "Objeto excluído", Toast.LENGTH_SHORT).show();
                isListavazia();
            }else if(resultCode == EDITAR){
                donoAtual = data.getStringExtra("donoAtual");
                idObjeto = data.getStringExtra("idObjeto");
                idDonoAtual = data.getStringExtra("idDonoAtual");
                objetoDAO.updateObjeto(idObjeto,
                        idDonoAtual,
                        data.getStringExtra("nome"),
                        data.getStringExtra("status"));

                Objeto obj = new Objeto(idObjeto,
                        donoAtual,
                        data.getStringExtra("nome"),
                        data.getStringExtra("status"),
                        null);
                objetos.set(data.getIntExtra("posicao",0),obj);
                adapter.notifyItemChanged(data.getIntExtra("posicao",0));
            }
        }else if(requestCode == PEDIR){
            if (resultCode == SOLICITADO){
                donoAtual = data.getStringExtra("donoAtual");
                idObjeto = data.getStringExtra("idObjeto");
                idDonoAtual = data.getStringExtra("idDonoAtual");

                Objeto obj = new Objeto(idObjeto,
                        data.getStringExtra("dono"),
                        data.getStringExtra("nome"),
                        data.getStringExtra("status"),
                        null);
                Pedido pedido = new Pedido(obj,data.getStringExtra("local"),data.getStringExtra("periodo"));
                meusPedidos.add(pedido);
                Log.d("msg","Adicionei o pedido, do dono: "+data.getStringExtra("dono"));
            }
        }
    }

    @Override
    public void onItemClicked(int posicao, ArrayList<Objeto> objetos) {
        Intent intent = new Intent(MeusObjetos.this, VisualizarObjeto.class);
        Objeto obj = this.objetos.get(posicao);
        intent.putExtra("donoAtual",obj.getDono());
        intent.putExtra("idObjeto",obj.getIdObjeto());
        intent.putExtra("idDonoAtual",idDonoAtual);
        intent.putExtra("nome",obj.getNome());
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