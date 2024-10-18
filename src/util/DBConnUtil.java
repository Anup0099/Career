package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exception.DatabaseConnectionException;

public class DBConnUtil {

    public static Connection getConnection(String connectionString) throws DatabaseConnectionException {
        String[] parts = connectionString.split(",");
        if (parts.length != 3) {
            throw new DatabaseConnectionException("Invalid connection string format.");
        }
        String url = parts[0];
        String username = parts[1];
        String password = parts[2];
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to connect to the database: " + e.getMessage());
        }
        return conn;
    }
}
