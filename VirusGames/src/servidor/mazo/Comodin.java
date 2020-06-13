/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.mazo;

/**
 *
 * @author josue
 */
public class Comodin extends Carta{

    public int tipoComodin;
    public Comodin(int color, int type, String img, int numCartas) 
    {
        super(numCartas);
        tipoComodin = type;
        colorCarta = color;
        imgCarta = img;
        //setImage(img);
    }

    @Override
    public void validarJugada(Carta cartaSelec) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
