package com.shans.kaluhin.DAO;

import com.shans.kaluhin.entity.Order;
import com.shans.kaluhin.entity.enums.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao implements Dao<Order> {
    public int totalRows;

    @Override
    public Order findById(int orderId) {
        return findBy("id", orderId);
    }

    @Override
    public List<Order> findAll(int start, int total) {
        String selectOrders = "Select *,count(*) OVER() AS total_count from orders LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectOrders)) {
            preparedStatement.setInt(1, total);
            preparedStatement.setInt(2, start);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_count");
                Order order = buildObjectFromResultSet(resultSet);
                orders.add(order);
            }
            resultSet.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return orders;
    }

    @Override
    public void insert(Order order) {
        String addOrder = "INSERT INTO orders(user_id, master_id, price, name, description, location, status, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addOrder, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, order.getUserId());
            preparedStatement.setInt(2, order.getMasterId());
            preparedStatement.setInt(3, order.getPrice());
            preparedStatement.setString(4, order.getName());
            preparedStatement.setString(5, order.getDescription());
            preparedStatement.setString(6, order.getLocation());
            preparedStatement.setString(7, order.getStatus().name());
            preparedStatement.setDate(8, new Date(order.getDate().getTime()));
            preparedStatement.execute();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            order.setId(id);
            rs.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public Order findBy(String by, int value) {
        String find = String.format("SELECT * FROM orders WHERE %s = ?", by);
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
    public Order findBy(String by, String value) {
        String find = String.format("SELECT * FROM orders WHERE %s = ?", by);
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

    public List<Order> findOrdersByStatus(OrderStatus status, int start, int total) {
        String find = "SELECT *,count(*) OVER() AS total_count FROM orders WHERE status = ? LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setString(1, status.name());
            preparedStatement.setInt(2, total);
            preparedStatement.setInt(3, start);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_count");
                orders.add(buildObjectFromResultSet(resultSet));
            }
            resultSet.close();
            return orders;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public List<Order> findOrdersByStatusAndMaster(OrderStatus status, int masterId, int start, int total) {
        String find = "SELECT *, count(*) OVER() AS total_count FROM orders WHERE status = ? AND master_id = ? LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setString(1, status.name());
            preparedStatement.setInt(2, masterId);
            preparedStatement.setInt(3, total);
            preparedStatement.setInt(4, start);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_count");
                orders.add(buildObjectFromResultSet(resultSet));
            }
            resultSet.close();
            return orders;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public void setVariable(String variable, int orderId, String value) {
        String set = String.format("UPDATE orders SET %s = ? WHERE id = ?", variable);
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(set)) {
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, orderId);
            preparedStatement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void setVariable(String variable, int orderId, int value) {
        String set = String.format("UPDATE orders SET %s = ? WHERE id = ?", variable);
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(set)) {
            preparedStatement.setInt(1, value);
            preparedStatement.setInt(2, orderId);
            preparedStatement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public Order buildObjectFromResultSet(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getInt("id"));
        order.setUserId(resultSet.getInt("user_id"));
        order.setMasterId(resultSet.getInt("master_id"));
        order.setPrice(resultSet.getInt("price"));
        order.setName(resultSet.getString("name"));
        order.setDescription(resultSet.getString("description"));
        order.setLocation(resultSet.getString("location"));
        order.setStatus(OrderStatus.valueOf(resultSet.getString("status")));
        order.setDate(resultSet.getDate("date"));

        return order;
    }

    public List<Order> findOrdersByMaster(int masterId, int start, int total) {
        String find = "SELECT *, count(*) OVER() AS total_count FROM orders WHERE master_id = ? LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setInt(1, masterId);
            preparedStatement.setInt(2, total);
            preparedStatement.setInt(3, start);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_count");
                orders.add(buildObjectFromResultSet(resultSet));
            }
            resultSet.close();
            return orders;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public List<Order> findOrdersByUser(int userId, int start, int total) {
        String find = "SELECT *, count(*) OVER() AS total_count FROM orders WHERE user_id = ? LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, total);
            preparedStatement.setInt(3, start);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_count");
                orders.add(buildObjectFromResultSet(resultSet));
            }
            resultSet.close();
            return orders;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}