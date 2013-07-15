/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

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
import utils.CookieReader;

/**
 *
 * @author esteban
 */
@WebFilter(filterName = "UserInSession", urlPatterns = {"/*"})
public class UserInSession implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        Cookie[] cookie = request.getCookies();
        String user = CookieReader.getCookieValue(cookie, "user", null);
        String CKtoken = CookieReader.getCookieValue(cookie, "token", null);
        try {
            request.getSession(true).setAttribute("user", user);
            request.getSession(true).setAttribute("token", CKtoken);
        } finally {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
    }
}
