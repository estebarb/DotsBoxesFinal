/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cus.Jugar;

import entities.Juegos;

/**
 *
 * @author esteban
 */
class InteligenteBot implements IMachineBot {

    @Override
    public boolean Jugar(Juegos j) {
        return (new RandomBot()).Jugar(j);
    }
    
}
