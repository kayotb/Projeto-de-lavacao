

package br.edu.ifsc.fln.model.domain;

public class Veiculo {

    private int id;
    private String placa;
    private String observacoes;
    private Cor cor;
    private Modelo modelo;

    private Cliente cliente;

    public Veiculo() {
    }

    public Veiculo(String placa) {
        this.placa = placa;
    }

    public Veiculo(String placa, Modelo modelo) {
        this.placa = placa;
        this.modelo = modelo;
    }
 
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    @Override
    public String toString() {
        return "Veiculo{" + "id=" + id + ", placa=" + placa + ", observacoes=" + observacoes + ", cor=" + cor + ", modelo=" + modelo + '}';
    }
    
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}

