package com.example.emprestaai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MeusObjetos extends AppCompatActivity {
    ArrayList<Objeto> objetos;
    RecyclerView lista;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView tvObjeto;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_objetos);

        tvObjeto = (TextView) findViewById(R.id.tvObjeto);
        lista = (RecyclerView) findViewById(R.id.rvObjetos);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        lista.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);

        objetos = new ArrayList<Objeto>();
//        objetos.add(new Objeto("Escova","Testando aqui a funcionalidade",R.drawable.img,1));
//        objetos.add(new Objeto("Carteira","Serve para guardar cartao de credito, dinheiro tbm",R.drawable.img,2));
//        objetos.add(new Objeto("Fone","Melhore sua experciencia ouvindo musica com qualidade",R.drawable.img,3));
        adapter = new ObjetoAdapter(this,objetos);
        lista.setAdapter(adapter);

        if(objetos.isEmpty()){
            lista.setVisibility(View.GONE);
            tvObjeto.setVisibility(View.VISIBLE);
        }else{
            lista.setVisibility(View.VISIBLE);
            tvObjeto.setVisibility(View.GONE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}