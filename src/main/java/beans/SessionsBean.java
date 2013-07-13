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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.Base64Conversions;
import utils.CookieReader;

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

                    String token = Base64Conversions.byteToBase64(aut.GenerateSessionHash(idAsignado.toString()));
                    ec.addResponseCookie("user", idAsignado.toString(), null);
                    ec.addResponseCookie("token", token, null);

                    ec.redirect("/app/index.xhtml");
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
        System.out.println("DO LOGOUT");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        Cookie[] cookies = request.getCookies();
        try {
            // Borro todas las cookies:
            for(Cookie c : cookies){
                c.setMaxAge(0);
                c.setValue("");
                c.setPath("/");
                response.addCookie(c);
            }

            // Se redirige a la página de login
            response.sendRedirect("/login.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(SessionsBean.class.getName()).log(Level.SEVERE, "Imposible redirigirse a /login.xhtml", ex);
        }
        return null;
    }

    public boolean isAutenticated(HttpServletRequest req) {
        HttpServletRequest request = (HttpServletRequest) req;
        Cookie[] cookie = request.getCookies();
        Autenticar autenticador = new Autenticar();
        String user = CookieReader.getCookieValue(cookie, "user", null);
        String CKtoken = CookieReader.getCookieValue(cookie, "token", null);
        if (cookie == null || user == null || CKtoken == null) {
            return false; // No logged-in user found, so redirect to login page.
        } else {
            // Ahora debemos verificar que la sesión y el usuario sean correctos:
            byte[] token;
            try {
                token = Base64Conversions.base64ToByte(CKtoken);
                boolean valido = autenticador.ValidateUser(user, token);
                return valido;
            } catch (IOException ex) {
                Logger.getLogger(SessionsBean.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

        }// fin else de ¿sesión válida?
    }

    /**
     * Creates a new instance of SessionsBean
     */
    public SessionsBean() {
    }
}
