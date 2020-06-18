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
    private ArrayList<Carta> cartaComodin;
    public Map<Integer, ArrayList<Carta>> juegoPropio;
    public Map<Integer, Boolean> condicionColor;
    private Boolean isYellowComplete = false;
    private Boolean isBlueComplete = false;
    private Boolean isGreenComplete = false;
    private Boolean isRedComplete = false;
    private Boolean isComodinComplete = false;
    public boolean isWinner = false;
    public int mesa = 0;
    
    public Jugador(ArrayList<Carta> mano, int mesa) 
    {
        this.mano = mano;
        this.mesa = mesa;
        cartaAmarilla = new ArrayList<>();
        cartaAzul = new ArrayList<>();
        cartaVerde = new ArrayList<>();
        cartaRoja = new ArrayList<>();
        cartaComodin = new ArrayList<>();
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
        juegoPropio.put(5, cartaComodin);
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

    public Boolean getIsComodinComplete() {
        return isComodinComplete;
    }

    public void setIsComodinComplete(Boolean isComodinComplete) {
        this.isComodinComplete = isComodinComplete;
    }
    
    private void prepararDiccionarioCC() {
        condicionColor = new HashMap<>();
        condicionColor.put(1, isYellowComplete);
        condicionColor.put(2, isBlueComplete);
        condicionColor.put(3, isRedComplete);
        condicionColor.put(4, isGreenComplete);
        condicionColor.put(5, isComodinComplete);
    }

    @Override
    public String toString() {
        return "Jugador{" + "juegoPropio=" + juegoPropio + '}';
    }

    public Map<Integer, Boolean> getCondicionColor() {
        return condicionColor;
    }

    public ArrayList<Carta> getCartaComodin() {
        return cartaComodin;
    }

    public void setCartaComodin(ArrayList<Carta> cartaComodin) {
        this.cartaComodin = cartaComodin;
    }

}
