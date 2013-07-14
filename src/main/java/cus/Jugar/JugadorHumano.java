/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cus.Jugar;

import beans.UserBean;
import cus.Autenticar.Autenticar;
import entities.Juegos;
import entities.Jugadores;
import entities.Usuarios;
import modelo.MUsuarios;

/**
 *
 * @author Esteban
 */
public class JugadorHumano extends IJugador{
    
    private Usuarios user;
    private Jugadores jugador;
    
    public JugadorHumano(){
        this.tipo = EPlayerTypes.Human;
    }

    @Override
    public boolean AddPendiente(Juegos p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getNombre() {
        return user.getNombre();
    }

    @Override
    public int getRanking() {
        return user.getRanking();
    }

    @Override
    public long getId() {
        return jugador.getId();
    }

    @Override
    public EPlayerTypes getTipo() {
        return EPlayerTypes.Human;
    }

    /**
     * Llena los datos del usuario a partir de un correo electrónico
     * @param s
     * @return 
     */
    @Override
    public boolean fromString(String s) {
        MUsuarios muser = new MUsuarios();
        user = muser.getUserByEmail(s);
        jugador = muser.getJugadorByUsuario(user);
        return user != null;
    }

    @Override
    public boolean fromJugadoresEntity(Jugadores j) {
        user = j.getUsuario();
        jugador = j;
        return true;
    }    
}
