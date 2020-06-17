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
import javafx.application.Platform;
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
import servidor.Jugador;
import servidor.LogicalGame;
import servidor.mazo.Carta;
import servidor.mazo.Comodin;
import servidor.mazo.Medicina;
import servidor.mazo.Organo;
import servidor.mazo.PersonalStackPane;
import servidor.mazo.Tratamiento;
import servidor.mazo.Virus;
import virusgames.serviceconexion.Cliente;
import virusgames.util.AppContext;

/**
 * FXML Controller class
 *
 * @author josue
 */
public class JuegoViewFXMLController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    private VBox vbMesa3;
    @FXML
    private VBox vbMesa2;
    private VBox vbMesa4;
    private VBox vbMesa5;
    @FXML
    private VBox vbMesa1;
    private VBox vbMesa6;
    @FXML
    private ImageView ivMazo;
    @FXML
    private Button btnDrawCard;
    private Text txtPlayer3;
    @FXML
    private Text txtPlayer2;
    private Text txtPlayer4;
    @FXML
    private Text txtPlayer1;
    private Text txtPlayer5;
    private Text txtPlayer6;

    /*Variables Propias*/
    public ArrayList<VBox> mesasDisponibles = new ArrayList<>();
    public static Cliente cliente;
    public LogicalGame logical;
    public Map<String, String> diccionario;
    public static SimpleIntegerProperty turnoActual = new SimpleIntegerProperty(0);
    public int jugadorTurno;
    public SimpleIntegerProperty cantidadCartas = new SimpleIntegerProperty(0);
    
    /*Variables Relacionada Con La Jugabilidad*/
    public int cantidadJugador;
    public Carta cartaSeleccionada = null;
    public ImageView cartaSeleccionadaIV = null;
    public ArrayList<Carta> cartasSelecionada = new ArrayList<>();
    public ArrayList<ImageView> cartasSelecionadaIV = new ArrayList<>();
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
        cliente = (Cliente)AppContext.getInstance().get("cliente");
//        turnoActual.setValue(1);
//        jugadorTurno = cliente.getTurno();
//        generarDiccionario();
//        cargarJuego();
//
//        asignacionMesasInterfaz(cantidadJugador);
        cargarJuego();
        btnDrawCard.setDisable(!true);
        cliente.nuevoJuego.addListener(t->{
            /* Refrescar/Actualizar la vista*/
            cargarJuegov2();
            Platform.runLater(()->{
                btnDrawCard.setText("HOLA: " + logical.version);
            }); 
        });
    }    

    @FXML
    private void changeCard(ActionEvent event) {
        
        cliente.pasarDeTurno(new LogicalGame(logical));
        
    }
    
    public void cargarJuegov2()
    {
        if(AppContext.getInstance().get("nuevoJuego") != null){
            logical = (LogicalGame) AppContext.getInstance().get("nuevoJuego");
            cantidadJugador = logical.cantidadJugadores;
        }
    }
    
    public void generarDiccionario() //Validado-Funcional
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
    public void cargarJuego()
    {
        if(AppContext.getInstance().get("juegoCargado") != null){
            logical = (LogicalGame) AppContext.getInstance().get("juegoCargado");
            cantidadJugador = logical.cantidadJugadores;
        }
    }
    public void tableroDinamico(int cantidad)
    {
        if(cantidad == 2)
        {
            mesasDisponibles.add(vbMesa1);
            txtPlayer1.setText(cliente.getUsersName().get(0));
            mesasDisponibles.add(vbMesa2);
            txtPlayer2.setText(cliente.getUsersName().get(1));
        }
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
        }
    }
    
    public void definirMovimientos(ImageView carta, Carta card)//Le asigna movilidad de acuerdo al tipo de carta ---Validado-Funcional
    {
       carta.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
                if(cantidadCartas.getValue() != 0)
                {
                    if(turnoActual.getValue() == card.jugador)
                    {
                        if(cartasSelecionada.isEmpty())
                        {
                            cartasSelecionada.add(card);
                            carta.setOpacity(0.5);
                            cartasSelecionadaIV.add(carta);
                        }
                        else
                        {
                            if(cartasSelecionada.contains(card))
                            {
                                cartasSelecionada.remove(card);
                                carta.setOpacity(1);
                                cartasSelecionadaIV.remove(carta);
                            }
                            else
                            {
                                if(cartasSelecionada.size() != cantidadCartas.getValue())
                                {
                                    cartasSelecionada.add(card);
                                    carta.setOpacity(0.5);
                                    cartasSelecionadaIV.add(carta);
                                }
                            }
                        }
                    }
                }
                else
                {
                    if(jugadorTurno == turnoActual.getValue() )
                    {
                        if (turnoActual.getValue() == card.jugador) 
                        {
                            if (cartaSeleccionada == null) {
                                cartaSeleccionada = card;
                                cartaSeleccionadaIV = carta;
                                cartaSeleccionadaIV.setOpacity(0.5);
                                eleccionTipoCarta();
                            } else {
                                if (cartaSeleccionada == card) {
                                    cartaSeleccionadaIV.setOpacity(1);
                                    cartaSeleccionadaIV = null;
                                    cartaSeleccionada = null;
                                } else {
                                    cartaSeleccionadaIV.setOpacity(1);
                                    cartaSeleccionadaIV = carta;
                                    cartaSeleccionadaIV.setOpacity(0.5);
                                    cartaSeleccionada = card;
                                    eleccionTipoCarta();
                                }
                            }
                        }

                    }
                }
       });
    }
    public void eleccionTipoCarta()//Validado-Funcional
    {
        switch(tipoCarta(cartaSeleccionada))
        {
            case 1: moverOrgano();
                break;
            case 2: moverVirus();
                break;
            case 3: moverMedicina();
                break;
            case 4: ejecutarTratamiento();
                break;
            case 5: moverComodin();
                break;
        }
    }
    public int tipoCarta(Carta carta) //Validado-Funcional
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
    public void moverOrgano() /*Funcional - Validado*/
    {
        EventHandler event = e->
        {
            if (turnoActual.getValue() == jugadorTurno) {
                if (!cartaSeleccionada.isPlayed) {
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
                        pasarDeTurno();
                    }
                } else {
                    System.out.println("ORGANO PARTE 3");
                    cartaSeleccionada = null;
                    cartaSeleccionadaIV.setOpacity(1);
                    mesaPropia.setOnMouseClicked(null);
                }
            }
        };
        mesaPropia.setOnMouseClicked(event);
    }
    private void moverVirus() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private void moverMedicina() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private void ejecutarTratamiento() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private void moverComodin() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                    cartasPropias.getChildren().add(ivCarta);
                    definirMovimientos(ivCarta, carta);
                }
            }
        }
    }
    public void pasarDeTurno()
    {
        logical.nuevoTurno();
        turnoActual.set(logical.turno);
        cliente.pasarDeTurno(logical);
    }

    @Override
    public void initialize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
