package com.example.emprestaai.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class UsuarioDAO extends SQLiteOpenHelper {
    private Context context;
    private static final String NOME_BD = "EmprestaAi.db";
    private static final int VERSAO_BD = 1;

    private static final String NOME_TABELA = "usuario";
    private static final String COLUNA_ID = "id";
    private static final String COLUNA_NOME = "nome";
    private static final String COLUNA_EMAIL = "email";
    private static final String COLUNA_SENHA = "senha";


    public UsuarioDAO(@Nullable Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
        this.context = context;
    }
        //TODO: DAO Pedido, Solicitacao
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Msg","Criei o usuario");
        String query = "CREATE TABLE " + NOME_TABELA +
                "(" + COLUNA_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUNA_NOME + " TEXT," +
                COLUNA_EMAIL + " TEXT," +
                COLUNA_SENHA + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ NOME_TABELA);
        onCreate(db);
    }

    public void addUsuario(String nome, String email, String senha){
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUNA_NOME, nome);
        cv.put(COLUNA_EMAIL, email);
        cv.put(COLUNA_SENHA, senha);
        long resultado = bd.insert(NOME_TABELA, null, cv);
        if(resultado == -1) Toast.makeText(context, "Deu ruim", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, "Deu bom", Toast.LENGTH_SHORT).show();
    }

    public Cursor procurarUsuario(String email){
        String query = "SELECT * FROM " + NOME_TABELA +" WHERE "+COLUNA_EMAIL+" = '"+email+"'";
        SQLiteDatabase bd = this.getReadableDatabase();

        Cursor cursor = null;
        if(bd != null){
            cursor = bd.rawQuery(query,null);
        }

        return cursor;
    }

    public Cursor pegarNomes(){
        String query = "SELECT id,nome FROM " + NOME_TABELA;
        SQLiteDatabase bd = this.getReadableDatabase();

        Cursor cursor = null;
        if(bd != null){
            cursor = bd.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor getNome(int idDono) {
        String query = "SELECT nome FROM " + NOME_TABELA+" WHERE id = " +idDono;
        SQLiteDatabase bd = this.getReadableDatabase();

        Cursor cursor = null;
        if(bd != null){
            cursor = bd.rawQuery(query,null);
        }
        return cursor;
    }
}
