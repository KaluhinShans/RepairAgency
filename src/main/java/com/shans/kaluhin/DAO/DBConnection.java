package com.shans.kaluhin.DAO;

import com.shans.kaluhin.ProjectProperties;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = ProjectProperties.getProperty("connection.url");

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException throwable) {
            Logger.getLogger(DBConnection.class).error("Error connection to the database");
            throw new RuntimeException("Error connection to the database", throwable);
        }
    }

}
