package com.shans.kaluhin.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})
public class SecurityFilter implements Filter {
    private static final Logger log = Logger.getLogger(SecurityFilter.class);

    private final List<String> START_PAGES = Arrays.asList("/", "/login", "/register", "/images", "/activate");

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getServletPath();

        if (req.getSession().getAttribute("user") == null) {
            if (!START_PAGES.contains(uri)) {
                log.info(uri + ": forbidden");
                resp.sendRedirect("/login");
                return;
            }
            log.info(uri + ": access");
        }
        chain.doFilter(request, response);
    }

    public void destroy() {
    }

    public void init(FilterConfig arg0) {
    }
}