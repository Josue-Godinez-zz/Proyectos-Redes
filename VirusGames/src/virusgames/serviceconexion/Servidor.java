/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virusgames.serviceconexion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import virusgames.controller.LogicalGame;

/**
 *
 * @author josue
 */
public class Servidor {
    
    static ServerSocket serversocket = null;
    static Thread proceso = null;
    public static ObservableList<ServidorHilo> clients =  FXCollections.observableArrayList();
    
    public int idEnable = 0;
    public boolean aceptarClientes = true;
    public boolean gameStart = false;
    Thread procesoSecundario;
    public SimpleIntegerProperty cantPlayer = new SimpleIntegerProperty(0);
    ArrayList<Object> paquete;
    
    
    public Servidor(SimpleIntegerProperty cantPlayer)
    {
        this.cantPlayer = cantPlayer;
    }
    
    public void iniciarServidor()
    {   
        clients.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable t) {
                cantPlayer.set(clients.size());
                if(clients.size() == 6)
                {
                    aceptarClientes = false;
                }
                else
                {
                    aceptarClientes = true;
                }
            }
        });
        System.out.print("Inicializando Servidor... ");
        procesoSecundario = new Thread(new Runnable() {
            @Override
            public void run() {
            try {
                serversocket = new ServerSocket(10578);
                System.out.println("\t[OK]");
                int idSession = 0;
                while (!gameStart) {
                if(aceptarClientes && clients.size() <= 5)
                {
                    Socket socket;
                    socket = serversocket.accept();
                    System.out.println("Nueva conexión entrante: " + socket);
                    ServidorHilo sh = new ServidorHilo(socket, idSession, idEnable, clients, Servidor.this);
                    sh.start();
                    clients.add(sh);
                    idSession++;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Hubo Un Problema Al Iniciar El Servidor");
        }
            }
        });
        procesoSecundario.start();
    }
    
    public void iniciarProceso()
    {
        if(proceso == null)
        {
            System.out.print("Inicializando Proceso...");
            try {
                proceso = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("\t[OK]");
                        iniciarServidor();
                    }
                });
            } catch (Exception e) {
                System.out.println("Hubo Un Problema Al Iniciar Proceso");
            }
            proceso.start();
        } 
    }
    
    public void cerrarServidor()
    {
        if(serversocket!=null && proceso != null)
        {
            procesoSecundario.stop();
            for(ServidorHilo sh: clients)
            {
                sh.cerrarServidorHilo();
            }
            System.out.print("Cerrando Servidor...");
            try {
                serversocket.close();
                System.out.println("\t[OK]");
                serversocket = null;
                System.out.print("Cerrando Proceso...");
                try {
                    proceso.stop();
                    proceso = null;
                    System.out.println("\t[OK]");
                } catch (Exception e) {
                    System.out.println("Hubo Problemas Al Cerrar El Proceso");
                }
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Hubo Problemas Al Cerrar El Servidor");
            }
        }
    }
    
    public void setAcceptClient(Boolean cond)
    {
        aceptarClientes = cond;
    }
    
    public void setGameStart(Boolean cond)
    {
        this.gameStart = cond;
    }
    
    public void startGame() //Envia señal para cambiar pantalla y comenzar la partida
    {
        for(int x = 0; x < clients.size(); x++)
        {
            clients.get(x).startGame(clients.size(), x+1);
        }
    }
    
    public void enviarJuego(LogicalGame logical)
    {
        for(int x = 0; x < clients.size(); x++)
        {
            clients.get(x).enviarJuego(logical);
        }
    }
    
    public void actualizarJuego()
    {
        for(int x = 0; x < clients.size(); x++)
        {
            clients.get(x).enviarJuego(paquete);
        }
    }
}
