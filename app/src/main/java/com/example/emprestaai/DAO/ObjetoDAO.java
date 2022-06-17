package com.example.emprestaai.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ObjetoDAO extends SQLiteOpenHelper {
    private Context context;
    private static final String NOME_BD = "Objetos.db";
    private static final int VERSAO_BD = 1;

    private static final String NOME_TABELA = "objeto";
    private static final String COLUNA_ID = "id";
    private static final String COLUNA_NOME = "nome";
    private static final String COLUNA_DONO = "id_dono";
    private static final String COLUNA_STATUS = "status";
    //private static final String COLUNA_IMAGEM = "imagem";

    public ObjetoDAO(@Nullable Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + NOME_TABELA+
                "(" + COLUNA_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUNA_DONO + " INTEGER, " +
                COLUNA_NOME + " TEXT, " +
                COLUNA_STATUS + " TEXT, "+
                "FOREIGN KEY("+COLUNA_DONO+") REFERENCES usuario(id))";
                //COLUNA_IMAGEM + "BLOB )";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ NOME_TABELA);
        onCreate(db);
    }

    //Todo: Terminar a migração de Drawable para Bitmap
    public String addObjeto(String idDono,String nome, String status){//, Bitmap imagem){
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUNA_DONO,Integer.parseInt(idDono));
        cv.put(COLUNA_NOME, nome);
        cv.put(COLUNA_STATUS, status);
        //cv.put(COLUNA_IMAGEM, null);

        long resultado = bd.insert(NOME_TABELA, null, cv);
        if(resultado == -1) Toast.makeText(context, "Deu ruim", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, "Deu bom", Toast.LENGTH_SHORT).show();

        return Long.toString(resultado);
    }
    //TOdo: ajeitar o editar no banco de dados
    public void updateObjeto(String idObjeto, String dono, String nome, String status){
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues cv = new ContentValues();


        cv.put(COLUNA_DONO,Integer.parseInt(dono));
        cv.put(COLUNA_NOME,nome);
        cv.put(COLUNA_STATUS,status);

        long resultado =bd.update(NOME_TABELA,cv,"id=?", new String[]{idObjeto});

    }

    public Cursor procurarObjetosDono(String idDono){
        String query = "SELECT * FROM " + NOME_TABELA +" WHERE "+COLUNA_DONO+" = "+Integer.parseInt(idDono);
        SQLiteDatabase bd = this.getReadableDatabase();

        Cursor cursor = null;
        if(bd != null){
            cursor = bd.rawQuery(query,null);
        }

        return cursor;
    }

    public Cursor procurarObjetos(String idDono){
        String query = "SELECT * FROM "+NOME_TABELA+" WHERE "+COLUNA_DONO+ " != "+Integer.parseInt(idDono);
        SQLiteDatabase bd = this.getReadableDatabase();

        Cursor cursor = null;
        if(bd != null){
            cursor = bd.rawQuery(query,null);
        }

        return cursor;
    }
}
