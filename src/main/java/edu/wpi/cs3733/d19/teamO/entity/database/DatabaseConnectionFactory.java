package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A factory that creates connections to a SQL database.
 */
interface DatabaseConnectionFactory {
    /**
     * Get a {@link java.sql.Connection} to the database.
     *
     * @return The connection
     * @throws SQLException If there was a problem establishing a connection
     */
    Connection getConnection() throws SQLException;

    /**
     * Shutdown the SQL database.
     */
    void drop() throws SQLException;
}
