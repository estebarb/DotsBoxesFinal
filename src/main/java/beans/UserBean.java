/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.Usuarios;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import modelo.MUsuarios;

/**
 *
 * @author Esteban
 */
@ManagedBean
@RequestScoped
public class UserBean {

    Usuarios UsuarioActual;

    public void init() {
        MUsuarios mUser = new MUsuarios();
        UsuarioActual = mUser
                .getUserByRequest((HttpServletRequest) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getRequest());
    }

    public Usuarios getUsuarioActual() {
        if (UsuarioActual == null) {
            init();
        }
        return UsuarioActual;
    }

    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
    }
}
