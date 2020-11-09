package com.shans.kaluhin.DAO.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {
    int totalRows = 1;

    boolean insert(T t);

    T findById(int id);

    List<T> findAll(int start, int total);

    T buildObjectFromResultSet(ResultSet resultSet) throws SQLException;

}
