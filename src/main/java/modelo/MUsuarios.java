/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

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
        List<Usuarios> users = em.createNamedQuery("Usuarios.findById")
                .setParameter("id", Long.parseLong(id))
                .getResultList();
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }

    public Usuarios getUserByCookie(Cookie[] c) {
        String user = CookieReader.getCookieValue(c, "user", null);
        return getUserByID(user);
    }

    public Usuarios getUserByRequest(HttpServletRequest req) {
        return getUserByCookie(req.getCookies());
    }
}
