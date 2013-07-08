// Tomado de: http://stackoverflow.com/tags/servlet-filters/info

import cus.Autenticar.Autenticar;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.CookieReader;

//@WebFilter(urlPatterns = {"/login.xhtml", "/register.xhtml"})
public class NoReloginFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
        // If you have any <init-param> in web.xml, then you could get them
        // here by config.getInitParameter("name") and assign it as field.
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        Cookie[] cookie = request.getCookies();

        Autenticar autenticador = new Autenticar();
        String user = CookieReader.getCookieValue(cookie, "user", null);
        String CKtoken = CookieReader.getCookieValue(cookie, "token", null);

        if (cookie == null || user == null || CKtoken == null) {
            chain.doFilter(req, res);
        } else {
            // Ahora debemos verificar que la sesión y el usuario sean correctos:
            byte[] token = CKtoken.getBytes();
            boolean valido = autenticador.ValidateUser(user, token);
            if (valido) {
                response.sendRedirect(request.getContextPath() + "/index.xhtml");
            } else {
                chain.doFilter(req, res); // Logged-in user found, so just continue request.
            }// fin else de ¿sesión válida?

        }// fin else de ¿hay sesión?
    }

    @Override
    public void destroy() {
        // If you have assigned any expensive resources as field of
        // this Filter class, then you could clean/close them here.
    }
}