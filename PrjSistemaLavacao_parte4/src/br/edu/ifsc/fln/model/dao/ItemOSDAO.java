package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Servico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemOSDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(ItemOS item, long numeroOrdemServico) {
        String sql = "INSERT INTO itemos(valorServico, observacoes, ordemServico_id, servico_id) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDouble(1, item.getValorServico());
            stmt.setString(2, item.getObservacoes());
            stmt.setLong(3, numeroOrdemServico);
            stmt.setInt(4, item.getServico().getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(ItemOS item) {
        String sql = "UPDATE itemos SET valorServico=?, observacoes=?, servico_id=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDouble(1, item.getValorServico());
            stmt.setString(2, item.getObservacoes());
            stmt.setInt(3, item.getServico().getId());
            stmt.setInt(4, item.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(ItemOS item) {
        String sql = "DELETE FROM itemos WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, item.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<ItemOS> listar() {
        String sql = "SELECT * FROM item_os";
        List<ItemOS> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                ItemOS itemOS = new ItemOS();
                Servico servico = new Servico();
                OrdemServico ordemServico = new OrdemServico();
                
                itemOS.setId(resultado.getInt("id"));
                itemOS.setValorServico(resultado.getDouble("valorServico"));
                itemOS.setObservacoes(resultado.getString("observacoes"));
                
                servico.setId(resultado.getInt("servico_id"));
                ordemServico.setNumero(resultado.getInt("ordemServico_id"));
                
                //Obtendo os dados completos do Servico associado ao Item da Ordem de Servico
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                servico = servicoDAO.buscar(servico);
                
                OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
                ordemServicoDAO.setConnection(connection);
                ordemServico = ordemServicoDAO.buscar(ordemServico);
                
                itemOS.setServico(servico);
                itemOS.setOrdemServico(ordemServico);
                
                retorno.add(itemOS);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    public List<ItemOS> listarPorOrdemServico(OrdemServico ordemServico) {
        String sql = "SELECT * FROM item_os WHERE ordemServico_id=?";
        List<ItemOS> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, ordemServico.getNumero());
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                ItemOS itemOS = new ItemOS();
                Servico servico = new Servico();
                OrdemServico o = new OrdemServico();
                
                itemOS.setId(resultado.getInt("id"));
                itemOS.setValorServico(resultado.getDouble("valorServico"));
                itemOS.setObservacoes(resultado.getString("observacoes"));
                
                servico.setId(resultado.getInt("servico_id"));
                o.setNumero(resultado.getInt("ordemServico_id"));
                
                //Obtendo os dados completos do Produto associado ao Item de Venda
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                servico = servicoDAO.buscar(servico);
                
                itemOS.setServico(servico);
                itemOS.setOrdemServico(o);
                
                retorno.add(itemOS);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
