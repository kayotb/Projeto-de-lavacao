/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author kayot
 */
public class ItemOS {
    private int id;
    private double valorServico;
    private String observacoes;
    private OrdemServico ordemServico; // Associação com OrdemServico
    private Servico servico; // Associação com Servico

    // Construtor
    public ItemOS(int id, double valorServico, String observacoes, Servico servico) {
        this.id = id;
        this.valorServico = valorServico;
        this.observacoes = observacoes;
        this.servico = servico;
    }

    public ItemOS() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValorServico() {
        return valorServico;
    }

    public void setValorServico(double valorServico) {
        this.valorServico = valorServico;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    // Método para retornar uma descrição detalhada do ItemOS
    @Override
    public String toString() {
        return "ItemOS{" +
                "id=" + id +
                ", valorServico=" + valorServico +
                ", observacoes='" + observacoes + '\'' +
                ", servico=" + (servico != null ? servico.getDescricao() : "N/A") +
                '}';
    }
}