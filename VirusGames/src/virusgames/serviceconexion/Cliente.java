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
import javafx.beans.property.SimpleBooleanProperty;
import servidor.LogicalGame;
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
    public String username;
    public String hostID;
    public Boolean isClientAvaible = true;
    public Thread procesoCliente;
    public static SimpleBooleanProperty cambioVista = new SimpleBooleanProperty(false);
    public static SimpleBooleanProperty nuevoJuego = new SimpleBooleanProperty(false);
    public int turno = 0;
    public ArrayList<Object> paquete = new ArrayList<>();
    public static LogicalGame juego = null;
    public ArrayList<String> usersName = new ArrayList<>();
    public boolean terminarPartida = false;
    
    public User(String username,Cliente cliente, String hostID,SimpleBooleanProperty bool, SimpleBooleanProperty bool2) {
        this.username = username;
        this.cliente = cliente;
        this.hostID = hostID;
        this.cambioVista.bindBidirectional(bool);
        this.nuevoJuego.bindBidirectional(bool2);
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
                        /*Registro inicial*/
                        oos.writeObject(username);
                        ArrayList<Object> paquete = (ArrayList<Object>)ois.readObject();
                        /*Recibe el juego inicial y lo ajusta al cliente*/
                        turno = (int) paquete.get(0);
                        AppContext.getInstance().set("juegoCargado", (LogicalGame) paquete.get(1));
                        usersName.addAll((ArrayList<String>) paquete.get(3));
                        cambioVista.set((boolean) paquete.get(2));
                        System.out.println(paquete.get(2));
                        
                         /*Juego*/
                        do {
                            juego = (LogicalGame) ois.readObject();
                            AppContext.getInstance().set("nuevoJuego", juego);
                        } while(terminarPartida);
                         /**/
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

    void isReady(boolean rdy) {
        try {
            ArrayList<Object> paquete = new ArrayList<>();
            paquete.add(2);
            paquete.add(rdy);
            oos.writeObject(paquete);
        } catch (IOException iOException) {
            
        }
    }
    
    public int getTurno()
    {
        return turno;
    }
    
    public ArrayList<String> getUserName()
    {
        return usersName;
    }
}

public class Cliente {
    User user;
    public boolean accionRealizada = false;
    public String hostIP;
    public static SimpleBooleanProperty cambiarVista = new SimpleBooleanProperty(false);
    public static SimpleBooleanProperty nuevoJuego = new SimpleBooleanProperty(false);

    public Cliente(String hostIP)
    {
        this.hostIP = hostIP;
    }

    public void nuevoClient(String username)
    {
        user = new User(username, this, hostIP, cambiarVista, nuevoJuego);
        user.start();
    }
    public void desconectarCliente() {
        user.desconectarCliente();
        user.stop();
    }
    
    public void isReady(boolean rdy){
        user.isReady(rdy);
    }
    
    public int getTurno()
    {
        return user.getTurno();
    }
    
    public ArrayList<String> getUsersName()
    {
        return user.getUserName();
    }

    public void pasarDeTurno(LogicalGame logical) {
        user.pasarDeTurno(logical);
    }
} 
    