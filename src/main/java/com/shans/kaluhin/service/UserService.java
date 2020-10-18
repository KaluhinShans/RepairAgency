package com.shans.kaluhin.service;

import com.shans.kaluhin.DAO.UserDao;
import com.shans.kaluhin.ProjectProperties;
import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.entity.enums.Locales;
import com.shans.kaluhin.entity.enums.Role;
import org.apache.log4j.Logger;

import javax.servlet.http.Part;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class UserService {
    private final Logger log = Logger.getLogger(UserService.class);
    private final UserDao userDao = new UserDao();
    public String error;

    public void save(User user) {
        user.hashPassword();
        user.setActivationCode(UUID.randomUUID().toString());
        userDao.insert(user);
        log.info("Save user");

        MailSenderService.sendActivationCode(user);
    }

    public boolean isUserLogin(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.hashPassword();

        User userFromDB = getUserByEmail(user.getEmail());

        if (userFromDB == null) {
            log.info("User don't register");
            error = "userNotFoundError";
            return false;
        }
        if (!userFromDB.getPassword().equals(user.getPassword())) {
            log.info("Passwords do not match");
            error = "passwordError";
            return false;
        }
        log.info("User logged in");
        return true;
    }

    public boolean isUserExist(String email) {
        User userFromDB = getUserByEmail(email);
        if (userFromDB != null) {
            log.info("User already register");
            error = "emailAlreadyError";
            return true;
        }
        return false;
    }

    public void setLocale(User user, Locales locales) {
        user.setLocale(locales);
        userDao.setLocale(user);
    }

    public boolean activateEmail(String code) {
        User user = userDao.findByActivationCode(code);
        if (user == null) {
            log.info("Email address don't activated");
            return false;
        }
        user.setActivationCode(null);
        userDao.setActivationCode(user);
        log.info("Email address activated");
        return true;
    }

    public void giveRole(int userId, Role role) {
        userDao.giveRole(userId, role);
        log.info("User " + userId + "get role: " + role.name());
    }

    public void pickUpRole(int userId, Role role) {
        userDao.pickUpRole(userId, role);
        log.info("User " + userId + "lost role: " + role.name());
    }

    public User getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public User getUserByID(int id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers(int start, int total) {
        return userDao.findAll(start, total);
    }

    public List<User> getAllMasters() {
        return userDao.findByRole(Role.MASTER);
    }

    public int getNumberOfRows() {
        if (userDao.totalRows == 0) {
            return 1;
        }
        return userDao.totalRows;
    }

    public void savePhoto(User user, Part filePart) {
        String name = user.getEmail() + ".jpg";
        File file = new File(ProjectProperties.getProperty("images") + name);

        try (BufferedInputStream bis = new BufferedInputStream(filePart.getInputStream())) {

            FileOutputStream fos = new FileOutputStream(file);

            int ch;
            while ((ch = bis.read()) != -1) {
                fos.write(ch);
            }

            user.setPhoto(name);
            userDao.savePhoto(user);
            log.info("User photo saved");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Can't save photo");
        }
    }

}
