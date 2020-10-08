package com.shans.kaluhin.service;

import com.shans.kaluhin.ProjectProperties;
import com.shans.kaluhin.entity.enums.Locales;
import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.repos.UserRepo;

import java.util.List;
import java.util.UUID;

public class UserService {
    public static String error;

    public static void insertUser(User user) {
        user.hashPassword();
        user.setActivationCode(UUID.randomUUID().toString());
        UserRepo.save(user);

        String message = String.format("Hello, %s \n" +
                "Welcome to Repair Agency, Please visit next link to activation your email: " +
                ProjectProperties.getProperty("host") + "/activate/?code=%s", user.getFullName(), user.getActivationCode());
        MailSenderService.send(user.getEmail(), "Activation code", message);
    }

    public static boolean isUserLogin(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.hashPassword();

        User userFromDB = getUserByEmail(user.getEmail());

        if (userFromDB == null) {
            error = "userNotFoundError";
            return false;
        }
        if (!userFromDB.getPassword().equals(user.getPassword())) {
            error = "passwordError";
            return false;
        }
        return true;
    }

    public static boolean isUserExist(String email) {
        User userFromDB = getUserByEmail(email);
        if (userFromDB != null) {
            error = "emailAlreadyError";
            return true;
        }
        return false;
    }

    public static void setLocale(User user, Locales locales) {
        user.setLocale(locales);
        UserRepo.setLocale(user);
    }

    public static boolean activateEmail(String code) {
        User user = UserRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        UserRepo.setActivationCode(user);
        return true;
    }

    public static User getUserByEmail(String email) {
        return UserRepo.findUserByEmail(email);
    }

    public static User getUserByID(int id) {
        return UserRepo.findUserById(id);
    }

    public static List<User> getAllUsers() {
        return UserRepo.findAllUsers();
    }

    public static void savePhoto(User user, String name) {
        user.setPhoto(name);
        UserRepo.savePhoto(user);
    }
}
