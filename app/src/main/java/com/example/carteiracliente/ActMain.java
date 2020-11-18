package com.example.carteiracliente;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.carteiracliente.database.DadosOpenHelper;
import com.example.carteiracliente.dominio.entidade.Cliente;
import com.example.carteiracliente.dominio.repositorio.ClienteRepositorio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

public class ActMain extends AppCompatActivity {


    private RecyclerView lstDados;
    private FloatingActionButton fab;
    private ConstraintLayout coordinatorLayout;

    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;

    private ClienteRepositorio clienteRepositorio;

    private ClienteAdapter clienteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        lstDados = (RecyclerView) findViewById(R.id.lstDados);

        coordinatorLayout = (ConstraintLayout) findViewById(R.id.coordinatorLayout);

        criarConexao();

        lstDados.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        lstDados.setLayoutManager(linearLayoutManager);
        ;

        clienteRepositorio = new ClienteRepositorio(conexao);

        List<Cliente> dados = clienteRepositorio.buscarTodos();

        clienteAdapter = new ClienteAdapter(dados);

        lstDados.setAdapter(clienteAdapter);
    }

    private void criarConexao() {

        try {

            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();

            Snackbar.make(coordinatorLayout, R.string.message_conexao, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.action_ok, null).show();

        } catch (SQLException ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }
    }

    public void cadastrar(View view) {
        Intent it = new Intent(ActMain.this, ActCadCliente.class);
        startActivity(it);
        startActivityForResult(it, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        List<Cliente> dados = clienteRepositorio.buscarTodos();
        clienteAdapter = new ClienteAdapter(dados);
        lstDados.setAdapter(clienteAdapter);

    }
}
