package com.shans.kaluhin.service;

import com.shans.kaluhin.DAO.BillingDao;
import com.shans.kaluhin.DAO.UserDao;
import com.shans.kaluhin.entity.Billing;
import com.shans.kaluhin.entity.Order;
import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.entity.enums.OrderStatus;
import org.apache.log4j.Logger;

import java.util.List;

public class BillingService {
    private BillingDao billingDao = new BillingDao();
    private OrderService orderService = new OrderService();
    private UserDao userDao = new UserDao();

    private Logger log = Logger.getLogger(BillingService.class);
    private MailSenderService mailSender = new MailSenderService();
    public String error;

    public List<Billing> getUserBillings(int userId, int startPosition, int total) {
        return billingDao.findBillingsByUser(userId, startPosition, total);
    }

    public boolean topUpBalance(User user, int sum, String card) {
        if (sum <= 0) {
            error = "negativeSumError";
            log.info("Negative sum");
            return false;
        }
        user.setBalance(user.getBalance() + sum);
        Billing billing = new Billing();
        billing.setReminder(user.getBalance());
        billing.setUserId(user.getId());
        billing.setCard(card);
        billing.setAmount(sum);

        billingDao.insert(billing);

        userDao.setBalance(user.getId(), user.getBalance());

        log.info("Top up user balance");
        mailSender.sendTopUpBalance(user, sum);
        return true;
    }

    public boolean withdrawBalance(User user, int sum, String card) {
        if (user.getBalance() - sum < 0) {
            error = "notEnoughMoneyError";
            log.info("User have not enough money for withdraw balance");
            return false;
        }
        user.setBalance(user.getBalance() - sum);
        Billing billing = new Billing();
        billing.setReminder(user.getBalance());
        billing.setUserId(user.getId());
        billing.setCard(card);
        billing.setAmount(-sum);

        billingDao.insert(billing);
        userDao.setBalance(user.getId(), user.getBalance());

        log.info("User withdraw balance for " + sum);
        mailSender.sendWithdrawBalance(user, sum, card);
        return true;
    }

    public int getNumberOfRows() {
        return billingDao.totalRows;
    }

    public boolean payForOrder(User user, int orderId) {
        Order order = orderService.getById(orderId);
        if (user.getId() != order.getUserId()) {
            error = "notUsersOrderError";
            log.info("User can't pay for not him order");
            return false;
        }

        if (!order.getStatus().equals(OrderStatus.PAYMENT)) {
            error = "alreadyPayError";
            log.info("User already pay for this order");
            return false;
        }

        int sum = user.getBalance() - order.getPrice();

        if (sum < 0) {
            error = "notEnoughMoneyError";
            log.info("User have not enough money");
            return false;
        }

        Billing billing = new Billing();
        billing.setOrderId(orderId);
        billing.setUserId(user.getId());
        billing.setAmount(-order.getPrice());
        billing.setReminder(sum);
        billingDao.insert(billing);

        orderService.setOrderStatus(orderId, OrderStatus.PENDING);

        user.setBalance(sum);
        userDao.setBalance(user.getId(), user.getBalance());

        log.info("User paid for order");
        mailSender.sendOrderPayment(user, order);
        return true;

    }
}
