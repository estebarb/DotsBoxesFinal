/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import entities.Jugadores;
import java.util.List;
import javax.persistence.EntityManager;
import entities.Usuarios;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import utils.CookieReader;
import utils.EMF;

/**
 *
 * @author Esteban
 */
public class MUsuarios {

    EntityManager em;

    public MUsuarios() {
        this.em = EMF.createEntityManager();
    }

    public Usuarios getUserByEmail(String email) {
        List<Usuarios> users = em.createNamedQuery("Usuarios.findByEmail")
                .setParameter("email", email)
                .getResultList();
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }

    public Usuarios getUserByID(String id) {
        Usuarios usr = null;
        try {
            long idUsuario = Long.parseLong(id);
            List<Usuarios> users = em.createNamedQuery("Usuarios.findById")
                    .setParameter("id", idUsuario)
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

    public Usuarios getUserByCookie(Cookie[] c) {
        String user = CookieReader.getCookieValue(c, "user", null);
        return getUserByID(user);
    }

    public Usuarios getUserByRequest(HttpServletRequest req) {
        return getUserByCookie(req.getCookies());
    }

    public Jugadores getJugadorByUsuario(Usuarios user) {
        Jugadores jug;
        List<Jugadores> jugs = em.createQuery("select j FROM Jugadores j where j.type = 0 AND j.usuario = :user")
                .setParameter("user", user)
                .getResultList();
        if (!jugs.isEmpty()) {
            jug = jugs.get(0);
        } else {
            // No existe un Jugador para ese ID, entonces hay que hacerlo...
            em.getTransaction().begin();
            jug = new Jugadores();
            jug.setType(0);
            jug.setUsuario(user);
            jug.setEquipo(null);
            jug.setGanador(null);
            jug.setJugadorpc(null);
            em.persist(jug);
            em.getTransaction().commit();
        }
        return jug;
    }
}
