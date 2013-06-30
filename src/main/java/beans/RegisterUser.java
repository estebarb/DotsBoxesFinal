/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

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
        System.out.println(">>> SUBMITTED <<<");
        System.out.println("Mail " + getEmail());
        System.out.println("Nombre " + getNombre());
        System.out.println("Pass " + getPassword1());
        System.out.println("_________________");
        return null;
    }
}
