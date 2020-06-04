/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virusgames.mazo;

import java.io.Serializable;

/**
 *
 * @author jerso
 */
public abstract class Carta implements Serializable{

    private static final long serialVersionUID = -2723363051271966964L;
    public String imgCarta;
    public int colorCarta;
    public boolean isSelected = false;
    public int jugador;
    static public int turno = 1;
    static Carta carta = null;
    
    Carta(int numCarta){
//        addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
//            if(carta == null)
//            {
//                if(this.turno == this.jugador)
//                {
//                    carta = this;
//                    if(jugador == 1)
//                    {
//                        VBox aux = (VBox)contenedores.get(0);
//                        contenedorPropio = (HBox)aux.getChildren().get(0);
//                        for(int x = 1; x < 6; x++)
//                        {
//                            contenedoresEnemigos.add((HBox)contenedores.get(x).getChildren().get(0));
//                        }
//                    }
//                    contenedorPropio.addEventHandler(MouseEvent.MOUSE_CLICKED, c->{
//                        if(carta != null)
//                        {
//                            if(carta instanceof Organo)
//                            {
//                                if(contenedorPropio.getChildren().isEmpty())
//                                {
//                                    contenedorPropio.getChildren().add(carta);
//                                    carta = null;
//                                    contenedorPropio = null;
//                                }
//                                else
//                                {
//                                    if(!contenedorPropio.getChildren().contains(carta))
//                                    {
//                                        contenedorPropio.getChildren().add(carta);
//                                        carta = null;
//                                        contenedorPropio = null;
//                                    }
//                                }
//                            }
//                        }
//                    });
//                    if(carta instanceof  Virus)
//                    {
//                        System.out.println("VIRUS");
//                        for(int x = 0; x < 5; x++)
//                        {
//                            HBox aux = contenedoresEnemigos.get(x);
//                            aux.addEventHandler(MouseEvent.MOUSE_CLICKED, q->{
//                               if(!aux.getChildren().isEmpty())
//                               {
//                                   
//                               }
//                               aux.getChildren().add(carta);
//                               carta = null;
//                            });
//                        }
//                    }
////                    System.out.println("HOLA");
////                    HBox aux = (HBox) carta.getParent();
////                    VBox aux2 = (VBox) aux.getParent();
////                    
////                    /*Especifica para el Contenedor de organos del jugador Cliente (Personal)*/
////                    contenedorActual = (HBox) aux2.getChildren().get(0);
////                    contenedorActual.addEventHandler(MouseEvent.MOUSE_CLICKED, c->{
////                        if(carta != null){
////                            if(carta instanceof Organo)
////                            {
//////                                System.out.println("xdxd");
//////                                contenedorActual.getChildren().add(carta);
//////                                carta = null;
////                            }
////                        }
////                    });
//                }
//            }
//            else
//            {
//                /*Si es la misma, se deselecciona*/
//                if(carta==this)
//                {
//                    carta = null;
//                    System.out.println("NADA");
//                }
//                /*Aqui se hacen los metodos para validar las jugadas*/
//                else
//                {
//                    Carta aux = this;
//                    if(aux.jugador != carta.jugador)
//                    {
//                        this.validarJugada(carta);
//                    }
//                    else
//                    {
//                        carta = this;
//                        System.out.println("Soy la hostia");
//                    }
//                    
//                }
//            }
//        });
    }
    public abstract void validarJugada(Carta cartaSelec);
    
    public int getColor()
    {
        return colorCarta;
    }
}