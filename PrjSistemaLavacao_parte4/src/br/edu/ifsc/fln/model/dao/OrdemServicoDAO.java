package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Veiculo; // Importação da classe Veiculo
import br.edu.ifsc.fln.model.domain.EStatus;
import br.edu.ifsc.fln.model.domain.ItemOS;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdemServicoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(OrdemServico ordemServico) {
        String sql = "INSERT INTO ordemservico(numero, total, data, status, veiculo_id, desconto) VALUES(?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, ordemServico.getNumero());
            stmt.setDouble(2, ordemServico.getTotal());
            stmt.setDate(3, Date.valueOf(ordemServico.getData()));
            stmt.setString(4, ordemServico.getStatus().name());
            stmt.setInt(5, ordemServico.getVeiculo().getId()); // Ajuste para usar o Veiculo
            stmt.setDouble(6, ordemServico.getDesconto());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(OrdemServico ordemServico) {
        String sql = "UPDATE ordemservico SET total=?, data=?, status=?, veiculo_id=?, desconto=? WHERE numero=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDouble(1, ordemServico.getTotal());
            stmt.setDate(2, Date.valueOf(ordemServico.getData()));
            stmt.setString(3, ordemServico.getStatus().name());
            stmt.setInt(4, ordemServico.getVeiculo().getId()); // Ajuste para usar o Veiculo
            stmt.setDouble(5, ordemServico.getDesconto());
            stmt.setLong(6, ordemServico.getNumero());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(OrdemServico ordemServico) {
        String sql = "DELETE FROM ordemservico WHERE numero=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, ordemServico.getNumero());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<OrdemServico> listar() {
        String sql = "SELECT * FROM ordem_servico";
        List<OrdemServico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                OrdemServico ordemServico = new OrdemServico();
                Veiculo veiculo = new Veiculo();
                List<ItemOS> itensOS = new ArrayList();

                ordemServico.setNumero(resultado.getInt("numero"));
                //ordemServico.setTotal(resultado.getBigDecimal("total"));
                ordemServico.setData(resultado.getDate("data").toLocalDate());
                ordemServico.setDesconto(resultado.getDouble("desconto"));
                veiculo.setId(resultado.getInt("id_veiculo"));
                ordemServico.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
               
                //Obtendo os dados completos do Veículo associado à Ordem de Serviço
                VeiculoDAO veiculoDAO = new VeiculoDAO();
                veiculoDAO.setConnection(connection);
                veiculo = veiculoDAO.buscar(veiculo);

                //Obtendo os dados completos dos Itens de Venda associados à Venda
                ItemOSDAO itemOS_DAO = new ItemOSDAO();
                itemOS_DAO.setConnection(connection);
                itensOS = itemOS_DAO.listarPorOrdemServico(ordemServico);

                ordemServico.setVeiculo(veiculo);
                ordemServico.setItensOS(itensOS);
                retorno.add(ordemServico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public OrdemServico buscar(OrdemServico ordemServico) {
        String sql = "SELECT * FROM ordem_servico WHERE numero=?";
        OrdemServico ordemServicoRetorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, (int) ordemServico.getNumero());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Veiculo veiculo = new Veiculo();
                ordemServicoRetorno.setNumero(resultado.getInt("numero"));
                //ordemServicoRetorno.setTotal(resultado.getBigDecimal("total"));
                ordemServicoRetorno.setData(resultado.getDate("data").toLocalDate());
                ordemServicoRetorno.setDesconto(resultado.getDouble("desconto"));
                veiculo.setId(resultado.getInt("id_veiculo"));
                ordemServicoRetorno.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
                ordemServicoRetorno.setVeiculo(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ordemServicoRetorno;
    }
    
    public OrdemServico buscar(int id) {
        /*
            Método necessário para evitar que a instância de retorno seja 
            igual a instância a ser atualizada.
        */
        String sql = "SELECT * FROM ordem_servico WHERE numero=?";
        OrdemServico ordemServicoRetorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Veiculo veiculo = new Veiculo();
                ordemServicoRetorno.setNumero(resultado.getInt("numero"));
                //ordemServicoRetorno.setTotal(resultado.getBigDecimal("total"));
                ordemServicoRetorno.setData(resultado.getDate("data").toLocalDate());
                ordemServicoRetorno.setDesconto(resultado.getDouble("desconto"));
                veiculo.setId(resultado.getInt("id_veiculo"));
                ordemServicoRetorno.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
                ordemServicoRetorno.setVeiculo(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ordemServicoRetorno;
    }    
}
