/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virusgames.controller;

import java.io.Serializable;
import java.util.ArrayList;
import virusgames.mazo.Carta;

/**
 *
 * @author josue
 */
public class Jugador implements Serializable{
    
    private static final long serialVersionUID = -2723363051271966964L;
    public ArrayList<Carta> mano;
    public ArrayList<Carta> mesaPropia;
    public int mesa = 0;
    
    public Jugador(ArrayList<Carta> mano, int mesa) 
    {
        this.mano = mano;
        this.mesa = mesa;
        mesaPropia = new ArrayList<>();
    }
}
