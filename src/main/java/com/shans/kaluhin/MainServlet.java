package com.shans.kaluhin;

import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "Main")
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            int id = isUserRemembered(req);
            if (id != -1) {
                HttpSession session = req.getSession();
                User user = getRememberedUser(id);
                session.setAttribute("user", user);
            }
        }
        RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/index.jsp");
        view.forward(req, resp);
    }

    private int isUserRemembered(HttpServletRequest req) {
        if (req.getCookies() == null) {
            return -1;
        }
        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals("agency.user_id")) {
                return Integer.parseInt(cookie.getValue());
            }
        }
        return -1;
    }

    private User getRememberedUser(int id) {
        return UserService.getUserByID(id);
    }
}
