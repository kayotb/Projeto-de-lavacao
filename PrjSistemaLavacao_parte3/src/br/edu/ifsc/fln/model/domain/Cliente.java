/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kayot
 */
public class Cliente {
    

    protected int id;
    protected String nome;
    protected String celular;
    protected String email;
    protected LocalDate dataCadastro;
    
//    public Pontuacao pontuacao;

    public List<Veiculo> veiculos = new ArrayList<>();
    
    public Cliente() {
    }

    public Cliente(int id, String nome, String celular, String email, LocalDate dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.celular = celular;
        this.email = email;
        this.dataCadastro = dataCadastro;
//        this.pontuacao = new Pontuacao();
    }

//    public Pontuacao getPontuacao() {
//        return pontuacao;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

//    @Override
//    public String toString() {
//        return "Cliente{" + "id=" + id + ", nome=" + nome + ", celular=" + celular + ", email=" + email + ", dataCadastro=" + dataCadastro + '}';
//    }
    
    @Override
    public String toString() {
        return nome;  // Retorna apenas o nome do cliente
    }
  
    public void add(Veiculo veiculo){
        veiculos.add(veiculo);
        veiculo.setCliente(null);
    }
    
    public void remove(Veiculo veiculo) throws ExceptionLavacao{
        if(!veiculos.remove(veiculo)){
            throw new ExceptionLavacao("Veículo não encontrado na lista do cliente.");
        }
    }
    
//    @Override
//    public String getDados() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("Nome.......: ").append(nome).append("\n");
//        sb.append("Email......: ").append(email).append("\n");
//        sb.append("Telefone.......: ").append(celular).append("\n");
//        sb.append("Data de cadastro.......: ").append(dataCadastro).append("\n");
//        return sb.toString();
//    }
        
}

