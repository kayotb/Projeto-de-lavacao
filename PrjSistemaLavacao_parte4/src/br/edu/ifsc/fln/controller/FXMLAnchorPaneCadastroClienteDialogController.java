/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Fisica;
import br.edu.ifsc.fln.model.domain.Juridica;
import java.net.URL;
import java.util.ResourceBundle;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mpisc
 */
public class FXMLAnchorPaneCadastroClienteDialogController implements Initializable {

     
    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private DatePicker dpCadastro;

    @FXML
    private Group gbTipo;

    @FXML
    private RadioButton rbFisica;

    @FXML
    private RadioButton rbJurudica;

    @FXML
    private TextField tfCPF;
    
    @FXML
    private Label lbCPF;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfIE;

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfTelefone;

    @FXML
    private ToggleGroup tgTipo;


    
    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Cliente cliente;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        dpCadastro.setValue(LocalDate.now());
        rbFisica.setSelected(true);  // Define "Pessoa Física" como padrão
        handleRbFisica();            // Desativa o campo Inscrição Estadual
    }       

    public boolean isBtConfirmarClicked() {
        return btConfirmarClicked;
    }

    public void setBtConfirmarClicked(boolean btConfirmarClicked) {
        this.btConfirmarClicked = btConfirmarClicked;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
            this.tfNome.setText(this.cliente.getNome());
            this.tfEmail.setText(this.cliente.getEmail());
            this.tfTelefone.setText(this.cliente.getCelular());
            this.gbTipo.setDisable(true);
            cliente.setDataCadastro(dpCadastro.getValue());
            if (cliente instanceof Fisica) {
                rbFisica.setSelected(true);
                tfCPF.setText(((Fisica) this.cliente).getCpf());
            } else {              
                rbJurudica.setSelected(true);
                tfCPF.setText(((Juridica) this.cliente).getCnpj());
                tfIE.setText(((Juridica) this.cliente).getInscricaoEstadual());
                handleRbJuridica();
            }
            
        //Data picker 
        if (this.cliente.getDataCadastro() != null) {
            dpCadastro.setValue(this.cliente.getDataCadastro());
        } else {
            dpCadastro.setValue(LocalDate.now());        }
    }
    

    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            cliente.setNome(tfNome.getText());

            // Verifica o tipo de cliente e atribui os valores correspondentes
            if (cliente instanceof Fisica) {
                ((Fisica) cliente).setCpf(tfCPF.getText());
            } else if (cliente instanceof Juridica) {
                ((Juridica) cliente).setCnpj(tfCPF.getText());
                ((Juridica) cliente).setInscricaoEstadual(tfIE.getText());
            }
            cliente.setCelular(tfTelefone.getText());
            cliente.setEmail(tfEmail.getText());
            cliente.setDataCadastro(dpCadastro.getValue());  // Salva a data selecionada

            btConfirmarClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    public void handleBtCancelar() {
        dialogStage.close();
    }
    
    @FXML
    public void handleRbFisica() {
        System.out.println("Pessoa Física selecionada");
        lbCPF.setText("CPF");        // Muda o texto do Label para CPF
        tfCPF.setPromptText("CPF");  // Ajusta o texto de dica para CPF
        tfIE.setDisable(true);       // Desativa o campo Inscrição Estadual
        tfIE.setText("");            // Limpa o campo Inscrição Estadual
    }

    @FXML
    public void handleRbJuridica() {
        System.out.println("Pessoa Juridica selecionada");
        lbCPF.setText("CNPJ");        // Muda o texto do Label para CPF
        tfCPF.setPromptText("CNPJ"); // Ajusta o texto de dica para CNPJ
        tfIE.setDisable(false);    // Ativa o campo Inscrição Estadual
    }
    
    //método para validar a entrada de dados
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        // Validação do nome
        if (this.tfNome.getText() == null || this.tfNome.getText().trim().isEmpty()) {
            errorMessage += "Nome inválido.\n";
        }

        // Validação do CPF/CNPJ
        if (this.tfCPF.getText() == null || this.tfCPF.getText().trim().isEmpty()) {
            if (rbFisica.isSelected()) {
                errorMessage += "CPF inválido.\n";
            } else if (rbJurudica.isSelected()) {
                errorMessage += "CNPJ inválido.\n";
            }
        }

        // Validação do Telefone
        if (this.tfTelefone.getText() == null || this.tfTelefone.getText().trim().isEmpty()) {
            errorMessage += "Telefone inválido.\n";
        }

        // Validação do Email
        if (this.tfEmail.getText() == null || this.tfEmail.getText().trim().isEmpty()) {
            errorMessage += "Email inválido.\n";
        }

        // Validação da Inscrição Estadual (somente para clientes Jurídicos)
        if (rbJurudica.isSelected() && (this.tfIE.getText() == null || this.tfIE.getText().trim().isEmpty())) {
            errorMessage += "Inscrição Estadual inválida.\n";
        }        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            //exibindo uma mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
    
}
