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

public class DeletarActivity extends AppCompatActivity {

   RelativeLayout btnBuscar, btnInserir, btnDeletar, btnListar;
   Button btnDeletarInfo;
   EditText edID;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_deletar);
      btnBuscar = findViewById(R.id.btnBusca);
      btnInserir = findViewById(R.id.btnInsere);
      btnDeletar = findViewById(R.id.btnDelete);
      btnListar = findViewById(R.id.btnLista);
      btnDeletarInfo = findViewById(R.id.btnDeletarInfo);
      edID = findViewById(R.id.edID);

      ExecutorService executor = Executors.newSingleThreadExecutor();
      Handler handler = new Handler(Looper.getMainLooper());

      Aula12Util.desativaComponente(getApplicationContext(), btnDeletar);
      btnInserir.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
                Intent i = new Intent(DeletarActivity.this, InserirActivity.class);
                startActivity(i);
                finish();
         }
      });
      btnBuscar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
                Intent i = new Intent(DeletarActivity.this, BuscarActivity.class);
                startActivity(i);
                finish();
         }
      });
      btnListar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
                Intent i = new Intent(DeletarActivity.this, ListarActivity.class);
                startActivity(i);
                finish();
         }
      });

      btnDeletarInfo.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            if (edID.getText().toString().isEmpty()){
               Toast.makeText(DeletarActivity.this, "ID não inserido!!!!!!!!!", Toast.LENGTH_SHORT).show();
            }else{
               StringBuilder resp = new StringBuilder();
               executor.execute(new Runnable() {
                  @Override
                  public void run() {
                     HttpURLConnection connection = null;
                     Scanner sc = null;
                     try{

                        URL url = new URL("http://ferdinandizdoom.com/deletar.php?id="+edID.getText().toString());
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(5000);
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
                           if(resposta.equals("excluido")){
                              Toast.makeText(DeletarActivity.this, "Deletado com Sucesso!", Toast.LENGTH_SHORT).show();
                           } else if (resposta.equals("naoExiste")){
                              Toast.makeText(DeletarActivity.this, "ID não existe!", Toast.LENGTH_SHORT).show();
                           } else {
                              Toast.makeText(DeletarActivity.this, "Erro ao tentar excluir!", Toast.LENGTH_SHORT).show();
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
