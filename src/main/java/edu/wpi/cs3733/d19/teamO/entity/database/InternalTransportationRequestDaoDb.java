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

import edu.wpi.cs3733.d19.teamO.entity.InternalTransportationRequest;

public class InternalTransportationRequestDaoDb implements InternalTransportationRequestDao {

	private static final String QUERY_FILE_NAME =
			"internal_transportation_request_queries.properties";

	private static final Logger logger =
			Logger.getLogger(InternalTransportationRequestDaoDb.class.getName());
	private static final Properties queries;

	static {
		queries = new Properties();
		try (InputStream is = InternalTransportationRequestDaoDb.class
				.getResourceAsStream(QUERY_FILE_NAME)) {
			queries.load(is);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Unable to load property file: " + QUERY_FILE_NAME, ex);
		}
	}

	private static final String TABLE_NAME = queries.getProperty(
			"internal_transportation_request.table_name");
	private final DatabaseConnectionFactory dcf;
	private final NodeDaoDb nodeDaoDb;

	InternalTransportationRequestDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
		this.dcf = dcf;
		nodeDaoDb = new NodeDaoDb(dcf);
		createTable();
	}

	@Override
	public Optional<InternalTransportationRequest> get(final Integer id) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("internal_transportation_request.select")
			);

			statement.setInt(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return Optional.of(extractInternalTransportationRequestFromResultSet(resultSet));
				}
			}
		} catch (SQLException exception) {
			logger.log(Level.WARNING, "Failed to get InternalTransportationRequest", exception);
		}
		return Optional.empty();
	}

	@Override
	public Set<InternalTransportationRequest> getAll() {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("internal_transportation_request.select_all")
			);

			try (ResultSet resultSet = statement.executeQuery()) {
				Set<InternalTransportationRequest> internalTransportationRequests = new HashSet<>();
				while (resultSet.next()) {
					internalTransportationRequests.add(extractInternalTransportationRequestFromResultSet(
							resultSet));
				}
				return internalTransportationRequests;
			}
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "Failed to get InternalTransportationRequests", ex);
		}
		return Collections.emptySet();
	}

	private InternalTransportationRequest extractInternalTransportationRequestFromResultSet(
			final ResultSet resultSet) throws SQLException {
		return new InternalTransportationRequest(
				resultSet.getInt("sr_id"),
				resultSet.getTimestamp("TIMEREQUESTED").toLocalDateTime(),
				resultSet.getTimestamp("TIMECOMPLETED").toLocalDateTime(),
				nodeDaoDb.get(resultSet
						.getString("LOCATIONNODEID"))
						.orElseThrow(() -> new SQLException(
								"Could not get node for internal transportation request")),
				resultSet.getString("WHOCOMPLETED"),
				InternalTransportationRequest.InternalTransportationRequestType.get(
						resultSet.getString("INTERNALTRANSPORTATIONTYPE")),
				resultSet.getString("DESCRIPTION"),
				resultSet.getString("NAME")
		);
	}

	@Override
	public boolean insert(final InternalTransportationRequest internalTransportationRequest) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("internal_transportation_request.insert"),
					Statement.RETURN_GENERATED_KEYS
			);
			statement.setTimestamp(1,
					Timestamp.valueOf(internalTransportationRequest.getTimeRequested()));
			statement.setTimestamp(2,
					Timestamp.valueOf(internalTransportationRequest.getTimeCompleted()));
			statement.setString(3, internalTransportationRequest.getWhoCompleted());
			statement.setString(4,
					internalTransportationRequest.getLocationNode().getNodeId());
			statement.setString(5, internalTransportationRequest.getType().name());
			statement.setString(6, internalTransportationRequest.getDescription());
			statement.setString(7, internalTransportationRequest.getPerson());

			statement.executeUpdate();
			try (ResultSet keys = statement.getGeneratedKeys()) {
				if (!keys.next()) {
					return false;
				}
				internalTransportationRequest.setId(keys.getInt(1));
				return true;
			}
		} catch (SQLException exception) {
			logger.log(Level.WARNING, "Failed to insert InternalTransportationRequest", exception);
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
						= connection.prepareStatement(queries.getProperty(
						"internal_transportation_request.create_table"));
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

	@Override
	public boolean update(InternalTransportationRequest internalTransportationRequest) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("internal_transportation_request.update")
			);
			statement.setTimestamp(1,
					Timestamp.valueOf(internalTransportationRequest.getTimeRequested()));
			statement.setTimestamp(2,
					Timestamp.valueOf(internalTransportationRequest.getTimeCompleted()));
			statement.setString(3, internalTransportationRequest.getWhoCompleted());
			statement.setString(4, internalTransportationRequest.getLocationNode().getNodeId());
			statement.setString(5, internalTransportationRequest.getType().name());
			statement.setString(6, internalTransportationRequest.getDescription());
			statement.setString(7, internalTransportationRequest.getPerson());
			statement.setInt(8, internalTransportationRequest.getId());

			return statement.executeUpdate() == 1;
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "Failed to update InternalTransportationRequest", ex);
		}
		return false;
	}

	@Override
	public boolean delete(InternalTransportationRequest internalTransportationRequest) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("internal_transportation_request.delete"));
			statement.setInt(1, internalTransportationRequest.getId());
			return statement.executeUpdate() == 1;
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "FAILED to delete InternalTransportationRequest");
		}
		return false;
	}
}
