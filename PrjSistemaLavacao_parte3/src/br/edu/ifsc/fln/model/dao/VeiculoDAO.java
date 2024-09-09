package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.Modelo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VeiculoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Veiculo veiculo) {
        String sql = "INSERT INTO veiculo (placa, observacoes, modelo_id, cor_id, cliente_id) VALUES(?,?,?,?,?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setInt(3, veiculo.getModelo().getId());
            stmt.setLong(4, veiculo.getCor().getId());
            stmt.setInt(5, veiculo.getCliente().getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Veiculo veiculo) {
        String sql = "UPDATE veiculo SET placa=?, observacoes=?, modelo_id=?, cor_id=?, cliente_id=? WHERE id=?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setInt(3, veiculo.getModelo().getId());
            stmt.setLong(4, veiculo.getCor().getId());
            stmt.setInt(5, veiculo.getCliente().getId());
            stmt.setInt(6, veiculo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Veiculo veiculo) {
        String sql = "DELETE FROM veiculo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Veiculo> listar() {
        String sql = "SELECT v.id AS veiculo_id, v.placa, v.observacoes, " +
                     "c.id AS cliente_id, c.nome AS cliente_nome, " +
                     "m.id AS modelo_id, m.descricao AS modelo_descricao, " +
                     "co.id AS cor_id, co.nome AS cor_nome " +
                     "FROM veiculo v " +
                     "JOIN cliente c ON v.cliente_id = c.id " +
                     "JOIN modelo m ON v.modelo_id = m.id " +
                     "JOIN cor co ON v.cor_id = co.id";

        List<Veiculo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Veiculo veiculo = new Veiculo();
                veiculo.setId(resultado.getInt("veiculo_id"));
                veiculo.setPlaca(resultado.getString("placa"));
                veiculo.setObservacoes(resultado.getString("observacoes"));

                Modelo modelo = new Modelo();
                modelo.setId(resultado.getInt("modelo_id"));
                modelo.setDescricao(resultado.getString("modelo_descricao"));
                veiculo.setModelo(modelo);

                Cor cor = new Cor();
                cor.setId(resultado.getLong("cor_id"));
                cor.setNome(resultado.getString("cor_nome"));
                veiculo.setCor(cor);

                Cliente cliente = new Cliente();
                cliente.setId(resultado.getInt("cliente_id"));
                cliente.setNome(resultado.getString("cliente_nome"));
                veiculo.setCliente(cliente);

                retorno.add(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Veiculo buscar(Veiculo veiculo) {
        String sql = "SELECT v.id AS veiculo_id, v.placa, v.observacoes, " +
                     "c.nome AS cliente_nome, " +  // Apenas o nome do cliente
                     "m.id AS modelo_id, m.descricao AS modelo_descricao, " +
                     "co.id AS cor_id, co.nome AS cor_nome " +
                     "FROM veiculo v " +
                     "JOIN cliente c ON v.cliente_id = c.id " +
                     "JOIN modelo m ON v.modelo_id = m.id " +
                     "JOIN cor co ON v.cor_id = co.id " +
                     "WHERE v.id = ?";

        Veiculo retorno = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = new Veiculo();
                retorno.setId(resultado.getInt("veiculo_id"));
                retorno.setPlaca(resultado.getString("placa"));
                retorno.setObservacoes(resultado.getString("observacoes"));

                Modelo modelo = new Modelo();
                modelo.setId(resultado.getInt("modelo_id"));
                modelo.setDescricao(resultado.getString("modelo_descricao"));
                retorno.setModelo(modelo);

                Cor cor = new Cor();
                cor.setId(resultado.getLong("cor_id"));
                cor.setNome(resultado.getString("cor_nome"));
                retorno.setCor(cor);

                Cliente cliente = new Cliente();
                cliente.setNome(resultado.getString("cliente_nome")); // Apenas o nome do cliente
                retorno.setCliente(cliente);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
