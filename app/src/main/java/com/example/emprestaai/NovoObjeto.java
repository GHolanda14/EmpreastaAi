package com.example.emprestaai;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;

public class NovoObjeto extends AppCompatActivity {
    Button btnFoto, btnAddObj;
    ImageView imageView;
    TextInputLayout tiNomeObj;
    ToggleButton tgStatus;
    int EDITAR = 4,BOTAR_IMAGEM=7;
    byte[] imagem;
    Bitmap a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_objeto);

        Intent intent = getIntent();
        ActivityCompat.requestPermissions(NovoObjeto.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        tiNomeObj = (TextInputLayout) findViewById(R.id.tiNomeObj);
        tgStatus = (ToggleButton) findViewById(R.id.tgStatus);
        btnFoto = (Button) findViewById(R.id.btnFoto);
        imageView =(ImageView) findViewById(R.id.ivFoto);
        btnAddObj = (Button) findViewById(R.id.btnAddObj);
        imageView.setVisibility(View.GONE);


        if(intent.hasExtra("nome")){
            tiNomeObj.getEditText().setText(intent.getStringExtra("nome"));
            tgStatus.setChecked(intent.getStringExtra("status").equals(getString(R.string.tgStatusOn)) ? true : false);
            imagem = intent.getByteArrayExtra("imagem");
            imageView.setImageBitmap(getImage(imagem));
            imageView.setVisibility(View.VISIBLE);
        }

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1,0);
            }
        });

        btnAddObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tiNomeObj.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(NovoObjeto.this, "Preencha o nome!", Toast.LENGTH_SHORT).show();
                }
                else if(imageView.getVisibility() == View.GONE){
                    Toast.makeText(NovoObjeto.this, "Escolha ou tire uma foto do objeto!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent1 = new Intent();
                    intent1.putExtra("idObjeto",intent.getStringExtra("idObjeto"));
                    intent1.putExtra("nome", tiNomeObj.getEditText().getText().toString());
                    intent1.putExtra("status", tgStatus.getText().toString());
                    intent1.putExtra("imagem", imagem);
                    setResult(RESULT_OK, intent1);
                    NovoObjeto.this.finish();
                }
            }
        });
    }
    //Todo: Adicionar campo de StatusPedido no PedidoDAO
    //Todo: Criar uma lógica para poder termos uma atividade chamada Solicitações   
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data.getData() != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            a = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            a.compress(Bitmap.CompressFormat.PNG,0,byteArrayOutputStream);
            imagem = byteArrayOutputStream.toByteArray();
            imageView.setVisibility(View.VISIBLE);
//            adicionarImagemDB();
        }
    }

//    public void adicionarImagemDB(){
//        String path = "objetos/"+ UUID.randomUUID() + ".png";
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference sr = storage.getReference(path);
//
//        StorageMetadata metadata = new StorageMetadata.Builder()
//                .setCustomMetadata("text",tiNomeObj.getEditText().getText().toString()).build();
//
//        UploadTask uploadTask = sr.putBytes(imagem,metadata);
//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Uri url = taskSnapshot.getUploadSessionUri();
//                Toast.makeText(NovoObjeto.this, url.toString(), Toast.LENGTH_SHORT).show();
//                Log.d("msg",url.toString());
//            }
//        });
//    }
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}