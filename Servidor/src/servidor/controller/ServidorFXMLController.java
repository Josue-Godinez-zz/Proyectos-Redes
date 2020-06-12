/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.controller;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import servidor.serviceConexion.Server;
import servidor.serviceConexion.ServidorHilo;

/**
 *
 * @author josue
 */
public class ServidorFXMLController implements Initializable {
    
    private Label label;
    @FXML
    private Button btnIniciarServidor;
    @FXML
    private Button btnStopServer;
    @FXML
    private Text txtCantidadPlayer;
    @FXML
    private TableView<ServidorHilo> tvLista;
    @FXML
    private TableColumn<ServidorHilo, String> tcUserName;
    @FXML
    private TableColumn<ServidorHilo, Boolean> tcisReady;
    @FXML
    private Button btnStartGame;
    
    /* Variables Propias*/
    Server server = null;
    public SimpleIntegerProperty cantidadJugadores = new SimpleIntegerProperty(0);
    public boolean isGameStart = false;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cantidadJugadores.addListener(t->{
            txtCantidadPlayer.setText(cantidadJugadores.getValue().toString());
        });
        
        tcUserName.setCellValueFactory(e -> e.getValue().userName);
        tcisReady.setCellValueFactory(e -> e.getValue().boolListo);

        Thread refresh = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isGameStart)
                {
                    tvLista.refresh();
                }
            }
        });
        refresh.start();
    }    

    @FXML
    private void arrancarServer(ActionEvent event) {
        if(server == null)
        {
            System.out.println("Abriendo Server...");
            server = new Server(cantidadJugadores);
            server.iniciarProceso();
            txtCantidadPlayer.setText(server.cantPlayer.getValue().toString());
            
            tvLista.setItems(server.clients);
        }
    }

    @FXML
    private void pararServer(ActionEvent event) {
        if(server != null)
        {
            server.cerrarServidor();
            server = null;
        }
    }

    @FXML
    private void startGame(ActionEvent event) {
        isGameStart = true;
        if(server != null)
        {
            server.startGame();
        }
    }
    
}
