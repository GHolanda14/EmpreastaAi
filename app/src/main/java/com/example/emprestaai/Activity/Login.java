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

public class Login extends AppCompatActivity {
    Button btnLogin;
    TextInputLayout tiEmail, tiSenha;
    TextView tvErro;
    UsuarioDAO usuarioDAO;
    String email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tiEmail = (TextInputLayout) findViewById(R.id.tiEmail);
        tiSenha = (TextInputLayout) findViewById(R.id.tiSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin2);
        tvErro = (TextView) findViewById(R.id.tvErro);

        Intent intent = getIntent();
        usuarioDAO = new UsuarioDAO(Login.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = tiEmail.getEditText().getText().toString();
                senha = tiSenha.getEditText().getText().toString();
                if(email.isEmpty() || senha.isEmpty()){
                    Toast.makeText(Login.this,"Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
                else{
                    verificarUsuario();
                }
            }
        });

    }
    public void verificarUsuario() {
        Cursor cursor = usuarioDAO.procurarUsuario(email);

        if (cursor.getCount() == 0) {
            tvErro.setText(getString(R.string.emailNaoEncontrado));
            tvErro.setVisibility(View.VISIBLE);
        } else {
            cursor.moveToNext();
            if (cursor.getString(2).toString().equals(email) &&
                    cursor.getString(3).toString().equals(senha)) {
                Intent intent1 = new Intent(Login.this, MeusObjetos.class);
                intent1.putExtra("donoAtual", cursor.getString(1).toString());
                intent1.putExtra("idDono", Integer.toString(cursor.getInt(0)));
                startActivity(intent1);
            } else {
                tvErro.setText(getString(R.string.loginErrado));
                tvErro.setVisibility(View.VISIBLE);
            }
        }
    }
}