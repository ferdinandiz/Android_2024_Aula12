package com.fer.aula12_webservice.model;

public class Pessoa {
   String id;
   String nome;
   String telefone;

   public Pessoa() {
   }

   public Pessoa(String id, String nome, String telefone) {
      this.id = id;
      this.nome = nome;
      this.telefone = telefone;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getNome() {
      return nome;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public String getTelefone() {
      return telefone;
   }

   public void setTelefone(String telefone) {
      this.telefone = telefone;
   }
}
