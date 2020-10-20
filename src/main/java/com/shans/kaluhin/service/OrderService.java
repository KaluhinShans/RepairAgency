package com.shans.kaluhin.service;

import com.shans.kaluhin.DAO.OrderDao;
import com.shans.kaluhin.DAO.UserDao;
import com.shans.kaluhin.entity.Order;
import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.entity.enums.OrderStatus;
import org.apache.log4j.Logger;

import java.util.List;


public class OrderService {
    private OrderDao orderDao = new OrderDao();
    private UserDao userDao = new UserDao();
    private Logger log = Logger.getLogger(OrderService.class);
    private MailSenderService mailSender = new MailSenderService();
    public String error;

    public boolean save(Order order) {
        if(order.getName().length() < 5){
            error = "problemShortError";
            return false;
        }
        if(order.getDescription().length() < 20){
            error = "descriptionShortError";
            return false;
        }
        if(order.getLocation().length() < 10){
            error = "locationShortError";
            return false;
        }

        log.info("User add new order");
        return orderDao.insert(order);
    }

    public Order getById(int id) {
        return orderDao.findById(id);
    }

    public boolean saveManagerAnswer(int price, int masterId, int orderId) {
        if(price < 5){
            log.info("Manager set low price");
            error = "priceLowError";
            return false;
        }
        orderDao.setPrice(orderId, price);
        orderDao.setMaster(orderId, masterId);
        setOrderStatus(orderId, OrderStatus.PAYMENT);
        log.info("Manager check order");
        return true;
    }

    public void setOrderStatus(int orderId, OrderStatus status) {
        orderDao.setStatus(orderId, status);

        Order order = orderDao.findById(orderId);
        User user = userDao.findById(order.getUserId());
        mailSender.sendOrderStatusUpdate(user, order);
        log.info("Order " + orderId + " get status: " + status.name());
    }

    public int getNumberOfRows() {
        if (orderDao.totalRows == 0) {
            return 1;
        }
        return orderDao.totalRows;
    }

    public List<Order> getOrdersByUser(int userId, int startPosition, int total) {
        return orderDao.findByUser(userId, startPosition, total);
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
                    return orderDao.findSortedByStatusAndMaster(OrderStatus.valueOf(status), Integer.parseInt(masterId), sort, startPosition, totalOrders);
                } else {
                    //return master, status
                    return orderDao.findByStatusAndMaster(OrderStatus.valueOf(status), Integer.parseInt(masterId), startPosition, totalOrders);
                }
            } else {
                if (needSort) {
                    //return master, sort
                    return orderDao.findSortedByMaster(Integer.parseInt(masterId), sort, startPosition, totalOrders);
                } else {
                    //return master
                    return orderDao.findByMaster(Integer.parseInt(masterId), startPosition, totalOrders);

                }

            }
        }

        if (needStatus) {
            if (needSort) {
                //return status, sort
                return orderDao.findSortedByStatus(OrderStatus.valueOf(status), sort, startPosition, totalOrders);
            } else {
                //return status
                return orderDao.findByStatus(OrderStatus.valueOf(status), startPosition, totalOrders);
            }
        }

        if (needSort) {
            //return sort
            return orderDao.findAllSorted(sort, startPosition, totalOrders);
        } else {
            //return all
            return orderDao.findAll(startPosition, totalOrders);
        }
    }
}
