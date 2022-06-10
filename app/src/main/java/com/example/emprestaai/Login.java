package com.example.emprestaai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {
    Button btnLogin;
    TextInputLayout tiEmail, tiSenha;
    TextView tvErro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tiEmail = (TextInputLayout) findViewById(R.id.tiEmail);
        tiSenha = (TextInputLayout) findViewById(R.id.tiSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin2);
        tvErro = (TextView) findViewById(R.id.tvErro);

        Intent intent = getIntent();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tiEmail.getEditText().getText().toString();
                String senha = tiSenha.getEditText().getText().toString();
                if(email.isEmpty() || senha.isEmpty()){
                    Toast.makeText(Login.this,"Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
                else if(email.equals(intent.getStringExtra("email")) &&
                        senha.equals(intent.getStringExtra("senha"))){
                    Intent intent1 = new Intent(Login.this,com.example.emprestaai.MeusObjetos.class);
                    startActivity(intent1);
                }
                else{
                    tvErro.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}