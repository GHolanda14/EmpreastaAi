package com.example.emprestaai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;


public class Cadastro extends AppCompatActivity {
    TextInputLayout tiNome, tiEmail, tiCep, tiSenha;
    Button btnCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        tiNome = (TextInputLayout) findViewById(R.id.tiNome);
        tiEmail = (TextInputLayout) findViewById(R.id.tiEmail);
        tiCep = (TextInputLayout) findViewById(R.id.tiCep);
        tiSenha = (TextInputLayout) findViewById(R.id.tiSenha);
        btnCadastro = (Button) findViewById(R.id.btnCadastro2);

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cadastro.this, com.example.emprestaai.Login.class);
                intent.putExtra("nome",tiNome.getEditText().getText().toString());
                intent.putExtra("email",tiEmail.getEditText().getText().toString());
                intent.putExtra("cep",tiCep.getEditText().getText().toString());
                intent.putExtra("senha",tiSenha.getEditText().getText().toString());
                startActivity(intent);
                Cadastro.this.finish();
            }
        });


    }
}