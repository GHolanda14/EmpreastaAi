package com.example.emprestaai.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emprestaai.Adapter.ObjetoAdapter;
import com.example.emprestaai.DAO.ObjetoDAO;
import com.example.emprestaai.DAO.UsuarioDAO;
import com.example.emprestaai.Model.Objeto;
import com.example.emprestaai.R;
import com.example.emprestaai.databinding.ActivityPesquisarObjetosBinding;

import java.util.ArrayList;

public class PesquisarObjetos extends AppCompatActivity implements ObjetoAdapter.ItemClicado{

    private AppBarConfiguration appBarConfiguration;
    private ActivityPesquisarObjetosBinding binding;
    SearchView pesquisa;
    ArrayList<Objeto> objetos;
    RecyclerView lista;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView tvObjVazio;
    int PEDIR = 5, SOLICITADO = 6;
    String idDonoAtual, donoAtual;
    ObjetoDAO objetoDAO;
    UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPesquisarObjetosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Intent intent = getIntent();
        idDonoAtual = intent.getStringExtra("idDonoAtual");
        donoAtual = intent.getStringExtra("donoAtual");
//
//        ArrayList<String> donos = intent.getStringArrayListExtra("donos");
//        ArrayList<String> nomes = intent.getStringArrayListExtra("nomes");
//        ArrayList<String> status = intent.getStringArrayListExtra("status");

        objetos = new ArrayList<Objeto>();
        objetoDAO = new ObjetoDAO(com.example.emprestaai.Activity.PesquisarObjetos.this);
//        objetos.add(new Objeto("Pedro","Escova",(getString(R.string.tgStatusOn)), null));
//        objetos.add(new Objeto("Pedro","Carteira",getString(R.string.tgStatusOff),null));
//        objetos.add(new Objeto("Josué","Fone",getString(R.string.tgStatusOn),null));
        carregarObjetos(intent.getStringExtra("idDonoAtual"));

        tvObjVazio = (TextView) findViewById(R.id.tvObjVazio);
        lista = (RecyclerView) findViewById(R.id.rvPedidos);
        lista.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);

        adapter = new ObjetoAdapter(this,objetos);
        lista.setAdapter(adapter);

        pesquisa = (SearchView) findViewById(R.id.svPesquisa);
        pesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    buscaAoDigitar("");
                }else{
                    buscaAoDigitar(newText.toLowerCase());
                }
                return false;
            }
        });
    }

    private void carregarObjetos(String idDono) {
        Cursor cursor = objetoDAO.procurarObjetos(idDono);
        usuarioDAO = new UsuarioDAO(com.example.emprestaai.Activity.PesquisarObjetos.this);
        Cursor cursor2 = usuarioDAO.pegarNomes();

        ArrayList<Pair<Integer, String>> pares = new ArrayList<Pair<Integer,String>>();
        while (cursor2.moveToNext()){
            pares.add(new Pair<Integer, String>(cursor2.getInt(0),cursor2.getString(1)));
        }
        if(cursor.getCount() == 0){
            listaVazia();
        }else{
            while (cursor.moveToNext()){
                String nomeDono = "";
                for(Pair<Integer,String> par : pares){
                    if(par.first == cursor.getInt(1)){
                        nomeDono = par.second;
                        break;
                    }
                }
                objetos.add(new Objeto(Integer.toString(cursor.getInt(0)),
                        nomeDono,
                        cursor.getString(2),
                        cursor.getString(3),null));
            }
        }
    }

    public void buscaAoDigitar(String texto){
        ArrayList<Objeto> objFiltrados = new ArrayList<Objeto>();

        for(Objeto objeto : objetos){
            if(objeto.getNome().toLowerCase().contains(texto)){
                objFiltrados.add(new Objeto(objeto.getIdObjeto(),objeto.getDono(),objeto.getNome(),objeto.getStatus(),objeto.getImagem()));
            }
        }
        adapter = new ObjetoAdapter(this,objFiltrados);
        lista.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(int posicao, ArrayList<Objeto> listaObjetos) {
        Intent intent1 = new Intent(PesquisarObjetos.this, AlugarObjeto.class);
        Objeto obj = listaObjetos.get(posicao);
        //donoatual é oto
        intent1.putExtra("dono",obj.getDono());
        intent1.putExtra("nome",obj.getNome());
        intent1.putExtra("status",obj.getStatus());
        intent1.putExtra("idObjeto",obj.getIdObjeto());
        intent1.putExtra("idDonoAtual",idDonoAtual);
        intent1.putExtra("donoAtual",donoAtual);
        startActivityForResult(intent1,PEDIR);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PEDIR){
            if(resultCode == SOLICITADO){
                Intent intent1 = new Intent();
                intent1.putExtra("idDonoAtual",data.getStringExtra("idDonoAtual"));
                intent1.putExtra("idObjeto",data.getStringExtra("idObjeto"));
                intent1.putExtra("donoAtual",data.getStringExtra("donoAtual"));
                intent1.putExtra("dono",data.getStringExtra("dono"));
                intent1.putExtra("nome",data.getStringExtra("nome"));
                intent1.putExtra("status",data.getStringExtra("status"));
                intent1.putExtra("periodo",data.getStringExtra("periodo"));
                intent1.putExtra("local",data.getStringExtra("local"));

                setResult(SOLICITADO,intent1);
                PesquisarObjetos.this.finish();
            }
        }
    }

    public void listaVazia(){
        tvObjVazio.setVisibility(View.VISIBLE);
        lista.setVisibility(View.GONE);
    }
}