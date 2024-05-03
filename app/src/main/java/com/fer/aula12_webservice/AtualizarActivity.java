package com.fer.aula12_webservice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AtualizarActivity extends AppCompatActivity {

    RelativeLayout btnBuscar, btnInserir, btnDeletar, btnListar;
    Button btnAtualizar;
    EditText IdEd, nomeEd, telefoneEd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar);
        btnBuscar = findViewById(R.id.btnBusca);
        btnInserir = findViewById(R.id.btnInsere);
        btnDeletar = findViewById(R.id.btnDelete);
        btnListar = findViewById(R.id.btnLista);
        btnAtualizar = findViewById(R.id.btnAtualizarInfo);
        telefoneEd = findViewById(R.id.edTelefoneAt);
        nomeEd = findViewById(R.id.edNomeAt);
        IdEd = findViewById(R.id.edIDAt);

        String id = getIntent().getStringExtra("ID");
        String nome = getIntent().getStringExtra("NOME");
        String telefone = getIntent().getStringExtra("TELEFONE");

        IdEd.setText(id);
        nomeEd.setText(nome);
        telefoneEd.setText(telefone);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AtualizarActivity.this, DeletarActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AtualizarActivity.this, BuscarActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AtualizarActivity.this, ListarActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AtualizarActivity.this, InserirActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (telefoneEd.getText().toString().isEmpty() || nomeEd.getText().toString().isEmpty()) {
                    Toast.makeText(AtualizarActivity.this, "Nome ou Telefone não podem ficar em branco!!!", Toast.LENGTH_SHORT).show();
                } else {
                    StringBuilder resp = new StringBuilder();

                    try {

                        String nomeAt = nomeEd.getText().toString();
                        String tefoneAt = telefoneEd.getText().toString();
                        String idAt = IdEd.getText().toString();

                        URL url = new URL("http://ferdinandizdoom.com/update.php?nome="
                                + nomeAt + "&telefone="
                                + tefoneAt + "&id="
                                + idAt);

                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                HttpURLConnection connection = null;
                                Scanner sc = null;
                                try {
                                    connection = (HttpURLConnection) url.openConnection();
                                    connection.setRequestMethod("GET");
                                    connection.setRequestProperty("Accept", "application/json");
                                    connection.setConnectTimeout(5000);
                                    connection.connect();
                                    sc = new Scanner(url.openStream());

                                    while (sc.hasNext()) {
                                        resp.append(sc.next());
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    if (connection != null) {
                                        connection.disconnect();
                                    }
                                    if (sc != null) {
                                        sc.close();
                                    }
                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        String resposta = resp.toString();
                                        if (resposta.equals("atualizado")) {
                                            Toast.makeText(AtualizarActivity.this, "Contato atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(AtualizarActivity.this, "Contato não atualizado!", Toast.LENGTH_SHORT).show();
                                        }

                                        Intent i = new Intent(AtualizarActivity.this, ListarActivity.class);
                                        startActivity(i);
                                        finish();

                                    }
                                });
                            }
                        });
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
