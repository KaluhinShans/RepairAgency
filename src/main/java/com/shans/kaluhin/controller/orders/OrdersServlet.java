package com.shans.kaluhin.controller.orders;

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

@WebServlet(name = "OrdersServlet")
public class OrdersServlet extends HttpServlet {
    private int totalOrders = 6;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals("/orders/reject")) {
            OrderService orderService = new OrderService();
            orderService.setOrderStatus(Integer.parseInt(req.getParameter("orderId")), OrderStatus.REJECT);
            resp.sendRedirect(req.getContextPath() + "/ordersList");
            return;
        }
        User user = (User) req.getSession().getAttribute("user");
        pagination(req, user.getId());
        RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/orders/orders.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

    }

    private void pagination(HttpServletRequest req, int id) {
        String spage = req.getParameter("page");
        int page = 1;

        if (spage != null) {
            page = Integer.parseInt(spage);
        }

        int startPosition = page * totalOrders - totalOrders;
        req.setAttribute("page", page);

        OrderService orderService = new OrderService();
        List<Order> orders = orderService.getOrdersByUser(id, startPosition, totalOrders);

        int nOfPages = orderService.getNumberOfRows() / totalOrders;

        if (nOfPages % totalOrders > 0) {
            nOfPages++;
        }

        req.setAttribute("orders", orders);

        req.setAttribute("nOfPages", nOfPages);
    }
}
