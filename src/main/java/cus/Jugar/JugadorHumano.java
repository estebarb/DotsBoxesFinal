/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cus.Jugar;

import beans.UserBean;
import cus.Autenticar.Autenticar;
import entities.Juegos;
import entities.Jugadores;
import entities.Pendientes;
import entities.Usuarios;
import java.util.List;
import javax.persistence.EntityManager;
import modelo.MUsuarios;
import utils.EMF;

/**
 *
 * @author Esteban
 */
public class JugadorHumano extends IJugador {

    private Usuarios user;
    private Jugadores jugador;
    private EntityManager em;

    public JugadorHumano() {
        this.tipo = EPlayerTypes.Human;
        em = EMF.createEntityManager();
    }

    @Override
    public String getNombre() {
        return user.getNombre();
    }

    @Override
    public int getRanking() {
        return user.getRanking();
    }

    @Override
    public long getId() {
        return jugador.getId();
    }

    @Override
    public EPlayerTypes getTipo() {
        return EPlayerTypes.Human;
    }

    /**
     * Llena los datos del usuario a partir de un correo electrónico
     *
     * @param s
     * @return
     */
    @Override
    public boolean fromString(String s) {
        MUsuarios muser = new MUsuarios();
        user = muser.getUserByEmail(s);
        jugador = muser.getJugadorByUsuario(user);
        return user != null;
    }

    @Override
    public boolean fromJugadoresEntity(Jugadores j) {
        user = j.getUsuario();
        jugador = j;
        return true;
    }

    @Override
    public Jugadores getJUGADOR() {
        return jugador;
    }

    @Override
    public boolean AddPendiente(Juegos p) {
        Pendientes pendiente = (Pendientes) em.createQuery("select p from Pendientes p WHERE p.juego = :juego AND p.jugador = :jugador")
                .setParameter("juego", p)
                .setParameter("jugador", jugador)
                .getSingleResult();
        List<Pendientes> todos = em.createQuery("select p from Pendientes p WHERE p.juego = :juego")
                .setParameter("juego", p)
                .getResultList();
        
        
        em.getTransaction().begin();
        for(Pendientes pj : todos){
            pj.setTurno(false);
            em.persist(pj);
        }
        pendiente.setTurno(true);
        em.persist(pendiente);
        em.getTransaction().commit();
        return true;
    }
}
