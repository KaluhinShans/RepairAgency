package com.shans.kaluhin.service;

import com.shans.kaluhin.DAO.CommentDao;
import com.shans.kaluhin.DAO.OrderDao;
import com.shans.kaluhin.DAO.UserDao;
import com.shans.kaluhin.entity.Comment;
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
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    private OrderDao daoMock;

    @Mock
    private UserDao userDao;

    @Mock
    private CommentDao commentDao;

    @Mock
    private MailSenderService mailService;

    @InjectMocks
    private OrderService service;

    Order order = new Order();

    @Before
    public void setUp() {
        order.setId(0);
        order.setUserId(0);
        order.setName("Name ");
        order.setDescription("DescriptionLongLong ");
        order.setLocation("LocationLong");

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave(){
        when(daoMock.insert(order)).thenReturn(true);
        assertTrue(service.save(order));
        assertNull(service.error);
    }

    @Test
    public void testGetSortedOrders_shouldReturnSortedList_GetByMasterAndStatus(){
        service.getSortedOrders("0", "DONE","sort",0,0);
        verify(daoMock).findSortedByStatusAndMaster(
                OrderStatus.DONE,
                0,
                "sort",
                0,
                0);
    }

    @Test
    public void testGetSortedOrders_shouldReturnSortedList_GetByMaster(){
        service.getSortedOrders("0", null,"sort",0,0);
        verify(daoMock).findSortedByMaster(0, "sort", 0, 0);
    }

    @Test
    public void testGetSortedOrders_shouldReturnSortedList_GetByStatus(){
        service.getSortedOrders(null, "DONE","sort",0,0);
        verify(daoMock).findSortedByStatus(OrderStatus.DONE, "sort", 0, 0);
    }

    @Test
    public void testGetSortedOrders_shouldReturnSortedList(){
        service.getSortedOrders(null, null,"sort",0,0);
        verify(daoMock).findAllSorted("sort",0, 0);
    }


    @Test
    public void testGetSortedOrders_shouldReturnNOTSortedList_GetByMasterAndStatus(){
        service.getSortedOrders("0", "DONE",null,0,0);
        verify(daoMock).findByStatusAndMaster(OrderStatus.DONE, 0, 0, 0);
    }

    @Test
    public void testGetSortedOrders_shouldReturnNOTSortedList_GetByMaster(){
        service.getSortedOrders("0", null,null,0,0);
        verify(daoMock).findByMaster(0, 0, 0);
    }

    @Test
    public void testGetSortedOrders_shouldReturnNOTSortedList_GetByStatus(){
        service.getSortedOrders(null, "DONE",null,0,0);
        verify(daoMock).findByStatus(OrderStatus.DONE, 0, 0);
    }

    @Test
    public void testGetSortedOrders_shouldReturnNOTSortedList(){
        service.getSortedOrders(null, null,null,0,0);
        verify(daoMock).findAll(0, 0);
    }

    @Test
    public void testSave_nameValidation(){
        order.setName("N");

        assertFalse(service.save(order));
        assertEquals(service.error, "problemShortError");
    }

    @Test
    public void testSave_descriptionValidation(){
        order.setDescription("D");

        assertFalse(service.save(order));
        assertEquals(service.error, "descriptionShortError");
    }

    @Test
    public void testSave_locationValidation(){
        order.setLocation("L");

        assertFalse(service.save(order));
        assertEquals(service.error, "locationShortError");
    }

    @Test
    public void testSaveManagerAnswer(){
        when(daoMock.findById(order.getId())).thenReturn(order);
        assertTrue(service.saveManagerAnswer(10, 0, 0));
        assertNull(service.error);
    }

    @Test
    public void testSaveManagerAnswer_lowPrice(){
        assertFalse(service.saveManagerAnswer(0, 0, 0));
        assertEquals(service.error, "priceLowError");
    }

    @Test
    public void testRateOrder(){
        Comment comment = new Comment();
        comment.setRate(3);
        comment.setDescription("DescriptionLongLong ");
        assertTrue(service.rateOrder(0, comment));
        assertNull(service.error);
    }

    @Test
    public void testRateOrder_lowRate(){
        Comment comment = new Comment();
        comment.setRate(0);
        comment.setDescription("DescriptionLongLong ");
        assertFalse(service.rateOrder(0, comment));
        assertEquals(service.error, "nullRateError");

    }

    @Test
    public void testRateOrder_shortDescription(){
        Comment comment = new Comment();
        comment.setRate(5);
        comment.setDescription("DescriptionShort");
        assertFalse(service.rateOrder(0, comment));
        assertEquals(service.error, "descriptionShortError");
    }

}