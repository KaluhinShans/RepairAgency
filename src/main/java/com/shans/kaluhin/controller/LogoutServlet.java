package com.shans.kaluhin.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name="Logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if(session != null)
        {
            Cookie cookie = new Cookie("agency.user_id", "");
            cookie.setMaxAge(0);
            resp.addCookie(cookie);
            session.invalidate();
            req.setAttribute("errMessage", "You have logged out successfully");
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
            requestDispatcher.forward(req, resp);
        }
    }
}
