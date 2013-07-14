package modelo;

import cus.Autenticar.Autenticar;
import entities.Usuarios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import utils.EMF;

/**
 * Tiene funcinoes variadas para encontrar todo tipo de
 * jugadores
 * @author esteban
 */
public class BuscaJugadores {
    
    @PersistenceContext(unitName = "DotsBoxesPU")
    private EntityManager em;

    public BuscaJugadores() {
        em = EMF.createEntityManager();
    }

    public Long getUserFromMail(String email) {
        List<Usuarios> users = em.createNamedQuery("Usuarios.findByEmail").setParameter("email", email).getResultList();
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0).getId();
        }
    }
    
}
