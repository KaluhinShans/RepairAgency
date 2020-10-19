package com.shans.kaluhin.controller;

import com.shans.kaluhin.entity.Order;
import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.entity.enums.OrderStatus;
import com.shans.kaluhin.service.OrderService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "MasterServlet")
public class MasterServlet extends HttpServlet {
    int totalOrders = 6;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (!user.isMaster()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        pagination(req, user.getId());
        RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/master.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User master = (User) req.getSession().getAttribute("user");
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        OrderService orderService = new OrderService();
        if (req.getRequestURI().equals("/master/finish")) {
            orderService.finishOrder(master, orderId);
        } else {
            orderService.setOrderStatus(orderId, OrderStatus.PROCESS);
        }

        resp.sendRedirect(req.getContextPath() + "/master");
    }

    private void pagination(HttpServletRequest req, int masterId) {
        String status = req.getParameter("status");
        String sort = req.getParameter("sort");
        String spage = req.getParameter("page");
        int page = 1;

        if (spage != null) {
            page = Integer.parseInt(spage);
        }

        int startPosition = page * totalOrders - totalOrders;

        OrderService orderService = new OrderService();
        List<Order> orders = orderService.getSortedOrders(String.valueOf(masterId), status, sort, startPosition, totalOrders);
        int nOfPages = orderService.getNumberOfRows() / totalOrders;

        if (nOfPages % totalOrders > 0) {
            nOfPages++;
        }

        req.setAttribute("orders", orders);
        req.setAttribute("page", page);
        req.setAttribute("nOfPages", nOfPages);
    }
}
