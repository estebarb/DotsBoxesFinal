/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import cus.Jugar.IJugador;
import entities.Juegos;
import entities.Jugadoresjuego;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import utils.EMF;

/**
 *
 * @author esteban
 */
public class MJuego {
    
    EntityManager em;
    
    public MJuego(){
        em = EMF.createEntityManager();
    }
    
    public boolean CrearJuego(int filas, int columnas, Date fecha, Long torneo, List<IJugador> jugadores){
        try{
        // Hay que agregar el juego a la base de datos
            Juegos juego = new Juegos();
            juego.setColumnas(columnas);
            juego.setFilas(filas);
            juego.setFecha(new Date());
            juego.setGanador(null);
            juego.setIsterminado(false);
            juego.setTorneo(null);
            juego.setTurnoactual(1);
            juego.setGanadoresCollection(null);
            juego.setTablero(new byte[columnas * filas * 4]);
            // Los jugadores
            Collection<Jugadoresjuego> jugadoresJuego = new ArrayList<>();
            for (IJugador j : jugadores) {
                Jugadoresjuego jj = new Jugadoresjuego();
                jj.setId(j.getId());
                jj.setJuego(juego);
                jj.setJugador(j.getJUGADOR());
                jj.setPuntaje(0);
                jugadoresJuego.add(jj);
            }
            juego.setJugadoresjuegoCollection(jugadoresJuego);


            // Se guarda esto en la BD
            em.getTransaction().begin();
            for(Jugadoresjuego j : jugadoresJuego){
                em.persist(j);
            }
            em.persist(juego);
            jugadores.get(0).AddPendiente(juego);
            em.getTransaction().commit();
            return true;
        }catch(Exception e){
            System.err.println(e.toString());
            em.getTransaction().rollback();
            return false;
        }
    }
}
