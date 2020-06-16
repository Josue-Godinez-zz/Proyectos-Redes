/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import servidor.mazo.Carta;

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
    public Map<Integer, Boolean> condicionColor;
    private boolean isYellowComplete = false;
    private boolean isBlueComplete = false;
    private boolean isGreenComplete = false;
    private boolean isRedComplete = false;
    public boolean isWinner = false;
    public int mesa = 0;
    
    public Jugador(ArrayList<Carta> mano, int mesa) 
    {
        this.mano = mano;
        this.mesa = mesa;
        cartaAmarilla = new ArrayList<>();
//        cartaAmarilla.add(new Organo(1, "O1", 0));
//        cartaAmarilla.add(new Virus(1, "V1", 0));
        cartaAzul = new ArrayList<>();
//        cartaAzul.add(new Organo(2, "O2", 0));
//        cartaAzul.add(new Medicina(2, "M2", 0));
          cartaVerde = new ArrayList<>();
//        cartaVerde.add(new Organo(4, "O4", 0));
//        cartaVerde.add(new Virus(4, "V4",0));
        cartaRoja = new ArrayList<>();
//        cartaRoja.add(new Organo(3, "O3", 0));
//        cartaRoja.add(new Medicina(3, "M3", 0));
//        cartaRoja.add(new Virus(3, "V3", 0));
        preparaDiccionarioJP();
        prepararDiccionarioCC();
        
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

    public boolean isIsYellowComplete() {
        return isYellowComplete;
    }

    public void setIsYellowComplete(boolean isYellowComplete) {
        this.isYellowComplete = isYellowComplete;
    }

    public boolean isIsBlueComplete() {
        return isBlueComplete;
    }

    public void setIsBlueComplete(boolean isBlueComplete) {
        this.isBlueComplete = isBlueComplete;
    }

    public boolean isIsGreenComplete() {
        return isGreenComplete;
    }

    public void setIsGreenComplete(boolean isGreenComplete) {
        this.isGreenComplete = isGreenComplete;
    }

    public boolean isIsRedComplete() {
        return isRedComplete;
    }

    public void setIsRedComplete(boolean isRedComplete) {
        this.isRedComplete = isRedComplete;
    }

    private void prepararDiccionarioCC() {
        condicionColor = new HashMap<>();
        condicionColor.put(1, isYellowComplete);
        condicionColor.put(2, isBlueComplete);
        condicionColor.put(3, isRedComplete);
        condicionColor.put(4, isGreenComplete);
    }

    @Override
    public String toString() {
        return "Jugador{" + "juegoPropio=" + juegoPropio + '}';
    }
    
    
}
