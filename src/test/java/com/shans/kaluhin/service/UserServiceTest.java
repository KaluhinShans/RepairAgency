package com.shans.kaluhin.service;

import com.shans.kaluhin.DAO.UserDao;
import com.shans.kaluhin.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDao daoMock;

    @Mock
    private MailSenderService mailService;

    @InjectMocks
    private UserService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddUser() {
        when(daoMock.insert(any(User.class))).thenReturn(true);
        User user = new User();
        user.setPassword("123");
        user.setName("Name");
        user.setLastName("Surname");
        user.setEmail("email");
        assertThat(service.save(user), is(notNullValue()));
    }

    @Test
    public void testLoginUser() {
        User user = new User();
        user.setPassword("password");
        user.hashPassword();
        when(daoMock.findByEmail(any(String.class))).thenReturn(user);
        assertTrue(service.isUserLogin("email", "password"));
        assertNull(service.error);
    }


    @Test
    public void testLoginUser_userNotLogin() {
        when(daoMock.findByEmail(any(String.class))).thenReturn(null);
        assertFalse(service.isUserLogin("email", "password"));
        assertEquals(service.error, "userNotFoundError");
    }

    @Test
    public void testLoginUser_wrongPassword() {
        User user = new User();
        user.setPassword("password");
        when(daoMock.findByEmail(any(String.class))).thenReturn(user);
        assertFalse(service.isUserLogin("email", "password"));
        assertEquals(service.error, "passwordError");
    }

    @Test
    public void testEmailActivate() {
        User user = new User();
        when(daoMock.findByActivationCode(any(String.class))).thenReturn(user);
        assertTrue(service.activateEmail("code"));
        assertNull(service.error);
    }

    @Test
    public void testEmailActivate_error() {
        when(daoMock.findByActivationCode(any(String.class))).thenReturn(null);
        assertFalse(service.activateEmail("code"));
        assertEquals(service.error, "emailActivateError");
    }

    @Test
    public void testSortUsers_shouldReturnOnlyOneUserByEmail() {
        when(daoMock.findByEmail(any(String.class))).thenReturn(new User());
        List<User> users = service.getSortedUsers("searchEmail", null, 0, 0);
        assertEquals(users.size(), 1);
    }

    @Test
    public void testSortUsers_shouldFindByEmail() {
        String email = "searchEmail";
        service.getSortedUsers(email, null, 0, 0);
        verify(daoMock).findByEmail(email);
    }

    @Test
    public void testSortUsers_shouldFindAllSorted() {
        String sortBy = "date";
        service.getSortedUsers(null, sortBy, 0, 0);
        verify(daoMock).findAllSorted(sortBy, 0, 0);
    }

    @Test
    public void testSortUsers_shouldFindAll() {
        service.getSortedUsers(null, null, 0, 0);
        verify(daoMock).findAll(0, 0);
    }

    @Test
    public void profileEdit_shouldChangeEmail() {
        User user = new User();
        user.setId(0);
        user.setName("name");
        user.setLastName("surname");
        user.setEmail("email");

        service.editProfile(user, "newEmail", "name", "surname", null);
        verify(daoMock).setVariable("email", user.getId(), "newEmail");

        assertEquals(user.getEmail(), "newEmail");
        assertEquals(user.getFullName(), "name surname");

    }

    @Test
    public void profileEdit_shouldChangeFullName() {
        User user = new User();
        user.setId(0);
        user.setName("name");
        user.setLastName("surname");
        user.setEmail("email");

        service.editProfile(user, "email", "newName", "newSurname", null);
        verify(daoMock).setVariable("name", user.getId(), "newName");
        verify(daoMock).setVariable("last_name", user.getId(), "newSurname");

        assertEquals(user.getEmail(), "email");
        assertEquals(user.getFullName(), "newName newSurname");
    }
}