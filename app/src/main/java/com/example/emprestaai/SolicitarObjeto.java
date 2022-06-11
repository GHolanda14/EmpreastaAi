package com.example.emprestaai;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.time.format.DateTimeFormatter;

public class SolicitarObjeto extends AppCompatActivity {
    TextInputLayout tiLocal, tiContato;
    TextInputEditText tiData;
    Button btnSolicitar, btnRecebimento, btnData;
    DatePickerDialog datePickerDialog;
    TextView tvPeriodo;
    DateTimeFormatter dtFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_objeto);

        tiLocal = (TextInputLayout) findViewById(R.id.tiLocal);
        tiContato = (TextInputLayout) findViewById(R.id.tiContato);
        btnSolicitar = (Button) findViewById(R.id.btnSolicitar);
        tiData = (TextInputEditText) findViewById(R.id.tiData);

        //Todo: Formatar datas

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
        materialDateBuilder.setTitleText("Período do aluguel");
        materialDateBuilder.setCalendarConstraints(constraintBuilder.build());
        MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        tiData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        tiData.setText("Período do aluguel: " + materialDatePicker.getHeaderText());
                    }
                });

        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tiLocal.getEditText().getText().toString().isEmpty() ||
                tiData.getText().toString().isEmpty() ||
                tiContato.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(SolicitarObjeto.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("Msg",tiLocal.getEditText().getText().toString() + tiData.getText().toString() + tiContato.getEditText().getText().toString());
                }
            }
        });
        //Todo: Manipular os dados
        //Todo: Criar a tela de solicitações
    }
}