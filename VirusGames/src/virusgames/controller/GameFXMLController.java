/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virusgames.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import virusgames.mazo.Carta;
import virusgames.serviceconexion.Cliente;
import virusgames.serviceconexion.Servidor;
import virusgames.util.AppContext;

/**
 * FXML Controller class
 *
 * @author josue
 */
public class GameFXMLController extends Controller implements Initializable {

    @FXML
    private VBox vbMesa3;
    @FXML
    private VBox vbMesa2;
    @FXML
    private VBox vbMesa4;
    @FXML
    private VBox vbMesa5;
    @FXML
    private VBox vbMesa1;
    @FXML
    private VBox vbMesa6;
    @FXML
    private Button btnDrawCard;
    @FXML
    private AnchorPane root;
    @FXML
    private ImageView ivMazo;
    
    /*Variables Propias*/
    public ArrayList<VBox> mesasDisponibles = new ArrayList<>();
    public Servidor servidor;
    public Cliente cliente;
    LogicalGame logical;
    public static Carta carta;
    public HBox mesaPropia;
    public ArrayList<HBox> mesaEnemigas;
    public int changeCard;
    Map<String, String> diccionario;
    public ArrayList<Carta> cartasSelecionada = new ArrayList<>();
    public int cantidadCartas = 0;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        servidor = (Servidor)AppContext.getInstance().get("servidor");
        cliente = (Cliente)AppContext.getInstance().get("cliente");
        
        tableroDinamico(cliente.getCantidadJugadores());
        generarDiccionario();
        if(cliente.isHost)
        {
            generarJuego();
            cargarLogical();
        }
        else
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
            cargarJuego();
            cargarLogical();
            
        }
        
        ivMazo.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
            cantidadCartas++;
            if(cantidadCartas == 1)
            {
                btnDrawCard.setDisable(false);
                btnDrawCard.setText("DRAW <"+cantidadCartas+">");
            }
            if(cantidadCartas == 2)
            {
                btnDrawCard.setText("DRAW <"+cantidadCartas+">");
            }
            if(cantidadCartas == 3)
            {
                btnDrawCard.setText("DRAW <"+cantidadCartas+">");
            }
            if(cantidadCartas == 4)
            {
                btnDrawCard.setDisable(true);
                btnDrawCard.setText("DRAW");
                cantidadCartas = 0;
            }
        });
    }    

    @Override
    public void initialize() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @FXML
    private void changeCard(ActionEvent event) {
        
    }
    
    public void tableroDinamico(int cantidad)
    {
        if(cantidad == 2)
        {
            mesasDisponibles.add(vbMesa1);
            mesasDisponibles.add(vbMesa2);
            vbMesa3.setVisible(false);
            vbMesa5.setVisible(false);
            vbMesa4.setVisible(false);
            vbMesa6.setVisible(false);
        }
        if(cantidad == 3)
        {
            mesasDisponibles.add(vbMesa1);
            mesasDisponibles.add(vbMesa3);
            mesasDisponibles.add(vbMesa4);
            vbMesa5.setVisible(false);
            vbMesa2.setVisible(false);
            vbMesa6.setVisible(false);
        }
        if(cantidad == 4)
        {
            mesasDisponibles.add(vbMesa3);
            mesasDisponibles.add(vbMesa5);
            mesasDisponibles.add(vbMesa4);
            mesasDisponibles.add(vbMesa6);
            vbMesa2.setVisible(false);
            vbMesa1.setVisible(false);
        }
        if(cantidad == 5)
        {
            mesasDisponibles.add(vbMesa2);
            mesasDisponibles.add(vbMesa3);
            mesasDisponibles.add(vbMesa4);
            mesasDisponibles.add(vbMesa5);
            mesasDisponibles.add(vbMesa6);
            vbMesa1.setVisible(false);
        }
        if(cantidad == 6) 
        {
          mesasDisponibles.add(vbMesa1);
          mesasDisponibles.add(vbMesa2);
          mesasDisponibles.add(vbMesa3);
          mesasDisponibles.add(vbMesa4);
          mesasDisponibles.add(vbMesa5);
          mesasDisponibles.add(vbMesa6);
        }
    }
    
    public void generarJuego()
    {
        logical = new LogicalGame(cliente.cantidadPlayer);
        servidor.enviarJuego(logical);
    }
    
    public void cargarJuego()
    {
        logical = cliente.getJuego();
    }
    
    public void cargarLogical()
    {
        if(logical != null)
        {
            for(int x = 0; x < logical.cantidadJugadores; x++)
            {
                HBox aux = (HBox) mesasDisponibles.get(x).getChildren().get(1);
                ArrayList<Carta> manoJugador = logical.mazoJugadores.get(x);
                for(int y = 0 ; y < 3; y++)
                {
                    String img = (String) manoJugador.get(y).imgCarta;
                    ImageView carta = new ImageView(new Image(diccionario.get(img)));
                    carta.setFitHeight(85);
                    carta.setFitWidth(62);
                    carta.setPreserveRatio(true);
                    carta.setSmooth(true);
                    definirMovimientos(carta, manoJugador.get(y));
                    aux.getChildren().add(carta);
                }
            }
        }
    }
    
    public void definirMovimientos(ImageView carta, Carta card)
    {
       carta.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
           System.out.println("click");
           
       });
    }

    public void generarDiccionario()
    {
        diccionario = new HashMap<>();
        diccionario.put("O1", virusgames.VirusGames.class.getResource("resource/O1.png").toString());
        diccionario.put("O2", virusgames.VirusGames.class.getResource("resource/O2.png").toString());
        diccionario.put("O3", virusgames.VirusGames.class.getResource("resource/O3.png").toString());
        diccionario.put("O4", virusgames.VirusGames.class.getResource("resource/O4.png").toString());
        diccionario.put("V1", virusgames.VirusGames.class.getResource("resource/V1.png").toString());
        diccionario.put("V2", virusgames.VirusGames.class.getResource("resource/V2.png").toString());
        diccionario.put("V3", virusgames.VirusGames.class.getResource("resource/V3.png").toString());
        diccionario.put("V4", virusgames.VirusGames.class.getResource("resource/V4.png").toString());
        diccionario.put("M1", virusgames.VirusGames.class.getResource("resource/M1.png").toString());
        diccionario.put("M2", virusgames.VirusGames.class.getResource("resource/M2.png").toString());
        diccionario.put("M3", virusgames.VirusGames.class.getResource("resource/M3.png").toString());
        diccionario.put("M4", virusgames.VirusGames.class.getResource("resource/M4.png").toString());
        diccionario.put("T1", virusgames.VirusGames.class.getResource("resource/T1.png").toString());
        diccionario.put("T2", virusgames.VirusGames.class.getResource("resource/T2.png").toString());
        diccionario.put("T3", virusgames.VirusGames.class.getResource("resource/T3.png").toString());
        diccionario.put("T4", virusgames.VirusGames.class.getResource("resource/T4.png").toString());
        diccionario.put("T5", virusgames.VirusGames.class.getResource("resource/T5.png").toString());
        diccionario.put("C1", virusgames.VirusGames.class.getResource("resource/C1.png").toString());
        diccionario.put("C2", virusgames.VirusGames.class.getResource("resource/C2.png").toString());
        diccionario.put("C3", virusgames.VirusGames.class.getResource("resource/C3.png").toString());
    }
}
