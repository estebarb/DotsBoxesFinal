/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import cus.Jugar.IJugador;
import cus.Jugar.JugadorHumano;
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

    private int columnas = 6;
    private int filas = 6;
    private ArrayList<IJugador> jugadores;
    private String IdentificadorJugador;

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public ArrayList<IJugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(ArrayList<IJugador> jugadores) {
        this.jugadores = jugadores;
    }

    public String getIdentificadorJugador() {
        return IdentificadorJugador;
    }

    public void setIdentificadorJugador(String IdentificadorJugador) {
        this.IdentificadorJugador = IdentificadorJugador;
    }

    public String AddUsuario(String email) {
        IJugador usuario = new JugadorHumano();
        if (usuario.fromString(email)) {
            jugadores.add(usuario);
            IdentificadorJugador = "";
        }
        return null;
    }

    public String AddJugadorIA(String nombre) {
        return null;
    }

    public String AddEquipo(String equipoID) {
        return null;
    }

    public String AddGanador(String idJuego) {
        return null;
    }

    /**
     * Creates a new instance of NuevoJuegoBean
     */
    public NuevoJuegoBean() {
    }
}
