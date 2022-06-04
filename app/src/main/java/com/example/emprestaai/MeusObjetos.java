package com.example.emprestaai;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MeusObjetos extends AppCompatActivity implements ObjetoAdapter.ItemClicado {
    ArrayList<Objeto> objetos;
    RecyclerView lista;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView tvObjeto;
    FloatingActionButton add, pesquisar;
    int ADD = 1, VISUALIZAR=2, EXCLUIR = 3, EDITAR = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_objetos);

        tvObjeto = (TextView) findViewById(R.id.tvObjeto);
        lista = (RecyclerView) findViewById(R.id.rvObjetos);
        add = (FloatingActionButton) findViewById(R.id.add);
        pesquisar = (FloatingActionButton) findViewById(R.id.pesquisar);
        lista.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);
        //TODO: Conseguir a imagem aparecer aqui
        objetos = new ArrayList<Objeto>();
        //objetos.add(new Objeto("Escova","Testando aqui a funcionalidade",1,true,Drawable.createFromPath("app/src/main/res/drawable-v24/img.jpg")));
        //objetos.add(new Objeto("Carteira","Serve para guardar cartao de credito, dinheiro tbm",2,false,Drawable.createFromPath("img.jpg")));
        //objetos.add(new Objeto("Fone","Melhore sua experciencia ouvindo musica com qualidade",3,true,Drawable.createFromPath("img.jpg")));
        adapter = new ObjetoAdapter(this,objetos);
        lista.setAdapter(adapter);

        isListavazia();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MeusObjetos.this, NovoObjeto.class);
                startActivityForResult(intent,ADD);
            }
        });

        pesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeusObjetos.this,PesquisarObjetos.class);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD) {
            if (resultCode == RESULT_OK) {
                Objeto obj = new Objeto(data.getStringExtra("nome"),
                        data.getStringExtra("descricao"),
                        data.getBooleanExtra("status", true),
                        Drawable.createFromPath(data.getStringExtra("url")));
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
                Objeto obj = new Objeto(data.getStringExtra("nome"),
                        data.getStringExtra("descricao"),
                        data.getBooleanExtra("status",true),
                        null);
                objetos.set(data.getIntExtra("posicao",0),obj);
                adapter.notifyItemChanged(data.getIntExtra("posicao",0));
            }
        }
    }

    @Override
    public void onItemClicked(int posicao) {
        Intent intent = new Intent(MeusObjetos.this,com.example.emprestaai.VisualizarObjeto.class);
        Objeto obj = objetos.get(posicao);
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