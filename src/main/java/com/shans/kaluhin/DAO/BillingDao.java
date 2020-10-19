package com.shans.kaluhin.DAO;

import com.shans.kaluhin.entity.Billing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillingDao implements Dao<Billing> {
    public int totalRows;

    @Override
    public void insert(Billing billing) {
        String addBilling = "INSERT INTO billing(user_id, order_id, amount, reminder, card, date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addBilling, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, billing.getUserId());
            preparedStatement.setInt(2, billing.getOrderId());
            preparedStatement.setInt(3, billing.getAmount());
            preparedStatement.setInt(4, billing.getReminder());
            preparedStatement.setString(5, billing.getCard());
            preparedStatement.setDate(6, new Date(billing.getDate().getTime()));
            preparedStatement.execute();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            billing.setId(id);
            rs.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public List<Billing> findAll(int start, int total) {
        String selectBillings = "Select *, count(*) OVER() AS total_count from billing LIMIT ? OFFSET ?";
        List<Billing> billings = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectBillings)) {
            preparedStatement.setInt(1, total);
            preparedStatement.setInt(2, start);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_count");
                Billing billing = buildObjectFromResultSet(resultSet);
                billings.add(billing);
            }
            resultSet.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return billings;
    }

    @Override
    public Billing findBy(String by, int value) {
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
    public Billing findBy(String by, String value) {
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
    public List<Billing> findBy(String by, String value, int start, int total) {
        String find = String.format("Select *, count(*) OVER() AS total_count from billing WHERE %s = ? ORDER BY id DESC LIMIT ? OFFSET ?", by);
        List<Billing> billings = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, total);
            preparedStatement.setInt(3, start);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_count");
                Billing billing = buildObjectFromResultSet(resultSet);
                billings.add(billing);
            }
            resultSet.close();
            return billings;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Billing> findBy(String by, int value, int start, int total) {
        String find = String.format("Select *, count(*) OVER() AS total_count from billing WHERE %s = ? ORDER BY id DESC LIMIT ? OFFSET ?", by);
        List<Billing> billings = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setInt(1, value);
            preparedStatement.setInt(2, total);
            preparedStatement.setInt(3, start);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_count");
                Billing billing = buildObjectFromResultSet(resultSet);
                billings.add(billing);
            }
            resultSet.close();
            return billings;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public void setVariable(String variable, int id, String value) {
       //TODO
    }

    @Override
    public void setVariable(String variable, int id, int value) {
       //TODO
    }

    @Override
    public Billing buildObjectFromResultSet(ResultSet resultSet) throws SQLException {
        Billing billing = new Billing();

        billing.setId(resultSet.getInt("id"));
        billing.setUserId(resultSet.getInt("user_id"));
        billing.setOrderId(resultSet.getInt("order_id"));
        billing.setAmount(resultSet.getInt("amount"));
        billing.setReminder(resultSet.getInt("reminder"));
        billing.setCard(resultSet.getString("card"));
        billing.setDate(resultSet.getDate("date"));

        return billing;
    }

    public Billing findById(int id) {
        return findBy("id", id);
    }

    public List<Billing> findBillingsByUser(int userId, int start, int total) {
        return findBy("user_id", userId, start, total);
    }
}
