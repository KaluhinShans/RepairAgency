package com.shans.kaluhin.service;

import com.shans.kaluhin.DAO.BillingDao;
import com.shans.kaluhin.DAO.UserDao;
import com.shans.kaluhin.entity.Order;
import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.entity.enums.OrderStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BillingServiceTest {

    @Mock
    private BillingDao daoMock;

    @Mock
    private UserDao userDao;

    @Mock
    private OrderService orderService;

    @Mock
    private MailSenderService mailService;

    @InjectMocks
    private BillingService service;

    private User user = new User();

    private Order order = new Order();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTopUpBalance() {
        user.setBalance(0);

        assertTrue(service.topUpBalance(user, 10, "card"));
        assertEquals(user.getBalance(), 10);
        assertNull(service.error);
    }

    @Test
    public void testTopUpBalance_errorNegativeSum() {
        user.setBalance(0);

        assertFalse(service.topUpBalance(user, -1, "card"));
        assertEquals(user.getBalance(), 0);
        assertEquals(service.error, "negativeSumError");
    }

    @Test
    public void testWithdrawBalance() {
        user.setBalance(10);

        assertTrue(service.withdrawBalance(user, 10, "card"));
        assertEquals(user.getBalance(), 0);
        assertNull(service.error);
    }

    @Test
    public void testWithdrawBalance_notEnoughMoney() {
        user.setBalance(10);

        assertFalse(service.withdrawBalance(user, 20, "card"));
        assertEquals(user.getBalance(), 10);
        assertEquals(service.error, "notEnoughMoneyError");
    }

    @Test
    public void testPayForOrder() {
        user.setId(0);
        user.setBalance(10);

        order.setId(0);
        order.setPrice(5);
        order.setUserId(user.getId());
        order.setStatus(OrderStatus.PAYMENT);

        when(orderService.getById(order.getId())).thenReturn(order);
        assertTrue(service.payForOrder(user, order.getId()));
        assertNull(service.error);
    }

    @Test
    public void testPayForOrder_notUserOrder() {
        user.setId(0);
        user.setBalance(10);

        order.setId(0);
        order.setPrice(5);
        order.setUserId(1);
        order.setStatus(OrderStatus.PAYMENT);


        when(orderService.getById(order.getId())).thenReturn(order);
        assertFalse(service.payForOrder(user, order.getId()));
        assertEquals(service.error, "notUsersOrderError");
    }

    @Test
    public void testPayForOrder_notEnoughMoney() {
        user.setId(0);
        user.setBalance(0);

        order.setId(0);
        order.setPrice(5);
        order.setUserId(user.getId());
        order.setStatus(OrderStatus.PAYMENT);

        when(orderService.getById(order.getId())).thenReturn(order);
        assertFalse(service.payForOrder(user, order.getId()));
        assertEquals(service.error, "notEnoughMoneyError");
    }

    @Test
    public void testPayForOrder_orderAlreadyPay() {
        user.setId(0);
        user.setBalance(0);

        order.setId(0);
        order.setPrice(5);
        order.setUserId(user.getId());
        order.setStatus(OrderStatus.PROCESS);

        when(orderService.getById(order.getId())).thenReturn(order);
        assertFalse(service.payForOrder(user, order.getId()));
        assertEquals(service.error, "alreadyPayError");
    }
}