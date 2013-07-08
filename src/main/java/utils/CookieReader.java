/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javax.servlet.http.Cookie;

/**
 *
 * @author Esteban
 */
public class CookieReader {

    /**
     * Lee un valor de una cookie Tomado de:
     * http://www.apl.jhu.edu/~hall/java/Servlet-Tutorial/Servlet-Tutorial-Cookies.html
     *
     * @param cookies
     * @param cookieName
     * @param defaultValue
     * @return
     */
    public static String getCookieValue(Cookie[] cookies,
            String cookieName,
            String defaultValue) {
        if (cookies != null) {
            for(Cookie c : cookies){
                if (cookieName.equals(c.getName())) {
                    return (c.getValue());
                }
            }
        }
        return (defaultValue);
    }
}
