package com.shans.kaluhin.filter;

import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.entity.enums.Locales;
import com.shans.kaluhin.service.UserService;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "LocaleFilter", urlPatterns = {"/*"})
public class LocaleFilter implements Filter {
    private static final Logger log = Logger.getLogger(LocaleFilter.class);


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        User user = (User) req.getSession().getAttribute("user");
        String locale = req.getParameter("lang");

        if (locale != null) {
            if(Locales.contains(req.getParameter("lang"))) {
                req.getSession().setAttribute("lang", req.getParameter("lang"));
                if (user != null){
                    log.info("save user language");
                    UserService userService = new UserService();
                    userService.setLocale(user, Locales.valueOf(locale));
                }
            }
        } else if (user != null) {
            req.getSession().setAttribute("lang", user.getLocale());
        } else if (req.getSession().getAttribute("lang") == null) {
            req.getSession().setAttribute("lang", Locales.En.name());
        }
        chain.doFilter(request, response);
    }

    public void destroy() {
    }

    public void init(FilterConfig arg0) {
    }
}


