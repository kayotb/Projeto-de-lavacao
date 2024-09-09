package br.edu.ifsc.fln.model.domain;

public class Motor {
    private int potencia;
    private ETipoCombustivel tipoCombustivel;

    
    public Motor(){
    }

    public Motor(int potencia) {
        this.potencia = potencia;
    }

    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }

    public ETipoCombustivel getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(ETipoCombustivel tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    @Override
    public String toString() {
        return "Motor{" + "potencia=" + potencia + ", tipoCombustivel=" + tipoCombustivel + '}';
    }
    
    
}
