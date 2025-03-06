package org.example.db;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    @Getter
    private static final ConnectionFactory instance = new ConnectionFactory();
    private ConnectionFactory() { }

    @Getter
    private Connection connection;

    public Connection establishDBConnection(String dbUrl,
                                            String user,
                                            String pass) throws SQLException {
        return DriverManager.getConnection(dbUrl, user, pass);
    }
}
