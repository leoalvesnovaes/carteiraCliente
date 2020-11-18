package com.example.carteiracliente.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.carteiracliente.R;
import com.google.android.material.snackbar.Snackbar;

public class DadosOpenHelper extends SQLiteOpenHelper {

    private SQLiteDatabase conexao;

    public DadosOpenHelper(@Nullable Context context) {
        super(context, "DADOS", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //db.execSQL(ScriptDDL.getCreateTableCliente());


    }

/*
    private void criarConexao() {

        try {

           // dadosOpenHelper = new DadosOpenHelper(this);
            conexao = getWritableDatabase();

           // Snackbar.make(coordinatorLayout, R.string.message_conexao, Snackbar.LENGTH_SHORT)
              //      .setAction(R.string.action_ok, null).show();

        } catch (SQLException ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }
    }
*/


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
