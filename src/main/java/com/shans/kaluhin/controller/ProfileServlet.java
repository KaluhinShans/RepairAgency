package com.shans.kaluhin.controller;

import com.shans.kaluhin.entity.Billing;
import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.service.BillingService;
import com.shans.kaluhin.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Profile")
@MultipartConfig
public class ProfileServlet extends HttpServlet {
    private int totalTransactions = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.equals("/profile/edit")) {
            RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/authorization/profileEdit.jsp");
            view.forward(req, resp);
            return;
        }
        pagination(req);
        RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/authorization/profile.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        UserService userService = new UserService();
        if(req.getRequestURI().equals("/profile/edit/password")){
         String oldPassword = req.getParameter("old");
         String newPassword = req.getParameter("new");
         String repeatPassword = req.getParameter("repeat");

         if(userService.changePassword(user, oldPassword, newPassword, repeatPassword)){
             resp.sendRedirect("/profile");
         }else{
             req.setAttribute("error", userService.error);
             req.setAttribute("show", "show");
             req.getRequestDispatcher("/WEB-INF/jsp/authorization/profileEdit.jsp").forward(req, resp);
             return;
         }
        }

        Part photo = req.getPart("photo");
        String email = req.getParameter("email");
        String name = req.getParameter("name");
        String lastName = req.getParameter("lastName");

        if(userService.editProfile(user, email, name, lastName, photo)){
            resp.sendRedirect(req.getContextPath() + "/profile");
        }else{
            req.setAttribute("error", userService.error);
            doGet(req, resp);
        }
    }

    private void pagination(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute("user");
        String spage = req.getParameter("page");
        int page = 1;

        if (spage != null) {
            page = Integer.parseInt(spage);
        }

        int startPosition = page * totalTransactions - totalTransactions;

        BillingService billingService = new BillingService();

        List<Billing> transactions = billingService.getUserBillings(user.getId(), startPosition, totalTransactions);

        int nOfPages = billingService.getNumberOfRows() / totalTransactions;

        if (nOfPages % totalTransactions > 0) {
            nOfPages++;
        }

        req.setAttribute("transactions", transactions);
        req.setAttribute("page", page);
        req.setAttribute("nOfPages", nOfPages);
    }

}
