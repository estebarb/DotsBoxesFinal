/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cus.Jugar;

import beans.UserBean;
import cus.Autenticar.Autenticar;
import entities.Juegos;
import entities.Usuarios;
import modelo.MUsuarios;

/**
 *
 * @author Esteban
 */
public class JugadorHumano extends IJugador{
    
    private Usuarios user;
    
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
        MUsuarios muser = new MUsuarios();
        return muser.getJugadorByUsuario(user).getId();
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
        return user != null;
    }
    
}
