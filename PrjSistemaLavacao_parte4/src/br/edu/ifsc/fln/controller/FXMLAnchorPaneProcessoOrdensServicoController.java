/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;


import br.edu.ifsc.fln.model.dao.ItemOSDAO;
import br.edu.ifsc.fln.model.dao.OrdemServicoDAO;
import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneProcessoOrdensServicoController implements Initializable {

    @FXML
    private Button buttonAlterar;

    @FXML
    private Button buttonInserir;

    @FXML
    private Button buttonRemover;

    @FXML
    private Label labelOrdemServicoAgenda;

    @FXML
    private Label labelOrdemServicoDesconto;

    @FXML
    private Label labelOrdemServicoNumero;

    @FXML
    private Label labelOrdemServicoStatus;

    @FXML
    private Label labelOrdemServicoTotal;

    @FXML
    private Label labelOrdemServicoVeiculo;

    @FXML
    private TableColumn<OrdemServico, LocalDate> tableColumnOrdemServicoAgenda;

    @FXML
    private TableColumn<OrdemServico, Integer> tableColumnOrdemServicoNumero;

    @FXML
    private TableColumn<OrdemServico, Veiculo> tableColumnOrdemServicoVeiculo;

    @FXML
    private TableView<OrdemServico> tableView;

    private List<OrdemServico> listaOrdemServico;
    private ObservableList<OrdemServico> observableListOrdemServicos;

    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    
    private final OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
    private final ItemOSDAO itemOSDAO = new ItemOSDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ordemServicoDAO.setConnection(connection);

        carregarTableView();

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));
    }

    public void carregarTableView() {
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        tableColumnOrdemServicoNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        //tableColumnVendaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableColumnOrdemServicoAgenda.setCellFactory(column -> {
            return new TableCell<OrdemServico, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(myDateFormatter.format(item));
                    }
                }
            };
        });
       
        tableColumnOrdemServicoAgenda.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableColumnOrdemServicoVeiculo.setCellValueFactory(new PropertyValueFactory<>("veiculo"));

        listaOrdemServico = ordemServicoDAO.listar();

        observableListOrdemServicos = FXCollections.observableArrayList(listaOrdemServico);
        tableView.setItems(observableListOrdemServicos);
    }

    public void selecionarItemTableView(OrdemServico ordemServico) {
        if (ordemServico != null) {
            labelOrdemServicoNumero.setText(Long.toString(ordemServico.getNumero()));
            labelOrdemServicoTotal.setText(String.format("%.2f", ordemServico.getTotal()));
            labelOrdemServicoAgenda.setText(String.valueOf(
                    ordemServico.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            labelOrdemServicoDesconto.setText((String.format("%.2f", ordemServico.getDesconto())) + "%");
            labelOrdemServicoVeiculo.setText(ordemServico.getVeiculo().getCliente().getNome());
            labelOrdemServicoStatus.setText(ordemServico.getStatus().name());
        } else {
            labelOrdemServicoNumero.setText("");
            labelOrdemServicoTotal.setText("");
            labelOrdemServicoAgenda.setText("");
            labelOrdemServicoDesconto.setText("");
            labelOrdemServicoVeiculo.setText("");
            labelOrdemServicoStatus.setText("");
        }
    }

    @FXML
private void handleButtonInserir(ActionEvent event) throws IOException {
    OrdemServico ordemServico = new OrdemServico();
    List<ItemOS> itensOS = new ArrayList<>();
    ordemServico.setItensOS(itensOS);
    
    // Chama o método que exibe o diálogo de cadastro
    boolean buttonConfirmarClicked = showFXMLAnchorPaneOrdensDeServicoDialog(ordemServico);
    if (buttonConfirmarClicked) {
        // Define a conexão do DAO
        ordemServicoDAO.setConnection(connection);
        
        // Insere a nova ordem de serviço no banco de dados
        ordemServicoDAO.inserir(ordemServico);
        
        // Recarrega a tabela para refletir as mudanças
        carregarTableView();
    }
}

    @FXML
    private void handleButtonAlterar(ActionEvent event) throws IOException {
        OrdemServico ordemServico = tableView.getSelectionModel().getSelectedItem();
        if (ordemServico != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneOrdensDeServicoDialog(ordemServico);
            if (buttonConfirmarClicked) {
                ordemServicoDAO.alterar(ordemServico);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma ordem de serviço na Tabela.");
            alert.show();
        }        
    }

    @FXML
    private void handleButtonRemover(ActionEvent event) throws SQLException {
     OrdemServico ordemServico = tableView.getSelectionModel().getSelectedItem();
        if (ordemServico != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Excluir Ordem de Serviço");
            alert.setContentText("Deseja realmente excluir essa ordem de serviço ?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK)
            {
                ordemServicoDAO.remover(ordemServico);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma Ordem de Serviço na Tabela.");
            alert.show();
        }
    }

    public boolean showFXMLAnchorPaneOrdensDeServicoDialog(OrdemServico ordemServico) throws IOException {
     FXMLLoader loader = new FXMLLoader();

     // Corrigindo o caminho para o arquivo FXML
     loader.setLocation(getClass().getResource("/br/edu/ifsc/fln/view/FXMLAnchorPaneOrdensServicoDialog.fxml"));

     AnchorPane page = (AnchorPane) loader.load();

     // Criando um estágio de diálogo (Stage Dialog)
     Stage dialogStage = new Stage();
     dialogStage.setTitle("Cadastro de Ordens de Serviço");
     Scene scene = new Scene(page);
     dialogStage.setScene(scene);

     // Setando a ordem de serviço ao controlador
     FXMLAnchoPaneOrdensServicoDialogController controller = loader.getController();
     controller.setDialogStage(dialogStage);
     controller.setOrdemServico(ordemServico);

     // Mostra o diálogo e espera até que o usuário o feche
     dialogStage.showAndWait();

     return controller.isButtonConfirmarClicked();
 }


}
