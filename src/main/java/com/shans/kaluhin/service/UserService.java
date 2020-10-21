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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService {
    private UserDao userDao = new UserDao();
    private final Logger log = Logger.getLogger(UserService.class);
    private MailSenderService mailSender = new MailSenderService();
    public String error;

    public boolean save(User user) {
        User userFromDB = getUserByEmail(user.getEmail());
        if (userFromDB != null) {
            log.info("User already register");
            error = "emailAlreadyError";
            return false;
        }
        if (user.getName().length() < 3) {
            error = "nameShortError";
            return false;
        }
        if (user.getLastName().length() < 3) {
            error = "surnameShortError";
            return false;
        }
        if (user.getPassword().length() < 5) {
            error = "passwordShortError";
            return false;
        }
        user.hashPassword();
        user.setActivationCode(UUID.randomUUID().toString());
        log.info("Save user");

        mailSender.sendActivationCode(user);
        return userDao.insert(user);
    }

    public boolean isUserLogin(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.hashPassword();

        User userFromDB = getUserByEmail(email);

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

    public void setLocale(User user, Locales locales) {
        user.setLocale(locales);
        userDao.setLocale(user);
    }

    public boolean activateEmail(String code) {
        User user = userDao.findByActivationCode(code);
        if (user == null) {
            error = "emailActivateError";
            log.info("Email address don't activated");
            return false;
        }
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
            userDao.setPhoto(user);
            log.info("User photo saved");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Can't save photo");
        }
    }

    public List<User> getSortedUsers(String email, String sort, int startPosition, int totalUsers) {
        if (email != null && !email.isEmpty()) {
            User user = getUserByEmail(email);
            List<User> users = new ArrayList<>();
            if (user != null) {
                users.add(user);
            }
            // return user with this email
            return users;
        } else if (sort != null && !sort.isEmpty()) {
            //return sorted users
            return userDao.findAllSorted(sort, startPosition, totalUsers);
        } else {
            // return all users
            return userDao.findAll(startPosition, totalUsers);
        }
    }

    public boolean editProfile(User user, String email, String name, String lastName, Part photo) {
        if (photo != null && photo.getSize() > 0) {
            savePhoto(user, photo);
        }
        if (!user.getName().equals(name)) {
            if (name.length() < 3){
                error = "nameShortError";
                return false;
            }
                user.setName(name);
            userDao.setVariable("name", user.getId(), name);
        }
        if (!user.getLastName().equals(lastName)) {
            if (lastName.length() < 3){
                error = "surnameShortError";
                return false;
            }
                user.setLastName(lastName);
            userDao.setVariable("last_name", user.getId(), lastName);
        }
        if (!user.getEmail().equals(email)) {
            user.setActivationCode(UUID.randomUUID().toString());
            user.setEmail(email);
            userDao.setActivationCode(user);
            userDao.setVariable("email", user.getId(), email);

            mailSender.sendChangeEmail(user);
        }

        return true;
    }

    public boolean changePassword(User user, String oldPassword, String newPassword, String repeatPassword) {
        String hashPassword = String.valueOf(oldPassword.hashCode());
        if(newPassword.length() < 6){
            error = "passwordShortError";
            return false;
        }
        if(!user.getPassword().equals(hashPassword)){
            error = "oldPasswordError";
            return false;
        }
        if(!newPassword.equals(repeatPassword)){
            error = "repeatPasswordError";
            return false;
        }
        user.setPassword(newPassword);
        user.hashPassword();

        userDao.setPassword(user.getId(), newPassword);
        log.info("User: " + user.getEmail() +" change his password");
        return true;
    }
}
