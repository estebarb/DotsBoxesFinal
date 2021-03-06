/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;


import cus.Jugar.*;
import entities.Juegos;
import entities.Usuarios;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import modelo.MJuego;
import modelo.MUsuarios;
import utils.EMF;

/**
 *
 * @author esteban
 */
@ManagedBean
@ViewScoped
public class JuegoBean {

    private int filas = 4;
    private int columnas = 4;
    private byte[] data = new byte[filas * columnas * 5];
    private List<IJugador> jugadores = new ArrayList<>();
    private int TurnoActual;
    private String movida;
    private IJugador JugadorActual;
    private Usuarios usuario;
    private Juegos juego;
    private final EntityManager em = EMF.createEntityManager();

    public String getSizeData() {
        return columnas + "," + filas;
    }

    public String getLineData() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        sb.append("[");
        for (int i = 0; i < data.length; i = i + 5) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            sb.append("[");
            sb.append(data[i]).append(",");
            sb.append(data[i + 1]).append(",");
            sb.append(data[i + 2]).append(",");
            sb.append(data[i + 3]);
            sb.append("]");
        }
        sb.append("]");
        return sb.toString();
    }

    public String getBoxData() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < data.length; i = i + 5) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            sb.append(data[i + 4]);
        }
        return sb.toString();
    }

    public boolean getIsTurno() {
        //System.out.println("Es el turno de: " + JugadorActual.getNombre());
        //System.out.println("    " + JugadorActual.getSTipo());
        try {
            if (JugadorActual.getTipo().compareTo(EPlayerTypes.Human) == 0) {
                return !juego.getIsterminado() && JugadorActual.getJUGADOR().getUsuario().getEmail().equals(usuario.getEmail());
                //return JugadorActual.getJUGADOR().getUsuario().getId() == usuario.getId();
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.toString());
            //e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a new instance of JuegoBean
     */
    public JuegoBean() {
        MUsuarios mUser = new MUsuarios();
        usuario = mUser
                .getUserByRequest((HttpServletRequest) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getRequest());
        init();
    }

    @PostConstruct
    public void init() {
        MJuego mjuego = new MJuego();
        juego = (Juegos) FacesContext.getCurrentInstance().getExternalContext()
                .getRequestMap().get("juego");

        InitFromJuego(juego);
    }

    private void InitFromJuego(Juegos juego) {
        MJuego mjuego = new MJuego();
        if (juego != null) {
            filas = juego.getFilas();
            columnas = juego.getColumnas();
            data = juego.getTablero();
            jugadores = mjuego.getJugadores(juego);
            TurnoActual = juego.getTurnoactual();
            JugadorActual = jugadores.get(TurnoActual % jugadores.size());
            movida = "";
        }
    }

    /**
     * Realiza una movida desde el canvas
     *
     * @return
     */
    public String RealizarMovida() {
        MJuego mjuego = new MJuego();
        // movida es una número generado así:
        // movida = BoxNumber*4 + lineNumber
        // con line number = 0,1,2,3 según sea norte, este, sur, oeste
        try {
            if (getIsTurno()) {
                //System.out.println("hubo movida: " + movida);
                int mov = Integer.parseInt(movida);
                int BoxNumber = mov / 4;
                int LineNumber = mov % 4;
                //System.out.println("mjuego.Jugar");
                Juegos juegoNuevo = mjuego.Jugar(juego, BoxNumber, LineNumber);
                //System.out.println("InitFromJuego");
                InitFromJuego(juegoNuevo);
                UpdateData();
            }
        } catch (Exception e) {
            System.err.println("error en realizar movida");
            e.printStackTrace();
        } finally {
            return null;
        }
    }

    public boolean getIsTerminado() {
        if (juego == null) {
            return false;
        }
        return juego.getIsterminado();
    }

    public String UpdateData() {
        MJuego mjuego = new MJuego();
        List<Juegos> listaJuegos = em.createQuery("SELECT j FROM Juegos j WHERE j.id = :id")
                .setParameter("id", juego.getId())
                .getResultList();
        if (listaJuegos != null && listaJuegos.size() > 0) {
            this.juego = listaJuegos.get(0);
        }
        if (!getIsTurno()) {
            em.refresh(juego);

            InitFromJuego(juego);            
        }
        return null;
    }
    
    public String ForzarJugada(){
        IMachineBot bot = new InteligenteBot();
        bot.Jugar(juego);
        return null;
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

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }
}
