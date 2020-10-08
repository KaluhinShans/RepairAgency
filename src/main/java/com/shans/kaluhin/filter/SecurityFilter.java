package com.shans.kaluhin.filter;

import com.shans.kaluhin.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})
public class SecurityFilter implements Filter {
    List<String> startPages = Arrays.asList("/", "/login", "/register", "/images", "/activate");

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        User user = (User) req.getSession().getAttribute("user");
        String uri = req.getServletPath();

        if (user == null) {
            if (!startPages.contains(uri)) {
                System.out.println(uri + ": user not login");
                resp.sendRedirect("/login");
                return;
            }
            System.out.println(uri + ": access");
        }

        if(user != null) {
            switch (uri) {
                case "/users":
                    if (!user.isManager()) {
                        resp.sendRedirect("/");
                        return;
                    }
                case "/work":
                    if (!user.isMaster()) {
                        resp.sendRedirect("/");
                        return;
                    }
                case "/login":
                    resp.sendRedirect("/");
                    System.out.println("you already login");
                    return;

                case "/register":
                    resp.sendRedirect("/");
                    System.out.println("you already login");
                    return;
            }
        }
        chain.doFilter(request, response);
    }

    public void destroy() {
    }

    public void init(FilterConfig arg0) throws ServletException {
    }
}