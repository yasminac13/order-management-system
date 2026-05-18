package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory class responsible for managing database connections.
 * Implements the Singleton design pattern to provide a centralized access point
 * for opening and safely closing JDBC database resources.
 * * @author Yasmina
 * @version 1.0
 */
public class ConnectionFactory {
    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/orders_management";
    private static final String USER = "root";
    private static final String PASS = "Teodora2006!";
    private static ConnectionFactory singleInstance = new ConnectionFactory();

    /**
     * Private constructor to prevent external instantiation.
     * Automatically registers and loads the MySQL JDBC driver.
     */
    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Attempts to establish a connection to the configured MySQL database URL.
     * * @return A fresh Connection object, or null if a connection failure occurs.
     */
    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred while trying to connect to the database");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Public static access method to obtain a functional database connection.
     * * @return A ready-to-use Connection instance.
     */
    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * Safely closes a database Connection resource if it is open.
     * * @param connection The Connection object to be closed.
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occurred while trying to close the connection");
            }
        }
    }

    /**
     * Safely closes a database Statement resource if it is open.
     * * @param statement The Statement (or PreparedStatement) object to be closed.
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occurred while trying to close the statement");
            }
        }
    }

    /**
     * Safely closes a database ResultSet resource if it is open.
     * * @param resultSet The ResultSet object to be closed.
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occurred while trying to close the ResultSet");
            }
        }
    }
}
