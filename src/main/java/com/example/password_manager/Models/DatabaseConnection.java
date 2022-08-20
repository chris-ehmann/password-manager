package com.example.password_manager.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public Connection DatabaseLink;

    public Connection GetConnection() {
        String DatabaseName = "userdb";
        String url = "jdbc:sqlite:identifier.sqlite";
        try {
            DatabaseLink = DriverManager.getConnection(url);

        } catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return DatabaseLink;
    }

    public void close() throws SQLException {
        DatabaseLink.close();
    }
}
