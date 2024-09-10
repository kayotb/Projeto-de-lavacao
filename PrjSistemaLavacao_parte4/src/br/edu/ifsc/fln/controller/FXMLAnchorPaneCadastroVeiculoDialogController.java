package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class FXMLAnchorPaneCadastroVeiculoDialogController implements Initializable {

    @FXML
    private TextField tfPlaca;

    @FXML
    private TextArea txObs;

    @FXML
    private ComboBox<Cliente> cbCliente;

    @FXML
    private ChoiceBox<Modelo> chModelo;

    @FXML
    private ChoiceBox<Cor> chCor;

    @FXML
    private Button btConfirmar;

    @FXML
    private Button btCancelar;

    // Atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    private final CorDAO corDAO = new CorDAO();

    private ObservableList<Cliente> observableListClientes;
    private ObservableList<Modelo> observableListModelos;
    private ObservableList<Cor> observableListCores;

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Veiculo veiculo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        veiculoDAO.setConnection(connection);
        clienteDAO.setConnection(connection);
        modeloDAO.setConnection(connection);
        corDAO.setConnection(connection);

        carregarComboBoxCliente();
        carregarChoiceBoxModelo();
        carregarChoiceBoxCor();
    }

    private void carregarComboBoxCliente() {
        List<Cliente> listaCliente = clienteDAO.listar();
        observableListClientes = FXCollections.observableArrayList(listaCliente);
        cbCliente.setItems(observableListClientes);

        // Configura o StringConverter para exibir apenas o nome do cliente na ComboBox
        cbCliente.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente cliente) {
                return cliente != null ? cliente.getNome() : "";
            }

            @Override
            public Cliente fromString(String string) {
                return cbCliente.getItems().stream()
                        .filter(cliente -> cliente.getNome().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    private void carregarChoiceBoxModelo() {
        try {
            List<Modelo> listaModelo = modeloDAO.listar();
            observableListModelos = FXCollections.observableArrayList(listaModelo);
            chModelo.setItems(observableListModelos);

            // Configura o StringConverter para exibir apenas a descrição do modelo na ChoiceBox
            chModelo.setConverter(new StringConverter<Modelo>() {
                @Override
                public String toString(Modelo modelo) {
                    return modelo != null ? modelo.getDescricao() : "";
                }

                @Override
                public Modelo fromString(String string) {
                    return chModelo.getItems().stream()
                            .filter(modelo -> modelo.getDescricao().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ao carregar modelos");
            alert.setHeaderText("Não foi possível carregar os modelos.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void carregarChoiceBoxCor() {
        try {
            List<Cor> listaCor = corDAO.listar();
            observableListCores = FXCollections.observableArrayList(listaCor);
            chCor.setItems(observableListCores);

            // Configura o StringConverter para exibir apenas o nome da cor na ChoiceBox
            chCor.setConverter(new StringConverter<Cor>() {
                @Override
                public String toString(Cor cor) {
                    return cor != null ? cor.getNome() : "";
                }

                @Override
                public Cor fromString(String string) {
                    return chCor.getItems().stream()
                            .filter(cor -> cor.getNome().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ao carregar cores");
            alert.setHeaderText("Não foi possível carregar as cores.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isBtConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
        txObs.setText(veiculo.getObservacoes());
        tfPlaca.setText(veiculo.getPlaca());
        cbCliente.getSelectionModel().select(veiculo.getCliente());
        chCor.getSelectionModel().select(veiculo.getCor());
        chModelo.getSelectionModel().select(veiculo.getModelo());
    }

    @FXML
    private void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            veiculo.setObservacoes(txObs.getText());
            veiculo.setPlaca(tfPlaca.getText());
            veiculo.setCliente(cbCliente.getSelectionModel().getSelectedItem());
            veiculo.setModelo(chModelo.getSelectionModel().getSelectedItem());
            veiculo.setCor(chCor.getSelectionModel().getSelectedItem());

            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleBtCancelar() {
        dialogStage.close();
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (txObs.getText() == null || txObs.getText().isEmpty()) {
            errorMessage += "Observações inválidas!\n";
        }

        if (tfPlaca.getText() == null || tfPlaca.getText().isEmpty()) {
            errorMessage += "Placa inválida!\n";
        }

        if (cbCliente.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione um Cliente!\n";
        }

        if (chModelo.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione um modelo!\n";
        }

        if (chCor.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma cor!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campo(s) inválido(s), por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}
