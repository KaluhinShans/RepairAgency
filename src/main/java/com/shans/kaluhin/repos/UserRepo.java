package com.shans.kaluhin.repos;

import com.shans.kaluhin.ProjectProperties;
import com.shans.kaluhin.entity.enums.Locales;
import com.shans.kaluhin.entity.enums.Role;
import com.shans.kaluhin.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepo {
    private static final String url = ProjectProperties.getProperty("connection.url");

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    public static void save(User user) {
        String addUser = "INSERT INTO usr(email, password, name, last_name, locale, activation_code) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addUser, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getLocale());
            preparedStatement.setString(6, user.getActivationCode());
            preparedStatement.execute();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            user.setId(id);
            for (Role role : user.getRoles()) {
                giveRole(user, role);
            }
            rs.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static List<User> findAllUsers() {
        String selectUsers = "Select * from usr";
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectUsers)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = buildUserFromResultSet(resultSet);
                users.add(user);
            }
            resultSet.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return users;
    }


    public static User findByActivationCode(String code) {
        return findUserBy("activation_code", code);
    }

    public static User findUserByEmail(String email) {
        return findUserBy("email", email);
    }

    public static User findUserById(int id) {
        return findUserBy("id", id);
    }

    public static void savePhoto(User user) {
        String addPhoto = "UPDATE usr SET photo = '%s' WHERE id = '%d'";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.format(addPhoto,
                             user.getPhoto(), user.getId()))) {
            preparedStatement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static List<Role> getUserRoles(User user) {
        List<Role> roles = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.format("SELECT * FROM users_roles WHERE usr_id = '%d'", user.getId()))) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                roles.add(Role.valueOf(resultSet.getString("usr_role")));
            }
            resultSet.close();
            return roles;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    private static User findUserBy(String by, int value) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.format("SELECT * FROM usr WHERE %s = '%d'", by, value))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return buildUserFromResultSet(resultSet);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    private static User findUserBy(String by, String value) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.format("SELECT * FROM usr WHERE %s = '%s'", by, value))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return buildUserFromResultSet(resultSet);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public static void giveRole(User user, Role role) {
        String addRole = "INSERT INTO users_roles(usr_id, usr_role) VALUES (?,?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addRole)) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, role.name());
            preparedStatement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void setLocale(User user) {
        String setLanguage = "UPDATE usr SET locale = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(setLanguage)) {
            preparedStatement.setString(1, user.getLocale());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void setActivationCode(User user) {
        String setLanguage = "UPDATE usr SET activation_code = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(setLanguage)) {
            preparedStatement.setString(1,user.getActivationCode());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    private static User buildUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setBalance(resultSet.getInt("balance"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setName(resultSet.getString("name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setPhoto(resultSet.getString("photo"));
        user.setActivationCode(resultSet.getString("activation_code"));
        user.setPhone(resultSet.getInt("phone"));
        user.setRoles(getUserRoles(user));
        user.setLocale(Locales.valueOf(resultSet.getString("locale")));
        return user;
    }


}
