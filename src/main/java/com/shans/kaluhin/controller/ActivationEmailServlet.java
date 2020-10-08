package com.shans.kaluhin.controller;

import com.shans.kaluhin.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "Activate")
public class ActivationEmailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        boolean isActivated = UserService.activateEmail(req.getParameter("code"));

        if(isActivated){
            req.setAttribute("message", "emailActivateSuccess");
        }else {
            req.setAttribute("error", "emailActivateError");
        }


        if(req.getSession().getAttribute("user") == null){
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        }else{
            req.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(req, resp);
        }
    }
}