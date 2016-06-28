package net.gangelov.memories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    public static Connection connection;

    public static void connect(String connectionString) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(connectionString);
    }

    public static PreparedStatement query(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }
}
