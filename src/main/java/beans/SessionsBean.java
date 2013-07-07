/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import cus.Autenticar.Autenticar;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Esteban
 */
@ManagedBean
@RequestScoped
public class SessionsBean {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String DoLogin() {
        final Autenticar aut = new Autenticar();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            try {
                // Se puede proceder a crear el usuario
                Long idAsignado = aut.AutenticarUsuario(email, password);
                if (idAsignado != null) {
                    // Inicializa la sesión


                    ec.addResponseCookie("user", idAsignado.toString(), null);
                    ec.addResponseCookie("token", aut.GenerateSessionHash(idAsignado.toString()).toString(), null);

                    ec.redirect("/index.xhtml");
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("La contraseña dada no es válida o el usuario no existe"));

                }
            } catch (SQLException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Problemas SQL al registrarse", ex);
                ec.redirect("/bsod.jsp");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Al parecer no tenemos SHA-1", ex);
                ec.redirect("/bsod.jsp");
            }
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Imposible redirigirse a /index.xhtml", ex);
        }


        return null;
    }

    public String DoLogout() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            // Podría borrar las cookies, pero estas también servirán...
            ec.addResponseCookie("user", "", null);
            ec.addResponseCookie("token", "", null);
            
            // Se redirige a la página de login
            ec.redirect("/login.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(SessionsBean.class.getName()).log(Level.SEVERE, "Imposible redirigirse a /index.xhtml", ex);
        }
        return null;
    }

    /**
     * Creates a new instance of SessionsBean
     */
    public SessionsBean() {
    }
}
