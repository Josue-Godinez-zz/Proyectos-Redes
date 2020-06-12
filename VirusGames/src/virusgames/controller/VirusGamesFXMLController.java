/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virusgames.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import virusgames.serviceconexion.Cliente;
import virusgames.util.AppContext;
import virusgames.util.FlowController;
import virusgames.util.Formato;

/**
 * FXML Controller class
 *
 * @author josue
 */
public class VirusGamesFXMLController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Button btnSalir;
    @FXML
    private TextField tbIPHost;
    @FXML
    private Text txtError;
    @FXML
    private Button btnJoin;
    @FXML
    private Button btnLogOut;
    @FXML
    private TextField tbUserName;
    @FXML
    private Button btnReady;
    
    Map<String, String> diccionario = new HashMap<>();
    
    /*Variable Propias*/
    Stage stage;
    public SimpleIntegerProperty cantidadJugador = new SimpleIntegerProperty(0);
    public SimpleBooleanProperty isGameStart = new SimpleBooleanProperty(false);
    public Cliente cliente;
    public Boolean isHost;
    public boolean isReady = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        isGameStart.addListener(t->{
            AppContext.getInstance().set("cliente", cliente);
            FlowController.getInstance().goViewInStage("GameFXML", (Stage)root.getScene().getWindow(), true);
        });
        
       tbUserName.setTextFormatter(Formato.getInstance().maxLengthFormat(7));
       tbIPHost.setTextFormatter(Formato.getInstance().ipFormat());
    }
     @Override
    public void initialize() {
        
        stage = (Stage)root.getScene().getWindow();
        
    }

    @FXML
    private void OnActionbtnCliente(ActionEvent event) {
        if (tbUserName.getText().length() != 0 && tbIPHost.getText().length() != 0) {
            tbIPHost.setStyle("");
            tbUserName.setStyle("");
            cliente = new Cliente(tbIPHost.getText());
            cliente.nuevoClient(tbUserName.getText());
            tbIPHost.setDisable(true);
            btnJoin.setVisible(false);
            btnLogOut.setVisible(true);
            Thread changeView = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (cliente != null) {
//                        isGameStart.setValue(cliente.getChangeView());
                    }
                }
            });
            changeView.start();
        } else {
            if(tbUserName.getText().length() == 0)
            {
                tbUserName.setStyle("-fx-border-color: RED;");
            }
            else
            {
                tbUserName.setStyle("");
            }
            if(tbIPHost.getText().length() == 0)
            {
                tbIPHost.setStyle("-fx-border-color: RED;");
            }
            else
            {
                tbIPHost.setStyle("");
            }
        }
    }

    @FXML
    private void exit(ActionEvent event) {
    }

    @FXML
    private void desconectar(ActionEvent event) {
        cliente.desconectarCliente();
        cliente = null;
        btnJoin.setVisible(true);
        btnLogOut.setVisible(false);
        tbIPHost.setDisable(false);
    }

    @FXML
    private void OnActionbtnReady(ActionEvent event) {
        if(isReady){
            btnReady.setText("Listo");
            isReady = false;
            
            if(cliente != null){
                cliente.isReady(isReady);
            }
        }
        else {
            btnReady.setText("No Listo");
            isReady = true;
            
            if(cliente != null){
                cliente.isReady(isReady);
            }
        }
        
        
    }

}
