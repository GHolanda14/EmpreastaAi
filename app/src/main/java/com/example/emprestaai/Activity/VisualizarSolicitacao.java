package com.example.emprestaai.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emprestaai.R;

public class VisualizarSolicitacao extends AppCompatActivity {
    ImageView ivObjeto;
    TextView tvNomeObjSol, tvStatusSol, tvSolicitanteSol, tvPeriodoSol, tvLocalSol;
    Button btnAceitar, btnRecusar, btnVerLocal;
    String idPedido, idObjeto;
    int ACEITO = 8, RECUSADO = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_solicitacao);

        inicializarComponentes(getIntent());
        idPedido = getIntent().getStringExtra("idPedido");
        idObjeto = getIntent().getStringExtra("idObjeto");

        btnVerLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizarSolicitacao.this,Mapa.class);
                intent.putExtra("local",tvLocalSol.getText().toString());
                startActivity(intent);
            }
        });

        btnAceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("idPedido",idPedido);
                intent.putExtra("idObjeto",idObjeto);
                intent.putExtra("status",getString(R.string.hEmprestado));
                setResult(ACEITO,intent);
                VisualizarSolicitacao.this.finish();
            }
        });

        btnRecusar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("idPedido",idPedido);
                intent.putExtra("idObjeto",idObjeto);
                intent.putExtra("status",getString(R.string.hRecusado));
                setResult(ACEITO,intent);
                VisualizarSolicitacao.this.finish();
            }
        });
    }

    private void inicializarComponentes(Intent intent) {
        ivObjeto = (ImageView) findViewById(R.id.ivObjetoSol);
        tvNomeObjSol = (TextView) findViewById(R.id.tvNomeObjSol);
        tvStatusSol = (TextView) findViewById(R.id.tvStatusSol);
        tvSolicitanteSol = (TextView) findViewById(R.id.tvSolicitanteSol);
        tvPeriodoSol = (TextView) findViewById(R.id.tvPeriodoSol);
        tvLocalSol = (TextView) findViewById(R.id.tvLocalSol);
        btnAceitar = (Button) findViewById(R.id.btnAceitar);
        btnRecusar = (Button) findViewById(R.id.btnRecusar);
        btnVerLocal = (Button) findViewById(R.id.btnVerLocal);

        ivObjeto.setImageBitmap(getImage(intent.getByteArrayExtra("imagem")));
        tvNomeObjSol.setText(intent.getStringExtra("nome"));
        tvStatusSol.setText(intent.getStringExtra("status"));
        tvSolicitanteSol.setText(intent.getStringExtra("solicitante"));
        tvPeriodoSol.setText(intent.getStringExtra("periodo"));
        tvLocalSol.setText(intent.getStringExtra("local"));
        idObjeto = intent.getStringExtra("idObjeto");
        idPedido = intent.getStringExtra("idPedido");

        if(tvStatusSol.getText().toString().equals(getString(R.string.hEmprestado))){
            btnRecusar.setVisibility(View.GONE);
            btnAceitar.setVisibility(View.GONE);
        }
    }

    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


}