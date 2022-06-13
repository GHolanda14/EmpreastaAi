package com.example.emprestaai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
        donoAtual = "Gabriel Holanda";
//        donoAtual = intent.getStringExtra("donoAtual");
//
//        ArrayList<String> donos = intent.getStringArrayListExtra("donos");
//        ArrayList<String> nomes = intent.getStringArrayListExtra("nomes");
//        ArrayList<String> descricoes = intent.getStringArrayListExtra("descricoes");
//        ArrayList<String> status = intent.getStringArrayListExtra("status");

        objetos = new ArrayList<Objeto>();
        objetos.add(new Objeto("Pedro","Escova","Testando aqui a funcionalidade",(getString(R.string.tgStatusOn)), getDrawable(R.drawable.img)));
        objetos.add(new Objeto("Pedro","Carteira","Serve para guardar cartao de credito, dinheiro tbm",getString(R.string.tgStatusOff),getDrawable(R.drawable.img)));
        objetos.add(new Objeto("Josué","Fone","Melhore sua experciencia ouvindo musica com qualidade",getString(R.string.tgStatusOn),getDrawable(R.drawable.img)));

//        for(int i = 0; i < nomes.size(); i++){
//            Log.d("Msg",donoAtual);
//            if(!donos.get(i).equals(donoAtual)){
//                objetos.fabAdd(new Objeto(donos.get(i),nomes.get(i),descricoes.get(i),status.get(i),getDrawable(R.drawable.img)));
//            }
//        }

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
        Intent intent1 = new Intent(PesquisarObjetos.this,com.example.emprestaai.AlugarObjeto.class);
        Objeto obj = objetos.get(posicao);
        //donoatual é oto
        intent1.putExtra("dono",obj.getDono());
        intent1.putExtra("nome",obj.getNome());
        intent1.putExtra("descricao",obj.getDescricao());
        intent1.putExtra("status",obj.getStatus());
        intent1.putExtra("posicao",posicao);
        startActivityForResult(intent1,PEDIR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PEDIR){
            if(resultCode == SOLICITADO){

                Objeto obj = new Objeto(data.getStringExtra("dono"),
                        data.getStringExtra("nome"),
                        data.getStringExtra("descricao"),
                        data.getStringExtra("status"),
                        getDrawable(R.drawable.img));
                objetos.set(data.getIntExtra("posicao",0),obj);
                adapter.notifyItemChanged(data.getIntExtra("posicao",0));

                Toast.makeText(this, "Objeto solicitado com sucesso!", Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent();
                intent1.putExtra("donoAtual",donoAtual);
                intent1.putExtra("dono",data.getStringExtra("dono"));
                intent1.putExtra("nome",data.getStringExtra("nome"));
                intent1.putExtra("descricao",data.getStringExtra("descricao"));
                intent1.putExtra("status",data.getStringExtra("status"));
                intent1.putExtra("periodo",data.getStringExtra("periodo"));
                intent1.putExtra("local",data.getStringExtra("local"));
                setResult(SOLICITADO,intent1);
            }
        }
    }
}