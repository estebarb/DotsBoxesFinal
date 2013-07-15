/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import cus.Jugar.EPlayerTypes;
import cus.Jugar.IJugador;
import cus.Jugar.JugadorBot;
import cus.Jugar.JugadorHumano;
import entities.Juegos;
import entities.Jugadoresjuego;
import entities.Pendientes;
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

    public MJuego() {
        em = EMF.createEntityManager();
    }

    public List<IJugador> getJugadores(Juegos juego) {
        List<IJugador> salida = new ArrayList<>();
        for (Jugadoresjuego j : juego.getJugadoresjuegoCollection()) {
            IJugador jugador;
            switch (j.getJugador().getType()) {
                case 0:
                    jugador = new JugadorHumano();
                    jugador.fromJugadoresEntity(j.getJugador());
                    break;
                case 1:
                    jugador = new JugadorBot();
                    jugador.fromJugadoresEntity(j.getJugador());
                    break;
                /*case 2:
                    // Equipo
                    break;
                case 3:
                    // Ganador
                    break;*/
                default:
                    jugador = new JugadorBot();
                    jugador.fromString("hard");
                    break;
            }
            jugador.setPuntajeActual(0);
            salida.add(jugador);
        }
        return salida;
    }

    public Juegos CrearJuego(int filas, int columnas, Date fecha, Long torneo, List<IJugador> jugadores) {
        try {
            // Hay que agregar el juego a la base de datos
            Juegos juego = new Juegos();
            juego.setColumnas(columnas);
            juego.setFilas(filas);
            juego.setFecha(new Date());
            juego.setGanador(null);
            juego.setIsterminado(false);
            juego.setTorneo(null);
            juego.setTurnoactual(0);
            juego.setGanadoresCollection(null);
            juego.setTablero(new byte[columnas * filas * 4]);
            // Los jugadores
            Collection<Jugadoresjuego> jugadoresJuego = new ArrayList<>();
            Collection<Pendientes> pendientes = new ArrayList<>();
            for (IJugador j : jugadores) {
                // Jugadores juego
                Jugadoresjuego jj = new Jugadoresjuego();
                jj.setId(j.getId());
                jj.setJuego(juego);
                jj.setJugador(j.getJUGADOR());
                jj.setPuntaje(0);
                jugadoresJuego.add(jj);

                // Pendientes
                if (j.getTipo() == EPlayerTypes.Human) {
                    Pendientes pp = new Pendientes();
                    pp.setJuego(juego);
                    pp.setJugador(((JugadorHumano) j).getUser());
                    pp.setTurno(false);
                }
            }
            juego.setJugadoresjuegoCollection(jugadoresJuego);
            juego.setPendientesCollection(pendientes);



            // Se guarda esto en la BD
            em.getTransaction().begin();
            for (Jugadoresjuego j : jugadoresJuego) {
                em.persist(j);
            }
            for (Pendientes p : pendientes) {
                em.persist(p);
            }
            em.persist(juego);
            jugadores.get(0).AddPendiente(juego);
            em.getTransaction().commit();
            return juego;
        } catch (Exception e) {
            System.err.println(e.toString());
            em.getTransaction().rollback();
            return null;
        }
    }
}
