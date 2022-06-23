package com.example.emprestaai;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    ImageView ivObjetoAluObj;
    TextView tvNomeAlugarObj, tvDonoObj;
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
        layForm = (LinearLayout) findViewById(R.id.layForm);
        tiLocal = (TextInputLayout) findViewById(R.id.tiLocal);
        tiData = (TextInputEditText) findViewById(R.id.tiData);
        btnSolicitar = (Button) findViewById(R.id.btnSolicitar);
        ivObjetoAluObj = (ImageView) findViewById(R.id.ivObjetoAluObj);

        tvDonoObj.setText(intent.getStringExtra("dono"));
        tvNomeAlugarObj.setText(intent.getStringExtra("nome"));
        ivObjetoAluObj.setImageBitmap(getImage(intent.getByteArrayExtra("imagem")));

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
                //Todo: Remover comentário
//                if(tiLocal.getEditText().getText().toString().isEmpty() ||
//                        tiData.getText().toString().isEmpty()){
//                    Toast.makeText(AlugarObjeto.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
//                }else{
                    Intent intent1 = new Intent();
                    intent1.putExtra("dono",tvDonoObj.getText().toString().trim());
                    intent1.putExtra("idObjeto", intent.getStringExtra("idObjeto"));
                    intent1.putExtra("nome",tvNomeAlugarObj.getText().toString().trim());
                    intent1.putExtra("status",getString(R.string.hSolicitado));
                    intent1.putExtra("periodo",tiData.getText().toString());
                    intent1.putExtra("local",tiLocal.getEditText().getText().toString());
                    intent1.putExtra("imagem",intent.getByteArrayExtra("imagem"));
                    setResult(SOLICITADO,intent1);
                    AlugarObjeto.this.finish();
                //}
            }
        });
    }
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}