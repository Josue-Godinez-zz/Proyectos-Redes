/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.serviceConexion;

;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import servidor.LogicalGame;


/**
 *
 * @author josue
 */
public class ServidorHilo extends Thread {
    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
    
    public SimpleStringProperty userName = new SimpleStringProperty();
    public SimpleBooleanProperty boolListo = new SimpleBooleanProperty(false);
    public Object accion;
    public Boolean clienteAvailable = true;
    public ObservableList<ServidorHilo> clients;
    Server servidor;
    
    public ServidorHilo(Socket socket, ObservableList<ServidorHilo> clients, Server servidor) {
        this.clients = clients;
        this.socket = socket;
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
                userName.setValue((String) ois.readObject());
                
                while (clienteAvailable) 
                {                    
                    ArrayList<Object> paquete = (ArrayList<Object>) ois.readObject();
                    System.out.println(paquete.get(0));
                    int accion = (int) paquete.get(0);
                    switch (accion) {
                        case 0:
                            cerrarCliente();
                            break;
                        case 1: 
                            actualizarPartida((LogicalGame)paquete.get(1));
                            break;
                        case 2:
                            isReady((Boolean)paquete.get(1));
                            break;
                        
                    }
                }
                
            } catch (IOException ex) {
                Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  

    private void cerrarCliente() {
        
        try {
            clienteAvailable = false;
            oos.close();
            ois.close();
            socket.close();
            clients.remove(this);
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void actualizarPartida(LogicalGame get) {
        servidor.actualizarPartida(get);
//        for(ServidorHilo sh: clients){
//            try {
//                oos.writeObject(get);
//            } catch (IOException ex) {
//                Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }
    
    public void enviarJuegoInicial(int turno, LogicalGame logical, boolean cambiarVista, ArrayList<String>username)
    {
        try {
            ArrayList<Object> paquete = new ArrayList<>();
            paquete.add(turno);
            paquete.add(logical);
            paquete.add(cambiarVista);
            paquete.add(username);
            oos.writeObject(paquete);
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void isReady(Boolean isReady) {
        boolListo.setValue(isReady);
    }

    @Override
    public String toString() {
        return "ServidorHilo{" + "userName=" + userName + '}';
    }

    void enviarJuegoActualizado(LogicalGame get) {
        try {
            ArrayList<Object> paquete = new ArrayList<>();
            paquete.add(get);
            oos.writeObject(paquete);
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
