/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virusgames.controller;

import java.io.Serializable;
import javafx.scene.layout.VBox;

/**
 *
 * @author josue
 */
public class Prueba extends VBox implements Serializable{
    
    private static final long serialVersionUID = -2723363051271966964L;
    
    public int x; 
    public Prueba()
    {
        super();
        x = 10;
    }
    
}
