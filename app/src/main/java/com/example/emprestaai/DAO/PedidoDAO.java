package com.example.emprestaai.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class PedidoDAO extends SQLiteOpenHelper {
    private Context context;
    private static final String NOME_BD = "Pedidos.db";
    private static final int VERSAO_BD = 1;

    private static final String NOME_TABELA = "pedido";
    private static final String COLUNA_ID = "id";
    private static final String COLUNA_ID_OBJETO = "id_objeto";
    private static final String COLUNA_DONO = "dono";
    private static final String COLUNA_ID_SOLICITANTE = "id_solicitante";
    private static final String COLUNA_PERIODO = "periodo";
    private static final String COLUNA_LOCAL = "local";

    public PedidoDAO(@Nullable Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + NOME_TABELA+
                "(" + COLUNA_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUNA_ID_OBJETO + " INTEGER, " +
                COLUNA_ID_SOLICITANTE + " INTEGER, "+
                COLUNA_DONO + " TEXT, "+
                COLUNA_PERIODO + " TEXT, "+
                COLUNA_LOCAL + " TEXT, "+
                "FOREIGN KEY("+COLUNA_ID_SOLICITANTE+") REFERENCES usuario(id)," +
                "FOREIGN KEY("+COLUNA_ID_OBJETO+") REFERENCES objeto(id))";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ NOME_TABELA);
        onCreate(db);
    }

    public String addPedido(String idObjeto, String idSolicitante, String nomeDono, String periodo, String local){
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUNA_ID_OBJETO,Integer.parseInt(idObjeto) );
        cv.put(COLUNA_ID_SOLICITANTE, Integer.parseInt(idSolicitante));
        cv.put(COLUNA_DONO, nomeDono);
        cv.put(COLUNA_PERIODO, periodo);
        cv.put(COLUNA_LOCAL, local);

        long resultado = bd.insert(NOME_TABELA, null, cv);
        if(resultado == -1) Toast.makeText(context, "Erro ao pedir", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, "Pedido feito com sucesso", Toast.LENGTH_SHORT).show();

        return Long.toString(resultado);
    }

    public Cursor buscarPedidos(String idSolicitante){
        String query = "SELECT * FROM " + NOME_TABELA +" WHERE "+COLUNA_ID_SOLICITANTE+" = "+Integer.parseInt(idSolicitante);
        SQLiteDatabase bd = this.getReadableDatabase();

        Cursor cursor = null;
        if(bd != null){
            cursor = bd.rawQuery(query,null);
        }

        return cursor;
    }
}
