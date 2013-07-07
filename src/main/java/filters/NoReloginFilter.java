/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import cus.Autenticar.Autenticar;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Esteban
 */
//@WebFilter(filterName = "NoReloginFilter", urlPatterns = {"/start.jsp"}, servletNames = {"RegisterServlet", "LoginServlet"})
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
        HttpSession session = request.getSession(true);

        if (session == null || session.getAttribute("user") == null || session.getAttribute("token") == null) {
            // No hay sesión.. se puede seguir
            chain.doFilter(req, res);
        } else {
            String reintentar = (String) request.getParameter("retry");
            if ("true".equalsIgnoreCase(reintentar)) {
                chain.doFilter(req, res);
            } else {
                // hay sesión abierta... vaya a la app
                response.sendRedirect(request.getContextPath());
            }

        }// fin else de ¿hay sesión?
    }

    @Override
    public void destroy() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
