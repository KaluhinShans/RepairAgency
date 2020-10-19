package com.shans.kaluhin;

import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.service.UserService;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "Main")
public class MainServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(UserService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getSession().getAttribute("user") == null) {
            int id = isUserRemembered(req);
            if (id != -1) {
                HttpSession session = req.getSession();
                UserService userService = new UserService();
                User user = userService.getUserByID(id);
                session.setAttribute("user", user);
                log.info("User logged by remember me");
                resp.sendRedirect(req.getContextPath() + req.getRequestURI());
                return;
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
}
