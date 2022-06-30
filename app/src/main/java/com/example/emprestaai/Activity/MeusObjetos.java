package com.example.emprestaai.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import com.example.emprestaai.Adapter.ObjetoAdapter;
import com.example.emprestaai.Model.Objeto;
import com.example.emprestaai.R;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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
    int ADD = 1, VISUALIZAR=2, EXCLUIR = 3, EDITAR = 4, PEDIR = 5, SOLICITADO = 6, SOLICITACOES = 7;
    String idObjeto;
    String DONO_ATUAL, ID_DONO_ATUAL;
    ImageView imageView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_objetos);

        ID_DONO_ATUAL = FirebaseAuth.getInstance().getUid();
        getDonoAtual();
        inicializarComponentes();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
            .build();
        ImageLoader.getInstance().init(config);

        lista.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);
        objetos = new ArrayList<Objeto>();
        //Todo: Colocar a câmera para tirar foto
        //Todo: Colocar o maps e localização
        fabPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MeusObjetos.this, PesquisarObjetos.class);
                intent1.putExtra("donoAtual",DONO_ATUAL);
                startActivityForResult(intent1,PEDIR);
            }
        });

        fabPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MeusObjetos.this, ListaPedidos.class);
                intent1.putExtra("donoAtual",DONO_ATUAL);
                startActivityForResult(intent1,PEDIR);
            }
        });

        fabSolicitacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MeusObjetos.this, Solicitacoes.class);
                intent1.putExtra("donoAtual",DONO_ATUAL);
                startActivityForResult(intent1,SOLICITACOES);
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MeusObjetos.this, NovoObjeto.class);
                startActivityForResult(intent1,ADD);
            }
        });
    }

    private void inicializarComponentes() {
        tvObjeto = (TextView) findViewById(R.id.tvObjeto);
        tvObjeto.setVisibility(View.GONE);
        lista = (RecyclerView) findViewById(R.id.rvMeusObjetos);
        fabAdd = (FloatingActionButton) findViewById(R.id.add);
        fabPesquisar = (FloatingActionButton) findViewById(R.id.pesquisar);
        fabPedidos = (FloatingActionButton) findViewById(R.id.meusPedidos);
        fabSolicitacoes = (FloatingActionButton) findViewById(R.id.solicitacoes);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageView = (ImageView) findViewById(R.id.imageView);
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
                String idImagem = UUID.randomUUID().toString();
                String path = "objetos/"+ ID_DONO_ATUAL+"/" + idImagem + ".png";
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference sr = storage.getReference(path);

                StorageMetadata metadata = new StorageMetadata.Builder()
                        .setCustomMetadata("Nome",data.getStringExtra("nome")).build();
                UploadTask uploadTask = sr.putBytes(data.getByteArrayExtra("imagem"),metadata);
                loadingData();
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
                            if (downloadUri != null) {
                                String photoStringLink = downloadUri.toString();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                Map<String,Object> obj = new HashMap<>();
                                obj.put("dono",DONO_ATUAL);
                                obj.put("nome",data.getStringExtra("nome"));
                                obj.put("status",data.getStringExtra("status"));
                                obj.put("imagem",downloadUri.toString());
                                obj.put("idImagem",idImagem);

                                db.collection("Objetos").add(obj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Objeto objeto = new Objeto(documentReference.getId(),
                                                DONO_ATUAL,
                                                data.getStringExtra("nome"),
                                                data.getStringExtra("status"),
                                                getImage(data.getByteArrayExtra("imagem")));
                                        objetos.add(objeto);
                                        adapter.notifyItemInserted(objetos.size() - 1);
                                        isListavazia();
                                        Toast.makeText(MeusObjetos.this, "Objeto adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MeusObjetos.this, "Não funcionou", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }

        else if(requestCode == VISUALIZAR){
            if(resultCode == EXCLUIR){
                idObjeto = data.getStringExtra("idObjeto");
                excluirFotoAtual(EXCLUIR,"","",null);
            }

            else if(resultCode == EDITAR){
                idObjeto = data.getStringExtra("idObjeto");
                int posi = getIndexObj();

                if(data.getBooleanExtra("novaFoto",false)){
                    excluirFotoAtual(EDITAR,
                            data.getStringExtra("nome"),
                            data.getStringExtra("status"),
                            data.getByteArrayExtra("imagem"));
                }else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Objetos").document(idObjeto)
                            .update("nome",data.getStringExtra("nome"),
                                    "status",data.getStringExtra("status"))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(MeusObjetos.this, "Objeto alterado com sucesso!", Toast.LENGTH_SHORT).show();
                                        Objeto obj = new Objeto(idObjeto,
                                                DONO_ATUAL,
                                                data.getStringExtra("nome"),
                                                data.getStringExtra("status"),
                                                getImage(data.getByteArrayExtra("imagem")));
                                        objetos.set(posi,obj);
                                        adapter.notifyItemChanged(posi);
                                        isListavazia();
                                    }
                                    else{
                                        Toast.makeText(MeusObjetos.this, "Erro ao alterar objeto!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("msg",e.getMessage());
                                }
                            });
                }


            }
        }else if(requestCode == PEDIR){
            if (resultCode == SOLICITADO){
                idObjeto = data.getStringExtra("idObjeto");
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String,Object> pedido = new HashMap<>();
                pedido.put("idObjeto",idObjeto);
                pedido.put("solicitante",DONO_ATUAL);
                pedido.put("status",data.getStringExtra("status"));
                pedido.put("dono",data.getStringExtra("dono"));
                pedido.put("periodo",data.getStringExtra("periodo"));
                pedido.put("local",data.getStringExtra("local"));

                db.collection("Pedidos").add(pedido).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MeusObjetos.this, "Objeto solicitado", Toast.LENGTH_SHORT).show();
                        fabPedidos.callOnClick();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MeusObjetos.this, "Não funcionou", Toast.LENGTH_LONG).show();
                    }
                });
                }
            }else if(requestCode == SOLICITACOES){
                if(resultCode == RESULT_OK){
                    loadingData();
                    idObjeto = data.getStringExtra("idObjeto");
                    int posi = getIndexObj();

                    Objeto obj = new Objeto(idObjeto,
                            DONO_ATUAL,
                            objetos.get(posi).getNome(),
                            data.getStringExtra("status"),
                            objetos.get(posi).getImagem());
                    objetos.set(posi,obj);
                    adapter.notifyItemChanged(posi);
                    isListavazia();
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
                    carregarObjetos();
                }
            }
        });
    }

    private void carregarObjetos() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Carregando os objetos do usuário atual
        loadingData();

        db.collection("Objetos")
                .whereEqualTo("dono", DONO_ATUAL)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                objetos.clear();
                if(task.isSuccessful()){
                    if(task.getResult().isEmpty()){
                        adapter = new ObjetoAdapter(MeusObjetos.this, objetos);
                        lista.setAdapter(adapter);
                        isListavazia();
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
                                    adapter = new ObjetoAdapter(MeusObjetos.this, objetos);
                                    lista.setAdapter(adapter);
                                    isListavazia();
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    public void guardarFotoBanco(String nome, String status, byte[] imagem) {
        int posi = getIndexObj();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String idImagem = UUID.randomUUID().toString();
        String path = "objetos/" + ID_DONO_ATUAL + "/" + idImagem + ".png";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference sr = storage.getReference(path);

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("Nome", nome).build();
        UploadTask uploadTask = sr.putBytes(imagem, metadata);
        loadingData();
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
                    if (downloadUri != null) {
                        String linkFoto = downloadUri.toString();

                        db.collection("Objetos").document(idObjeto)
                                .update("nome",nome,
                                        "status",status
                                        ,"imagem",linkFoto,
                                        "idImagem",idImagem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Objeto obj = new Objeto(idObjeto,
                                                    DONO_ATUAL,
                                                    nome,
                                                    status,
                                                    getImage(imagem));
                                            objetos.set(posi,obj);
                                            adapter.notifyItemChanged(posi);
                                            isListavazia();
                                            Toast.makeText(MeusObjetos.this, "Objeto alterado com sucesso!", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(MeusObjetos.this, "Erro ao alterar objeto!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("msg",e.getMessage());
                                    }
                                });
                        }
                    }
                }
            });
    }

    public void excluirFotoAtual(int opcao, String nome, String status, byte[] imagem) {
        int posi = getIndexObj();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        loadingData();
        db.collection("Objetos").document(idObjeto).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    StorageReference refer = storage.getReference();
                    String path = "objetos/" + ID_DONO_ATUAL + "/" + document.get("idImagem") + ".png";
                    StorageReference sr = refer.child(path);
                    sr.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            if(opcao == EXCLUIR){
                                db.collection("Objetos").document(idObjeto).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        objetos.remove(posi);
                                        adapter.notifyItemRemoved(posi);
                                        isListavazia();
                                        Toast.makeText(MeusObjetos.this, "Objeto deletado!", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MeusObjetos.this, "Erro ao deletar objeto!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                guardarFotoBanco(nome,status,imagem);
                            }
                        }
                    });
                }
            }
        });
    }
    public void isListavazia(){
        progressBar.setVisibility(View.GONE);
        if (objetos.isEmpty()) {
            lista.setVisibility(View.GONE);
            tvObjeto.setVisibility(View.VISIBLE);
        } else {
            lista.setVisibility(View.VISIBLE);
            tvObjeto.setVisibility(View.GONE);
        }
    }

    public void loadingData(){
        progressBar.setVisibility(View.VISIBLE);
        lista.setVisibility(View.GONE);
        tvObjeto.setVisibility(View.GONE);
    }

    public Bitmap getImage(byte[] imagem) {
        return BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
    }

    public byte[] getBytes(@NonNull Bitmap bitmap) {
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