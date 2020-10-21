package com.shans.kaluhin.DAO;

import com.shans.kaluhin.DAO.interfaces.Dao;
import com.shans.kaluhin.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDao implements Dao<Comment> {
    private int totalRows;

    @Override
    public boolean insert(Comment comment) {
        String addOrder = "INSERT INTO comments(user_id, master_id, rate, description, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addOrder, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, comment.getUserId());
            preparedStatement.setInt(2, comment.getMasterId());
            preparedStatement.setInt(3, comment.getRate());
            preparedStatement.setString(4, comment.getDescription());
            preparedStatement.setDate(5, new Date(comment.getDate().getTime()));
            preparedStatement.execute();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            int commentId = rs.getInt(1);
            comment.setId(commentId);
            rs.close();
            return true;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

    @Override
    public Comment findById(int id) {
        String find = "SELECT * FROM comments WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setInt(1, id);

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
    public List<Comment> findAll(int start, int total) {
        String find = "SELECT *, count(*) OVER() AS total_count FROM comments LIMIT ? OFFSET ?";
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setInt(1, total);
            preparedStatement.setInt(2, start);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_count");
                comments.add(buildObjectFromResultSet(resultSet));
            }
            resultSet.close();
            return comments;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public List<Comment> findAllByMasterId(int masterId, int start, int total) {
        String find = "SELECT *, count(*) OVER() AS total_count FROM comments WHERE master_id = ? LIMIT ? OFFSET ?";
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(find)) {
            preparedStatement.setInt(1, masterId);
            preparedStatement.setInt(2, total);
            preparedStatement.setInt(3, start);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalRows = resultSet.getInt("total_count");
                comments.add(buildObjectFromResultSet(resultSet));
            }
            resultSet.close();
            return comments;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public Comment buildObjectFromResultSet(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();

        comment.setId(resultSet.getInt("id"));
        comment.setRate(resultSet.getInt("rate"));
        comment.setDescription(resultSet.getString("description"));
        comment.setUserId(resultSet.getInt("user_id"));
        comment.setMasterId(resultSet.getInt("master_id"));
        comment.setDate(resultSet.getDate("date"));

        return comment;
    }

}
