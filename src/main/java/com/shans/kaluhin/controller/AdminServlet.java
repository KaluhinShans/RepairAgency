package com.shans.kaluhin.controller;

import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.entity.enums.Role;
import com.shans.kaluhin.service.BillingService;
import com.shans.kaluhin.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminServlet")
public class AdminServlet extends HttpServlet {
    int totalUsers = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (!user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        pagination(req);
        RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/admin.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals("/usersList/replenishUser")) {
            int userId = Integer.parseInt(req.getParameter("userId"));
            int sum = Integer.parseInt(req.getParameter("sum"));

            BillingService billingService = new BillingService();
            UserService userService = new UserService();
            billingService.topUpBalance(userService.getUserByID(userId), sum, "Repair Agency");
            doGet(req, resp);
            return;
        }

        int userId = Integer.parseInt(req.getParameter("id"));
        String hasRole = req.getParameter("hasRole");
        String role = req.getParameter("role");

        UserService userService = new UserService();
        if (hasRole.equals("true")) {
            userService.pickUpRole(userId, Role.valueOf(role));
        } else {
            userService.giveRole(userId, Role.valueOf(role));
        }
        resp.setStatus(200);
        resp.getOutputStream().flush();
    }

    private void pagination(HttpServletRequest req) {
        String spage = req.getParameter("page");
        String email = req.getParameter("email");
        String sort = req.getParameter("sort");
        int page = 1;

        if (spage != null) {
            page = Integer.parseInt(spage);
        }

        int startPosition = page * totalUsers - totalUsers;

        UserService userService = new UserService();
        List<User> users = userService.getSortedUsers(email, sort, startPosition, totalUsers);

        int nOfPages = userService.getNumberOfRows() / totalUsers;

        if (nOfPages % totalUsers > 0) {
            nOfPages++;
        }

        req.setAttribute("users", users);
        req.setAttribute("page", page);
        req.setAttribute("nOfPages", nOfPages);
    }

}
