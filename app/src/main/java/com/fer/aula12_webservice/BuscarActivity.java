package com.fer.aula12_webservice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fer.aula12_webservice.model.Pessoa;
import com.fer.aula12_webservice.util.Aula12Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BuscarActivity extends AppCompatActivity {

    RelativeLayout btnBuscar, btnInserir, btnDeletar, btnListar;
    Button btnBuscarInfo;
    EditText edID;
    TextView txtID, txtNome, txtTelefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);
        btnBuscar = findViewById(R.id.btnBusca);
        btnInserir = findViewById(R.id.btnInsere);
        btnDeletar = findViewById(R.id.btnDelete);
        btnListar = findViewById(R.id.btnLista);
        btnBuscarInfo = findViewById(R.id.btnBuscarInfo);
        edID = findViewById(R.id.edID);
        txtID = findViewById(R.id.txtID);
        txtNome = findViewById(R.id.txtNome);
        txtTelefone = findViewById(R.id.txtTelefone);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        Aula12Util.desativaComponente(getApplicationContext(), btnBuscar);
        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BuscarActivity.this, InserirActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BuscarActivity.this, DeletarActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BuscarActivity.this, ListarActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnBuscarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edID.getText().toString().isEmpty()){
                    Toast.makeText(BuscarActivity.this, "ID não inserido!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                }else{
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder resp = new StringBuilder();
                            HttpURLConnection connection = null;
                            Scanner sc = null;
                            try {
                                URL url = new URL("http://ferdinandizdoom.com/consultar.php?id="+edID.getText().toString());
                                connection = (HttpURLConnection) url.openConnection();
                                connection.setConnectTimeout(5000);
                                connection.setRequestMethod("GET");
                                connection.setRequestProperty("Accept","application/json");
                                connection.connect();
                                sc = new Scanner(url.openStream());
                                while (sc.hasNext()){
                                    resp.append(sc.next());
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                            }finally {
                                if(connection != null){
                                    connection.disconnect();
                                }
                                if(sc != null){
                                    sc.close();
                                }
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Pessoa pessoa = new Gson().fromJson(resp.toString(), Pessoa.class);
                                    if(!pessoa.getNome().isEmpty()){
                                        try {
                                            txtID.setText(pessoa.getId());
                                            txtNome.setText(pessoa.getNome());
                                            txtTelefone.setText(pessoa.getTelefone());
                                        } catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}