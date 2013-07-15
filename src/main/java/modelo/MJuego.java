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
            em.getTransaction().rollback();
            return null;
        }
    }

    public Juegos Jugar(Juegos juego, int BoxNumber, int LineNumber) {
        byte[] data = juego.getTablero();

        if (isJugadaValida(juego, BoxNumber, LineNumber)) {
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

        data[BoxNumber * 5 + LineNumber] = numJugadorActual;
        int filaUsada = BoxNumber / juego.getColumnas();
        int columnaUsada = BoxNumber % juego.getColumnas();
        int filas = juego.getFilas();
        int columnas = juego.getColumnas();
        int cajasLlenas = testBox(data, BoxNumber) ? 1 : 0;
        switch (LineNumber) {
            case 0: // norte
                if (filaUsada != 0) {
                    data[(BoxNumber - columnas) * 5 + 2] = numJugadorActual;
                    cajasLlenas = testBox(data, BoxNumber - columnas) ? 1 : 0;
                }
            case 1: // este
                if (columnaUsada != columnas - 1) {
                    data[(BoxNumber + 1) * 5 + 3] = numJugadorActual;
                    cajasLlenas = testBox(data, BoxNumber + 1) ? 1 : 0;
                }
            case 2: // sur
                if (filaUsada != filas - 1) {
                    data[(BoxNumber + columnas) * 5 + 0] = numJugadorActual;
                    cajasLlenas = testBox(data, BoxNumber + columnas) ? 1 : 0;
                }
            case 3: // oeste
                if (columnaUsada != 0) {
                    data[(BoxNumber - 1) * 5 + 1] = numJugadorActual;
                    cajasLlenas = testBox(data, BoxNumber - 1) ? 1 : 0;
                }
        }


        Jugadoresjuego[] jugadoresjuego = (Jugadoresjuego[]) (juego.getJugadoresjuegoCollection().toArray());
        Jugadoresjuego jj = jugadoresjuego[numJugadorActual];
        boolean isTerminado = false;
        if (cajasLlenas > 0) {
            // Se completó una caja:
            data[BoxNumber * 5 + 4] = numJugadorActual;

            jj.setPuntaje(jj.getPuntaje() + cajasLlenas);
            //juego.setTurnoactual(juego.getTurnoactual() - 1);

            isTerminado = true;
            int i = 0;
            while (isTerminado && i < data.length) {
                isTerminado &= (data[i] != 0);
            }
            juego.setIsterminado(isTerminado);
            if (isTerminado) {
                juego.setGanador(jj.getJugador());
                em.getTransaction().begin();
                em.persist(jj);

                // Actualiza el ranking
                for (Jugadoresjuego jugjuego : juego.getJugadoresjuegoCollection()) {
                    Jugadores jug = jugjuego.getJugador();
                    switch (jug.getType()) {
                        case 0: // Humano
                            Usuarios user = jug.getUsuario();
                            user.setRanking(user.getRanking() + jugjuego.getPuntaje());
                            em.persist(user);
                            break;
                        case 1:
                            Jugadorespc pcia = jug.getJugadorpc();
                            pcia.setRanking(pcia.getRanking() + jugjuego.getPuntaje());
                            em.persist(pcia);
                            break;
                        case 2:
                        //TODO equipo
                        case 3:
                        //TODO tipo "ganador"
                        default:
                    }
                }

                // Actualiza el juego
                em.persist(juego);
                em.getTransaction().commit();
            }
        } else {
            em.getTransaction().begin();
            juego.setTurnoactual(juego.getTurnoactual() + 1);
            juego.setTablero(data);
            // Se añade la jugada del próximo jugador como pendiente:
            numJugadorActual = (byte) (juego.getTurnoactual() % juego.getJugadoresjuegoCollection().size());
            jj = jugadoresjuego[numJugadorActual];

            IJugador siguienteJugador;
            switch (jj.getJugador().getType()) {
                case 0: // Humano
                    siguienteJugador = new JugadorHumano();
                    siguienteJugador.fromJugadoresEntity(jj.getJugador());
                    break;
                case 1:
                    siguienteJugador = new JugadorBot();
                    siguienteJugador.fromJugadoresEntity(jj.getJugador());
                    break;
                case 2:
                //TODO equipo
                case 3:
                //TODO tipo "ganador"
                default:
                    siguienteJugador = new JugadorBot();
                    siguienteJugador.fromString("hard");
            }
            em.persist(juego);
            siguienteJugador.AddPendiente(juego);



            em.getTransaction().commit();
        }
        return juego;
    }
}
