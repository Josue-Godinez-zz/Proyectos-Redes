/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virusgames.mazo;

/**
 *
 * @author Eduardo
 */
public class Virus extends Carta{
    
    public Virus(int color, String img, int numCartas)
    {
        super(numCartas);
        colorCarta = color;
        imgCarta = img;
//        setImage(img);
    }
    
    @Override
    public void validarJugada(Carta cartaSelec) {
        /*Este metodo se encargar de decirle a la clase como reaccionar ante la carta seleccionada*/
        /*Debe realizar cambios en el juego si es que la carta selecionada tiene jugabilidad con esta.*/
        
        if(cartaSelec instanceof Medicina){
        }
        if(cartaSelec instanceof Virus){
        }
        if(cartaSelec instanceof Tratamiento){
        }
        if(cartaSelec instanceof Organo){
        }
    }
}