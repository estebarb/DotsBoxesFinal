/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cus.Jugar;

import entities.Juegos;
import java.util.Random;
import modelo.MJuego;

/**
 *
 * @author esteban
 */
public class RandomBot implements IMachineBot {

    @Override
    public boolean Jugar(Juegos p) {        
        byte[] data = p.getTablero();
        int largo = data.length/5;
        int movida;
        Random rnd = new Random();
        MJuego juego = new MJuego();
        for(movida = rnd.nextInt(largo); movida < largo; movida=(movida+1)%largo){
            for(int i = 0; i < 4; i++){
                if(data[movida*5 + i] == 0){
                    juego.Jugar(p, movida, i);
                }
            }
        }
        return true;
    }
    
}
