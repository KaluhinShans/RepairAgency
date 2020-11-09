package com.shans.kaluhin.DAO;

import com.shans.kaluhin.DAO.interfaces.ElasticDao;
import com.shans.kaluhin.entity.Order;
import com.shans.kaluhin.entity.enums.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao implements ElasticDao<Order> {
    public int totalRows;

    @Override
    public boolean insert(Order order) {
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
            return true;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Order> findAll(int start, int total) {
        String selectOrders = "Select *, count(*) OVER() AS total_count from orders LIMIT ? OFFSET ?";
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

    public List<Order> findAllSorted(String sortBy, int start, int total) {
        String find = String.format("SELECT *, count(*) OVER() AS total_count FROM orders ORDER BY %s LIMIT ? OFFSET ?", sortBy);
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
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

    @Override
    public List<Order> findBy(String by, String value, int start, int total) {
        String find = String.format("SELECT *, count(*) OVER() AS total_count FROM orders WHERE %s = ? LIMIT ? OFFSET ?", by);
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setString(1, value);
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

    @Override
    public List<Order> findBy(String by, int value, int start, int total) {
        String find = String.format("SELECT *, count(*) OVER() AS total_count FROM orders WHERE %s = ? LIMIT ? OFFSET ?", by);
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setInt(1, value);
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

    public List<Order> findBy(String by, String value, String sortBy, int start, int total) {
        String find = String.format("SELECT *, count(*) OVER() AS total_count FROM orders WHERE %s = ? ORDER BY %s LIMIT ? OFFSET ?", by, sortBy);
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setString(1, value);
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

    public List<Order> findBy(String by, int value, String sortBy, int start, int total) {
        String find = String.format("SELECT *, count(*) OVER() AS total_count FROM orders WHERE %s = ? ORDER BY %s LIMIT ? OFFSET ?", by, sortBy);
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setInt(1, value);
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
        order.setCommentId(resultSet.getInt("comment_id"));
        order.setPrice(resultSet.getInt("price"));
        order.setName(resultSet.getString("name"));
        order.setDescription(resultSet.getString("description"));
        order.setLocation(resultSet.getString("location"));
        order.setStatus(OrderStatus.valueOf(resultSet.getString("status")));
        order.setDate(resultSet.getDate("date"));

        return order;
    }

    @Override
    public Order findById(int id) {
        return findBy("id", id);
    }

    public void setStatus(int id, OrderStatus status){
        setVariable("status", id, status.name());
    }

    public void setPrice(int id, int price){
        setVariable("price", id, price);
    }

    public void setMaster(int id, int masterId){
       setVariable("master_id", id, masterId);
    }

    public void setCommentId(int id, int commentId){
        setVariable("comment_id", id, commentId);
    }

    public List<Order> findByMaster(int masterId, int start, int total) {
        return findBy("master_id", masterId, start, total);
    }

    public List<Order> findByUser(int userId, int start, int total) {
        return findBy("user_id", userId, start, total);
    }

    public List<Order> findByStatus(OrderStatus status, int start, int total) {
        return findBy("status", status.name(), start, total);
    }

    public List<Order> findSortedByStatus(OrderStatus status, String sortBy, int startPosition, int total){
        return findBy("status", status.name(), sortBy, startPosition, total);
    }

    public List<Order> findSortedByMaster(int masterId, String sortBy, int startPosition, int total){
        return findBy("master_id", masterId, sortBy, startPosition, total);
    }

    public List<Order> findByStatusAndMaster(OrderStatus status, int masterId, int start, int total) {
        String findBy = String.format("status = '%s' AND master_id", status.name());
        return findBy(findBy, masterId, start, total);
    }

    public List<Order> findSortedByStatusAndMaster(OrderStatus status, int masterId, String sortBy, int start, int total) {
        String findBy = String.format("status = '%s' AND master_id", status.name());
        return findBy(findBy, masterId, sortBy, start, total);
    }


}
