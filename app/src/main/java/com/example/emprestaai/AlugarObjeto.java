package com.example.emprestaai;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AlugarObjeto extends AppCompatActivity {
    LinearLayout layForm;
    TextView tvNomeAlugarObj, tvDescricasoAlugarObj, tvDonoObj;
    TextInputLayout tiLocal;
    TextInputEditText tiData;
    Button btnSolicitar;
    DatePickerDialog datePickerDialog;
    int SOLICITADO = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alugar_objeto);

        Intent intent = getIntent();
        tvDonoObj = (TextView) findViewById(R.id.tvDonoObj);
        tvNomeAlugarObj = (TextView) findViewById(R.id.tvNomeAlugarObj);
        tvDescricasoAlugarObj = (TextView) findViewById(R.id.tvDescricaoAlugarObj);
        layForm = (LinearLayout) findViewById(R.id.layForm);
        tiLocal = (TextInputLayout) findViewById(R.id.tiLocal);
        tiData = (TextInputEditText) findViewById(R.id.tiData);
        btnSolicitar = (Button) findViewById(R.id.btnSolicitar);

        tvDonoObj.setText(intent.getStringExtra("dono"));
        tvNomeAlugarObj.setText(intent.getStringExtra("nome"));
        tvDescricasoAlugarObj.setText(intent.getStringExtra("descricao"));

        String status = intent.getStringExtra("status");
        if(status.equals(getString(R.string.tgStatusOn))){
            layForm.setVisibility(View.VISIBLE);
        }else{
            layForm.setVisibility(View.GONE);
        }
        btnSolicitar.setText(status.equals(getString(R.string.tgStatusOn)) ? getString(R.string.btnPedir) : status);
        btnSolicitar.setEnabled(status.equals(getString(R.string.tgStatusOn)));

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
        materialDateBuilder.setTitleText("Período do aluguel");
        materialDateBuilder.setCalendarConstraints(constraintBuilder.build());
        MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        tiData.setText(materialDatePicker.getHeaderText());
                    }
                });

        tiData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tiLocal.getEditText().getText().toString().isEmpty() ||
                        tiData.getText().toString().isEmpty()){
                    Toast.makeText(AlugarObjeto.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent1 = new Intent(AlugarObjeto.this, com.example.emprestaai.ListaPedidos.class);
                    intent1.putExtra("donoAtual",intent.getStringExtra("donoAtual"));
                    intent1.putExtra("dono",tvDonoObj.getText().toString().trim());
                    intent1.putExtra("posicao", intent.getIntExtra("posicao",0));
                    intent1.putExtra("nome",tvNomeAlugarObj.getText().toString().trim());
                    intent1.putExtra("descricao",tvDescricasoAlugarObj.getText().toString().trim());
                    intent1.putExtra("status","Solicitado");
                    intent1.putExtra("periodo",tiData.getText().toString());
                    intent1.putExtra("local",tiLocal.getEditText().getText().toString());
                    setResult(SOLICITADO,intent1);
                    AlugarObjeto.this.finish();
                }
            }
        });

        //TODO: Página com minhas solicitações e solicitações pendentes
    }
}