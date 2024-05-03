package com.fer.aula12_webservice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fer.aula12_webservice.adapter.PessoaAdapter;
import com.fer.aula12_webservice.model.Pessoa;
import com.fer.aula12_webservice.util.Aula12Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListarActivity extends AppCompatActivity {
   RelativeLayout btnBuscar, btnDeletar, btnListar, btnInserir;
   TextView texto2;
   RecyclerView recyclerView;
   PessoaAdapter adapter;
   List<Pessoa> listaPessoa;
   ExecutorService executorService = Executors.newSingleThreadExecutor();
   Handler handler = new Handler(Looper.getMainLooper());

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_listar);
      btnDeletar = findViewById(R.id.btnDelete);
      btnBuscar = findViewById(R.id.btnBusca);
      btnListar = findViewById(R.id.btnLista);
      btnInserir = findViewById(R.id.btnInsere);
      recyclerView = findViewById(R.id.lista);
      texto2 = findViewById(R.id.texto2);
      texto2.setText("NOVA TELA - LISTAR");
      Aula12Util.desativaComponente(ListarActivity.this, btnListar);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      retrieveList();
      btnBuscar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent i = new Intent(ListarActivity.this, BuscarActivity.class);
            startActivity(i);
            finish();
         }
      });
      btnDeletar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent i = new Intent(ListarActivity.this, DeletarActivity.class);
            startActivity(i);
            finish();
         }
      });
      btnInserir.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent i = new Intent(ListarActivity.this, InserirActivity.class);
            startActivity(i);
            finish();
         }
      });
   }

   private void retrieveList() {
      executorService.execute(() -> {
         List<Pessoa> pessoas = fetchPessoas();
         handler.post(() -> {
            adapter = new PessoaAdapter(ListarActivity.this, pessoas, pessoa -> {
               Intent intent = new Intent(ListarActivity.this, AtualizarActivity.class);
               intent.putExtra("ID", pessoa.getId());
               intent.putExtra("NOME", pessoa.getNome());
               intent.putExtra("TELEFONE", pessoa.getTelefone());
               startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
         });
      });
   }

   private List<Pessoa> fetchPessoas() {
      listaPessoa = new ArrayList<>();
      HttpURLConnection connection = null;
      BufferedReader buffer = null;
      try {
         URL url = new URL("http://ferdinandizdoom.com/listar.php");
         connection = (HttpURLConnection) url.openConnection();
         connection.setConnectTimeout(5000);
         connection.connect();
         InputStream inputStream = connection.getInputStream();
         buffer = new BufferedReader(new InputStreamReader(inputStream));
         StringBuilder stringBuffer = new StringBuilder();
         String linha;
         while ((linha = buffer.readLine()) != null) {
            stringBuffer.append(linha);
         }
         JSONObject objetoPessoa = new JSONObject(stringBuffer.toString());
         JSONArray array = objetoPessoa.getJSONArray("pessoa");
         for (int i = 0; i < array.length(); i++) {
            JSONObject objetoArray = array.getJSONObject(i);
            Pessoa pessoa = new Pessoa();
            pessoa.setId(objetoArray.getString("id"));
            pessoa.setNome(objetoArray.getString("nome"));
            pessoa.setTelefone(objetoArray.getString("telefone"));
            listaPessoa.add(pessoa);
         }
      } catch (IOException | JSONException e) {
         e.printStackTrace();
      } finally {
         try {
            if (connection != null) connection.disconnect();
            if (buffer != null) buffer.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      return listaPessoa;
   }
}

