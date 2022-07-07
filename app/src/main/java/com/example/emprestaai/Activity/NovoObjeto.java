package com.example.emprestaai.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emprestaai.R;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;

public class NovoObjeto extends AppCompatActivity {
    Button btnFoto, btnAddObj;
    ImageView imageView;
    TextInputLayout tiNomeObj;
    ToggleButton tgStatus;
    int EDITAR = 4, BOTAR_IMAGEM = 7;
    byte[] imagem;
    Bitmap a;
    private boolean novaFoto;
    Uri uriFilePath;
    private String imageurl;
    ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_objeto);

        Intent intent = getIntent();

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        tiNomeObj = (TextInputLayout) findViewById(R.id.tiNomeObj);
        tgStatus = (ToggleButton) findViewById(R.id.tgStatus);
        btnFoto = (Button) findViewById(R.id.btnFoto);
        imageView = (ImageView) findViewById(R.id.ivFoto);
        btnAddObj = (Button) findViewById(R.id.btnAddObj);
        imageView.setVisibility(View.GONE);

        if (intent.hasExtra("nome")) {
            tiNomeObj.getEditText().setText(intent.getStringExtra("nome"));
            tgStatus.setChecked(intent.getStringExtra("status").equals(getString(R.string.tgStatusOn)) ? true : false);
            tgStatus.setClickable(intent.getStringExtra("status").equals(getString(R.string.tgStatusOn)) ? true : false);
            imagem = intent.getByteArrayExtra("imagem");
            imageView.setImageBitmap(getImage(imagem));
            imageView.setVisibility(View.VISIBLE);
        }

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(NovoObjeto.this);
            }
        });

        btnAddObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tiNomeObj.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(NovoObjeto.this, "Preencha o nome!", Toast.LENGTH_SHORT).show();
                } else if (imageView.getVisibility() == View.GONE) {
                    Toast.makeText(NovoObjeto.this, "Escolha ou tire uma foto do objeto!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent1 = new Intent();
                    intent1.putExtra("idObjeto", intent.getStringExtra("idObjeto"));
                    intent1.putExtra("nome", tiNomeObj.getEditText().getText().toString());
                    intent1.putExtra("status", tgStatus.getText().toString());
                    intent1.putExtra("imagem", imagem);
                    intent1.putExtra("novaFoto",novaFoto);
                    setResult(RESULT_OK, intent1);
                    NovoObjeto.this.finish();
                }
            }
        });
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Tirar foto", "Escolher da galeria","Cancelar" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Escolha a foto do objeto");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Tirar foto")) {
                    values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    uriFilePath = getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFilePath);
                    startActivityForResult(intent, 0);
                } else if (options[item].equals("Escolher da galeria")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private Bitmap getImage(byte[] imagem) {
        return BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        try {
                            a = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), uriFilePath);
                            imageView.setImageBitmap(a);
                            imageView.setVisibility(View.VISIBLE);
                            imageurl = getRealPathFromURI(uriFilePath);

                            a = BitmapFactory.decodeFile(imageurl);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            a.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                            imagem = byteArrayOutputStream.toByteArray();
                            novaFoto = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();

                                a = BitmapFactory.decodeFile(picturePath);
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                a.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                                imagem = byteArrayOutputStream.toByteArray();
                                imageView.setVisibility(View.VISIBLE);
                                novaFoto = true;
                            }
                        }
                    }
                    break;
            }
        }
    }
}