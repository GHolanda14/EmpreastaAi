package com.example.emprestaai;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emprestaai.Adapter.ObjetoAdapter;
import com.example.emprestaai.DAO.ObjetoDAO;
import com.example.emprestaai.DAO.PedidoDAO;
import com.example.emprestaai.Model.Objeto;
import com.example.emprestaai.Model.Pedido;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MeusObjetos extends AppCompatActivity implements ObjetoAdapter.ItemClicado {
    ArrayList<Objeto> objetos;
    RecyclerView lista;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView tvObjeto;
    FloatingActionButton fabAdd, fabPesquisar, fabPedidos, fabSolicitacoes;
    int ADD = 1, VISUALIZAR=2, EXCLUIR = 3, EDITAR = 4, PEDIR = 5, SOLICITADO = 6;
    String idObjeto;
    String DONO_ATUAL, ID_DONO_ATUAL;
    ObjetoDAO objetoDAO;
    PedidoDAO pedidoDAO;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_objetos);

        ID_DONO_ATUAL = FirebaseAuth.getInstance().getUid();
        getDonoAtual();
        Intent intent = getIntent();

        tvObjeto = (TextView) findViewById(R.id.tvObjeto);
        tvObjeto.setVisibility(View.GONE);
        lista = (RecyclerView) findViewById(R.id.rvPedidos);
        fabAdd = (FloatingActionButton) findViewById(R.id.add);
        fabPesquisar = (FloatingActionButton) findViewById(R.id.pesquisar);
        fabPedidos = (FloatingActionButton) findViewById(R.id.meusPedidos);
        fabSolicitacoes = (FloatingActionButton) findViewById(R.id.solicitacoes);
        lista.setHasFixedSize(true);
        imageView = (ImageView) findViewById(R.id.imageView);
        //Todo: Linkar a imagem ao firestore

        layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);

        objetos = new ArrayList<Objeto>();
        objetoDAO = new ObjetoDAO(MeusObjetos.this);
        /*Cursor cursor = objetoDAO.procurarObjetosDono(ID_DONO_ATUAL);

        //Carregando meus objetos do banco
        if(cursor.getCount() == 0){
            isListavazia();
        }else{
            while(cursor.moveToNext()){
                Objeto obj = new Objeto(Integer.toString(cursor.getInt(0)),
                        DONO_ATUAL,
                        cursor.getString(2),
                        cursor.getString(3),
                        getImage(cursor.getBlob(4)));
                objetos.add(obj);
            }
        }
        cursor.close();

        adapter = new ObjetoAdapter(this,objetos);
        lista.setAdapter(adapter);*/
        //Todo: Solicitar ou recusar pedidos

        fabPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MeusObjetos.this, PesquisarObjetos.class);
                intent1.putExtra("idDonoAtual",ID_DONO_ATUAL);
                startActivityForResult(intent1,PEDIR);
            }
        });

        fabPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MeusObjetos.this, ListaPedidos.class);
                intent1.putExtra("idDonoAtual",ID_DONO_ATUAL);
                startActivityForResult(intent1,PEDIR);
            }
        });

        fabSolicitacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent1 = new Intent(MeusObjetos.this, Solicitacoes.class);
                //startActivity(intent1);
            }
        });
        fabSolicitacoes.setVisibility(View.GONE);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MeusObjetos.this, NovoObjeto.class);
                startActivityForResult(intent1,ADD);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meus_objetos,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        Intent intent1 = new Intent(MeusObjetos.this,Login.class);
        startActivity(intent1);
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD) {
            if (resultCode == RESULT_OK) {
                String path = "objetos/"+ ID_DONO_ATUAL+"/" +UUID.randomUUID() + ".png";
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference sr = storage.getReference(path);

                StorageMetadata metadata = new StorageMetadata.Builder()
                        .setCustomMetadata("text",data.getStringExtra("nome")).build();

                UploadTask uploadTask = sr.putBytes(data.getByteArrayExtra("imagem"),metadata);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return sr.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Toast.makeText(MeusObjetos.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                            if (downloadUri != null) {
                                String photoStringLink = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                                System.out.println("Upload " + photoStringLink);

                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                Map<String,Object> obj = new HashMap<>();
                                obj.put("dono",DONO_ATUAL);
                                obj.put("nome",data.getStringExtra("nome"));
                                obj.put("status",data.getStringExtra("status"));
                                obj.put("imagem",downloadUri.toString());

                                db.collection("Objetos").add(obj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Objeto objeto = new Objeto(idObjeto,
                                                DONO_ATUAL,
                                                data.getStringExtra("nome"),
                                                data.getStringExtra("status"),
                                                getImage(data.getByteArrayExtra("imagem")));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MeusObjetos.this, "Não funcionou", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            }
                        else {
                            // Handle failures
                            // ...
                        }
                    }
                });

//            idObjeto = objetoDAO.addObjeto(ID_DONO_ATUAL,
//                        data.getStringExtra("nome"),
//                        data.getStringExtra("status"),
//                        data.getByteArrayExtra("imagem"));
//                if(!idObjeto.equals("-1")) {
//                    Objeto obj = new Objeto(idObjeto,
//                            DONO_ATUAL,
//                            data.getStringExtra("nome"),
//                            data.getStringExtra("status"),
//                            getImage(data.getByteArrayExtra("imagem")));
//                    objetos.add(obj);
//                    adapter.notifyItemInserted(objetos.size() - 1);
//                    isListavazia();
                }

            }
        else if(requestCode == VISUALIZAR){
            if(resultCode == EXCLUIR){
                idObjeto = data.getStringExtra("idObjeto");
                int posi = getIndexObj();

                objetos.remove(posi);
                adapter.notifyItemRemoved(posi);
                Toast.makeText(this, "Objeto excluído com sucesso", Toast.LENGTH_SHORT).show();
                isListavazia();
            }else if(resultCode == EDITAR){
                idObjeto = data.getStringExtra("idObjeto");
                int posi = getIndexObj();

                objetoDAO.updateObjeto(idObjeto,
                        ID_DONO_ATUAL,
                        data.getStringExtra("nome"),
                        data.getStringExtra("status"));

                Objeto obj = new Objeto(idObjeto,
                        DONO_ATUAL,
                        data.getStringExtra("nome"),
                        data.getStringExtra("status"),
                        getImage(data.getByteArrayExtra("imagem")));
                objetos.set(posi,obj);
                adapter.notifyItemChanged(posi);
            }
        }else if(requestCode == PEDIR){
            if (resultCode == SOLICITADO){
                pedidoDAO = new PedidoDAO(MeusObjetos.this);
                idObjeto = data.getStringExtra("idObjeto");
                String idPedido = pedidoDAO.addPedido(idObjeto,ID_DONO_ATUAL,
                        data.getStringExtra("dono"),
                        data.getStringExtra("periodo"),
                        data.getStringExtra("local"));

                if(!idPedido.equals("-1")) {
                    Objeto obj = new Objeto(idObjeto,
                            data.getStringExtra("dono"),
                            data.getStringExtra("nome"),
                            data.getStringExtra("status"),
                            getImage(data.getByteArrayExtra("imagem")));
                    Pedido pedido = new Pedido(idPedido, obj, data.getStringExtra("periodo"),data.getStringExtra("local"), ID_DONO_ATUAL);
                    fabPedidos.callOnClick();
                }
            }
        }
    }

    @Override
    public void onItemClicked(int posicao, ArrayList<Objeto> objetos) {
        Intent intent = new Intent(MeusObjetos.this, VisualizarObjeto.class);
        Objeto obj = this.objetos.get(posicao);
        intent.putExtra("donoAtual", DONO_ATUAL);
        intent.putExtra("idObjeto",obj.getIdObjeto());
        intent.putExtra("idDonoAtual",ID_DONO_ATUAL);
        intent.putExtra("nome",obj.getNome());
        intent.putExtra("status",obj.getStatus());
        intent.putExtra("imagem",getBytes(obj.getImagem()));
        startActivityForResult(intent,VISUALIZAR);
    }

    public void getDonoAtual(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Usuarios").document(ID_DONO_ATUAL);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null){
                    DONO_ATUAL = value.getString("nome");
                }
            }
        });
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

    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public int getIndexObj(){
        for(Objeto o : objetos){
            if(o.getIdObjeto().equals(idObjeto)){
                return objetos.indexOf(o);
            }
        }
        return 0;
    }
}