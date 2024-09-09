/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Motor;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroModeloDialogController implements Initializable {

    @FXML
    private TextField tfDescricao;   
    @FXML
    private ComboBox<Marca> cbMarca;
    
    @FXML
    private ChoiceBox<ECategoria> chCategoria;
    
    @FXML
    private Spinner<Integer> spPotencia;
    
    @FXML
    private ChoiceBox<ETipoCombustivel> chCombustivel;

    @FXML
    private Button btConfirmar;

    @FXML
    private Button btCancelar;
    
//    private List<Marca> listaMarcas;
//    private ObservableList<Marca> observableListMarcas;
        
    //atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    
    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Modelo modelo;  
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marcaDAO.setConnection(connection);
        carregarComboBoxMarcas();
        carregarChoiceBoxCategoria();
        carregarChoiceBoxCombustivel();
        carregarSpinnerPotencia();
        setFocusLostHandle();
    } 
    
    private void setFocusLostHandle() {
        tfDescricao.focusedProperty().addListener((ov, oldV, newV) -> {
        if (!newV) { // focus lost
                if (tfDescricao.getText() == null || tfDescricao.getText().isEmpty()) {
                    //System.out.println("teste focus lost");
                    tfDescricao.requestFocus();
                }
            }
        });
    }
    
//This works fine too:    
//root.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//    focusState(newValue);
//});
//
//private void focusState(boolean value) {
//    if (value) {
//        System.out.println("Focus Gained");
//    }
//    else {
//        System.out.println("Focus Lost");
//    }
//} 
    
    private List<Marca> listaMarcas;
    private ObservableList<Marca> observableListMarcas; 
    
    public void carregarComboBoxMarcas() {
        listaMarcas = marcaDAO.listar();
        observableListMarcas = 
                FXCollections.observableArrayList(listaMarcas);
        cbMarca.setItems(observableListMarcas);
    } 
    
    public void carregarChoiceBoxCategoria() {
        chCategoria.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
                    event.consume();
                }
            }
        });
        chCategoria.setItems(FXCollections.observableArrayList(ECategoria.values()));
        //chModelos.getSelectionModel().select(-1);
    }
    
    public void carregarChoiceBoxCombustivel() {
        chCombustivel.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
                    event.consume();
                }
            }
        });
               chCombustivel.setItems(FXCollections.observableArrayList(ETipoCombustivel.values()));
        //chModelos.getSelectionModel().select(-1);
    }    
    
//      public void carregarSpinnerPotencia() {
//          SpinnerValueFactory<Integer> valueFactory = 
//                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 0);  // Intervalo de 50 a 1000 com valor inicial 100
//        spPotencia.setValueFactory(valueFactory);
//    }
      
    public void carregarSpinnerPotencia() {
        SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0);  // Intervalo de 1 a 1000 com valor inicial 0
        spPotencia.setValueFactory(valueFactory);

        // Permitir a edição direta no Spinner
        spPotencia.setEditable(true);

        // Adicionar um listener para validar a entrada do usuário
        spPotencia.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int value = Integer.parseInt(newValue);
                if (value < 1 || value > 1000) {
                    spPotencia.getEditor().setText(oldValue); // Mantém o valor anterior se a entrada for inválida
                } else {
                    valueFactory.setValue(value); // Atualiza o Spinner com o novo valor válido
                }
            } catch (NumberFormatException e) {
                spPotencia.getEditor().setText(oldValue); // Se o valor digitado não for um número, mantém o valor anterior
            }
        });
    }
      
    public Stage getDialogStage() {
        return dialogStage;
    }

    /**
     * @param dialogStage the dialogStage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * @return the buttonConfirmarClicked
     */
    public boolean isBtConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    /**
     * @param buttonConfirmarClicked the buttonConfirmarClicked to set
     */
    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    /**
     * @return the modelo
     */
    public Modelo getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
        tfDescricao.setText(modelo.getDescricao());
        cbMarca.getSelectionModel().select(modelo.getMarca());
        chCategoria.getSelectionModel().select(modelo.getCategoria());
        spPotencia.getValueFactory().setValue(modelo.getMotor().getPotencia());
        chCombustivel.getSelectionModel().select(modelo.getMotor().getTipoCombustivel());
    }    
    
    @FXML
    private void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            modelo.setDescricao(tfDescricao.getText());
            modelo.setMarca(
                    cbMarca.getSelectionModel().getSelectedItem());
            modelo.setCategoria(
                    chCategoria.getSelectionModel().getSelectedItem());
            modelo.getMotor().setPotencia(spPotencia.getValue());
            modelo.getMotor().setTipoCombustivel(
                    chCombustivel.getSelectionModel().getSelectedItem());
            

            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    private void handleBtCancelar() {
        dialogStage.close();
    }
    
        //validar entrada de dados do cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        
        if (tfDescricao.getText() == null || tfDescricao.getText().isEmpty()) {
            errorMessage += "Nome inválido!\n";
        }

       
        
        if (cbMarca.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma marca!\n";
        }
        
        if (chCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma categoria!\n";
        }
        
//        if (cbMarca.getSelectionModel().getSelectedItem() == null) {
//            errorMessage += "Selecione uma marca!\n";
//        }
        
        if (chCombustivel.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione um tipo de combustivel!\n";
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
