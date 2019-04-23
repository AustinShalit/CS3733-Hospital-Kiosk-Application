package edu.wpi.cs3733.d19.teamO.entity.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.cs3733.d19.teamO.entity.InterpreterRequest;

public class InterpreterRequestDaoDb implements InterpreterRequestDao {

	private static final String QUERY_FILE_NAME = "interpreter_request_queries.properties";

	private static final Logger logger = Logger.getLogger(InterpreterRequestDaoDb.class.getName());
	private static final Properties queries;

	static {
		queries = new Properties();
		try (InputStream is = InterpreterRequestDaoDb.class.getResourceAsStream(QUERY_FILE_NAME)) {
			queries.load(is);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Unable to load property file: " + QUERY_FILE_NAME, ex);
		}
	}

	private static final String TABLE_NAME = queries.getProperty("interpreter_request.table_name");
	private final DatabaseConnectionFactory dcf;
	private final NodeDaoDb nodeDaoDb;

	InterpreterRequestDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
		this.dcf = dcf;
		nodeDaoDb = new NodeDaoDb(dcf);
		createTable();
	}

	@Override
	public Optional<InterpreterRequest> get(final Integer id) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("interpreter_request.select")
			);

			statement.setInt(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return Optional.of(extractInterpreterRequestFromResultSet(resultSet));
				}
			}
		} catch (SQLException exception) {
			logger.log(Level.WARNING, "Failed to get Interpreter Request", exception);
		}
		return Optional.empty();
	}

	@Override
	public Set<InterpreterRequest> getAll() {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("interpreter_request.select_all")
			);

			try (ResultSet resultSet = statement.executeQuery()) {
				Set<InterpreterRequest> interpreterRequest = new HashSet<>();
				while (resultSet.next()) {
					interpreterRequest.add(extractInterpreterRequestFromResultSet(resultSet));
				}
				return interpreterRequest;
			}
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "Failed to get Interpreter Requests", ex);
		}
		return Collections.emptySet();
	}

	private InterpreterRequest extractInterpreterRequestFromResultSet(final ResultSet resultSet)
			throws SQLException {
		return new InterpreterRequest(
				resultSet.getInt("sr_id"),
				resultSet.getTimestamp(2).toLocalDateTime(),
				resultSet.getTimestamp(3).toLocalDateTime(),
				resultSet.getString(4),
				resultSet.getString(7),
				nodeDaoDb.get(resultSet
						.getString(5))
						.orElseThrow(() -> new SQLException("Could not get node for interpreter request")),
				InterpreterRequest.Language.get(
						resultSet.getString(6)),
				resultSet.getString(8)
		);
	}

	@Override
	public boolean insert(final InterpreterRequest interpreterRequest) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("interpreter_request.insert"),
					Statement.RETURN_GENERATED_KEYS
			);
			statement.setTimestamp(1,
					Timestamp.valueOf(interpreterRequest.getTimeRequested()));
			statement.setTimestamp(2,
					Timestamp.valueOf(interpreterRequest.getTimeCompleted()));
			statement.setString(3, interpreterRequest.getWhoCompleted());
			statement.setString(4, interpreterRequest.getLocationNode().getNodeId());
			statement.setString(5, interpreterRequest.getLanguage().name());
			statement.setString(6, interpreterRequest.getDescription());
			statement.setString(7, interpreterRequest.getName());

			statement.executeUpdate();
			try (ResultSet keys = statement.getGeneratedKeys()) {
				if (!keys.next()) {
					return false;
				}
				interpreterRequest.setId(keys.getInt(1));
				return true;
			}
		} catch (SQLException exception) {
			logger.log(Level.WARNING, "Failed to insert Interpreter Request", exception);
		}
		return false;
	}

	@Override
	public boolean update(InterpreterRequest interpreterRequest) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("interpreter_request.update")
			);
			statement.setTimestamp(1,
					Timestamp.valueOf(interpreterRequest.getTimeRequested()));
			statement.setTimestamp(2,
					Timestamp.valueOf(interpreterRequest.getTimeCompleted()));
			statement.setString(3, interpreterRequest.getWhoCompleted());
			statement.setString(4, interpreterRequest.getLocationNode().getNodeId());
			statement.setString(5, interpreterRequest.getLanguage().name());
			statement.setString(6, interpreterRequest.getDescription());
			statement.setString(7, interpreterRequest.getName());
			statement.setInt(8, interpreterRequest.getId());
			return statement.executeUpdate() == 1;
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "Failed to update Interpreter Request", ex);
		}
		return false;
	}

	@Override
	public boolean delete(InterpreterRequest interpreterRequest) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("interpreter_request.delete"));
			statement.setInt(1, interpreterRequest.getId());
			return statement.executeUpdate() == 1;
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "FAILED to delete Interpreter Request");
		}
		return false;
	}

	private void createTable() throws SQLException {

		try (Connection connection = dcf.getConnection();
		     ResultSet resultSet = connection.getMetaData().getTables(null, null,
				     TABLE_NAME, null)) {
			if (!resultSet.next()) {
				logger.info("Table "
						+ TABLE_NAME
						+ " does not exist. Creating");
				PreparedStatement statement
						= connection.prepareStatement(queries.getProperty("interpreter_request.create_table"));
				statement.executeUpdate();
				logger.info("Table " + TABLE_NAME + " created");
			} else {
				logger.info("Table " + TABLE_NAME + " exists");
			}
		} catch (SQLException exception) {
			logger.log(Level.WARNING, "Failed to create table", exception);
			throw exception;
		}
	}
}
