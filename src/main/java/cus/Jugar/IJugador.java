/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cus.Jugar;

import entities.Juegos;

/**
 *
 * @author Esteban
 */
public abstract class IJugador {
    protected EPlayerTypes tipo;

    public abstract String getNombre();

    public abstract int getRanking();

    public abstract long getId();

    public abstract EPlayerTypes getTipo();

    public String getSTipo() {
        switch (tipo) {
            case AI:
                return "Máquina";
            case GameWinner:
                return "Ganador de partido anterior";
            case Human:
                return "Jugador";
            case Team:
                return "Equipo";
            default:
                return "Otro";
        }
    }
    
    public abstract boolean fromString(String s);

    public abstract boolean AddPendiente(Juegos p);
}
