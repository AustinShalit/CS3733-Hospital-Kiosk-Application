package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.cs3733.d19.teamO.FileManager;

import static com.google.common.base.Preconditions.checkNotNull;

final class DatabaseConnectionFactoryMemory implements DatabaseConnectionFactory {

	private static final Logger kLogger
			= Logger.getLogger(DatabaseConnectionFactoryMemory.class.getName());

	private static final String EMBEDDED_PROTOCOL = "jdbc:derby:";
	private static final String MEMORY_PROTOCOL = EMBEDDED_PROTOCOL + "memory:";

	private final String name;

	DatabaseConnectionFactoryMemory(final String name) {
		this.name = checkNotNull(name);

		Properties properties = System.getProperties();
		properties.setProperty("derby.system.home", FileManager.APP_DIRECTORY.getAbsolutePath());

		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
			kLogger.log(Level.SEVERE, "Derby embedded driver not found!", ex);
		}
	}

	DatabaseConnectionFactoryMemory() {
		this("database");
	}

	@Override
	public Connection getConnection() throws SQLException {
		try {
			return DriverManager.getConnection(MEMORY_PROTOCOL
					+ name
					+ ";create=true");
		} catch (SQLException ex) {
			kLogger.log(Level.SEVERE, "Exception getting connection to database", ex);
			throw ex;
		}
	}

	@Override
	public void drop() throws SQLException {
		try {
			DriverManager.getConnection(MEMORY_PROTOCOL
					+ name
					+ ";drop=true");
		} catch (SQLException ex) {
			if (ex.getErrorCode() != 45000) {
				throw ex;
			}
		}
	}
}
