package com.example.emprestaai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VisualizarObjeto extends AppCompatActivity {
    TextView tvNome, tvDescricao,tvStatus;
    Button btnEditar, btnExcluir;
    int EXCLUIR = 3, EDITAR = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_objeto);

        tvNome = (TextView) findViewById(R.id.tvNomeAlugarObj);
        tvDescricao = (TextView) findViewById(R.id.tvDescricaoAlugarObj);
        tvStatus = (TextView) findViewById(R.id.tvStatusVisuObj);
        btnEditar = (Button) findViewById(R.id.btnEditar);
        btnExcluir = (Button) findViewById(R.id.btnStatus);

        Intent intent = getIntent();

        tvNome.setText(intent.getStringExtra("nome"));
        tvDescricao.setText(intent.getStringExtra("descricao"));
        String status = intent.getStringExtra("status");
        tvStatus.setText(status);

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvStatus.getText().toString().equals(getString(R.string.tgStatusOff))){
                    Toast.makeText(VisualizarObjeto.this, "Não é possível deletar um objeto que está emprestado.", Toast.LENGTH_LONG).show();
                }else{
                    Intent ints = new Intent(VisualizarObjeto.this,com.example.emprestaai.MeusObjetos.class);
                    ints.putExtra("posicao",intent.getIntExtra("posicao",0));
                    setResult(EXCLUIR,ints);
                    VisualizarObjeto.this.finish();
                }
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(VisualizarObjeto.this, com.example.emprestaai.NovoObjeto.class);
                intent1.putExtra("donoAtual",intent.getStringExtra("donoAtual"));
                intent1.putExtra("nome",tvNome.getText().toString());
                intent1.putExtra("descricao",tvDescricao.getText().toString());
                intent1.putExtra("status",tvStatus.getText().toString());
                intent1.putExtra("posicao",intent.getIntExtra("posicao",0));
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
                tvDescricao.setText(data.getStringExtra("descricao"));
                String status = data.getStringExtra("status");
                tvStatus.setText(status);
                setResult(EDITAR,data);
            }else if(resultCode == RESULT_CANCELED){
                setResult(RESULT_CANCELED);
                VisualizarObjeto.this.finish();
            }
        }
    }
}
