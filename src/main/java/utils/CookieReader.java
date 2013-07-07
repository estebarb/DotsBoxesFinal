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
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookieName.equals(cookie.getName())) {
                return (cookie.getValue());
            }
        }
        return (defaultValue);
    }
}
