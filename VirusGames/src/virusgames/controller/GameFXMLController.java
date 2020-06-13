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
import servidor.LogicalGame;
import servidor.mazo.Carta;
import servidor.mazo.Comodin;
import servidor.mazo.Medicina;
import servidor.mazo.Organo;
import servidor.mazo.PersonalStackPane;
import servidor.mazo.Tratamiento;
import servidor.mazo.Virus;
import servidor.Jugador;
import virusgames.serviceconexion.Cliente;
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
    public static Cliente cliente;
    public LogicalGame logical;
    public Map<String, String> diccionario;
    public static SimpleIntegerProperty turnoActual = new SimpleIntegerProperty(0);
    public int jugadorTurno = 0;
    
    /*Variables Relacionada Con La Jugabilidad*/
    public int cantidadJugador;
    public Thread actualizarJuegoView;
    public SimpleIntegerProperty cantidadCartas = new SimpleIntegerProperty(0);
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
        cantidadJugador=2;
        turnoActual.setValue(1);
        jugadorTurno = 1;
        logical = new LogicalGame(cantidadJugador);
        System.out.println(logical);
        jugadorTurno = cliente.getTurno();
        tableroDinamico(cantidadJugador);
        asignacionMesasInterfaz(cantidadJugador);
        generarDiccionario();
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(GameFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargarJuego();
        cargarLogical();
        
        asignacionMesasCodigo(cantidadJugador);
        ivMazo.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
            cantidadCartas.set(cantidadCartas.getValue()+1);
            if(cantidadCartas.getValue() == 1)
            {
                if(cartaSeleccionada != null)
                {
                    cartaSeleccionada = null;
                    cartaSeleccionadaIV.setOpacity(1);
                    cartaSeleccionadaIV = null;
                }
                btnDrawCard.setDisable(false);
                btnDrawCard.setText("DRAW <"+cantidadCartas.getValue()+">");
            }
            if(cantidadCartas.getValue() == 2)
            {
                btnDrawCard.setText("DRAW <"+cantidadCartas.getValue()+">");
            }
            if(cantidadCartas.getValue() == 3)
            {
                btnDrawCard.setText("DRAW <"+cantidadCartas.getValue()+">");
            }
            if(cantidadCartas.getValue() == 4)
            {
                btnDrawCard.setDisable(true);
                btnDrawCard.setText("DRAW");
                for(ImageView iv : cartasSelecionadaIV)
                {
                    iv.setOpacity(1);
                    cartasSelecionadaIV.remove(iv);
                }
                for(Carta carta : cartasSelecionada)
                {
                    cartasSelecionada.remove(carta);
                }
                cantidadCartas.setValue(0);
            }
        });
        
        cliente.nuevoJuego.addListener(t->{
            /* Refrescar/Actualizar la vista*/
            logical = (LogicalGame) AppContext.getInstance().get("nuevoJuego");
            Platform.runLater(()->{
                borrarInterfaz(logical.cantidadJugadores);
                cargarLogical();
            });
            
        });
        
//        actualizarJuegoView = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {    
//                    if (AppContext.getInstance().get("cond") != null) {
//                        
//                        boolean cond = (Boolean) AppContext.getInstance().get("cond");
//
////                    boolean cond = client.getPasarTurno();
//                        if (cond) {
//                            System.out.println("Requiere Cambios");
//                            cliente.setPasarTurno(false);
//                            logical = cliente.getJuego();
//                            AppContext.getInstance().set("cond", false);
//                            turnoActual.setValue(logical.turn);
//                        }
//                    }
//                }
//            }
//        });
//        actualizarJuegoView.start();
    }    

    @Override
    public void initialize() {
    }
    
    @FXML
    private void changeCard(ActionEvent event) {
        if(cartasSelecionada.size() == cantidadCartas.getValue())
        {
            for(int x = 0; x<cartasSelecionada.size() ;x++)
            {
                logical.cartasDesechas.add(cartasSelecionada.get(x));
                jugadorPropio.getMano().remove(cartasSelecionada.get(x));
                cartasPropias.getChildren().remove(cartasSelecionadaIV.get(x));
            }
            tomarCarta(cartasSelecionada.size());
            cartasSelecionadaIV.clear();
            cartasSelecionada.clear();
            cantidadCartas.set(0);
            btnDrawCard.setText("DRAW");
            btnDrawCard.setDisable(true);
            pasarDeTurno();
        }
        else if(cartasSelecionada.size() < cantidadCartas.getValue())
        {
            System.out.println("Necesitas seleccionar mas cartas");
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
            txtPlayer3.setText(cliente.getUsersName().get(0));
            mesasDisponibles.add(vbMesa5);
            txtPlayer5.setText(cliente.getUsersName().get(1));
            mesasDisponibles.add(vbMesa4);
            txtPlayer4.setText(cliente.getUsersName().get(2));
            mesasDisponibles.add(vbMesa6);
            txtPlayer6.setText(cliente.getUsersName().get(3));
            vbMesa2.setVisible(false);
            txtPlayer2.setVisible(false);
            vbMesa1.setVisible(false);
            txtPlayer1.setVisible(false);
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
            txtPlayer1.setVisible(false);
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
    
    public void cargarJuego()
    {
        if(AppContext.getInstance().get("juegoCargado") != null){
            logical = (LogicalGame) AppContext.getInstance().get("juegoCargado");
        }
    }
    
    public void cargarLogical() //Carga la partida, situa las carta en el campo correspondiente
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
                    definirMovimientos(carta, manoJugador.get(y));
                    aux.getChildren().add(carta);
                }
                HBox aux2 = (HBox)mesasDisponibles.get(x).getChildren().get(0);
                servidor.Jugador jugador = logical.players.get(x);
                for(int y = 1; y <= 4; y++)
                {
                    ArrayList pilaColor = jugador.getJuegoPropio().get(y);
                    if(!pilaColor.isEmpty())
                    {
                        PersonalStackPane sp = new PersonalStackPane(y);
                        for(int z = 0; z<pilaColor.size(); z++)
                        {
                            ImageView img = new ImageView();
                            img.setFitHeight(85);
                            img.setFitWidth(62);
                            img.setPreserveRatio(true);
                            img.setSmooth(true);
                            Carta carta = (Carta) pilaColor.get(z);
                            if(z == 0)
                            {
                                img.setImage(new Image(diccionario.get(carta.imgCarta)));
                            }
                            else
                            {
                                img.setImage(new Image(diccionario.get(carta.imgCarta)));
                                img.setRotate(13);
                            }
                            sp.getChildren().add(img);
                        }
                        aux2.getChildren().add(sp);
                    }
                }
            }
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
    public void moverOrgano() /*Funcional - Validado*/
    {
        EventHandler event = e->
        {
            System.out.println("HOLA");
            if (turnoActual.getValue() == jugadorTurno) {
                if (!cartaSeleccionada.isPlayed) {
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
    
    public void moverVirus()
    {
        EventHandler event = e->
        {
            if(jugadorTurno == turnoActual.getValue())
            {
                if(!cartaSeleccionada.isPlayed)
                {
                    int color = cartaSeleccionada.getColor();
                    for(int x = 0; x < jugadoresEnemigos.size(); x++)
                    {
                        Boolean condColor = jugadoresEnemigos.get(x).condicionColor.get(color);
                        if(!condColor)
                        {
                            ArrayList<Carta> pilaColor = jugadoresEnemigos.get(x).getJuegoPropio().get(color);
                            if(!pilaColor.isEmpty())
                            {                                
                                if(pilaColor.size() == 1)
                                {
                                    pilaColor.add(cartaSeleccionada);
                                    for(int  y = 0; y < mesaEnemigas.get(x).getChildren().size(); y++)
                                    {
                                        PersonalStackPane psp = (PersonalStackPane) mesaEnemigas.get(x).getChildren().get(y);
                                        if(psp.colorCarta == color)
                                        {
                                            cartaSeleccionadaIV.setRotate(12);
                                            cartaSeleccionadaIV.setOpacity(1);
                                            psp.getChildren().add(cartaSeleccionadaIV);
                                            jugadorPropio.getMano().remove(cartaSeleccionada);
                                            mesaPropia.getChildren().remove(cartaSeleccionadaIV);
                                            cartaSeleccionada.isPlayed = true;
                                            tomarCarta(1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else
                {
                    cartaSeleccionada = null;
                    cartaSeleccionadaIV.setOpacity(1);
                    mesaPropia.setOnMouseClicked(null);
                }
            }
        };
        
        for(HBox me : mesaEnemigas)
        {
            me.setOnMouseClicked(event);
        }
    }

    public void moverMedicina() 
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
                    cartasPropias.getChildren().add(ivCarta);
                    definirMovimientos(ivCarta, carta);
                }
            }
        }
        else if(logical.cantidadJugadores == 3)
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
            else if(jugadorTurno == 3)
            {
                for(int c = 0; c < cantidad; c++)
                {
                    carta = logical.mazo.get(0);
                    carta.jugador = 3;
                    logical.players.get(2).getMano().add(carta);
                    logical.mazo.remove(0);
                    ivCarta = new ImageView(new Image(diccionario.get(carta.imgCarta)));
                    ivCarta.setFitHeight(85);
                    ivCarta.setFitWidth(62);
                    cartasPropias.getChildren().add(ivCarta);
                    definirMovimientos(ivCarta, carta);
                }
            }
        }
        else if(logical.cantidadJugadores == 4)
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
            else if(jugadorTurno == 3)
            {
                for(int c = 0; c < cantidad; c++)
                {
                    carta = logical.mazo.get(0);
                    carta.jugador = 3;
                    logical.players.get(2).getMano().add(carta);
                    logical.mazo.remove(0);
                    ivCarta = new ImageView(new Image(diccionario.get(carta.imgCarta)));
                    ivCarta.setFitHeight(85);
                    ivCarta.setFitWidth(62);
                    cartasPropias.getChildren().add(ivCarta);
                    definirMovimientos(ivCarta, carta);
                }
            }
            else if(jugadorTurno == 4)
            {
                for(int c = 0; c < cantidad; c++)
                {
                    carta = logical.mazo.get(0);
                    carta.jugador = 4;
                    logical.players.get(3).getMano().add(carta);
                    logical.mazo.remove(0);
                    ivCarta = new ImageView(new Image(diccionario.get(carta.imgCarta)));
                    ivCarta.setFitHeight(85);
                    ivCarta.setFitWidth(62);
                    cartasPropias.getChildren().add(ivCarta);
                    definirMovimientos(ivCarta, carta);
                }
            }
        }
        else if(logical.cantidadJugadores == 5)
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
            else if(jugadorTurno == 3)
            {
                for(int c = 0; c < cantidad; c++)
                {
                    carta = logical.mazo.get(0);
                    carta.jugador = 3;
                    logical.players.get(2).getMano().add(carta);
                    logical.mazo.remove(0);
                    ivCarta = new ImageView(new Image(diccionario.get(carta.imgCarta)));
                    ivCarta.setFitHeight(85);
                    ivCarta.setFitWidth(62);
                    cartasPropias.getChildren().add(ivCarta);
                    definirMovimientos(ivCarta, carta);
                }
            }
            else if(jugadorTurno == 4)
            {
                for(int c = 0; c < cantidad; c++)
                {
                    carta = logical.mazo.get(0);
                    carta.jugador = 4;
                    logical.players.get(3).getMano().add(carta);
                    logical.mazo.remove(0);
                    ivCarta = new ImageView(new Image(diccionario.get(carta.imgCarta)));
                    ivCarta.setFitHeight(85);
                    ivCarta.setFitWidth(62);
                    cartasPropias.getChildren().add(ivCarta);
                    definirMovimientos(ivCarta, carta);
                }
            }
            else if(jugadorTurno == 5)
            {
                for(int c = 0; c < cantidad; c++)
                {
                    carta = logical.mazo.get(0);
                    carta.jugador = 5;
                    logical.players.get(4).getMano().add(carta);
                    logical.mazo.remove(0);
                    ivCarta = new ImageView(new Image(diccionario.get(carta.imgCarta)));
                    ivCarta.setFitHeight(85);
                    ivCarta.setFitWidth(62);
                    cartasPropias.getChildren().add(ivCarta);
                    definirMovimientos(ivCarta, carta);
                }
            }
        }
        else if(logical.cantidadJugadores == 6)
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
            else if(jugadorTurno == 3)
            {
                for(int c = 0; c < cantidad; c++)
                {
                    carta = logical.mazo.get(0);
                    carta.jugador = 3;
                    logical.players.get(2).getMano().add(carta);
                    logical.mazo.remove(0);
                    ivCarta = new ImageView(new Image(diccionario.get(carta.imgCarta)));
                    ivCarta.setFitHeight(85);
                    ivCarta.setFitWidth(62);
                    cartasPropias.getChildren().add(ivCarta);
                    definirMovimientos(ivCarta, carta);
                }
            }
            else if(jugadorTurno == 4)
            {
                for(int c = 0; c < cantidad; c++)
                {
                    carta = logical.mazo.get(0);
                    carta.jugador = 4;
                    logical.players.get(3).getMano().add(carta);
                    logical.mazo.remove(0);
                    ivCarta = new ImageView(new Image(diccionario.get(carta.imgCarta)));
                    ivCarta.setFitHeight(85);
                    ivCarta.setFitWidth(62);
                    cartasPropias.getChildren().add(ivCarta);
                    definirMovimientos(ivCarta, carta);
                }
            }
            else if(jugadorTurno == 5)
            {
                for(int c = 0; c < cantidad; c++)
                {
                    carta = logical.mazo.get(0);
                    carta.jugador = 5;
                    logical.players.get(4).getMano().add(carta);
                    logical.mazo.remove(0);
                    ivCarta = new ImageView(new Image(diccionario.get(carta.imgCarta)));
                    ivCarta.setFitHeight(85);
                    ivCarta.setFitWidth(62);
                    cartasPropias.getChildren().add(ivCarta);
                    definirMovimientos(ivCarta, carta);
                }
            }
            else if(jugadorTurno == 6)
            {
                for(int c = 0; c < cantidad; c++)
                {
                    carta = logical.mazo.get(0);
                    carta.jugador = 6;
                    logical.players.get(5).getMano().add(carta);
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
 
    public void borrarInterfaz(int cantidadJugadores)
    {
        HBox hbAux;
        if(cantidadJugadores == 2)
        {
            hbAux = (HBox) vbMesa1.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa1.getChildren().get(1);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa2.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa2.getChildren().get(1);
            hbAux.getChildren().clear();
        }
        else if(cantidadJugadores == 3)
        {
            hbAux = (HBox) vbMesa1.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa1.getChildren().get(1);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa3.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa3.getChildren().get(1);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa4.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa4.getChildren().get(1);
            hbAux.getChildren().clear();
        }
        else if(cantidadJugadores == 4)
        {
            hbAux = (HBox) vbMesa5.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa5.getChildren().get(1);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa3.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa3.getChildren().get(1);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa4.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa4.getChildren().get(1);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa6.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa6.getChildren().get(1);
            hbAux.getChildren().clear();
        }
        else if(cantidadJugadores == 5)
        {
            hbAux = (HBox) vbMesa5.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa5.getChildren().get(1);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa3.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa3.getChildren().get(1);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa2.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa2.getChildren().get(1);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa4.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa4.getChildren().get(1);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa6.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa6.getChildren().get(1);
            hbAux.getChildren().clear();
        }
        else if(cantidadJugadores == 6)
        {
            hbAux = (HBox) vbMesa1.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa1.getChildren().get(1);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa2.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa2.getChildren().get(1);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa3.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa3.getChildren().get(1);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa4.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa4.getChildren().get(1);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa5.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa5.getChildren().get(1);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa6.getChildren().get(0);
            hbAux.getChildren().clear();
            hbAux = (HBox) vbMesa6.getChildren().get(1);
            hbAux.getChildren().clear();
        }
    }

    public void ejecutarTratamiento() {
        System.out.println("Tratamiento");
    }

    public void moverComodin() {
        System.out.println("Comodin");
    }
    
    public void pasarDeTurno()
    {
        logical.nuevoTurno();
        jugadorTurno++;
        turnoActual.set(jugadorTurno);
        if(jugadorTurno > cantidadJugador)
        {
            jugadorTurno =1;
            turnoActual.set(jugadorTurno);
        }
        System.out.println(logical);
//        System.out.println("Turno Actual: " +logical.turno);
//        System.out.println(logical.mazo.size());
//        System.out.println(logical.getPlayers().get(0).getJuegoPropio());
//        System.out.println(logical.getPlayers().get(1).getJuegoPropio());
//        System.out.println(logical.cartasDesechas);
//        System.out.println(logical);
        cliente.pasarDeTurno(logical);
    }
}