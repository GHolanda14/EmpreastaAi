package com.example.emprestaai.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.example.emprestaai.R;
import com.google.android.material.textfield.TextInputLayout;

public class NovoObjeto extends AppCompatActivity {
    Button btnFoto, btnAddObj;
    ImageView imageView;
    TextInputLayout tiNomeObj;
    ToggleButton tgStatus;
    int EDITAR = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_objeto);

        Intent intent = getIntent();

        tiNomeObj = (TextInputLayout) findViewById(R.id.tiNomeObj);
        tgStatus = (ToggleButton) findViewById(R.id.tgStatus);
        btnFoto = (Button) findViewById(R.id.btnFoto);
        imageView =(ImageView) findViewById(R.id.ivFoto);
        btnAddObj = (Button) findViewById(R.id.btnAddObj);



        if(intent.hasExtra("nome")){
            tiNomeObj.getEditText().setText(intent.getStringExtra("nome"));
            tgStatus.setChecked(intent.getStringExtra("status").equals(getString(R.string.tgStatusOn)) ? true : false);
        }

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(NovoObjeto.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        PackageManager.PERMISSION_GRANTED);
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //long selectedImageUri = ContentUris.parseId(Uri.parse());
                //Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(                        .getContentResolver(), selectedImageUri,MediaStore.Images.Thumbnails.MICRO_KIND,                        null );
                startActivityForResult(intent1,3);
                //Log.d("Msg","depoiss de chamar: "+intent1.getData().getPath().);
                //selectedImageUri = ContentUris.parseId(Uri.parse(intent1.getData().getPath()));
            }
        });

        btnAddObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tiNomeObj.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(NovoObjeto.this, "Preencha o nome!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent1 = new Intent();
                    intent1.putExtra("donoAtual",intent.getStringExtra("donoAtual"));
                    intent1.putExtra("idDonoAtual",intent.getStringExtra("idDonoAtual"));
                    intent1.putExtra("idObjeto",intent.getStringExtra("idObjeto"));
                    intent1.putExtra("nome", tiNomeObj.getEditText().getText().toString());
                    intent1.putExtra("status", tgStatus.getText().toString());
                    intent1.putExtra("posicao",intent.getIntExtra("posicao",0));
                    if(imageView.getDrawable() == null){
                        intent1.putExtra("url", "");//Valor padrão
                    }else{
                        intent1.putExtra("url", imageView.getDrawable().toString());
                    }
                    setResult(RESULT_OK, intent1);
                    NovoObjeto.this.finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null) {
            Uri imgSelecionada = data.getData();
            imageView.setImageURI(imgSelecionada);
        }
    }
}