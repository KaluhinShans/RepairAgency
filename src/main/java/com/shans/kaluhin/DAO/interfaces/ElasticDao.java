package com.shans.kaluhin.DAO.interfaces;

import com.shans.kaluhin.DAO.interfaces.Dao;

import java.util.List;

public interface ElasticDao<T> extends Dao<T> {

    T findBy(String by, int value);

    T findBy(String by, String value);

    List<T> findBy(String by, String value, int start, int total);

    List<T> findBy(String by, int value, int start, int total);

    void setVariable(String variable, int id, String value);

    void setVariable(String variable, int id, int value);

}
