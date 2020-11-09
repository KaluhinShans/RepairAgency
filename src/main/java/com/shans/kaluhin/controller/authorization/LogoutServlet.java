package com.shans.kaluhin.controller.authorization;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name="Logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        HttpSession session = req.getSession(false);

        if(session != null)
        {
            Cookie cookie = new Cookie("agency.user_id", "");
            cookie.setMaxAge(0);
            resp.addCookie(cookie);
            session.invalidate();
            req.setAttribute("errMessage", "You have logged out successfully");
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }
}
