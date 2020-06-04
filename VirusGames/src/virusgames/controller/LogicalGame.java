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
    public ArrayList<ArrayList<Carta>> mazoJugadores;
    public int cantidadJugadores;
    public int turno = 1;
    
    public LogicalGame()
    {
        
    }
    
    public LogicalGame(int cantidadJugadores)
    {
        this.cantidadJugadores = cantidadJugadores;
        generarMazo();
        asignarCartas(cantidadJugadores);
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
                    organ = new Organo(x, virusgames.VirusGames.class.getResource("resource/O1.png").toString(), 0);
                    virus = new Virus(x, virusgames.VirusGames.class.getResource("resource/V1.png").toString(), 0);
                    medicine = new Medicina(x, virusgames.VirusGames.class.getResource("resource/M1.png").toString(), 0);
                    mazo.add(organ);
                    mazo.add(virus);
                    mazo.add(medicine);
                }
                if(x == 2) //Cartas Azules
                {
                    organ = new Organo(x, virusgames.VirusGames.class.getResource("resource/O2.png").toString(), 0);
                    virus = new Virus(x, virusgames.VirusGames.class.getResource("resource/V2.png").toString(), 0);
                    medicine = new Medicina(x, virusgames.VirusGames.class.getResource("resource/M2.png").toString(), 0);
                    mazo.add(organ);
                    mazo.add(virus);
                    mazo.add(medicine);
                }
                if(x == 3) //Cartas Rojos
                {
                    organ = new Organo(x, virusgames.VirusGames.class.getResource("resource/O3.png").toString(), 0);
                    virus = new Virus(x, virusgames.VirusGames.class.getResource("resource/V3.png").toString(), 0);
                    medicine = new Medicina(x, virusgames.VirusGames.class.getResource("resource/M3.png").toString(), 0);
                    mazo.add(organ);
                    mazo.add(virus);
                    mazo.add(medicine);
                }
                if(x == 4) //Cartas Verde
                {
                    organ = new Organo(x, virusgames.VirusGames.class.getResource("resource/O4.png").toString(), 0);
                    virus = new Virus(x, virusgames.VirusGames.class.getResource("resource/V4.png").toString(), 0);
                    medicine = new Medicina(x, virusgames.VirusGames.class.getResource("resource/M4.png").toString(), 0);
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
                organ = new Organo(x, virusgames.VirusGames.class.getResource("resource/O1.png").toString(), 0);
                mazo.add(organ);
            }
            if(x == 2)
            {
                organ = new Organo(x, virusgames.VirusGames.class.getResource("resource/O2.png").toString(), 0);
                mazo.add(organ);
            }
            if(x == 3)
            {
                organ = new Organo(x,virusgames.VirusGames.class.getResource("resource/O3.png").toString(), 0);
                mazo.add(organ);
            }
            if(x == 4)
            {
                organ = new Organo(x, virusgames.VirusGames.class.getResource("resource/O4.png").toString(), 0);
                mazo.add(organ);
            }
            if(x == 5)
            {
                comodin = new Comodin(x, 1, virusgames.VirusGames.class.getResource("resource/C1.png").toString(), 0);
                mazo.add(comodin);
                comodin = new Comodin(x, 2, virusgames.VirusGames.class.getResource("resource/C2.png").toString(), 0);
                mazo.add(comodin);
                for(int t = 1; t < 5; t++)
                {
                    comodin = new Comodin(x, 3, virusgames.VirusGames.class.getResource("resource/C3.png").toString(), 0);
                    mazo.add(comodin);
                }
            }
        }
        for(int x = 0; x < 2; x++)
        {
            tratamiento = new Tratamiento(0, 1, virusgames.VirusGames.class.getResource("resource/T1.png").toString(), 0);
            mazo.add(tratamiento);
            tratamiento = new Tratamiento(0, 2, virusgames.VirusGames.class.getResource("resource/T2.png").toString(), 0);
            mazo.add(tratamiento);
            tratamiento = new Tratamiento(0, 3, virusgames.VirusGames.class.getResource("resource/T3.png").toString(), 0);
            mazo.add(tratamiento);
            tratamiento = new Tratamiento(0, 4, virusgames.VirusGames.class.getResource("resource/T4.png").toString(), 0);
            mazo.add(tratamiento);
            tratamiento = new Tratamiento(0, 5, virusgames.VirusGames.class.getResource("resource/T5.png").toString(), 0);
            mazo.add(tratamiento);
        }
        
        Collections.shuffle(mazo);
    }
    
    public void asignarCartas(int mesas)
    {
        players = new ArrayList<>();
        mazoJugadores = new  ArrayList<>();
        for(int x = 1; x <= mesas; x++)
        {
            ArrayList<Carta> mano = new ArrayList<>();
            for(int y = 0; y < 3; y++)
            {
                Carta carta = (Carta)mazo.get(0);
                carta.jugador = x;
                mano.add(carta);
                mazo.remove(0);
            }
            Jugador player = new Jugador(mano, x);
            mazoJugadores.add(mano);
            players.add(player);
        }
    }
    
}

