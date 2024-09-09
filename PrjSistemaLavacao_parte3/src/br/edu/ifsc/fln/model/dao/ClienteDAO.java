/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Fisica;
import br.edu.ifsc.fln.model.domain.Juridica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mpisc
 */
public class ClienteDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Cliente cliente) {
        String sql = "INSERT INTO cliente(nome, celular, email, data_cadastro) VALUES(?, ?, ?, ?)";
        String sqlCF = "INSERT INTO fisica(id_cliente, cpf) VALUES((SELECT max(id) FROM cliente), ?)";
        String sqlCJ = "INSERT INTO juridica(id_cliente, cnpj, inscricao_estadual) VALUES((SELECT max(id) FROM cliente), ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCelular());
            stmt.setString(3, cliente.getEmail());
            stmt.setDate(4, java.sql.Date.valueOf(cliente.getDataCadastro()));
            stmt.execute();
            
            if (cliente instanceof Fisica) {
                stmt = connection.prepareStatement(sqlCF);
                stmt.setString(1, ((Fisica)cliente).getCpf());
                stmt.execute();
            } else {
                stmt = connection.prepareStatement(sqlCJ);
                stmt.setString(1, ((Juridica)cliente).getCnpj());
                stmt.setString(2, ((Juridica)cliente).getInscricaoEstadual());
                stmt.execute();
            }
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome=?, cpf=?, telefone=?, endereco=?, data_nascimento=? WHERE id=?";
        String sqlCF = "";
        String sqlCJ = "";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCelular());
            stmt.setString(3, cliente.getEmail());
            stmt.setDate(4, java.sql.Date.valueOf(cliente.getDataCadastro()));
            stmt.setInt(6, cliente.getId());
            stmt.execute();
            
            if (cliente instanceof Fisica) {
                stmt = connection.prepareStatement(sqlCF);
                stmt.setString(1, ((Fisica)cliente).getCpf());
                stmt.execute();
            } else {
                stmt = connection.prepareStatement(sqlCJ);
                stmt.setString(1, ((Juridica)cliente).getCnpj());
                stmt.setString(2, ((Juridica)cliente).getInscricaoEstadual());
                stmt.execute();
            }
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Cliente cliente) {
        String sql = "DELETE FROM cliente WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Cliente> listar() {
        String sql = "SELECT * FROM cliente c "
                        + "LEFT JOIN fisica f on f.id_cliente = c.id "
                        + "LEFT JOIN juridica j on j.id_cliente = c.id;";
        List<Cliente> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Cliente cliente = populateVO(resultado);
                retorno.add(cliente);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Cliente buscar(Cliente cliente) {
        String sql = "SELECT * FROM cliente c "
                        + "LEFT JOIN fisica f on f.id_cliente = c.id "
                        + "LEFT JOIN juridica j on j.id_cliente = c.id;";
        Cliente retorno = new Cliente();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    private Cliente populateVO(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        if (rs.getString("cnpj") == null || rs.getString("cnpj").length() <= 0) {
            
            cliente = new Fisica();
            ((Fisica)cliente).setCpf(rs.getString("cpf"));
        } else {
            
            cliente = new Juridica();
            ((Juridica)cliente).setCnpj(rs.getString("cnpj"));
            ((Juridica)cliente).setInscricaoEstadual(rs.getString("inscricao_estadual"));
        }
        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCelular(rs.getString("celular"));
        cliente.setEmail(rs.getString("email"));
        cliente.setDataCadastro(rs.getDate("data_cadastro").toLocalDate());
        return cliente;
    }
}
