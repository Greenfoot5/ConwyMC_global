package me.huntifi.conwymc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import me.huntifi.conwymc.ConwyMC;

/**
 * The connection to the SQL database
 */
public class MySQL {

    /** The database's host address */
    private final String host;

    /** The database's address port number */
    private final int port;

    /** The database's name */
    private final String database;

    /** The username for the database account */
    private final String username;

    /** The password for the database account */
    private final String password;

    /** The database connection */
    private Connection connection;

    /**
     * Register all data required to connect to a database.
     * @param host The database's host address
     * @param port The database's address port number
     * @param database The database's name
     * @param username The username for the database account
     * @param password The password for the database account
     */
    public MySQL(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    /**
     * Check if the database is connected.
     * @return Whether the database is connected
     * @throws SQLException If a database access error occurs
     */
    public boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    /**
     * Connect to the database.
     * @throws SQLException If a database access error occurs
     */
    public void connect() throws SQLException {
        if (!isConnected()) {
            connection = DriverManager.getConnection(
                    String.format("jdbc:mysql://%s:%d/%s?autoReconnect=true", host, port, database), username, password
            );
            ConwyMC.getInstance().getLogger().info("Successfully connected to the ConwyMC MySQL Database!");
        }
    }

    /**
     * Disconnect from the database.
     * @throws SQLException If a database access error occurs
     */
    public void disconnect() throws SQLException {
        if (isConnected())
            connection.close();
    }

    /**
     * Get the connection to the database.
     * @return The connection to the database
     * @throws SQLException If a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        connect();
        return connection;
    }
}
