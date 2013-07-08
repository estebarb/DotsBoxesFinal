/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.Jugadores;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Esteban
 */
@ManagedBean
@RequestScoped
public class NuevoJuegoBean {
    
    int columnas = 6;
    int filas = 6;
    ArrayList<Jugadores> jugadores;
    
    public void AddJugador

    /**
     * Creates a new instance of NuevoJuegoBean
     */
    public NuevoJuegoBean() {
    }
}
