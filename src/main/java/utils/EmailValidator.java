/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.regex.Pattern;

/**
 *
 * @author Esteban
 */
public class EmailValidator{

    private static final Pattern rfc2822 = Pattern.compile(
            "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");

    /**
     * Valida si una dirección de correo electrónico es correcta
     *
     * @param email
     * @return
     */
    public static boolean ValidateEMail(String email) {
        return rfc2822.matcher(email).matches();
    }
}
