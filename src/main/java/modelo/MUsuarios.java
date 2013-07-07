/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.List;
import javax.persistence.EntityManager;
import entities.Usuarios;
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
    
    public Usuarios getUserByEmail(String email){
        List<Usuarios> users = em.createNamedQuery("Usuarios.findByEmail")
                .setParameter("email", email)
                .getResultList();
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }
    
}
