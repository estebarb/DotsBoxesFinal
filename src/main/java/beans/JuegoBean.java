/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import cus.Jugar.IJugador;
import entities.Juegos;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.MJuego;

/**
 *
 * @author esteban
 */
@ManagedBean
@ViewScoped
public class JuegoBean {
    
    private int filas;
    private int columnas;
    private byte[] data;
    private List<IJugador> jugadores = new ArrayList<>();
    private int TurnoActual;
    private String movida;
    private IJugador JugadorActual;
    
    private Juegos juego;

    /**
     * Creates a new instance of JuegoBean
     */
    public JuegoBean() {
    }
    
    @PostConstruct
    public void init() {
        MJuego mjuego = new MJuego();
        juego = (Juegos) FacesContext.getCurrentInstance().getExternalContext()
            .getRequestMap().get("juego");
        if(juego != null){
            filas = juego.getFilas();
            columnas = juego.getColumnas();
            data = juego.getTablero();
            jugadores = mjuego.getJugadores(juego);
            TurnoActual = juego.getTurnoactual();
            movida = "";
        }
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public List<IJugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<IJugador> jugadores) {
        this.jugadores = jugadores;
    }

    public int getTurnoActual() {
        return TurnoActual;
    }

    public void setTurnoActual(int TurnoActual) {
        this.TurnoActual = TurnoActual;
    }

    public String getMovida() {
        return movida;
    }

    public void setMovida(String movida) {
        this.movida = movida;
    }

    public Juegos getJuego() {
        return juego;
    }

    public void setJuego(Juegos juego) {
        this.juego = juego;
    }

    public IJugador getJugadorActual() {
        return JugadorActual;
    }

    public void setJugadorActual(IJugador JugadorActual) {
        this.JugadorActual = JugadorActual;
    }
    
    
}
