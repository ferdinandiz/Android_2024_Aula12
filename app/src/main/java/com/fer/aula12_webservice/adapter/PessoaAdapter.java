package com.fer.aula12_webservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fer.aula12_webservice.R;
import com.fer.aula12_webservice.model.Pessoa;

import java.util.List;

public class PessoaAdapter extends RecyclerView.Adapter<PessoaAdapter.ViewHolder> {
   private List<Pessoa> pessoas;
   private Context context;
   private OnItemClickListener listener;

   public interface OnItemClickListener {
      void onItemClick(Pessoa pessoa);
   }

   public PessoaAdapter(Context context, List<Pessoa> pessoaList, OnItemClickListener listener) {
      this.pessoas = pessoaList;
      this.context = context;
      this.listener = listener;
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(
              R.layout.lista_item,
              parent,
              false
      );
      return new ViewHolder(view, listener);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      Pessoa pessoa = pessoas.get(position);
      holder.texto.setText(
              pessoa.getId() + " - "
              + pessoa.getNome() + " - "
              + pessoa.getTelefone()
      );
   }

   @Override
   public int getItemCount() {
      return pessoas.size();
   }

   public class ViewHolder extends RecyclerView.ViewHolder {
      public TextView texto;
      public ViewHolder(View itemView, OnItemClickListener listener) {
         super(itemView);
         texto = itemView.findViewById(android.R.id.text1);
         itemView.setOnClickListener(view -> {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
               listener.onItemClick(pessoas.get(position));
            }
         });
      }
   }
}
