package com.example.emprestaai;

import android.content.Intent;
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

import com.google.android.material.textfield.TextInputLayout;

public class NovoObjeto extends AppCompatActivity {
    Button btnFoto, btnAddObj;
    ImageView imageView;
    TextInputLayout tiNomeObj, tiDescObj;
    ToggleButton tgStatus;
    int EDITAR = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_objeto);

        Intent intent1 = getIntent();

        tiNomeObj = (TextInputLayout) findViewById(R.id.tiNomeObj);
        tiDescObj = (TextInputLayout) findViewById(R.id.tiDescObj);
        tgStatus = (ToggleButton) findViewById(R.id.tgStatus);
        btnFoto = (Button) findViewById(R.id.btnFoto);
        imageView =(ImageView) findViewById(R.id.ivFoto);
        btnAddObj = (Button) findViewById(R.id.btnAddObj);

        if(intent1.hasExtra("nome")){
            tiNomeObj.getEditText().setText(intent1.getStringExtra("nome"));
            tiDescObj.getEditText().setText(intent1.getStringExtra("descricao"));
            tgStatus.setChecked(intent1.getStringExtra("status").equals(getString(R.string.tgStatusOn)) ? true : false);
        }

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,3);
            }
        });

        btnAddObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tiNomeObj.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(NovoObjeto.this, "Preencha o nome!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("nome", tiNomeObj.getEditText().getText().toString());
                    intent.putExtra("descricao", tiDescObj.getEditText().getText().toString());
                    intent.putExtra("status", tgStatus.getText().toString());
                    if(imageView.getDrawable() == null){
                        intent.putExtra("url", "");//Valor padrão
                    }else{
                        intent.putExtra("url", imageView.getDrawable().toString());
                    }
                    setResult(RESULT_OK, intent);
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