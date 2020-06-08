/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virusgames.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import virusgames.mazo.Carta;

/**
 *
 * @author josue
 */
public class Jugador implements Serializable{
    
    private static final long serialVersionUID = -2723363051271966964L;
    
    private ArrayList<Carta> mano;
    private ArrayList<Carta> cartaAzul;
    private ArrayList<Carta> cartaAmarilla;
    private ArrayList<Carta> cartaVerde;
    private ArrayList<Carta> cartaRoja;
    public Map<Integer, ArrayList<Carta>> juegoPropio;
    
    public int mesa = 0;
    
    public Jugador(ArrayList<Carta> mano, int mesa) 
    {
        this.mano = mano;
        this.mesa = mesa;
        cartaAzul = new ArrayList<>();
        cartaAmarilla = new ArrayList<>();
        cartaVerde = new ArrayList<>();
        cartaRoja = new ArrayList<>();
        preparaDiccionarioJP();
    }

    public Map<Integer, ArrayList<Carta>> getJuegoPropio() {
        return juegoPropio;
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    public void setMano(ArrayList<Carta> mano) {
        this.mano = mano;
    }

    public ArrayList<Carta> getCartaAzul() {
        return cartaAzul;
    }

    public void setCartaAzul(ArrayList<Carta> cartaAzul) {
        this.cartaAzul = cartaAzul;
    }

    public ArrayList<Carta> getCartaAmarilla() {
        return cartaAmarilla;
    }

    public void setCartaAmarilla(ArrayList<Carta> cartaAmarilla) {
        this.cartaAmarilla = cartaAmarilla;
    }

    public ArrayList<Carta> getCartaVerde() {
        return cartaVerde;
    }

    public void setCartaVerde(ArrayList<Carta> cartaVerde) {
        this.cartaVerde = cartaVerde;
    }

    public ArrayList<Carta> getCartaRoja() {
        return cartaRoja;
    }

    public void setCartaRoja(ArrayList<Carta> cartaRoja) {
        this.cartaRoja = cartaRoja;
    }
    
    public void preparaDiccionarioJP()
    {
        juegoPropio = new HashMap<>();
        juegoPropio.put(1, cartaAmarilla);
        juegoPropio.put(2, cartaAzul);
        juegoPropio.put(3, cartaRoja);
        juegoPropio.put(4, cartaVerde);
    }
}
