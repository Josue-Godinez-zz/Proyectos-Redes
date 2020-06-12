/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virusgames.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import virusgames.mazo.Carta;
import virusgames.mazo.Comodin;
import virusgames.mazo.Medicina;
import virusgames.mazo.Organo;
import virusgames.mazo.Tratamiento;
import virusgames.mazo.Virus;

/**
 *
 * @author josue
 */
public final class LogicalGame implements Serializable{
    
    private static final long serialVersionUID = -2723363051271966964L;
    
    public ArrayList<Carta> mazo;
    public ArrayList<Jugador> players;
    public ArrayList<Carta> cartasDesechas;
    public int cantidadJugadores;
    public int turno = 1;
    public int turn ;
    public boolean isGameFinished = false;
    
    public LogicalGame(int cantidadJugadores)
    {
        this.cantidadJugadores = cantidadJugadores;
        generarMazo();
        asignarCartas(cantidadJugadores);
        cartasDesechas = new ArrayList<>();
        this.turn = turno;
    }
    
    public void generarMazo()
    {
        mazo =  new ArrayList<>();
        Organo organ;
        Virus virus;
        Medicina medicine;
        Tratamiento tratamiento;
        Comodin comodin;
        for(int x = 1; x <= 4; x++)
        {
            for(int q = 1; q <= 4; q++)
            {
                if(x == 1) //Cartas Amarillas
                {
                    organ = new Organo(x, "O1", 0);
                    virus = new Virus(x, "V1", 0);
                    medicine = new Medicina(x, "M1", 0);
                    mazo.add(organ);
                    mazo.add(virus);
                    mazo.add(medicine);
                }
                if(x == 2) //Cartas Azules
                {
                    organ = new Organo(x, "O2", 0);
                    virus = new Virus(x, "V2", 0);
                    medicine = new Medicina(x, "M2", 0);
                    mazo.add(organ);
                    mazo.add(virus);
                    mazo.add(medicine);
                }
                if(x == 3) //Cartas Rojos
                {
                    organ = new Organo(x, "O3", 0);
                    virus = new Virus(x, "V3", 0);
                    medicine = new Medicina(x, "M3", 0);
                    mazo.add(organ);
                    mazo.add(virus);
                    mazo.add(medicine);
                }
                if(x == 4) //Cartas Verde
                {
                    organ = new Organo(x, "O4", 0);
                    virus = new Virus(x, "V4", 0);
                    medicine = new Medicina(x, "M4", 0);
                    mazo.add(organ);
                    mazo.add(virus);
                    mazo.add(medicine);
                }
            }
        }
        for(int x = 1; x <=5; x++)
        {
            if(x == 1)
            {
                organ = new Organo(x, "O1", 0);
                mazo.add(organ);
            }
            if(x == 2)
            {
                organ = new Organo(x, "O2", 0);
                mazo.add(organ);
            }
            if(x == 3)
            {
                organ = new Organo(x, "O3", 0);
                mazo.add(organ);
            }
            if(x == 4)
            {
                organ = new Organo(x, "O4", 0);
                mazo.add(organ);
            }
            if(x == 5)
            {
                comodin = new Comodin(x, 1, "C1", 0);
                mazo.add(comodin);
                comodin = new Comodin(x, 2, "C2", 0);
                mazo.add(comodin);
                for(int t = 1; t < 5; t++)
                {
                    comodin = new Comodin(x, 3, "C3", 0);
                    mazo.add(comodin);
                }
            }
        }
        for(int x = 0; x < 2; x++)
        {
            tratamiento = new Tratamiento(0, 1, "T1", 0);
            mazo.add(tratamiento);
            tratamiento = new Tratamiento(0, 2, "T2", 0);
            mazo.add(tratamiento);
            tratamiento = new Tratamiento(0, 3, "T3", 0);
            mazo.add(tratamiento);
            tratamiento = new Tratamiento(0, 4, "T4", 0);
            mazo.add(tratamiento);
            tratamiento = new Tratamiento(0, 5, "T5", 0);
            mazo.add(tratamiento);
        }
        
        Collections.shuffle(mazo);
    }
    
    public void asignarCartas(int mesas)
    {
        players = new ArrayList<>();
        for(int x = 1; x <= mesas; x++)
        {
            ArrayList<Carta> mano = new ArrayList<>();
            for(int y = 0; y < 3; y++)//Prueba-Arreglar Despues
            {
                Carta carta = (Carta)mazo.get(0);
                carta.jugador = x;
                mano.add(carta);
                mazo.remove(0);
            }
//            Carta carta = new Organo(1, "O1", 1);
//            carta.jugador = x;
//            mano.add(carta);
//            mazo.remove(0);
//            carta = new Organo(2, "O2", 1);
//            carta.jugador = x;
//            mano.add(carta);
//            mazo.remove(0);
            Jugador player = new Jugador(mano, x);
            players.add(player);
        }
    }

    public ArrayList<Jugador> getPlayers() {
        return players;
    }
    
    public int nuevoTurno()
    {
        turno++;
        if(turno > cantidadJugadores)
        {
            turno = 1;
        }
        System.out.println("Nuevo Turno:" + turno);
        turn = turno;
        return turno;
    }

//    public static int getTurno() {
//        return turno;
//    }
//
//    public static void setTurno(int turno) {
//        LogicalGame.turno = turno;
//    }

    @Override
    public String toString() {
        return "LogicalGame{" +"players=" + players + ", cartasDesechas=" + cartasDesechas + ", cantidadJugadores=" + cantidadJugadores + ", turn=" + turn + ", isGameFinished=" + isGameFinished + '}';
    }
    
}

