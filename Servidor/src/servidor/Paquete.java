/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.Serializable;

/**
 *
 * @author josue
 */
public class Paquete implements Serializable{
    
    private static final long serialVersionUID = -2723363051271966964L;
    public String img;
    
    public Paquete(String img)
    {
        this.img = img;
    }
    
}
