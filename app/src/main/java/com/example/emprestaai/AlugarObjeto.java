package com.example.emprestaai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AlugarObjeto extends AppCompatActivity {
    TextView tvNomeAlugarObj, tvDescricasoAlugarObj;
    Button btnStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alugar_objeto);

        Intent intent = getIntent();
        tvNomeAlugarObj = (TextView) findViewById(R.id.tvNomeAlugarObj);
        tvDescricasoAlugarObj = (TextView) findViewById(R.id.tvDescricaoAlugarObj);
        btnStatus = (Button) findViewById(R.id.btnStatus);

        tvNomeAlugarObj.setText(intent.getStringExtra("nome"));
        tvDescricasoAlugarObj.setText(intent.getStringExtra("descricao"));

        String status = intent.getStringExtra("status");
        btnStatus.setText(status.equals(getString(R.string.tgStatusOn)) ? getString(R.string.btnPedir) : status);
        btnStatus.setEnabled(status.equals(getString(R.string.tgStatusOn)));

        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Página de Intenção do pedido (local,dia,devolução, contato)

            }
        });
    }
}