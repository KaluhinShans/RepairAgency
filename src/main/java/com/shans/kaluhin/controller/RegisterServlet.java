package com.shans.kaluhin.controller;

import com.shans.kaluhin.entity.enums.Locales;
import com.shans.kaluhin.entity.enums.Role;
import com.shans.kaluhin.entity.User;
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
        RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/register.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!LoginServlet.isCaptchaFill(req.getParameter("g-recaptcha-response"))){
            req.setAttribute("error", "captchaError");
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
            return;
        }

      String email = req.getParameter("email");

        if (!UserService.isUserExist(email)) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(req.getParameter("password"));
            user.setName(req.getParameter("name"));
            user.setLastName(req.getParameter("lastName"));
            user.getRoles().add(Role.USER);
            user.setLocale(Locales.valueOf((String) req.getSession().getAttribute("lang")));
            UserService.insertUser(user);

            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            req.setAttribute("email", user.getEmail());

            req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", UserService.error);
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
        }
    }
}