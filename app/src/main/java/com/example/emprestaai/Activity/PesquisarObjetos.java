package com.example.emprestaai.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emprestaai.Adapter.ObjetoAdapter;
import com.example.emprestaai.DAO.UsuarioDAO;
import com.example.emprestaai.Model.Objeto;
import com.example.emprestaai.R;
import com.example.emprestaai.databinding.ActivityPesquisarObjetosBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.ByteArrayOutputStream;
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
    String donoAtual;
    UsuarioDAO usuarioDAO;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPesquisarObjetosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Intent intent = getIntent();
        donoAtual = intent.getStringExtra("donoAtual");
        tvObjVazio = (TextView) findViewById(R.id.tvObjVazio);
        lista = (RecyclerView) findViewById(R.id.rvResultadoPesquisa);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        objetos = new ArrayList<Objeto>();
        carregarObjetos(donoAtual);


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

    private void carregarObjetos(String donoAtual) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Carregando os objetos do usuário atual
        loadingData();

        db.collection("Objetos")
                .whereNotEqualTo("dono",donoAtual)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        objetos.clear();
                        if(task.isSuccessful()){
                            if(task.getResult().isEmpty()){
                                progressBar.setVisibility(View.GONE);
                                isListavazia();
                                adapter = new ObjetoAdapter(PesquisarObjetos.this, objetos);
                                lista.setAdapter(adapter);
                            }else {
                                ImageLoader imageLoader = ImageLoader.getInstance();
                                for(DocumentSnapshot snapshot : task.getResult()) {
                                    imageLoader.loadImage(snapshot.getString("imagem"), new SimpleImageLoadingListener() {
                                        @Override
                                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                            Objeto obj = new Objeto(snapshot.getId(),
                                                    snapshot.getString("dono"),
                                                    snapshot.getString("nome"),
                                                    snapshot.getString("status"),
                                                    loadedImage);
                                            objetos.add(obj);
                                            adapter = new ObjetoAdapter(PesquisarObjetos.this, objetos);
                                            lista.setAdapter(adapter);
                                            progressBar.setVisibility(View.GONE);
                                            isListavazia();
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
        lista.setVisibility(View.GONE);
        tvObjVazio.setVisibility(View.GONE);
    }

    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
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
        intent1.putExtra("dono",obj.getDono());
        intent1.putExtra("nome",obj.getNome());
        intent1.putExtra("status",obj.getStatus());
        intent1.putExtra("idObjeto",obj.getIdObjeto());
        intent1.putExtra("imagem",getBytes(obj.getImagem()));
        startActivityForResult(intent1,PEDIR);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PEDIR){
            if(resultCode == SOLICITADO){
                Intent intent1 = new Intent();
                intent1.putExtra("idObjeto",data.getStringExtra("idObjeto"));
                intent1.putExtra("dono",data.getStringExtra("dono"));
                intent1.putExtra("nome",data.getStringExtra("nome"));
                intent1.putExtra("status",data.getStringExtra("status"));
                intent1.putExtra("periodo",data.getStringExtra("periodo"));
                intent1.putExtra("local",data.getStringExtra("local"));
                intent1.putExtra("imagem",data.getByteArrayExtra("imagem"));

                setResult(SOLICITADO,intent1);
                PesquisarObjetos.this.finish();
            }
        }
    }

    public void isListavazia() {
        if (objetos.isEmpty()) {
            tvObjVazio.setVisibility(View.VISIBLE);
            lista.setVisibility(View.GONE);
        } else {
            tvObjVazio.setVisibility(View.GONE);
            lista.setVisibility(View.VISIBLE);
        }
    }
}