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
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import virusgames.serviceconexion.Cliente;
import virusgames.serviceconexion.Servidor;
import virusgames.serviceconexion.ServidorHilo;
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
    private Button btnJugar;
    @FXML
    private Button btnSalir;
    @FXML
    private Button btnHost;
    @FXML
    private Button btnComenzar;
    @FXML
    private Text txtCantJugadores;
    @FXML
    private TextField tbIPHost;
    @FXML
    private Text txtError;
     @FXML
    private Button btnJoin;
    @FXML
    private Button btnLogOut;
    @FXML
    private Text txtPlayerError;
    @FXML
    private TextField tbUserName;
    @FXML
    private TextField tbUserHostName;
    @FXML
    private TableView<ServidorHilo> tableViewJugador;
    @FXML
    private TableColumn<ServidorHilo, String> columJugador;
    
    Map<String, String> diccionario = new HashMap<>();
    
    /*Variable Propias*/
    Stage stage;
    public SimpleIntegerProperty cantidadJugador = new SimpleIntegerProperty(0);
    public SimpleBooleanProperty isGameStart = new SimpleBooleanProperty(false);
    public Servidor servidor;
    public Cliente cliente;
    public Boolean isHost;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cantidadJugador.addListener(t->{
            txtCantJugadores.setText(cantidadJugador.getValue().toString());
        });
        
        isGameStart.addListener(t->{
            AppContext.getInstance().set("servidor", servidor);
            AppContext.getInstance().set("cliente", cliente);
            FlowController.getInstance().goViewInStage("GameFXML", (Stage)root.getScene().getWindow(), true);
        });
        
       tbUserName.setTextFormatter(Formato.getInstance().maxLengthFormat(7));
       tbIPHost.setTextFormatter(Formato.getInstance().ipFormat());
       tbUserHostName.setTextFormatter(Formato.getInstance().maxLengthFormat(7));
    }
     @Override
    public void initialize() {
        
        stage = (Stage)root.getScene().getWindow();
        stage.setOnCloseRequest(e->{
            if(servidor != null)
            {
                servidor.cerrarServidor();
            }
            if(cliente != null)
            {
                cliente.desconectarCliente();
            }
        });
    }

    @FXML
    private void OnActionbtnHost(ActionEvent event) {
        
        if (tbUserHostName.getText().length() != 0) {
            tbUserHostName.setStyle("");
            servidor = new Servidor(cantidadJugador);
            servidor.iniciarProceso();
            
            cliente = new Cliente("25.102.38.188");
            cliente.nuevoClient(0, tbUserHostName.getText(), 0);
            cliente.isHost = true;
            
            /*Setea la lista de jugadores*/
            tableViewJugador.setItems(servidor.clients);
            columJugador.setCellValueFactory(ed -> ed.getValue().userName);
            Servidor.clients.addListener((ListChangeListener<ServidorHilo>) s -> {
            tableViewJugador.refresh();
            });
            
            Thread changeView = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (cliente != null) {
                        isGameStart.setValue(cliente.getChangeView());
                    }
                }
            });
            changeView.start();
        }
        else
        {
            tbUserHostName.setStyle("-fx-border-color: RED;");
        }
    }

    @FXML
    private void OnActionbtnComenzar(ActionEvent event) {
        if (servidor != null) {
            if (cantidadJugador.getValue() > 1) {
                AppContext.getInstance().set("servidor", servidor);
                AppContext.getInstance().set("cliente", cliente);
                txtPlayerError.setVisible(false);
                servidor.setAcceptClient(false);
                servidor.setGameStart(true);
                servidor.startGame();
            } else {
                txtPlayerError.setVisible(true);
            }
        }
    }

    @FXML
    private void OnActionbtnCliente(ActionEvent event) {
        if (tbUserName.getText().length() != 0 && tbIPHost.getText().length() != 0) {
            tbIPHost.setStyle("");
            tbUserName.setStyle("");
            cliente = new Cliente(tbIPHost.getText());
            cliente.nuevoClient(0, tbUserName.getText(), 0);
            tbIPHost.setDisable(true);
            btnJoin.setVisible(false);
            btnLogOut.setVisible(true);
            Thread changeView = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (cliente != null) {
                        isGameStart.setValue(cliente.getChangeView());
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
    private void client(Event event) {
        if(servidor != null)
        {
            servidor.cerrarServidor();
        }
        if(cliente != null)
        {
            cliente.desconectarCliente();
        }
    }
    
    @FXML
    private void changeView(ActionEvent event) {

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

}
