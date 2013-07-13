package filters;

// Tomado de: http://stackoverflow.com/tags/servlet-filters/info

import beans.SessionsBean;
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

        if(request.getRequestURI().toString().contains("/public/")){
            chain.doFilter(req, res);
            return;
        }
        
        if (!request.getRequestURI().toString().contains("/faces/")) {
            boolean isNotProtected = false;
            isNotProtected = isNotProtected || request.getRequestURI().toString().contains("/login.xhtml");
            isNotProtected = isNotProtected || request.getRequestURI().toString().contains("/register.xhtml");
            isNotProtected = isNotProtected || request.getRequestURI().toString().contains("/bsod.jsp");

            SessionsBean sb = new SessionsBean();
            boolean isAutenticado = sb.isAutenticated(request);

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
    }

    @Override
    public void destroy() {
        // If you have assigned any expensive resources as field of
        // this Filter class, then you could clean/close them here.
    }
}