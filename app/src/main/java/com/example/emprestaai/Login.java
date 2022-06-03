package com.example.emprestaai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    Button btnLogin;
    EditText etEmail, etSenha;
    TextView tvErro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etEmail = (EditText) findViewById(R.id.etEmail2);
        etSenha = (EditText) findViewById(R.id.etSenha2);
        btnLogin = (Button) findViewById(R.id.btnLogin2);
        tvErro = (TextView) findViewById(R.id.tvErro);

        Intent intent = getIntent();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String senha = etSenha.getText().toString();
//                if(email.isEmpty() || senha.isEmpty()){
//                    Toast.makeText(Login.this,"Preencha todos os campos!", Toast.LENGTH_SHORT).show();
//                }
//                else if(email.equals(intent.getStringExtra("email")) &&
//                        senha.equals(intent.getStringExtra("senha"))){
                    Intent intent1 = new Intent(Login.this,com.example.emprestaai.MeusObjetos.class);
                    startActivity(intent1);
//                }
//                else{
//                    tvErro.setVisibility(View.VISIBLE);
//                }
            }
        });

    }
}