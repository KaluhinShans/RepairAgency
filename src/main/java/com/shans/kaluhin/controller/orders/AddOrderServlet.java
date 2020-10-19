package com.shans.kaluhin.controller.orders;

import com.shans.kaluhin.entity.Order;
import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.service.OrderService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="AddOrderServlet")
public class AddOrderServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/orders/addOrder.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        String name = req.getParameter("orderName");
        String description = req.getParameter("description");
        String address = req.getParameter("address");

        Order order = new Order();
        order.setName(name);
        order.setDescription(description);
        order.setLocation(address);
        order.setUserId(user.getId());
        OrderService orderService = new OrderService();
        orderService.saveOrder(order);

        req.getSession().setAttribute("user", user);
        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}