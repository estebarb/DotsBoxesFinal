/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import cus.Jugar.IJugador;
import cus.Jugar.JugadorBot;
import cus.Jugar.JugadorHumano;
import entities.Juegos;
import entities.Jugadores;
import entities.Usuarios;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import modelo.MJuego;
import modelo.MUsuarios;

/**
 *
 * @author Esteban
 */
@ManagedBean
@ViewScoped
public class NuevoJuegoBean {

    private Usuarios usuarioActual;
    private int columnas = 6;
    private int filas = 6;
    private ArrayList<IJugador> jugadores = new ArrayList<>();
    private String IdentificadorJugador;
    private String IdentificadorMaquina;

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

    public String getIdentificadorMaquina() {
        System.out.println(IdentificadorMaquina);
        return IdentificadorMaquina;
    }

    public void setIdentificadorMaquina(String IdentificadorMaquina) {
        this.IdentificadorMaquina = IdentificadorMaquina;
    }

    public String AddUsuario() {
        IJugador usuario = new JugadorHumano();
        String email = getIdentificadorJugador();
        setIdentificadorJugador("");
        if (usuario.fromString(email)) {
            // Hay que verificar que el usuario no haya sido agregado anteriormente.
            for (IJugador j : jugadores) {
                if (j.getId() == usuario.getId()) {
                    FacesContext.getCurrentInstance().addMessage("addUserMail", new FacesMessage("El usuario ya fue agregado"));
                    return null;
                }
            }
            jugadores.add(usuario);
        } else {
            FacesContext.getCurrentInstance().addMessage("addUserMail", new FacesMessage("El usuario no existe"));
        }
        return null;
    }

    public String AddJugadorIA() {
        IJugador maquina = new JugadorBot();
        String nivel = getIdentificadorMaquina();
        setIdentificadorMaquina("");
        if (maquina.fromString(nivel)) {
            jugadores.add(maquina);
        } else {
            FacesContext.getCurrentInstance().addMessage("addUserMachine", new FacesMessage("Máquina inválida"));
        }
        return null;
    }

    public String AddEquipo(String equipoID) {
        return null;
    }

    public String AddGanador(String idJuego) {
        return null;
    }

    public String OrdenSubir(IJugador j) {
        int a = jugadores.indexOf(j);
        jugadores.remove(j);
        jugadores.add(Math.max(0, a - 1), j);
        return null;
    }

    public String OrdenBajar(IJugador j) {
        int a = jugadores.indexOf(j);
        jugadores.remove(j);
        jugadores.add(Math.min(jugadores.size(), a + 1), j);
        return null;
    }

    public String OrdenBorrar(IJugador j) {
        jugadores.remove(j);
        return null;
    }

    /**
     * Creates a new instance of NuevoJuegoBean
     */
    public NuevoJuegoBean() {
        MUsuarios mUser = new MUsuarios();
        usuarioActual = mUser
                .getUserByRequest((HttpServletRequest) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getRequest());
        Jugadores jugadorActual;
        jugadorActual = mUser.getJugadorByUsuario(usuarioActual);
        JugadorHumano jh = new JugadorHumano();
        jh.fromJugadoresEntity(jugadorActual);
        jugadores.add(jh);
    }

    /**
     * Se encarga de crear el juego en la base de datos...
     *
     * @return
     */
    public String CrearJuego() {
        if (jugadores.size() < 2) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Debe haber al menos 2 jugadores."));
            return null;
        }
        MJuego mjuego = new MJuego();
        Juegos nuevoJuego = mjuego.CrearJuego(filas, columnas, new Date(), null, jugadores);
        if (null != nuevoJuego) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al crear el juego"));
            return null;
        } else {

            FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestMap().put("juego", nuevoJuego);

            return "/app/play";
        }
    }// fin crearJuego
}
