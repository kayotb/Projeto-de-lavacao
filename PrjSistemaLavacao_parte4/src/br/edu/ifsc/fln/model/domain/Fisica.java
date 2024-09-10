/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;

/**
 *
 * @author kayot
 */
public class Fisica extends Cliente{
    private String cpf;
    private LocalDate dataNascimento;

    public Fisica() {
    }

    public Fisica(String cpf, LocalDate dataNascimento) {
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public Fisica(String cpf, LocalDate dataNascimento, int id, String nome, String celular, String email, LocalDate dataCadastro) {
        super(id, nome, celular, email, dataCadastro);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        
    }  

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public String toString() {
        return super.toString() + "PessoaFisica{" + "cpf=" + cpf + ", dataNascimento=" + dataNascimento + '}';
    }
    
//    @Override
//    public String getDados() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(super.getDados());
//        sb.append("CPF.......: ").append(cpf).append("\n");
//        sb.append("Data de nascimento.......: ").append(dataNascimento).append("\n");
//        return sb.toString();
//    }
//
//    @Override
//    public String getDados(String obs) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(getDados()).append("\n").append(obs);
//        return sb.toString();
//    }

}
