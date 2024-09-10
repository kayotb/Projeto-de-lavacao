/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author kayot
 */
public class OrdemServico {
    private int id;
    private long numero;
    private LocalDate data;
    private double total;
    private double desconto;
    
    EStatus status;
    Veiculo veiculo; // Novo atributo adicionado para armazenar o identificador do veículo
    private List<ItemOS> itensOS;
    // Construtores, getters e setters

    public List<ItemOS> getItensOS() {
        return itensOS;
    }

    public void setItensOS(List<ItemOS> itensOS) {
        this.itensOS = itensOS;
    }

    public OrdemServico() {}

    public OrdemServico(int id, long numero, LocalDate data, double total, double desconto, EStatus status, Veiculo veiculo, List<ItemOS> itensOS) {
        this.id = id;
        this.numero = numero;
        this.data = data;
        this.total = total;
        this.desconto = desconto;
        this.status = status;
        this.veiculo = veiculo;
        this.itensOS = itensOS;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate agenda) {
        this.data = agenda;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }  

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }
    
    
}
