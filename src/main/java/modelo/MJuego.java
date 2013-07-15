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
import entities.Jugadores;
import entities.Jugadoresjuego;
import entities.Jugadorespc;
import entities.Pendientes;
import entities.Usuarios;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
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
            jugador.setPuntajeActual(j.getPuntaje());
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
            juego.setTablero(new byte[columnas * filas * 5]);
            // Los jugadores
            Collection<Jugadoresjuego> jugadoresJuego = new ArrayList<>();
            //Collection<Pendientes> pendientes = new ArrayList<>();
            for (IJugador j : jugadores) {
                // Jugadores juego
                Jugadoresjuego jj = new Jugadoresjuego();
                jj.setId(j.getId());
                jj.setJuego(juego);
                jj.setJugador(j.getJUGADOR());
                jj.setPuntaje(0);
                jugadoresJuego.add(jj);

                // Pendientes
                /*
                 if (j.getTipo() == EPlayerTypes.Human) {
                 Pendientes pp = new Pendientes();
                 pp.setJuego(juego);
                 pp.setJugador(((JugadorHumano) j).getUser());
                 pp.setTurno(false);
                 pendientes.add(pp);
                 }*/
            }
            juego.setJugadoresjuegoCollection(jugadoresJuego);
            //juego.setPendientesCollection(pendientes);

            Pendientes pp = null;
            if (jugadores.get(0).getTipo() == EPlayerTypes.Human) {
                pp = new Pendientes();
                pp.setJuego(juego);
                pp.setJugador(((JugadorHumano) jugadores.get(0)).getUser());
                pp.setTurno(true);
            }



            // Se guarda esto en la BD
            em.getTransaction().begin();
            for (Jugadoresjuego j : jugadoresJuego) {
                em.persist(j);
            }
            /*
             for (Pendientes p : pendientes) {
             em.persist(p);
             }
             * */
            if (pp != null) {
                em.persist(pp);
            }
            em.persist(juego);
            jugadores.get(0).AddPendiente(juego);
            em.getTransaction().commit();
            return juego;
        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
            em.getTransaction().rollback();
            return null;
        }
    }

    public Juegos Jugar(Juegos juego, int BoxNumber, int LineNumber) {
        if (isJugadaValida(juego, BoxNumber, LineNumber)) {
            //System.out.println("@MJuego.Jugar: isValida = true");
            juego = commitJugada(juego, BoxNumber, LineNumber);
        }
        return juego;
    }

    private boolean isJugadaValida(Juegos juego, int BoxNumber, int LineNumber) {
        byte[] data = juego.getTablero();
        byte linea = data[BoxNumber * 5 + LineNumber];
        return linea == 0;
    }

    /**
     * Determina si se completó una caja
     *
     * @param data
     * @param BoxNumber
     * @return
     */
    private boolean testBox(byte[] data, int BoxNumber) {
        return data[BoxNumber * 5] != 0
                && data[BoxNumber * 5 + 1] != 0
                && data[BoxNumber * 5 + 2] != 0
                && data[BoxNumber * 5 + 3] != 0;
    }

    private Juegos commitJugada(Juegos juego, int BoxNumber, int LineNumber) {
        byte numJugadorActual = (byte) (juego.getTurnoactual() % juego.getJugadoresjuegoCollection().size());
        byte[] data = juego.getTablero();

        // Se agregan la jugada a los datos---
        numJugadorActual++;
        data[BoxNumber * 5 + LineNumber] = numJugadorActual;
        int filaUsada = BoxNumber / juego.getColumnas();
        int columnaUsada = BoxNumber % juego.getColumnas();
        int filas = juego.getFilas();
        int columnas = juego.getColumnas();
        int cajasLlenas = testBox(data, BoxNumber) ? 1 : 0;
        if (testBox(data, BoxNumber)) {
            data[BoxNumber * 5 + 4] = numJugadorActual;
        }
        switch (LineNumber) {
            case 0: // norte
                if (filaUsada != 0) {
                    data[(BoxNumber - columnas) * 5 + 2] = numJugadorActual;
                    cajasLlenas += testBox(data, BoxNumber - columnas) ? 1 : 0;
                    if (testBox(data, BoxNumber - columnas)) {
                        data[(BoxNumber - columnas) * 5 + 4] = numJugadorActual;
                    }
                }
                break;
            case 1: // este
                if (columnaUsada != columnas - 1) {
                    data[(BoxNumber + 1) * 5 + 3] = numJugadorActual;
                    cajasLlenas += testBox(data, BoxNumber + 1) ? 1 : 0;
                    if (testBox(data, BoxNumber + 1)) {
                        data[(BoxNumber + 1) * 5 + 4] = numJugadorActual;
                    }
                }
                break;
            case 2: // sur
                if (filaUsada != filas - 1) {
                    data[(BoxNumber + columnas) * 5 + 0] = numJugadorActual;
                    cajasLlenas += testBox(data, BoxNumber + columnas) ? 1 : 0;
                    if (testBox(data, BoxNumber + columnas)) {
                        data[(BoxNumber + columnas) * 5 + 4] = numJugadorActual;
                    }
                }
                break;
            case 3: // oeste
                if (columnaUsada != 0) {
                    data[(BoxNumber - 1) * 5 + 1] = numJugadorActual;
                    cajasLlenas += testBox(data, BoxNumber - 1) ? 1 : 0;
                    if (testBox(data, BoxNumber - 1)) {
                        data[(BoxNumber - 1) * 5 + 4] = numJugadorActual;
                    }
                }
                break;
        }

        numJugadorActual--;
        Iterator<Jugadoresjuego> it = juego.getJugadoresjuegoCollection().iterator();
        for (int i = 0; i < numJugadorActual; i++) {
            it.next();
        }
        Jugadoresjuego jj = it.next();
        int nuevoPuntaje = jj.getPuntaje() + cajasLlenas;
        boolean isTerminado = false;
        if (cajasLlenas > 0) {
            // Se completó una caja:
            //data[BoxNumber * 5 + 4] = numJugadorActual;

            //juego.setTurnoactual(juego.getTurnoactual() - 1);

            isTerminado = true;
            int i = 0;
            while (isTerminado && i < data.length) {
                isTerminado &= (data[i+4] != 0);
                i += 5;
            }
            if (isTerminado) {
                em.getTransaction().begin();
                juego.setIsterminado(isTerminado);
                jj.setPuntaje(nuevoPuntaje);
                juego.setGanador(jj.getJugador());
                em.merge(jj);

                // Actualiza el ranking
                for (Jugadoresjuego jugjuego : juego.getJugadoresjuegoCollection()) {
                    Jugadores jug = jugjuego.getJugador();
                    switch (jug.getType()) {
                        case 0: // Humano
                            Usuarios user = jug.getUsuario();
                            user.setRanking(user.getRanking() + jugjuego.getPuntaje());
                            em.merge(user);
                            break;
                        case 1:
                            Jugadorespc pcia = jug.getJugadorpc();
                            pcia.setRanking(pcia.getRanking() + jugjuego.getPuntaje());
                            em.merge(pcia);
                            break;
                        case 2:
                        //TODO equipo
                        case 3:
                        //TODO tipo "ganador"
                        default:
                    }
                }

                // Actualiza el juego
                juego.setTablero(data);
                em.merge(juego);
                em.getTransaction().commit();
            } else {
                em.getTransaction().begin();
                juego.setIsterminado(false);
                em.merge(jj);
                jj.setPuntaje(nuevoPuntaje);

                juego.setTablero(data);
                //em.persist(juego);
                em.merge(juego);
                em.getTransaction().commit();
                IJugador siguienteJugador = IJugadorFromJugadoresJuego(jj);
                siguienteJugador.AddPendiente(juego);
            }
        } else {
            //System.out.println("-- 1");
            em.getTransaction().begin();
            juego.setIsterminado(false);
            int turnoActual = juego.getTurnoactual() + 1;
            juego.setTurnoactual(turnoActual);
            juego.setTablero(data);
            // Se añade la jugada del próximo jugador como pendiente:
            numJugadorActual = (byte) (turnoActual % juego.getJugadoresjuegoCollection().size());
            it = juego.getJugadoresjuegoCollection().iterator();
            for (int i = 0; i < numJugadorActual; i++) {
                it.next();
            }
            jj = it.next();
            //System.out.println("-- 2: Jug actual= " + numJugadorActual);
            IJugador siguienteJugador = IJugadorFromJugadoresJuego(jj);
            //System.out.println("-- 3: Jug siguiente= " + siguienteJugador.getNombre());

            //em.persist(jj);
            //em.persist(juego);
            em.merge(juego);
            System.out.println("El siguiente jugador es: " + siguienteJugador.getNombre() + " tipo " + siguienteJugador.getSTipo());
            em.getTransaction().commit();
            siguienteJugador.AddPendiente(juego);
        }
        return juego;
    }

    private IJugador IJugadorFromJugadoresJuego(Jugadoresjuego jj) {
        IJugador jugador;
        switch (jj.getJugador().getType()) {
            case 0: // Humano
                jugador = new JugadorHumano();
                jugador.fromJugadoresEntity(jj.getJugador());
                break;
            case 1:
                jugador = new JugadorBot();
                jugador.fromJugadoresEntity(jj.getJugador());
                break;
            case 2:
            //TODO equipo
            case 3:
            //TODO tipo "ganador"
            default:
                jugador = new JugadorBot();
                jugador.fromString("hard");
        }
        return jugador;
    }
}
