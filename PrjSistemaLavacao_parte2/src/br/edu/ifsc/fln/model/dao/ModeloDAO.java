/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Marca;  
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModeloDAO {

    private Connection connection;
    
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Modelo modelo) {
        String sql = "INSERT INTO modelo(descricao, id_marca) VALUES(?,?)";        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modelo.getDescricao());
            stmt.setInt(2, modelo.getMarca().getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Modelo modelo) {
        String sql = "UPDATE modelo SET descricao=?, id_marca=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modelo.getDescricao());
            stmt.setInt(3, modelo.getId());
            stmt.setInt(2, modelo.getMarca().getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Modelo modelo) {
        String sql = "DELETE FROM modelo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Modelo> listar() {
//        String sql = "SELECT * FROM modelo";
//        List<Modelo> retorno = new ArrayList<>();
//        try {
//            PreparedStatement stmt = connection.prepareStatement(sql);
//            ResultSet resultado = stmt.executeQuery();
//            while (resultado.next()) {
//                Modelo modelo = new Modelo();
//                modelo.setId(resultado.getInt("id"));
//                modelo.setDescricao(resultado.getString("descricao"));
//
//                retorno.add(modelo);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return retorno;
        String sql = "SELECT * FROM MODELO";
        List<Modelo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Modelo modelo = populateVO(resultado);
                retorno.add(modelo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Modelo buscar(Modelo modelo) {
        String sql = "SELECT * FROM modelo WHERE id=?";
        Modelo retorno = new Modelo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                modelo.setDescricao(resultado.getString("descricao"));
                retorno = modelo;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    private Modelo populateVO(ResultSet rs, boolean comMarca) throws SQLException {
        Modelo modelo = new Modelo();
        //modelo.setMarca(marca);

        modelo.setId(rs.getInt("id"));
        modelo.setDescricao(rs.getString("descricao"));
        if (comMarca) {
            Marca marca = new Marca();
            marca.setId(rs.getInt("id_marca"));
            MarcaDAO marcaDAO = new MarcaDAO();
            marcaDAO.setConnection(connection);
            marca = marcaDAO.buscar(marca);
            modelo.setMarca(marca);
        }

        return modelo;
    }

    private Modelo populateVO(ResultSet rs) throws SQLException {
        Modelo modelo = new Modelo();
        //modelo.setMarca(marca);

        modelo.setId(rs.getInt("id"));
        modelo.setDescricao(rs.getString("descricao"));
        Marca marca = new Marca();
        marca.setId(rs.getInt("id_marca"));
        MarcaDAO marcaDAO = new MarcaDAO();
        marcaDAO.setConnection(connection);
        marca = marcaDAO.buscar(marca);
        modelo.setMarca(marca);

        return modelo;
    }

}
