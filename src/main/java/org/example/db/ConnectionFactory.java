package org.example.db;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Creates a db connection according to credentials.
 */
public class ConnectionFactory {
    /**
     * Singleton instance
     */
    @Getter
    private static final ConnectionFactory instance = new ConnectionFactory();
    /**
     * Private singleton constructor
     */
    private ConnectionFactory() { }

    /**
     * Main method for establishing DB connections in this project
     * @param dbUrl Database url
     * @param user Database username
     * @param pass Database password
     * @return Connection instance
     * @throws SQLException If connection cannot be established.
     */
    public Connection establishDBConnection(String dbUrl,
                                            String user,
                                            String pass) throws SQLException {
        return DriverManager.getConnection(dbUrl, user, pass);
    }
}
