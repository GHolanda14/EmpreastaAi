package com.example.emprestaai;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class SolicitarObjeto extends AppCompatActivity {
    TextInputLayout tiLocal, tiRecebimento, tiDevolucao, tiContato;
    Button btnSolicitar, btnRecebimento;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_objeto);

        tiLocal = (TextInputLayout) findViewById(R.id.tiLocal);
        tiRecebimento = (TextInputLayout) findViewById(R.id.tiRecebimento);
        tiDevolucao = (TextInputLayout) findViewById(R.id.tiDevolucao);
        tiContato = (TextInputLayout) findViewById(R.id.tiContato);
        btnSolicitar = (Button) findViewById(R.id.btnSolicitar);

        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tiLocal.getEditText().getText().toString().isEmpty() ||
                tiRecebimento.getEditText().getText().toString().isEmpty() ||
                tiDevolucao.getEditText().getText().toString().isEmpty() ||
                tiContato.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(SolicitarObjeto.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("Msg",tiLocal.getEditText().getText().toString() + tiRecebimento.getEditText().getText().toString()+
                            tiDevolucao.getEditText().getText().toString() + tiContato.getEditText().getText().toString());
                }
            }
        });
        //Todo: Manipular os dados
        //Todo: Criar a tela de solicitações
    }
}