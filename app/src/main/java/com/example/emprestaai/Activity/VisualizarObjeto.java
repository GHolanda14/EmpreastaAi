package com.example.emprestaai.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emprestaai.R;

import java.io.ByteArrayOutputStream;

public class VisualizarObjeto extends AppCompatActivity {
    TextView tvNome, tvDescricao,tvStatus;
    Button btnEditar, btnExcluir;
    ImageView ivObjetoVisuObj;
    int EXCLUIR = 3, EDITAR = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_objeto);

        tvNome = (TextView) findViewById(R.id.tvNomeAlugarObj);
        tvStatus = (TextView) findViewById(R.id.tvStatusVisuObj);
        ivObjetoVisuObj = (ImageView) findViewById(R.id.ivObjetoAluObj);
        btnEditar = (Button) findViewById(R.id.btnEditar);
        btnExcluir = (Button) findViewById(R.id.btnStatus);

        Intent intent = getIntent();

        tvNome.setText(intent.getStringExtra("nome"));
        String status = intent.getStringExtra("status");
        ivObjetoVisuObj.setImageBitmap(getImage(intent.getByteArrayExtra("imagem")));
        tvStatus.setText(status);

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tvStatus.getText().toString().equals(getString(R.string.tgStatusOn))){
                    Toast.makeText(VisualizarObjeto.this, "Não é possível deletar um objeto que está emprestado.", Toast.LENGTH_LONG).show();
                }else{
                    Intent ints = new Intent();
                    ints.putExtra("idObjeto", intent.getStringExtra("idObjeto"));
                    setResult(EXCLUIR,ints);
                    VisualizarObjeto.this.finish();
                    }
                }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(VisualizarObjeto.this, NovoObjeto.class);
                intent1.putExtra("idObjeto",intent.getStringExtra("idObjeto"));
                intent1.putExtra("nome",tvNome.getText().toString());
                intent1.putExtra("status",tvStatus.getText().toString());
                intent1.putExtra("imagem",intent.getByteArrayExtra("imagem"));
                startActivityForResult(intent1,EDITAR);
                setResult(EDITAR,intent1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDITAR){
            if(resultCode == RESULT_OK){
                tvNome.setText(data.getStringExtra("nome"));
                String status = data.getStringExtra("status");
                tvStatus.setText(status);
                ivObjetoVisuObj.setImageBitmap(getImage(data.getByteArrayExtra("imagem")));
                setResult(EDITAR,data);
                VisualizarObjeto.this.finish();
            }else if(resultCode == RESULT_CANCELED){
                setResult(RESULT_CANCELED);
                VisualizarObjeto.this.finish();
            }
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

}
