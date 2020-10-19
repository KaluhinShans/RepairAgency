package com.shans.kaluhin.controller.authorization;

import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.entity.enums.Locales;
import com.shans.kaluhin.entity.enums.Role;
import com.shans.kaluhin.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "Register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/authorization/register.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!LoginServlet.isCaptchaFill(req.getParameter("g-recaptcha-response"))) {
            req.setAttribute("error", "captchaError");
            doGet(req, resp);
            return;
        }

        String email = req.getParameter("email");
        UserService userService = new UserService();
        if (!userService.isUserExist(email)) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(req.getParameter("password"));
            user.setName(req.getParameter("name"));
            user.setLastName(req.getParameter("lastName"));
            user.setLocale(Locales.valueOf((String) req.getSession().getAttribute("lang")));
            userService.save(user);

            HttpSession session = req.getSession();
            session.setAttribute("user", user);

            resp.sendRedirect(req.getContextPath() + "/");
        } else {
            req.setAttribute("error", userService.error);
            doGet(req, resp);
        }
    }
}