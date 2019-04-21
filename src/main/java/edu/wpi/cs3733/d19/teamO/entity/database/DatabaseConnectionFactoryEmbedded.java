package edu.wpi.cs3733.d19.teamO.entity.database;

import edu.wpi.cs3733.d19.teamO.FileManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

final class DatabaseConnectionFactoryEmbedded implements DatabaseConnectionFactory {

    private static final Logger logger
            = Logger.getLogger(DatabaseConnectionFactoryEmbedded.class.getName());

    private static final String EMBEDDED_PROTOCOL = "jdbc:derby:";

    private final String protocol;
    private final String name;

    private DatabaseConnectionFactoryEmbedded(final String protocol, final String name) {
        this.protocol = checkNotNull(protocol);
        this.name = checkNotNull(name);

        Properties properties = System.getProperties();
        properties.setProperty("derby.system.home", FileManager.APP_DIRECTORY.getAbsolutePath());

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            logger.log(Level.SEVERE, "Derby embedded driver not found!", ex);
        }
    }

    DatabaseConnectionFactoryEmbedded() {
        this(EMBEDDED_PROTOCOL, "database");
    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(protocol
                    + name
                    + ";derby.language.sequence.preallocator=1;create=true");
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Exception getting connection to database", ex);
            throw ex;
        }
    }

    @Override
    public void drop() throws SQLException {
        logger.config("Dropping database: " + name);
        try {
            DriverManager.getConnection(protocol
                    + name
                    + ";drop=true");
        } catch (SQLException ex) {
            if (ex.getErrorCode() != 45000) {
                throw ex;
            }
        }
    }
}
