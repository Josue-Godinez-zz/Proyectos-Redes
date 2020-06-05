/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virusgames.serviceconexion;

;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import virusgames.controller.LogicalGame;


/**
 *
 * @author josue
 */
public class ServidorHilo extends Thread {
    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
    
    public String userName;
    private int idSession;
    public Object accion;
    public Boolean clienteAvailable = true;
    public ObservableList<ServidorHilo> clients;
    int idEnable = 0;
    
    public ServidorHilo(Socket socket, int id, int isEnable, ObservableList<ServidorHilo> clients) {
        this.clients = clients;
        this.socket = socket;
        this.idSession = id;
        this.idEnable = isEnable;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            
            System.out.println("Inicializacion Completa...");
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Problemas Con Servidor Hilos");
        }
    }

    @Override
    public void run() {
        while(clienteAvailable)
        {
            try {
                Object object = ois.readObject();
                
                if(object instanceof String)
                {
                    String cmd = (String) object.toString();
                    switch(cmd)
                    {
                        case "closeClient": cerrarServidorHilo();
                        break;
                        default: userName = cmd;
                    }
                }
                
                object = null;
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
    
    public void startGame(int cantidad) //Enviar condicion para comenzar partida y cambiar pantalla
    {
        try
        {
            ArrayList<Object> paquete = new ArrayList<>();
            boolean x = true;
            paquete.add(x);
            paquete.add(cantidad);
            oos.writeObject(paquete);
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cerrarServidorHilo()
    {
        try {
            clienteAvailable = false;
            socket.close();
            clients.remove(this);
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void enviarJuego(LogicalGame logical) //Enviar el juego al cliente asociado para que este posea el juego actual
    {
        try 
        {
            ArrayList<Object> paquete = new ArrayList<>();
            paquete.add(logical);
            oos.writeObject(paquete);
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
