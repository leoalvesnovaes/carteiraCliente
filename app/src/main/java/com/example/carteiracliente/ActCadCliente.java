package com.example.carteiracliente;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.carteiracliente.database.DadosOpenHelper;
import com.example.carteiracliente.dominio.entidade.Cliente;
import com.example.carteiracliente.dominio.repositorio.ClienteRepositorio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class ActCadCliente extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtEndereco;
    private EditText edtEmail;
    private EditText edtTelefone;

    private ClienteRepositorio clienteRepositorio;

    private ConstraintLayout layoutContentActCad_Cliente;

    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;

    private Cliente cliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_cliente);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtEndereco = (EditText) findViewById(R.id.edtEndereco);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);

        layoutContentActCad_Cliente = (ConstraintLayout) findViewById((R.id.layoutContentActCad_Cliente));

        criarConexao();
        verificaParametro();
    }

    private void verificaParametro() {

        Bundle bundle = getIntent().getExtras();

        cliente = new Cliente();

        if ((bundle != null) && (bundle.containsKey("CLIENTE"))) {

            cliente = (Cliente) bundle.getSerializable("CLIENTE");
            edtNome.setText(cliente.nome);
            edtEndereco.setText(cliente.endereco);
            edtEmail.setText(cliente.email);
            edtTelefone.setText(cliente.telefone);

        }
    }

    private void criarConexao() {

        try {

            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();

            Snackbar.make(layoutContentActCad_Cliente, R.string.message_conexao, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.action_ok, null).show();

            clienteRepositorio = new ClienteRepositorio(conexao);

        } catch (SQLException ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_erro);
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }
    }

    private void confirmar() {


        if (validaCampos() == false) {

            try {

                if (cliente.codigo == 0) {
                    clienteRepositorio.inserir(cliente);

                } else {
                    clienteRepositorio.alterar(cliente);
                }

                finish();

            } catch (SQLException ex) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle(R.string.title_erro);
                dlg.setMessage(ex.getMessage());
                dlg.setNeutralButton(R.string.action_ok, null);
                dlg.show();
            }
        }

    }

    private boolean validaCampos() {

        boolean res = false;

        String nome = edtNome.getText().toString();
        String endereco = edtEndereco.getText().toString();
        String email = edtEmail.getText().toString();
        String telefone = edtTelefone.getText().toString();

        cliente.nome = nome;
        cliente.endereco = endereco;
        cliente.email = email;
        cliente.telefone = telefone;


        if (res = isCampoVazio(nome)) {
            edtNome.requestFocus();
        } else if (res = isCampoVazio(endereco)) {
            edtEndereco.requestFocus();
        } else if (res = !isEmailValido(email)) {
            edtEmail.requestFocus();
        } else if (res = isCampoVazio(telefone)) {
            edtTelefone.requestFocus();
        }

        if (res) {

            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_aviso);
            dlg.setMessage(R.string.message_campos_invalidos_brancos);
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }

        return res;
    }

    private boolean isCampoVazio(String valor) {

        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultado;
    }

    private boolean isEmailValido(String email) {

        boolean resultado = (!isCampoVazio(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        return resultado;
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_cad_cliente, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                finish();
                break;

            case R.id.action_ok:

                confirmar();
                break;

            case R.id.action_excluir:

               clienteRepositorio.excluir(cliente.codigo);
               finish();
               break;
        }

        return super.onOptionsItemSelected(item);
    }
}
