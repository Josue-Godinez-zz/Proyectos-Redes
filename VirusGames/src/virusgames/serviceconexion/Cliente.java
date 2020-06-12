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
import virusgames.util.AppContext;

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
    public Thread procesoCliente;
    public ArrayList<Object> paquete = new ArrayList<>();
    public static LogicalGame juego = null;
    public ArrayList<String> usersName;
    public int turno;
    public boolean changeCard = false;
    public static boolean pasarTurno = false;
    
    
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
                        }
                        paquete = null;
                        paquete = (ArrayList<Object>) ois.readObject();
                        LogicalGame aux = (LogicalGame)paquete.get(0);
                        setLogicalGame(aux);
                        do
                    {
                        if(!juego.isGameFinished)
                        {
                            try {
                                paquete = (ArrayList<Object>) ois.readObject();
//                                System.out.println(((LogicalGame)paquete.get(0)).getPlayers() + " Turno Actual: " + ((LogicalGame)paquete.get(0)).turn);
                                LogicalGame auxGame = (LogicalGame) paquete.get(0);
                                System.out.println(auxGame);
                                setLogicalGame(auxGame);
                                changeCard = true;
                                AppContext.getInstance().set("cond", changeCard);
                                paquete = null;
                            } catch (IOException ex) {
                                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    while(!juego.isGameFinished);
                        procesoCliente = null;
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
            ArrayList<Object> paquete = new ArrayList<>();
            int cmd = 0;
            paquete.add(cmd);
            oos.writeObject(paquete);
            ois.close();
            oos.close();
            socket.close();
            procesoCliente.stop();
        } catch (IOException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void pasarDeTurno(LogicalGame game)    
    {
        try {
            ArrayList<Object> paquete = new ArrayList<>();
            paquete.add(1);
            paquete.add(game);
            oos.writeObject(paquete);
        } catch (IOException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setLogicalGame(LogicalGame paquete)
    {
        User.juego = paquete;
    }
    
    public LogicalGame getJuego()
    {
        return User.juego;
    }
    
    public void setPasarTurno(boolean cond)
    {
        User.pasarTurno = cond;
    }
    
    public boolean getPasarTurno()
    {
        return User.pasarTurno;
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

    public void setChangeView(boolean cond)
    {
        user.changeCard = cond;
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
    public void setJuego(LogicalGame logical)
    {
        user.juego = logical;
    }

    public LogicalGame getJuego()
    {
        return user.getJuego();
    }
    
    public ArrayList<String> getUsersName()
    {
        return user.usersName;
    }
    
    public int getTurno()
    {
        return user.turno;
    }
    
    public void setPasarTurno(boolean cond)
    {
        user.setPasarTurno(cond);
    }
    
    public boolean getPasarTurno()
    {
        return user.getPasarTurno();
    }

    public void pasarDeTurno(LogicalGame game)
    {
        user.pasarDeTurno(game);
    }
    
} 
    