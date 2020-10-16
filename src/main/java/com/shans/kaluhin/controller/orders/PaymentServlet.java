package com.shans.kaluhin.controller.orders;

import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.service.BillingService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PaymentServlet")
public class PaymentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String orderId = req.getParameter("orderId");
        if (orderId != null) {
            BillingService billingService = new BillingService();
            boolean successful = billingService.payForOrder(user, Integer.parseInt(orderId));
            if (successful) {
                req.setAttribute("message", "orderPaymentSuccess");
            } else {
                req.setAttribute("error", billingService.error);
            }
        }
        req.getRequestDispatcher("/orders").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        String card = req.getParameter("card");
        int sum = Integer.parseInt(req.getParameter("sum"));

        BillingService billingService = new BillingService();
        if (req.getRequestURI().equals("/payment/withdraw")) {
            billingService.withdrawBalance(user, sum, card);
        }else{
            billingService.topUpBalance(user, sum, card);
        }


        resp.sendRedirect(req.getRequestURI());
    }

}
