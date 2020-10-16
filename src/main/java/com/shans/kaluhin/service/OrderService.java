package com.shans.kaluhin.service;

import com.shans.kaluhin.DAO.OrderDao;
import com.shans.kaluhin.entity.Order;
import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.entity.enums.OrderStatus;
import org.apache.log4j.Logger;

import java.util.List;


public class OrderService {
    private final Logger log = Logger.getLogger(UserService.class);
    private OrderDao orderDao = new OrderDao();

    public void saveOrder(Order order) {
        orderDao.insert(order);
    }

    public Order getByID(int id){
        return orderDao.findById(id);
    }

    public void saveManagerAnswer(int price, int managerId, int orderId) {
        orderDao.setVariable("price", orderId, price);
        orderDao.setVariable("master_id", orderId, managerId);
        setOrderStatus(orderId, OrderStatus.PAYMENT);
    }

    public void setOrderStatus(int orderId, OrderStatus status){
        orderDao.setVariable("status", orderId, status.name());

        Order order = orderDao.findById(orderId);
        User user = order.getUser();
        MailSenderService.sendOrderStatusUpdate(user, order);
    }

    public int getNumberOfRows() {
        if (orderDao.totalRows == 0) {
            return 1;
        }
        return orderDao.totalRows;
    }

    public List<Order> getAllOrders(int startPosition, int total) {
        return orderDao.findAll(startPosition, total);
    }

    public List<Order> getOrdersByStatus(OrderStatus status, int startPosition, int total) {
        return orderDao.findOrdersByStatus(status, startPosition, total);
    }

    public List<Order> getOrdersByStatusAndMaster(OrderStatus status, int masterId, int startPosition, int total) {
        return orderDao.findOrdersByStatusAndMaster(status, masterId, startPosition, total);
    }

    public List<Order> getOrdersByMaster(int masterId, int startPosition, int total) {
        return orderDao.findOrdersByMaster(masterId, startPosition, total);
    }

    public List<Order> getOrdersByUser(int userId, int startPosition, int total) {
        return orderDao.findOrdersByUser(userId, startPosition, total);
    }

    public void finishOrder(User master, int orderId) {
        setOrderStatus(orderId, OrderStatus.DONE);
        BillingService billingService = new BillingService();
        Order order = orderDao.findById(orderId);
        int sum = (int) (order.getPrice() * 0.8);
        billingService.topUpBalance(master, sum, "Repair Agency");
    }
}
