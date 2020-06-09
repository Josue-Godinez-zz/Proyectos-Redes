/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virusgames.serviceconexion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import virusgames.controller.LogicalGame;

/**
 *
 * @author josue
 */

class User extends Thread {
    protected Socket socket;
    protected ObjectOutputStream oos;
    protected ObjectInputStream ois;
    public Cliente cliente;
    
    static boolean escena = false;
    public int id;
    public String username;
    public int idEnbale;
    private String cmd = "";
    public String hostID;
    public Boolean isClientAvaible = true;
    public static int cantidadJugadores = 0;
    Thread procesoCliente;
    public ArrayList<Object> paquete = new ArrayList<>();
    public LogicalGame juego = null;
    public ArrayList<String> usersName;
    public int turno;
    
    public User(int id)
    {
        this.id = id;
    }
    
    public User(int id, String username, int idEnable, Cliente cliente, String hostID) {
        this.id = id;
        this.username = username;
        this.idEnbale = idEnable;
        this.cliente = cliente;
        this.hostID = hostID;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(hostID, 10578); //Escribir la ip de quien se va a conectar
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            
            procesoCliente = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        oos.writeObject(username);
                        ArrayList<Object> paquete = (ArrayList<Object>)ois.readObject();
                        if((Boolean)paquete.get(0))
                        {
                            escena = true;
                            cantidadJugadores = (int) paquete.get(1);
                            usersName = (ArrayList<String>) paquete.get(2);
                            turno = (int) paquete.get(3);
                            System.out.println(turno);
                        }
                        paquete = null;
                        paquete = (ArrayList<Object>) ois.readObject();
                        juego = (LogicalGame)paquete.get(0);
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            procesoCliente.start();
            
                    
        } catch (IOException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Problemas Creando Usuario");
        }
    }
    
    public void desconectarCliente()
    {
        try {
            isClientAvaible = false;
            procesoCliente.stop();
            oos.writeObject("closeClient");
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}

public class Cliente {
    User user;
    public boolean accionRealizada = false;
    public int idEnable;   
    public String hostIP;
    public int cantidadPlayer;
    public boolean isHost = false;

    public Cliente(String hostIP)
    {
        this.hostIP = hostIP;
    }

    public void nuevoClient(int id,String username, int idEnable)
    {
        user = new User(id, username, idEnable, this, hostIP);
        user.start();
        this.idEnable = idEnable;
    }

    public boolean getChangeView()
    {
        return user.escena;
    }

    public void desconectarCliente() {
        user.desconectarCliente();
        user.stop();
    }

    public int getCantidadJugadores()
    {
        cantidadPlayer = user.cantidadJugadores;
        return cantidadPlayer;
    }

    public LogicalGame getJuego()
    {
        return user.juego;
    }
    
    public ArrayList<String> getUsersName()
    {
        return user.usersName;
    }
    
    public int getTurno()
    {
        System.out.println(user.turno);
        return user.turno;
    }    
} 
    