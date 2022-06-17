package com.example.emprestaai.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emprestaai.DAO.UsuarioDAO;
import com.example.emprestaai.R;
import com.google.android.material.textfield.TextInputLayout;


public class Cadastro extends AppCompatActivity {
    TextInputLayout tiNome, tiEmail, tiSenha;
    TextView tvEmailCadastrado;
    Button btnCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        tiNome = (TextInputLayout) findViewById(R.id.tiNome);
        tiEmail = (TextInputLayout) findViewById(R.id.tiEmail);
        tiSenha = (TextInputLayout) findViewById(R.id.tiSenha);
        btnCadastro = (Button) findViewById(R.id.btnCadastro2);
        tvEmailCadastrado = (TextView) findViewById(R.id.tvEmailCadastrado);
        tvEmailCadastrado.setVisibility(View.GONE);

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tiNome.getEditText().toString().isEmpty() ||
                tiEmail.getEditText().toString().isEmpty() ||
                tiSenha.getEditText().toString().isEmpty()){
                    Toast.makeText(Cadastro.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }else{
                    UsuarioDAO usuarioDAO = new UsuarioDAO(com.example.emprestaai.Activity.Cadastro.this);
                    Cursor cursor = usuarioDAO.procurarUsuario(tiEmail.getEditText().getText().toString());
                    if(cursor.getCount() == 0){
                        usuarioDAO.addUsuario(tiNome.getEditText().getText().toString(),
                                tiEmail.getEditText().getText().toString(),
                                tiSenha.getEditText().getText().toString());

                        Intent intent = new Intent(Cadastro.this, Login.class);
                        startActivity(intent);
                        Cadastro.this.finish();
                    }else{
                        tvEmailCadastrado.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


    }
}