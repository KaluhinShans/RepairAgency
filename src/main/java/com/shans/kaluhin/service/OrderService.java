package com.shans.kaluhin.service;

import com.shans.kaluhin.DAO.OrderDao;
import com.shans.kaluhin.entity.Order;
import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.entity.enums.OrderStatus;
import org.apache.log4j.Logger;

import java.util.List;


public class OrderService {
    private final Logger log = Logger.getLogger(OrderService.class);
    private OrderDao orderDao = new OrderDao();

    public void saveOrder(Order order) {
        orderDao.insert(order);
        log.info("User add new order");
    }

    public Order getByID(int id) {
        return orderDao.findById(id);
    }

    public void saveManagerAnswer(int price, int managerId, int orderId) {
        orderDao.setVariable("price", orderId, price);
        orderDao.setVariable("master_id", orderId, managerId);
        setOrderStatus(orderId, OrderStatus.PAYMENT);
        log.info("Manager check order");
    }

    public void setOrderStatus(int orderId, OrderStatus status) {
        orderDao.setVariable("status", orderId, status.name());

        Order order = orderDao.findById(orderId);
        User user = order.getUser();
        MailSenderService.sendOrderStatusUpdate(user, order);
        log.info("Order " + orderId + " get status: " + status.name());
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

    public List<Order> getAllSortedOrders(String sortBy, int startPosition, int total) {
        return orderDao.findAllSorted(sortBy, startPosition, total);
    }

    public List<Order> getOrdersByUser(int userId, int startPosition, int total) {
        return orderDao.findOrdersByUser(userId, startPosition, total);
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

    public List<Order> getSortedOrdersByStatus(OrderStatus status, String sortBy, int startPosition, int total) {
        return orderDao.findBy("status", status.name(), sortBy, startPosition, total);
    }

    public List<Order> getSortedOrdersByStatusAndMaster(OrderStatus status, int masterId, String sortBy, int startPosition, int total) {
        return orderDao.findSortedOrdersByStatusAndMaster(status, masterId, startPosition, total, sortBy);
    }

    public List<Order> getSortedOrdersByMaster(int masterId, String sortBy, int startPosition, int total) {
        return orderDao.findBy("master_id", masterId, sortBy, startPosition, total);

    }

    public void finishOrder(User master, int orderId) {
        setOrderStatus(orderId, OrderStatus.DONE);
        BillingService billingService = new BillingService();
        Order order = orderDao.findById(orderId);
        int sum = (int) (order.getPrice() * 0.8);
        billingService.topUpBalance(master, sum, "Repair Agency");
        log.info("Order is done, and master get salary");
    }

    public List<Order> getSortedOrders(String masterId, String status, String sort, int startPosition, int totalOrders) {
        boolean needSort = true;
        boolean needMaster = true;
        boolean needStatus = true;

        if (sort == null || sort.equals("none")) {
            needSort = false;
        }
        if (masterId == null || masterId.equals("-1")) {
            needMaster = false;
        }
        if (status == null || status.equals("ALL")) {
            needStatus = false;
        }

        if (needMaster) {
            if (needStatus) {
                if (needSort) {
                    //return master, status, sort
                    return getSortedOrdersByStatusAndMaster(OrderStatus.valueOf(status), Integer.parseInt(masterId), sort, startPosition, totalOrders);
                } else {
                    //return master, status
                    return getOrdersByStatusAndMaster(OrderStatus.valueOf(status), Integer.parseInt(masterId), startPosition, totalOrders);
                }
            } else {
                if (needSort) {
                    //return master, sort
                    return getSortedOrdersByMaster(Integer.parseInt(masterId), sort, startPosition, totalOrders);
                } else {
                    //return master
                    return getOrdersByMaster(Integer.parseInt(masterId), startPosition, totalOrders);
                }

            }
        }

        if (needStatus) {
            if (needSort) {
                //return status, sort
                return getSortedOrdersByStatus(OrderStatus.valueOf(status), sort, startPosition, totalOrders);
            } else {
                //return status
                return getOrdersByStatus(OrderStatus.valueOf(status), startPosition, totalOrders);
            }
        }

        if (needSort) {
            //return sort
            return getAllSortedOrders(sort, startPosition, totalOrders);
        } else {
            //return all
            return getAllOrders(startPosition, totalOrders);
        }
    }
}
