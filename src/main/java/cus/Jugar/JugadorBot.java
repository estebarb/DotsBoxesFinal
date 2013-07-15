/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cus.Jugar;

import entities.Juegos;
import entities.Jugadores;
import entities.Jugadorespc;
import modelo.MJugadorPC;

/**
 *
 * @author esteban
 */
public class JugadorBot extends IJugador {

    public JugadorBot() {
        this.tipo = EPlayerTypes.AI;
    }
    Integer nivel;
    Jugadorespc JugadorPC;
    Jugadores jugador;
    private int PuntajeActual;

    @Override
    public String getNombre() {
        String name;
        switch (nivel) {
            case 1:
                name = "Fácil";
                break;
            case 2:
                name = "Moderado";
                break;
            case 3:
                name = "Dificil";
                break;
            default:
                name = "Dificil";
                break;
        }
        return name;
    }

    @Override
    public int getRanking() {
        return JugadorPC.getRanking();
    }

    @Override
    public long getId() {
        return jugador.getId();
    }

    @Override
    public EPlayerTypes getTipo() {
        return EPlayerTypes.AI;
    }

    @Override
    public boolean fromString(String s) {
        nivel = null;
        if (s.equals("easy")) {
            nivel = 1;
        }
        if (s.equals("medium")) {
            nivel = 2;
        }
        if (s.equals("hard")) {
            nivel = 3;
        }
        if (nivel == null) {
            s = "hard";
            nivel = 3;
        }

        MJugadorPC mPcIA = new MJugadorPC();
        JugadorPC = mPcIA.getUserByID(s);
        jugador = mPcIA.getJugadorByJugadorPC(JugadorPC);
        return JugadorPC != null;
    }

    @Override
    public boolean fromJugadoresEntity(Jugadores j) {
        jugador = j;
        JugadorPC = j.getJugadorpc();
        nivel = JugadorPC.getId().intValue();
        return true;
    }

    @Override
    public boolean AddPendiente(Juegos p) {
        // Voy a implementar un random bot simple...
        IMachineBot bot;
        double probabilidad;
        switch(nivel){
            case 0:
            case 1:
                probabilidad = 0.60;
                break;
            case 2:
                probabilidad = 0.20;
                break;
            case 3:
            default:
                probabilidad = 0;
        }
        if(Math.random() < probabilidad){
            bot = new RandomBot();
        } else {
            bot = new InteligenteBot();
        }
               System.out.println("JugadorBot.AddPendiente... va a jugar");
        return bot.Jugar(p);
    }

    @Override
    public Jugadores getJUGADOR() {
        return jugador;
    }
    
    @Override
    public int getPuntajeActual() {
        return PuntajeActual;
    }

    @Override
    public void setPuntajeActual(int puntos) {
        PuntajeActual = puntos;
    }
}
