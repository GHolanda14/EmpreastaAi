package com.example.emprestaai;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    int PEDIR = 5, SOLICITADO = 6;
    String donoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPesquisarObjetosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Intent intent = getIntent();

        donoAtual = intent.getStringExtra("donoAtual");

        ArrayList<String> donos = intent.getStringArrayListExtra("donos");
        ArrayList<String> nomes = intent.getStringArrayListExtra("nomes");
        ArrayList<String> descricoes = intent.getStringArrayListExtra("descricoes");
        ArrayList<String> status = intent.getStringArrayListExtra("status");

        objetos = new ArrayList<Objeto>();
        objetos.add(new Objeto("Pedro","Escova","Testando aqui a funcionalidade",(getString(R.string.tgStatusOn)), getDrawable(R.drawable.img)));
        objetos.add(new Objeto("Pedro","Carteira","Serve para guardar cartao de credito, dinheiro tbm",getString(R.string.tgStatusOff),getDrawable(R.drawable.img)));
        objetos.add(new Objeto("Josué","Fone","Melhore sua experciencia ouvindo musica com qualidade",getString(R.string.tgStatusOn),getDrawable(R.drawable.img)));

        for(int i = 0; i < nomes.size(); i++){
            Log.d("Msg",donoAtual);
            if(!donos.get(i).equals(donoAtual)){
                objetos.add(new Objeto(donos.get(i),nomes.get(i),descricoes.get(i),status.get(i),getDrawable(R.drawable.img)));
            }
        }

        lista = (RecyclerView) findViewById(R.id.rvObjetos);
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

    public void buscaAoDigitar(String texto){
        ArrayList<Objeto> objFiltrados = new ArrayList<Objeto>();

        for(Objeto objeto : objetos){
            if(objeto.getNome().toLowerCase().contains(texto) && !objeto.getDono().equals(donoAtual)){
                objFiltrados.add(new Objeto(objeto.getDono(),objeto.getNome(), objeto.getDescricao(),objeto.getStatus(),objeto.getImagem()));
            }
        }
        adapter = new ObjetoAdapter(this,objFiltrados);
        lista.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(int posicao) {
        Intent intent = new Intent(PesquisarObjetos.this,com.example.emprestaai.AlugarObjeto.class);
        Objeto obj = objetos.get(posicao);
        intent.putExtra("dono",obj.getDono());
        intent.putExtra("nome",obj.getNome());
        intent.putExtra("descricao",obj.getDescricao());
        intent.putExtra("status",obj.getStatus());
        intent.putExtra("posicao",posicao);
        startActivityForResult(intent,PEDIR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PEDIR){
            if(resultCode == SOLICITADO){

            }
        }
    }
}