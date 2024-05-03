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

import com.fer.aula12_webservice.util.Aula12Util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InserirActivity extends AppCompatActivity {

   RelativeLayout btnBuscar, btnInserir, btnDeletar, btnListar;
   Button btnInserirInfo;
   EditText nomeEd, telefoneEd;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_inserir);
      btnBuscar = findViewById(R.id.btnBusca);
      btnInserir = findViewById(R.id.btnInsere);
      btnDeletar = findViewById(R.id.btnDelete);
      btnListar = findViewById(R.id.btnLista);
      btnInserirInfo = findViewById(R.id.btnInserirInfo);
      telefoneEd = findViewById(R.id.edTelefone);
      nomeEd = findViewById(R.id.edNome);

      ExecutorService executor = Executors.newSingleThreadExecutor();
      Handler handler = new Handler(Looper.getMainLooper());

      Aula12Util.desativaComponente(getApplicationContext(), btnInserir);
      btnDeletar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
                Intent i = new Intent(InserirActivity.this, DeletarActivity.class);
                startActivity(i);
                finish();
         }
      });
      btnBuscar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent i = new Intent(InserirActivity.this, BuscarActivity.class);
            startActivity(i);
            finish();
         }
      });
      btnListar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
                Intent i = new Intent(InserirActivity.this, ListarActivity.class);
                startActivity(i);
                finish();
         }
      });

      btnInserirInfo.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            if (telefoneEd.getText().toString().isEmpty() || nomeEd.getText().toString().isEmpty()){
               Toast.makeText(InserirActivity.this, "Nome ou Telefone não podem ficar em branco!!!", Toast.LENGTH_SHORT).show();
            }else{
               StringBuilder resp = new StringBuilder();
               executor.execute(new Runnable() {
                  @Override
                  public void run() {
                     HttpURLConnection connection = null;
                     Scanner sc = null;
                     try{
                        URL url = new URL("http://ferdinandizdoom.com/cadastrar.php?nome="+nomeEd.getText().toString()+"&telefone="+telefoneEd.getText().toString());
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("Accept","application/json");
                        connection.connect();
                        sc = new Scanner(url.openStream());
                        while (sc.hasNext()){
                           resp.append(sc.next());
                        }

                     }catch (IOException e) {
                        e.printStackTrace();
                     } finally {
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
                           String resposta = resp.toString();
                           if(resposta.equals("Inserido")){
                              Toast.makeText(InserirActivity.this, "Inserido com Sucesso!", Toast.LENGTH_SHORT).show();
                              telefoneEd.setText("");
                              nomeEd.setText("");
                           }  else {
                              Toast.makeText(InserirActivity.this, "Erro ao tentar Inserir!", Toast.LENGTH_SHORT).show();
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