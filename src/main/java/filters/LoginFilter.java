// Tomado de: http://stackoverflow.com/tags/servlet-filters/info

import beans.SessionsBean;
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

@WebFilter(urlPatterns = {"/*"})
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
        System.out.println("Filtro login: " + request.getRequestURI().toString());

        if (!request.getRequestURI().toString().contains("/faces/")) {
            boolean isNotProtected = false;
            isNotProtected = isNotProtected || request.getRequestURI().toString().contains("/login.xhtml");
            isNotProtected = isNotProtected || request.getRequestURI().toString().contains("/register.xhtml");
            isNotProtected = isNotProtected || request.getRequestURI().toString().contains("/bsod.jsp");

            SessionsBean sb = new SessionsBean();
            boolean isAutenticado = sb.isAutenticated(request);
            System.out.println("  Autenticado: " + isAutenticado);

            if (isNotProtected) {
                if (isAutenticado) {
                    response.sendRedirect(request.getContextPath() + "/");
                } else {
                    chain.doFilter(req, res);
                }
            } else {
                if (!isAutenticado) {
                    response.sendRedirect(request.getContextPath() + "/login.xhtml");
                } else {
                    chain.doFilter(req, res);
                }
            }
        } else {
            chain.doFilter(req, res);
        }
        /*
         if (cookie == null || user == null || CKtoken == null) {
         response.sendRedirect(request.getContextPath() + "/login.xhtml"); // No logged-in user found, so redirect to login page.
         } else {
         // Ahora debemos verificar que la sesión y el usuario sean correctos:
         byte[] token = CKtoken.getBytes();
         boolean valido = autenticador.ValidateUser(user, token);
         if (valido) {
         chain.doFilter(req, res); // Logged-in user found, so just continue request.
         } else {
         // ¡¡¡No es válido!!!
         // Alguien nos está engañando (o reiniciamos el servlet)
         // Es necesario destruir los datos que recibimos...
         for (Cookie c : cookie) {
         c.setMaxAge(0);
         response.addCookie(c);
         }
         response.sendRedirect(request.getContextPath() + "/login.xhtml");
         }// fin else de ¿sesión válida?

         }// fin else de ¿hay sesión?
         */
    }

    @Override
    public void destroy() {
        // If you have assigned any expensive resources as field of
        // this Filter class, then you could clean/close them here.
    }
}