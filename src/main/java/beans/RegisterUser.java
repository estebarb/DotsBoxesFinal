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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import modelo.MUsuarios;
import static utils.EmailValidator.ValidateEMail;

/**
 *
 * @author Esteban
 */
@ManagedBean(name = "RegisterBean")
@RequestScoped
public class RegisterUser {

    private String Nombre;
    private String Email;
    private String Password1;
    private String Password2;
    private final int minimalPasswordLenght = 5;

    public int getMinimalPasswordLenght() {
        return minimalPasswordLenght;
    }

    /**
     * Creates a new instance of RegisterUser
     */
    public RegisterUser() {
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword1() {
        return Password1;
    }

    public void setPassword1(String Password1) {
        this.Password1 = Password1;
    }

    public String getPassword2() {
        return Password2;
    }

    public void setPassword2(String Password2) {
        this.Password2 = Password2;
    }

    public String Register() {
        Autenticar aut = new Autenticar();
        try {
            if (getPassword1().equals(getPassword2())) {
                boolean exitoso = aut.createUser(getEmail(), getNombre(), getPassword1());
                if (exitoso) {
                    // Se redirige a la página principal
                    SessionsBean login = new SessionsBean();
                    login.setEmail(getEmail());
                    login.setPassword(getPassword1());
                    login.DoLogin();
                    return null;
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Las contraseñas no coinciden."));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RegisterUser.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("¡Uups! Al parecer estamos en mantenimiento"));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RegisterUser.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("¡Uups! Al parecer tantas partidas nos han dejado cansados. Necesitamos mantenimiento urgente."));
        }
        return null;
    }

    public void checkEmail(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String email = (String) value;

        // Correo electrónico válido?
        if (!ValidateEMail(email)) {
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary("Correo electrónico inválido");
            message.setDetail("El correo electrónico no es válido. Por favor verifique que la dirección brindada sea correcta.");
            context.addMessage("regForm:reg-email", message);
            throw new ValidatorException(message);
        }

        // Correo electrónico repetido?
        MUsuarios mUsers = new MUsuarios();
        if (null != mUsers.getUserByEmail(email)) {
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary("Ya está registrado");
            message.setDetail("Ya hay un usuario registrado con dicho email.");
            context.addMessage("regForm:reg-email", message);
            throw new ValidatorException(message);
        }
    }
}
