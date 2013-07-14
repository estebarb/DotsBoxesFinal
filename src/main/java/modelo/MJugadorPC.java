/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import entities.Jugadores;
import entities.Jugadorespc;
import entities.Usuarios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import utils.CookieReader;
import utils.EMF;

/**
 *
 * @author esteban
 */
public class MJugadorPC {

    EntityManager em;

    public MJugadorPC() {
        this.em = EMF.createEntityManager();
    }

    public Jugadorespc getUserByID(String id) {
        Jugadorespc usr = null;
        try {
            List<Jugadorespc> users = em.createNamedQuery("Jugadorespc.findByNombre")
                    .setParameter("nombre", id)
                    .getResultList();
            if (users.isEmpty()) {
                usr = null;
            } else {
                usr = users.get(0);
            }
        } catch (Exception e) {
        } finally {
            return usr;
        }
    }

    public Jugadores getJugadorByJugadorPC(Jugadorespc pcIA) {
        Jugadores jug;
        List<Jugadores> jugs = em.createQuery("select j FROM Jugadores j where j.type = 1 AND j.jugadorpc = :user")
                .setParameter("user", pcIA)
                .getResultList();
        if (!jugs.isEmpty()) {
            jug = jugs.get(0);
        } else {
            // No existe un Jugador para ese ID, entonces hay que hacerlo...
            em.getTransaction().begin();
            jug = new Jugadores();
            jug.setType(1);
            jug.setUsuario(null);
            jug.setEquipo(null);
            jug.setGanador(null);
            jug.setJugadorpc(pcIA);
            em.persist(jug);
            em.getTransaction().commit();
        }
        return jug;
    }
}
