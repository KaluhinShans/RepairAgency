package com.shans.kaluhin.controller.authorization;

import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Activate")
public class ActivationEmailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserService();
        boolean isActivated = userService.activateEmail(req.getParameter("code"));

        if(isActivated){
            req.setAttribute("message", "emailActivateSuccess");
        }else {
            req.setAttribute("error", "emailActivateError");
        }

        User user = (User) req.getSession().getAttribute("user");

        if(user == null){
            req.getRequestDispatcher("/WEB-INF/jsp/authorization/login.jsp").forward(req, resp);
        }else{
            user.setActivationCode(null);
            req.getRequestDispatcher("/WEB-INF/jsp/authorization/profile.jsp").forward(req, resp);
        }
    }
}