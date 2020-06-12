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
import javafx.beans.property.SimpleStringProperty;
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
    
    public SimpleStringProperty userName = new SimpleStringProperty();
    private int idSession;
    public Object accion;
    public Boolean clienteAvailable = true;
    public ObservableList<ServidorHilo> clients;
    int idEnable = 0;
    Servidor servidor;
    
    public ServidorHilo(Socket socket, int id, int isEnable, ObservableList<ServidorHilo> clients, Servidor servidor) {
        this.clients = clients;
        this.socket = socket;
        this.idSession = id;
        this.idEnable = isEnable;
        this.servidor = servidor;
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
                        case "pasarDeTurno": pasarDeTurno();
                        break;
                        default: userName.set(cmd);
                    }
                }
                
                object = null;
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
    
    public void startGame(int cantidad, int turno) //Enviar condicion para comenzar partida y cambiar pantalla
    {
        try
        {
            ArrayList<Object> paquete = new ArrayList<>();
            ArrayList<String> usersname = new ArrayList<>();
            for(ServidorHilo sh : clients)
            {
                usersname.add(sh.userName.getValue());
            }
            boolean x = true;
            paquete.add(x);
            paquete.add(cantidad);
            paquete.add(usersname);
            paquete.add(turno);
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
    
    public void enviarJuego(LogicalGame logical) //Enviar el juego al cliente asociado para que este posea el juego actual desde Servidor
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
    
    private void pasarDeTurno() {
        try {
            LogicalGame game = (LogicalGame) ois.readObject();
//            System.out.println("PP= " + game.players);
            servidor.enviarJuego(game);
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
