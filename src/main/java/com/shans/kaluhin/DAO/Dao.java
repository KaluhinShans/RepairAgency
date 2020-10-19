package com.shans.kaluhin.DAO;

import com.shans.kaluhin.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {

    void insert(T t);

    List<T> findAll(int start, int total);

    T findBy(String by, int value);

    T findBy(String by, String value);

    List<T> findBy(String by, String value, int start, int total);

    List<T> findBy(String by, int value, int start, int total);

    void setVariable(String variable, int id, String value);

    void setVariable(String variable, int id, int value);

    T buildObjectFromResultSet(ResultSet resultSet) throws SQLException;
}
