/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import cus.Jugar.JugadorHumano;
import entities.Juegos;
import entities.Jugadores;
import entities.Jugadoresjuego;
import entities.Pendientes;
import entities.Usuarios;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import modelo.MUsuarios;
import utils.EMF;

/**
 *
 * @author esteban
 */
@ManagedBean
@ViewScoped
public class ReanudarJuegoBean {
    private Usuarios usuarioActual;
    private Jugadores jugadorActual;

    /**
     * Creates a new instance of ReanudarJuegoBean
     */
    public ReanudarJuegoBean() {
        MUsuarios mUser = new MUsuarios();
        usuarioActual = mUser
                .getUserByRequest((HttpServletRequest) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getRequest());        
        jugadorActual = mUser.getJugadorByUsuario(usuarioActual);
        JugadorHumano jh = new JugadorHumano();
        jh.fromJugadoresEntity(jugadorActual);
    }
    
    public List<Juegos> getJuegosPendientes(){
        List<Juegos> pendientes;
        EntityManager em = EMF.createEntityManager();
        System.out.println("JugadorActualID=" + jugadorActual.getId());
        pendientes = em.createQuery("select j.juego FROM Jugadoresjuego j WHERE j.jugador = :jugador and j.juego.isterminado <> TRUE")
                .setParameter("jugador", jugadorActual)
                .getResultList();
        return pendientes;
    }
    
    public String ReLoadGame(Juegos j){
        System.out.println("reloadgame " + j==null);
        FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestMap().put("juego", j);

            return "/app/play";
    }
    
    public String Borrar(Juegos j){
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();
        for(Jugadoresjuego jug : j.getJugadoresjuegoCollection()){
            em.merge(jug);
            em.remove(jug);
        }
        for(Pendientes pend : j.getPendientesCollection()){
            em.merge(pend);
            em.remove(pend);
        }
        em.merge(j);
        em.remove(j);
        em.getTransaction().commit();
        return null;
    }
}
