package com.shans.kaluhin.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {

    T findById(int id);

    List<T> findAll(int start, int total);

    void insert(T t);

    T findBy(String by, int value);

    T findBy(String by, String value);

    void setVariable(String variable, int id, String value);

    void setVariable(String variable, int id, int value);

    T buildObjectFromResultSet(ResultSet resultSet) throws SQLException;
}
