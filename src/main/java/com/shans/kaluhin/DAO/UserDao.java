package com.shans.kaluhin.DAO;

import com.shans.kaluhin.entity.Order;
import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.entity.enums.Locales;
import com.shans.kaluhin.entity.enums.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements Dao<User> {
    public int totalRows;

    @Override
    public void insert(User user) {
        String addUser = "INSERT INTO usr(email, password, name, last_name, locale, activation_code, balance, photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addUser, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getLocale());
            preparedStatement.setString(6, user.getActivationCode());
            preparedStatement.setInt(7, 0);
            preparedStatement.setString(8, "defaultProfile.jpg");
            preparedStatement.execute();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            user.setId(id);
            rs.close();

            giveRole(user.getId(), Role.USER);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public List<User> findAll(int start, int total) {
        String find = "SELECT *, count(*) OVER() AS total_count FROM usr LIMIT ? OFFSET ?";
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setInt(1, total);
            preparedStatement.setInt(2, start);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_count");
                users.add(buildObjectFromResultSet(resultSet));
            }
            resultSet.close();
            return users;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public User findBy(String by, int value) {
        String find = String.format("SELECT * FROM usr WHERE %s = ?", by);
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setInt(1, value);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return buildObjectFromResultSet(resultSet);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public User findBy(String by, String value) {
        String find = String.format("SELECT * FROM usr WHERE %s = ?", by);
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setString(1, value);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return buildObjectFromResultSet(resultSet);
        } catch (SQLException throwable) {

            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findBy(String by, String value, int start, int total) {
        String find = String.format("SELECT *, count(*) OVER() AS total_count FROM usr WHERE %s = ? LIMIT ? OFFSET ?", by);
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, total);
            preparedStatement.setInt(3, start);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_count");
                users.add(buildObjectFromResultSet(resultSet));
            }
            resultSet.close();
            return users;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findBy(String by, int value, int start, int total) {
        String find = String.format("SELECT *, count(*) OVER() AS total_count FROM usr WHERE %s = ? LIMIT ? OFFSET ?", by);
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setInt(1, value);
            preparedStatement.setInt(2, total);
            preparedStatement.setInt(3, start);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_count");
                users.add(buildObjectFromResultSet(resultSet));
            }
            resultSet.close();
            return users;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public void setVariable(String variable, int userId, String value) {
        String set = String.format("UPDATE usr SET %s = ? WHERE id = ?", variable);
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(set)) {
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, userId);
            preparedStatement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void setVariable(String variable, int userId, int value) {
        String set = String.format("UPDATE usr SET %s = ? WHERE id = ?", variable);
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(set)) {
            preparedStatement.setInt(1, value);
            preparedStatement.setInt(2, userId);
            preparedStatement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public User buildObjectFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(resultSet.getInt("id"));
        user.setBalance(resultSet.getInt("balance"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setName(resultSet.getString("name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setPhoto(resultSet.getString("photo"));
        user.setActivationCode(resultSet.getString("activation_code"));
        user.setRoles(getUserRoles(user.getId()));
        user.setLocale(Locales.valueOf(resultSet.getString("locale")));

        return user;
    }

    public User findById(int userId) {
        return findBy("id", userId);
    }

    public User findByActivationCode(String code) {
        return findBy("activation_code", code);
    }

    public User findByEmail(String email) {
        return findBy("email", email);
    }

    public void setLocale(User user) {
        setVariable("locale", user.getId(), user.getLocale());
    }

    public void setActivationCode(User user) {
        setVariable("activation_code", user.getId(), user.getActivationCode());
    }

    public void setPhoto(User user) {
        setVariable("photo", user.getId(), user.getPhoto());
    }

    public List<User> findAllSorted(String sortBy, int start, int total) {
        String find = String.format("SELECT *, count(*) OVER() AS total_count FROM usr ORDER BY %s LIMIT ? OFFSET ?", sortBy);
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setInt(1, total);
            preparedStatement.setInt(2, start);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_count");
                users.add(buildObjectFromResultSet(resultSet));
            }
            resultSet.close();
            return users;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }


    public List<User> findByRole(Role role) {
        String selectUsers = "Select * from users_roles where usr_role = ?";
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectUsers)) {
            preparedStatement.setString(1, role.name());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = findById(resultSet.getInt("usr_id"));
                users.add(user);
            }
            resultSet.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return users;
    }

    public void giveRole(int userId, Role role) {
        String addRole = "INSERT INTO users_roles(usr_id, usr_role) VALUES (?,?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addRole)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, role.name());
            preparedStatement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void pickUpRole(int userId, Role role) {
        String addRole = "DELETE FROM users_roles WHERE usr_id = ? AND usr_role = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addRole)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, role.name());
            preparedStatement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public List<Role> getUserRoles(int userId) {
        List<Role> roles = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.format("SELECT * FROM users_roles WHERE usr_id = '%d'", userId))) {

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

}
