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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//@WebFilter(urlPatterns = {"/api/*", "/", "/index.jsp"})
public class LoginFilter implements Filter {

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
        
        Autenticar autenticador = new Autenticar();

        if (session == null || session.getAttribute("user") == null || session.getAttribute("token") == null) {
            response.sendRedirect(request.getContextPath() + "/start.jsp"); // No logged-in user found, so redirect to login page.
        } else {
            // Ahora debemos verificar que la sesión y el usuario sean correctos:
            String user = (String) session.getAttribute("user");
            String sessionID = (String) session.getId();
            //System.out.println(user + " " + sessionID + " " + session.getAttribute("token").getClass().getCanonicalName());
            byte[] token = (byte[]) session.getAttribute("token");
            boolean valido = autenticador.ValidateUser(user, sessionID, token);
            if (valido) {
                chain.doFilter(req, res); // Logged-in user found, so just continue request.
            } else {
                // ¡¡¡No es válido!!!
                // Alguien nos está engañando (o reiniciamos el servlet)
                // Es necesario destruir los datos que recibimos...
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/start.jsp?retry=true");
            }// fin else de ¿sesión válida?
            
        }// fin else de ¿hay sesión?
    }

    @Override
    public void destroy() {
        // If you have assigned any expensive resources as field of
        // this Filter class, then you could clean/close them here.
    }
}