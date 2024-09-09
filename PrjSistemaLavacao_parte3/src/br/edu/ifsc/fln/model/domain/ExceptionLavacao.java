/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author kayot
 */
public class ExceptionLavacao extends Exception{
    public ExceptionLavacao(){
        super("Erro na operacao de lavacao");
    }
    
    public ExceptionLavacao(String msg){
        super(msg);
    }
}
