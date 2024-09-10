package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.EStatus;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Servico;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.Connection;
import java.util.ArrayList;
import javafx.util.StringConverter;

public class FXMLAnchoPaneOrdensServicoDialogController implements Initializable {

    @FXML
    private TextField textFieldCliente;
    @FXML
    private TextField textFieldModelo;
    @FXML
    private TextField textFieldMarca;
    @FXML
    private TextField textFieldCategoria;
    @FXML
    private DatePicker datePickerData;
    @FXML
    private ComboBox<Servico> comboBoxServico;
    @FXML
    private TextField textFieldValorServico;
    @FXML
    private TextField textFieldObservacoes;
    @FXML
    private Button buttonAdicionar;
    @FXML
    private TableView<ItemOS> tableViewServicos;
    @FXML
    private TableColumn<ItemOS, String> tableColumnServico;
    @FXML
    private TableColumn<ItemOS, BigDecimal> tableColumnValor;
    @FXML
    private TableColumn<ItemOS, String> tableColumnObservacoes;
    @FXML
    private TextField textFieldDesconto;
    @FXML
    private TextField textFieldValorTotal;
    @FXML
    private ChoiceBox<EStatus> choiceBoxStatus;
    @FXML
    private Button buttonConfirmar;
    @FXML
    private Button buttonCancelar;
    @FXML
    private ComboBox<Veiculo> comboBoxPesquisaPlaca;
    @FXML
    private Button buttonPesquisarDados;

    private List<Veiculo> listaVeiculos;
    private List<Servico> listaServicos;
    private ObservableList<Veiculo> observableListVeiculos;
    private ObservableList<Servico> observableListServicos;
    private ObservableList<ItemOS> observableListItensOS;

    // Atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private OrdemServico ordemServico;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        veiculoDAO.setConnection(connection);
        servicoDAO.setConnection(connection);
        carregarComboBoxVeiculos();
        carregarComboBoxServicos();
        carregarChoiceBoxStatus();
        configurarColunasTabela();

        // Inicialize a lista observável com uma lista vazia para evitar erros
        observableListItensOS = FXCollections.observableArrayList();
        tableViewServicos.setItems(observableListItensOS);
    }

    private void carregarComboBoxVeiculos() {
        listaVeiculos = veiculoDAO.listar();
        for (Veiculo veiculo : listaVeiculos) {
            System.out.println("Placa carregada: " + veiculo.getPlaca());
        }
        observableListVeiculos = FXCollections.observableArrayList(listaVeiculos);
        comboBoxPesquisaPlaca.setItems(observableListVeiculos);

        comboBoxPesquisaPlaca.setConverter(new StringConverter<Veiculo>() {
            @Override
            public String toString(Veiculo veiculo) {
                return veiculo != null ? veiculo.getPlaca() : "";
            }

            @Override
            public Veiculo fromString(String string) {
                return null; // Não utilizado, pois a seleção será feita via objeto
            }
        });
    }

    private void carregarComboBoxServicos() {
    listaServicos = servicoDAO.listar();
    observableListServicos = FXCollections.observableArrayList(listaServicos);
    comboBoxServico.setItems(observableListServicos);

    // Configura o ComboBox para exibir apenas o nome do serviço
    comboBoxServico.setConverter(new StringConverter<Servico>() {
        @Override
        public String toString(Servico servico) {
            return servico != null ? servico.getDescricao() : "";
        }

        @Override
        public Servico fromString(String string) {
            return null; // Não será necessário
        }
    });

    // Adiciona um listener para atualizar o valor do serviço quando um serviço for selecionado
    comboBoxServico.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
             textFieldValorServico.setText(String.valueOf(newValue.getValor())); 
        } else {
            textFieldValorServico.clear(); // Limpa o campo se nenhum serviço estiver selecionado
        }
    }); 
}

    private void carregarChoiceBoxStatus() {
        choiceBoxStatus.setItems(FXCollections.observableArrayList(EStatus.values()));
        choiceBoxStatus.getSelectionModel().select(0);
    }

    private void configurarColunasTabela() {
    // Coluna que mostra o nome do serviço
    tableColumnServico.setCellValueFactory(new PropertyValueFactory<>("servico")); 

    // Coluna que mostra o valor do serviço
    tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valorServico"));

    // Coluna que mostra as observações
    tableColumnObservacoes.setCellValueFactory(new PropertyValueFactory<>("observacoes"));
}


    @FXML
private void handleButtonPesquisarDados() {
    Veiculo veiculoSelecionado = comboBoxPesquisaPlaca.getSelectionModel().getSelectedItem();

    if (veiculoSelecionado != null) {
        // Agora usando o ID do veículo selecionado para buscar
        Veiculo veiculoParaBuscar = new Veiculo();
        veiculoParaBuscar.setId(veiculoSelecionado.getId());  // Utilizando o ID para a busca

        // Agora usando o objeto criado para buscar o veículo pelo ID
        Veiculo veiculo = veiculoDAO.buscar(veiculoParaBuscar);

        if (veiculo != null) {
            textFieldModelo.setText(veiculo.getModelo().getDescricao());

            // Verifica se a marca não é nula antes de acessar o nome
            if (veiculo.getModelo().getMarca() != null) {
                textFieldMarca.setText(veiculo.getModelo().getMarca().getNome());
            } else {
                textFieldMarca.setText("Marca não disponível");
            }

            // Verifica se a categoria não é nula
            if (veiculo.getModelo().getCategoria() != null) {
                textFieldCategoria.setText(veiculo.getModelo().getCategoria().name());
            } else {
                textFieldCategoria.setText("Categoria não disponível");
            }

            Cliente cliente = veiculo.getCliente();
            if (cliente != null) {
                textFieldCliente.setText(cliente.getNome());
            } else {
                textFieldCliente.setText("");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Veículo não encontrado");
            alert.setHeaderText("Nenhum veículo encontrado com a placa selecionada.");
            alert.setContentText("Por favor, selecione uma placa válida.");
            alert.show();
        }
    } else {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Seleção de Veículo");
        alert.setHeaderText("Nenhum veículo selecionado.");
        alert.setContentText("Por favor, selecione uma placa de veículo no campo de pesquisa.");
        alert.show();
    }
}
    


   @FXML
public void handleButtonAdicionar() {
    Servico servicoSelecionado = comboBoxServico.getSelectionModel().getSelectedItem();
    
    if (servicoSelecionado != null) {
        // Criar um novo item de ordem de serviço (ItemOS)
        ItemOS itemOS = new ItemOS();
        itemOS.setServico(servicoSelecionado);
        itemOS.setValorServico(servicoSelecionado.getValor());
        itemOS.setObservacoes(textFieldObservacoes.getText());

        // Inicializar a lista de itens da ordem de serviço se for nula
        if (ordemServico.getItensOS() == null) {
            ordemServico.setItensOS(new ArrayList<>());
        }

        // Adicionar o novo item à lista de itens da ordem de serviço
        ordemServico.getItensOS().add(itemOS);

        // Atualizar a ObservableList com os itens da ordem de serviço
        observableListItensOS = FXCollections.observableArrayList(ordemServico.getItensOS());
        tableViewServicos.setItems(observableListItensOS); // Atualizar a TableView

        // Atualizar o valor total
        atualizarValorTotal();
    } else {
        // Exibir um alerta caso nenhum serviço tenha sido selecionado
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Seleção de Serviço");
        alert.setHeaderText("Nenhum serviço selecionado.");
        alert.setContentText("Por favor, selecione um serviço para adicionar.");
        alert.show();
    }
}


private void atualizarValorTotal() {
    double valorTotal = 0.0;

    for (ItemOS item : ordemServico.getItensOS()) {
        // Somar o valor do serviço ao valor total
        valorTotal += item.getValorServico(); // Como getValorServico() retorna double, basta somar diretamente
    }

    // Exibir o valor total formatado no campo textFieldValorTotal
    textFieldValorTotal.setText(String.format("%.2f", valorTotal));
}




    @FXML
    private void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            ordemServico.setVeiculo(comboBoxPesquisaPlaca.getSelectionModel().getSelectedItem());
            ordemServico.setData(datePickerData.getValue());
            ordemServico.setStatus((EStatus) choiceBoxStatus.getSelectionModel().getSelectedItem());
            ordemServico.setDesconto(Double.parseDouble(textFieldDesconto.getText()));
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleButtonCancelar() {
        dialogStage.close();
    }

//    private void atualizarValorTotal() {
//        textFieldValorTotal.setText(String.format("%.2f", ordemServico.getTotal()));
//    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (comboBoxPesquisaPlaca.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Veículo inválido!\n";
        }
        if (datePickerData.getValue() == null) {
            errorMessage += "Data inválida!\n";
        }
        if (observableListItensOS == null || observableListItensOS.isEmpty()) {
            errorMessage += "Itens de Ordem de Serviço inválidos!\n";
        }

        try {
            new DecimalFormat("0.00").parse(textFieldDesconto.getText());
        } catch (ParseException e) {
            errorMessage += "A taxa de desconto está incorreta! Use \",\" como ponto decimal.\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
        if (ordemServico != null) {
            comboBoxPesquisaPlaca.getSelectionModel().select(ordemServico.getVeiculo());
            datePickerData.setValue(ordemServico.getData());
            observableListItensOS = FXCollections.observableArrayList(ordemServico.getItensOS());
            tableViewServicos.setItems(observableListItensOS);
            atualizarValorTotal();
            textFieldDesconto.setText(String.format("%.2f", ordemServico.getDesconto()));
            choiceBoxStatus.getSelectionModel().select(ordemServico.getStatus());
        }
    }
}
