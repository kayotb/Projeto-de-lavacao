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
public class Juridica extends Cliente{
    private String cnpj;
    private String inscricaoEstadual;
   
    public Juridica() {
    }

    public Juridica(String cnpj, String inscricaoEstadual) {
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public Juridica(String cnpj, String inscricaoEstadual, int id, String nome, String celular, String email, LocalDate dataCadastro) {
        super(id, nome, celular, email, dataCadastro);
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
       
    }
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    @Override
    public String toString() {
        return super.toString() +"PessoaJuridica{" + "cnpj=" + cnpj + ", inscricaoEstadual=" + inscricaoEstadual + '}';
    }
   
//    @Override
//    public String getDados() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(super.getDados());
//        sb.append("CNPJ.......: ").append(cnpj).append("\n");
//        sb.append("Inscricao Estadual.......: ").append(inscricaoEstadual).append("\n");
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
