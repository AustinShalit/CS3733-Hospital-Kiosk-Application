package edu.wpi.cs3733.d19.teamO.entity.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;

class SecurityRequestDaoDb implements SecurityRequestDao {

	private static final String QUERY_FILE_NAME = "security_request_queries.properties";

	private static final Logger logger
			= Logger.getLogger(SecurityRequestDaoDb.class.getName());
	private static final Properties queries;

	static {
		queries = new Properties();
		try (InputStream is = SecurityRequestDaoDb.class.getResourceAsStream(QUERY_FILE_NAME)) {
			queries.load(is);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Unable to load property file: " + QUERY_FILE_NAME, ex);
		}
	}

	private final DatabaseConnectionFactory dcf;
	private final NodeDaoDb nodeDaoDb;

	SecurityRequestDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
		this.dcf = dcf;
		nodeDaoDb = new NodeDaoDb(dcf);
		createTable();
	}

	@Override
	public Optional<SecurityRequest> get(final Integer id) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement
					= connection.prepareStatement(queries.getProperty("security_request.select"));
			statement.setInt(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return Optional.of(extractSecurityRequestFromResultSet(resultSet));
				}
			}
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "Failed to get Security Request", ex);
		}
		return Optional.empty();
	}

	@Override
	public Set<SecurityRequest> getAll() {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement
					= connection.prepareStatement(queries.getProperty("security_request.select_all"));
			try (ResultSet resultSet = statement.executeQuery()) {
				Set<SecurityRequest> nodes = new HashSet<>();
				while (resultSet.next()) {
					nodes.add(extractSecurityRequestFromResultSet(resultSet));
				}
				return nodes;
			}
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "Failed to get Service Request", ex);
		}
		return Collections.emptySet();
	}

	@Override
	public boolean insert(final SecurityRequest securityRequest) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement;
			statement = connection.prepareStatement(
					queries.getProperty("security_request.insert"));
			statement.setTimestamp(
					1,
					Timestamp.valueOf(securityRequest.getTimeRequested())
			);
			statement.setTimestamp(
					2,
					Timestamp.valueOf(securityRequest.getTimeCompleted())
			);
			statement.setString(3, securityRequest.getWhoCompleted());
			statement.setString(4, securityRequest.getDescription());
			statement.setString(5, securityRequest.getLocationNode().getNodeId());

			return statement.executeUpdate() == 1;
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "Failed to insert Security Request", ex);
		}
		return false;
	}

	@Override
	public boolean update(final SecurityRequest securityRequest) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("security_request.update"));
			statement.setTimestamp(1, Timestamp.valueOf(securityRequest.getTimeRequested()));
			statement.setTimestamp(2, Timestamp.valueOf(securityRequest.getTimeCompleted()));
			statement.setString(3, securityRequest.getWhoCompleted());
			statement.setString(4, securityRequest.getDescription());
			statement.setString(5, securityRequest.getLocationNode().getNodeId());
			statement.setInt(6, securityRequest.getId());
			return statement.executeUpdate() == 1;
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "Failed to update Security Request", ex);
		}
		return false;
	}

	@Override
	public boolean delete(final SecurityRequest securityRequest) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement
					= connection.prepareStatement(queries.getProperty("security_request.delete"));
			statement.setInt(1, securityRequest.getId());
			return statement.executeUpdate() == 1;
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "Failed to delete Security Request", ex);
		}
		return false;
	}

	private SecurityRequest extractSecurityRequestFromResultSet(final ResultSet resultSet)
			throws SQLException {
		return new SecurityRequest(
				resultSet.getInt("id"),
				resultSet.getTimestamp("time_requested").toLocalDateTime(),
				resultSet.getTimestamp("time_completed") != null
						? resultSet.getTimestamp("time_completed").toLocalDateTime() : null,
				resultSet.getString("who_completed"),
				resultSet.getString("description"),
				nodeDaoDb.get(resultSet
						.getString("location"))
						.orElseThrow(() -> new SQLException("Could not get node for security request"))
		);
	}

	private void createTable() throws SQLException {
		try (Connection connection = dcf.getConnection();
		     ResultSet resultSet = connection.getMetaData().getTables(null, null,
				     queries.getProperty("security_request.table_name"), null)) {
			if (!resultSet.next()) {
				logger.info("Table "
						+ queries.getProperty("security_request.table_name")
						+ " does not exist. Creating");
				PreparedStatement statement
						= connection.prepareStatement(queries.getProperty("security_request.create_table"));
				statement.executeUpdate();
				logger.info("Table " + queries.getProperty("security_request.table_name")
						+ " created");
			} else {
				logger.info("Table " + queries.getProperty("security_request.table_name") + " exists");
			}
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "Failed to create table", ex);
			throw ex;
		}
	}
}
