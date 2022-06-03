package com.example.emprestaai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Cadastro extends AppCompatActivity {
    EditText etNome, etEmail, etCep, etSenha;
    Button btnCadastro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etNome = (EditText) findViewById(R.id.etNome);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etCep = (EditText) findViewById(R.id.etCep);
        etSenha = (EditText) findViewById(R.id.etSenha);
        btnCadastro = (Button) findViewById(R.id.btnCadastro2);

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cadastro.this, com.example.emprestaai.Login.class);
                intent.putExtra("nome",etNome.getText().toString());
                intent.putExtra("email",etEmail.getText().toString());
                intent.putExtra("cep",etCep.getText().toString());
                intent.putExtra("senha",etSenha.getText().toString());
                startActivity(intent);
                Cadastro.this.finish();
            }
        });


    }
}