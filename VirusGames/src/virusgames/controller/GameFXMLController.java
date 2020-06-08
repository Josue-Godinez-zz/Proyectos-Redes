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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import virusgames.mazo.Carta;
import virusgames.mazo.Comodin;
import virusgames.mazo.Medicina;
import virusgames.mazo.Organo;
import virusgames.mazo.PersonalStackPane;
import virusgames.mazo.Tratamiento;
import virusgames.mazo.Virus;
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
    @FXML
    private Text txtPlayer3;
    @FXML
    private Text txtPlayer2;
    @FXML
    private Text txtPlayer4;
    @FXML
    private Text txtPlayer1;
    @FXML
    private Text txtPlayer5;
    @FXML
    private Text txtPlayer6;
    
    /*Variables Propias*/
    public ArrayList<VBox> mesasDisponibles = new ArrayList<>();
    public Servidor servidor;
    public Cliente cliente;
    public LogicalGame logical;
    public Map<String, String> diccionario;
    public SimpleIntegerProperty turnoActual = new SimpleIntegerProperty(0);
    public int jugadorTurno = 1;
    
    /*Variables Relacionada Con La Jugabilidad*/
    public int cantidadCartas = 0;
    public Carta cartaSeleccionada = null;
    public ImageView cartaSeleccionadaIV = null;
    public ArrayList<Carta> cartasSelecionada = new ArrayList<>();
    public HBox mesaPropia;
    public HBox cartasPropias;
    public ArrayList<HBox> mesaEnemigas;
    public Jugador jugadorPropio;
    public ArrayList<Jugador> jugadoresEnemigos;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        servidor = (Servidor)AppContext.getInstance().get("servidor");
        cliente = (Cliente)AppContext.getInstance().get("cliente");
        turnoActual.set(1);
        
        turnoActual.addListener(t->{
            /* Refrescar/Actualizar la vista*/

        });
        
        tableroDinamico(cliente.getCantidadJugadores());
        asignacionMesasInterfaz(cliente.getCantidadJugadores());
        generarDiccionario();
        jugadorTurno = cliente.getTurno();
        if(cliente.isHost)
        {
            generarJuego();
            cargarLogical();
        }
        else
        {
            try {
                Thread.sleep(250);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
            cargarJuego();
            cargarLogical();
            
        }
        
        asignacionMesasCodigo(cliente.getCantidadJugadores());
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
        System.out.println("Quiere Cambiar " +cantidadCartas+ " cartas");
    }
    
    public void tableroDinamico(int cantidad)
    {
        if(cantidad == 2)
        {
            mesasDisponibles.add(vbMesa1);
            txtPlayer1.setText(cliente.getUsersName().get(0));
            mesasDisponibles.add(vbMesa2);
            txtPlayer2.setText(cliente.getUsersName().get(1));
            vbMesa3.setVisible(false);
            txtPlayer3.setVisible(false);
            vbMesa5.setVisible(false);
            txtPlayer5.setVisible(false);
            vbMesa4.setVisible(false);
            txtPlayer4.setVisible(false);
            vbMesa6.setVisible(false);
            txtPlayer6.setVisible(false);
        }
        if(cantidad == 3)
        {
            mesasDisponibles.add(vbMesa1);
            txtPlayer1.setText(cliente.getUsersName().get(0));
            mesasDisponibles.add(vbMesa3);
            txtPlayer3.setText(cliente.getUsersName().get(1));
            mesasDisponibles.add(vbMesa4);
            txtPlayer4.setText(cliente.getUsersName().get(2));
            vbMesa5.setVisible(false);
            txtPlayer5.setVisible(false);
            vbMesa2.setVisible(false);
            txtPlayer2.setVisible(false);
            vbMesa6.setVisible(false);
            txtPlayer6.setVisible(false);
        }
        if(cantidad == 4)
        {
            mesasDisponibles.add(vbMesa3);
            txtPlayer4.setText(cliente.getUsersName().get(0));
            mesasDisponibles.add(vbMesa5);
            txtPlayer5.setText(cliente.getUsersName().get(1));
            mesasDisponibles.add(vbMesa4);
            txtPlayer4.setText(cliente.getUsersName().get(2));
            mesasDisponibles.add(vbMesa6);
            txtPlayer6.setText(cliente.getUsersName().get(3));
            vbMesa2.setVisible(false);
            vbMesa1.setVisible(false);
        }
        if(cantidad == 5)
        {
            mesasDisponibles.add(vbMesa2);
            txtPlayer2.setText(cliente.getUsersName().get(0));
            mesasDisponibles.add(vbMesa3);
            txtPlayer3.setText(cliente.getUsersName().get(1));
            mesasDisponibles.add(vbMesa4);
            txtPlayer4.setText(cliente.getUsersName().get(2));
            mesasDisponibles.add(vbMesa5);
            txtPlayer5.setText(cliente.getUsersName().get(3));
            mesasDisponibles.add(vbMesa6);
            txtPlayer6.setText(cliente.getUsersName().get(4));
            vbMesa1.setVisible(false);
        }
        if(cantidad == 6) 
        {
          mesasDisponibles.add(vbMesa1);
          txtPlayer1.setText(cliente.getUsersName().get(0));
          mesasDisponibles.add(vbMesa2);
          txtPlayer2.setText(cliente.getUsersName().get(1));
          mesasDisponibles.add(vbMesa3);
          txtPlayer3.setText(cliente.getUsersName().get(2));
          mesasDisponibles.add(vbMesa4);
          txtPlayer4.setText(cliente.getUsersName().get(3));
          mesasDisponibles.add(vbMesa5);
          txtPlayer5.setText(cliente.getUsersName().get(4));
          mesasDisponibles.add(vbMesa6);
          txtPlayer6.setText(cliente.getUsersName().get(5));
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
                ArrayList<Carta> manoJugador = logical.players.get(x).getMano();
                for(int y = 0 ; y < 3; y++)
                {
                    String img = (String) manoJugador.get(y).imgCarta;
                    ImageView carta = new ImageView(new Image(diccionario.get(img)));
                    carta.setFitHeight(85);
                    carta.setFitWidth(62);
                    carta.setPreserveRatio(true);
                    carta.setSmooth(true);
                    if(turnoActual.getValue() ==  jugadorTurno)
                    {
                        definirMovimientos(carta, manoJugador.get(y));
                    }
                    aux.getChildren().add(carta);
                }
            }
        }
    }
    
    public void definirMovimientos(ImageView carta, Carta card)
    {
       carta.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
           if(cantidadCartas != 0)
           {
                System.out.println("saco carta");
           }
           else
           {
               if(card.jugador == turnoActual.getValue())
               {
                   if(cartaSeleccionada == null)
                   {
                        cartaSeleccionada = card;
                        cartaSeleccionadaIV = carta;
                        cartaSeleccionadaIV.setOpacity(0.5);
                        eleccionTipoCarta();
                   }
                   else
                   {
                       if(cartaSeleccionada == card)
                       {
                           cartaSeleccionadaIV.setOpacity(1);
                           cartaSeleccionadaIV = null;
                           cartaSeleccionada = null;
                       }
                       else
                       {
                           cartaSeleccionadaIV.setOpacity(1);
                           cartaSeleccionadaIV = carta;
                           cartaSeleccionadaIV.setOpacity(0.5);
                           cartaSeleccionada = card;
                           eleccionTipoCarta();
                       }
                   }  
               }
           }
           
           
       });
    }

    public int tipoCarta(Carta carta)
    {
        if (carta instanceof Organo)
        {
            return 1;
        }
        if(carta instanceof Virus)
        {
            return 2;
        }
        if(carta instanceof Medicina)
        {
            return 3;
        }
        if(carta instanceof Tratamiento)
        {
            return 4;
        }
        if(carta instanceof Comodin)
        {
            return 5;
        }
        else
        {
            return 0;
        }
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
    
    public void eleccionTipoCarta()
    {
        switch(tipoCarta(cartaSeleccionada))
        {
            case 1: moverOrgano();
                break;
            case 2: moverVirus();
                break;
            case 3: moverMedicina();
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }
    public void moverOrgano()
    {
        System.out.println("ORGANO PARTE 1");
        EventHandler event = e->
        {
            if(!cartaSeleccionada.isPlayed) {
                System.out.println("ORGANO PARTE 2.1");
                int color = cartaSeleccionada.colorCarta;
                ArrayList<Carta> pilaColor = jugadorPropio.getJuegoPropio().get(color);
                if (pilaColor.isEmpty()) {
                    PersonalStackPane sp = new PersonalStackPane(color);
                    cartaSeleccionada.isPlayed = true;
                    pilaColor.add(cartaSeleccionada);
                    logical.players.get(jugadorTurno - 1).getMano().remove(cartaSeleccionada);
                    cartaSeleccionadaIV.setOpacity(1);
                    HBox aux = (HBox) cartaSeleccionadaIV.getParent();
                    aux.getChildren().remove(cartaSeleccionadaIV);
                    sp.getChildren().add(cartaSeleccionadaIV);
                    mesaPropia.getChildren().add(sp);
                    mesaPropia.setOnMouseClicked(null);
                    tomarCarta(1);
                }
                else
                {
                    System.out.println("ORGANO PARTE 2.2");
                    System.out.println("Contiene un organo de este mismo color");
                }
            }
            else
            {
                System.out.println("ORGANO PARTE 3");
                cartaSeleccionada = null;
                cartaSeleccionadaIV.setOpacity(1);
                mesaPropia.setOnMouseClicked(null);
            }
        };
        //mesaPropia.addEventFilter(MouseEvent.MOUSE_CLICKED, event);
        mesaPropia.setOnMouseClicked(event);
        
    }
    
    public void moverVirus()
    {
        System.out.println("Virus");
        
    }

    private void moverMedicina() 
    {
        System.out.println("Medicina");
    }
    
    public void asignacionMesasInterfaz(int cantidad)
    {
        mesaEnemigas = new ArrayList<>();
        switch (cantidad) {
            case 2:
                if(jugadorTurno == 1)
                {
                    mesaPropia = (HBox) vbMesa1.getChildren().get(0);
                    cartasPropias = (HBox) vbMesa1.getChildren().get(1);
                    mesaEnemigas.add((HBox) vbMesa2.getChildren().get(0));
                }
                else if(jugadorTurno == 2)
                {
                    mesaPropia = (HBox) vbMesa2.getChildren().get(0);
                    cartasPropias = (HBox) vbMesa2.getChildren().get(1);
                    mesaEnemigas.add((HBox) vbMesa1.getChildren().get(0));
                }   break;
            case 3:
                switch (jugadorTurno) {
                    case 1:
                        mesaPropia = (HBox) vbMesa1.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa1.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa3.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa4.getChildren().get(0));
                        break;
                    case 2:
                        mesaPropia = (HBox) vbMesa3.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa3.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa1.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa4.getChildren().get(0));
                        break;
                    case 3:
                        mesaPropia = (HBox) vbMesa4.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa4.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa1.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa3.getChildren().get(0));
                        break;
                }   break;
            case 4:
                switch (jugadorTurno) {
                    case 1:
                        mesaPropia = (HBox) vbMesa5.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa5.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa3.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa4.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa6.getChildren().get(0));
                        break;
                    case 2:
                        mesaPropia = (HBox) vbMesa3.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa3.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa5.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa4.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa6.getChildren().get(0));
                        break;
                    case 3:
                        mesaPropia = (HBox) vbMesa4.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa4.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa5.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa3.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa6.getChildren().get(0));
                        break;
                    case 4:
                        mesaPropia = (HBox) vbMesa6.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa6.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa5.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa3.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa4.getChildren().get(0));
                        break;
                }   break;
            case 5:
                switch (jugadorTurno) {
                    case 1:
                        mesaPropia = (HBox) vbMesa5.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa5.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa3.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa2.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa4.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa6.getChildren().get(0));
                        break;
                    case 2:
                        mesaPropia = (HBox) vbMesa3.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa3.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa5.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa2.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa4.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa6.getChildren().get(0));
                        break;
                    case 3:
                        mesaPropia = (HBox) vbMesa2.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa2.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa5.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa3.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa4.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa6.getChildren().get(0));
                        break;
                    case 4:
                        mesaPropia = (HBox) vbMesa4.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa4.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa5.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa3.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa2.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa6.getChildren().get(0));
                        break;
                    case 5:
                        mesaPropia = (HBox) vbMesa6.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa6.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa5.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa3.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa2.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa4.getChildren().get(0));
                        break;
                }   break;
            case 6:
                switch (jugadorTurno) {
                    case 1:
                        mesaPropia = (HBox) vbMesa1.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa1.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa2.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa3.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa4.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa5.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa6.getChildren().get(0));
                        break;
                    case 2:
                        mesaPropia = (HBox) vbMesa2.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa2.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa1.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa3.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa4.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa5.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa6.getChildren().get(0));
                        break;
                    case 3:
                        mesaPropia = (HBox) vbMesa3.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa3.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa1.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa2.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa4.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa5.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa6.getChildren().get(0));
                        break;
                    case 4:
                        mesaPropia = (HBox) vbMesa4.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa4.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa1.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa2.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa3.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa5.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa6.getChildren().get(0));
                        break;
                    case 5:
                        mesaPropia = (HBox) vbMesa5.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa5.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa1.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa2.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa3.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa4.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa6.getChildren().get(0));
                        break;
                    case 6:
                        mesaPropia = (HBox) vbMesa6.getChildren().get(0);
                        cartasPropias = (HBox) vbMesa6.getChildren().get(1);
                        mesaEnemigas.add((HBox) vbMesa1.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa2.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa3.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa4.getChildren().get(0));
                        mesaEnemigas.add((HBox) vbMesa5.getChildren().get(0));
                        break;
                }   break;
        }
    }
    
    public void tomarCarta(int cantidad)
    {
        Carta carta;
        ImageView ivCarta;
        
        if(logical.cantidadJugadores == 2) /*Terminar*/
        {
            if(jugadorTurno == 1)
            {
                for(int c = 0; c < cantidad; c++)
                {
                    carta = logical.mazo.get(0);
                    carta.jugador = 1;
                    logical.players.get(0).getMano().add(carta);
                    logical.mazo.remove(0);
                    ivCarta = new ImageView(new Image(diccionario.get(carta.imgCarta)));
                    ivCarta.setFitHeight(85);
                    ivCarta.setFitWidth(62);
                    cartasPropias.getChildren().add(ivCarta);
                    definirMovimientos(ivCarta, carta);
                }
            }
            else if(jugadorTurno == 2)
            {
                for(int c = 0; c < cantidad; c++)
                {
                    carta = logical.mazo.get(0);
                    carta.jugador = 2;
                    logical.players.get(1).getMano().add(carta);
                    logical.mazo.remove(0);
                    ivCarta = new ImageView(new Image(diccionario.get(carta.imgCarta)));
                    ivCarta.setFitHeight(85);
                    ivCarta.setFitWidth(62);
                    cartasPropias.getChildren().add(null);
                }
            }
        }
    }

    public void asignacionMesasCodigo(int cantidadJugadores) {
        jugadoresEnemigos = new ArrayList<>();
        if(cantidadJugadores == 2)
        {
            if(jugadorTurno == 1)
            {
                jugadorPropio = logical.getPlayers().get(0);
                jugadoresEnemigos.add(logical.getPlayers().get(1));
            }
            else if(jugadorTurno == 2)
            {
                jugadorPropio = logical.getPlayers().get(1);
                jugadoresEnemigos.add(logical.getPlayers().get(0));
            }
        }
        else if(cantidadJugadores == 3)
        {
            switch (jugadorTurno) {
                case 1:
                    jugadorPropio = logical.getPlayers().get(0);
                    jugadoresEnemigos.add(logical.getPlayers().get(1));
                    jugadoresEnemigos.add(logical.getPlayers().get(2));
                    break;
                case 2:
                    jugadorPropio = logical.getPlayers().get(1);
                    jugadoresEnemigos.add(logical.getPlayers().get(0));
                    jugadoresEnemigos.add(logical.getPlayers().get(2));
                    break;
                case 3:
                    jugadorPropio = logical.getPlayers().get(2);
                    jugadoresEnemigos.add(logical.getPlayers().get(1));
                    jugadoresEnemigos.add(logical.getPlayers().get(0));
                    break;
            }
        }
        else if(cantidadJugadores == 4)
        {
            switch (jugadorTurno) {
                case 1:
                    jugadorPropio = logical.getPlayers().get(0);
                    jugadoresEnemigos.add(logical.getPlayers().get(1));
                    jugadoresEnemigos.add(logical.getPlayers().get(2));
                    jugadoresEnemigos.add(logical.getPlayers().get(3));
                    break;
                case 2:
                    jugadorPropio = logical.getPlayers().get(1);
                    jugadoresEnemigos.add(logical.getPlayers().get(0));
                    jugadoresEnemigos.add(logical.getPlayers().get(2));
                    jugadoresEnemigos.add(logical.getPlayers().get(3));
                    break;
                case 3:
                    jugadorPropio = logical.getPlayers().get(2);
                    jugadoresEnemigos.add(logical.getPlayers().get(1));
                    jugadoresEnemigos.add(logical.getPlayers().get(0));
                    jugadoresEnemigos.add(logical.getPlayers().get(3));
                    break;
                case 4:
                    jugadorPropio = logical.getPlayers().get(3);
                    jugadoresEnemigos.add(logical.getPlayers().get(0));
                    jugadoresEnemigos.add(logical.getPlayers().get(1));
                    jugadoresEnemigos.add(logical.getPlayers().get(2));
                    break;
            }
        }
        else if(cantidadJugadores == 4)
        {
            switch (jugadorTurno) {
                case 1:
                    jugadorPropio = logical.getPlayers().get(0);
                    jugadoresEnemigos.add(logical.getPlayers().get(1));
                    jugadoresEnemigos.add(logical.getPlayers().get(2));
                    jugadoresEnemigos.add(logical.getPlayers().get(3));
                    jugadoresEnemigos.add(logical.getPlayers().get(4));
                    break;
                case 2:
                    jugadorPropio = logical.getPlayers().get(1);
                    jugadoresEnemigos.add(logical.getPlayers().get(0));
                    jugadoresEnemigos.add(logical.getPlayers().get(2));
                    jugadoresEnemigos.add(logical.getPlayers().get(3));
                    jugadoresEnemigos.add(logical.getPlayers().get(4));
                    break;
                case 3:
                    jugadorPropio = logical.getPlayers().get(2);
                    jugadoresEnemigos.add(logical.getPlayers().get(1));
                    jugadoresEnemigos.add(logical.getPlayers().get(0));
                    jugadoresEnemigos.add(logical.getPlayers().get(3));
                    jugadoresEnemigos.add(logical.getPlayers().get(4));
                    break;
                case 4:
                    jugadorPropio = logical.getPlayers().get(3);
                    jugadoresEnemigos.add(logical.getPlayers().get(0));
                    jugadoresEnemigos.add(logical.getPlayers().get(1));
                    jugadoresEnemigos.add(logical.getPlayers().get(2));
                    jugadoresEnemigos.add(logical.getPlayers().get(4));
                    break;
                case 5:
                    jugadorPropio = logical.getPlayers().get(4);
                    jugadoresEnemigos.add(logical.getPlayers().get(0));
                    jugadoresEnemigos.add(logical.getPlayers().get(1));
                    jugadoresEnemigos.add(logical.getPlayers().get(2));
                    jugadoresEnemigos.add(logical.getPlayers().get(3));
                    break;
            }
        }
        else if(jugadorTurno == 6)
        {
            switch (jugadorTurno) {
                case 1:
                    jugadorPropio = logical.getPlayers().get(0);
                    jugadoresEnemigos.add(logical.getPlayers().get(1));
                    jugadoresEnemigos.add(logical.getPlayers().get(2));
                    jugadoresEnemigos.add(logical.getPlayers().get(3));
                    jugadoresEnemigos.add(logical.getPlayers().get(4));
                    jugadoresEnemigos.add(logical.getPlayers().get(5));
                    break;
                case 2:
                    jugadorPropio = logical.getPlayers().get(1);
                    jugadoresEnemigos.add(logical.getPlayers().get(0));
                    jugadoresEnemigos.add(logical.getPlayers().get(2));
                    jugadoresEnemigos.add(logical.getPlayers().get(3));
                    jugadoresEnemigos.add(logical.getPlayers().get(4));
                    jugadoresEnemigos.add(logical.getPlayers().get(5));
                    break;
                case 3:
                    jugadorPropio = logical.getPlayers().get(2);
                    jugadoresEnemigos.add(logical.getPlayers().get(1));
                    jugadoresEnemigos.add(logical.getPlayers().get(0));
                    jugadoresEnemigos.add(logical.getPlayers().get(3));
                    jugadoresEnemigos.add(logical.getPlayers().get(4));
                    jugadoresEnemigos.add(logical.getPlayers().get(5));
                    break;
                case 4:
                    jugadorPropio = logical.getPlayers().get(3);
                    jugadoresEnemigos.add(logical.getPlayers().get(0));
                    jugadoresEnemigos.add(logical.getPlayers().get(1));
                    jugadoresEnemigos.add(logical.getPlayers().get(2));
                    jugadoresEnemigos.add(logical.getPlayers().get(4));
                    jugadoresEnemigos.add(logical.getPlayers().get(5));
                    break;
                case 5:
                    jugadorPropio = logical.getPlayers().get(4);
                    jugadoresEnemigos.add(logical.getPlayers().get(0));
                    jugadoresEnemigos.add(logical.getPlayers().get(1));
                    jugadoresEnemigos.add(logical.getPlayers().get(2));
                    jugadoresEnemigos.add(logical.getPlayers().get(3));
                    jugadoresEnemigos.add(logical.getPlayers().get(5));
                    break;
                case 6:
                    jugadorPropio = logical.getPlayers().get(5);
                    jugadoresEnemigos.add(logical.getPlayers().get(0));
                    jugadoresEnemigos.add(logical.getPlayers().get(1));
                    jugadoresEnemigos.add(logical.getPlayers().get(2));
                    jugadoresEnemigos.add(logical.getPlayers().get(3));
                    jugadoresEnemigos.add(logical.getPlayers().get(4));
                    break;
            }
        } 
    }
 
}
