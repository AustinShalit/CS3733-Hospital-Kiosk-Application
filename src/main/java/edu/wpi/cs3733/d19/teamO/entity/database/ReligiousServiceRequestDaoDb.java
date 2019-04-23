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

import edu.wpi.cs3733.d19.teamO.entity.ReligiousServiceRequest;

public class ReligiousServiceRequestDaoDb implements ReligiousServiceRequestDao {

	private static final String QUERY_FILE_NAME =
			"religious_service_request_queries.properties";

	private static final Logger logger =
			Logger.getLogger(ReligiousServiceRequestDaoDb.class.getName());
	private static final Properties queries;

	static {
		queries = new Properties();
		try (InputStream is = ReligiousServiceRequestDaoDb.class
				.getResourceAsStream(QUERY_FILE_NAME)) {
			queries.load(is);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Unable to load property file: " + QUERY_FILE_NAME, ex);
		}
	}

	private static final String TABLE_NAME = queries.getProperty(
			"religious_service_request.table_name");
	private DatabaseConnectionFactory dcf;
	private NodeDaoDb nodeDaoDb;

	ReligiousServiceRequestDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
		this.dcf = dcf;
		nodeDaoDb = new NodeDaoDb(dcf);
		createTable();
	}

	ReligiousServiceRequestDaoDb() throws SQLException {
		this(new DatabaseConnectionFactoryEmbedded());
	}

	@Override
	public Optional<ReligiousServiceRequest> get(final Integer id) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("religious_service_request.select")
			);

			statement.setInt(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return Optional.of(extractReligiousServiceRequestFromResultSet(resultSet));
				}
			}
		} catch (SQLException exception) {
			logger.log(Level.WARNING, "Failed to get ReligiousServiceRequest", exception);
		}
		return Optional.empty();
	}

	@Override
	public Set<ReligiousServiceRequest> getAll() {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("religious_service_request.select_all")
			);

			try (ResultSet resultSet = statement.executeQuery()) {
				Set<ReligiousServiceRequest> religiousServiceRequests = new HashSet<>();
				while (resultSet.next()) {
					religiousServiceRequests.add(extractReligiousServiceRequestFromResultSet(
							resultSet));
				}
				return religiousServiceRequests;
			}
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "Failed to get ReligiousServiceRequests", ex);
		}
		return Collections.emptySet();
	}

	private ReligiousServiceRequest extractReligiousServiceRequestFromResultSet(
			final ResultSet resultSet) throws SQLException {
		return new ReligiousServiceRequest(
				resultSet.getInt("sr_id"),
				resultSet.getTimestamp("TIMEREQUESTED").toLocalDateTime(),
				resultSet.getTimestamp("TIMECOMPLETED").toLocalDateTime(),
				nodeDaoDb.get(resultSet
						.getString("LOCATIONNODEID"))
						.orElseThrow(() -> new SQLException(
								"Could not get node for religious service request")),
				resultSet.getString("WHOCOMPLETED"),
				ReligiousServiceRequest.ReligiousServiceRequestType.get(
						resultSet.getString("RELIGIOUSSERVICETYPE")),
				resultSet.getString("DESCRIPTION"),
				resultSet.getString("NAME")
		);
	}

	@Override
	public boolean insert(final ReligiousServiceRequest religiousServiceRequest) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("religious_service_request.insert"),
					Statement.RETURN_GENERATED_KEYS
			);
			statement.setTimestamp(1,
					Timestamp.valueOf(religiousServiceRequest.getTimeRequested()));
			statement.setTimestamp(2,
					Timestamp.valueOf(religiousServiceRequest.getTimeCompleted()));
			statement.setString(3, religiousServiceRequest.getWhoCompleted());
			statement.setString(4,
					religiousServiceRequest.getLocationNode().getNodeId());
			statement.setString(5, religiousServiceRequest.getType().name());
			statement.setString(6, religiousServiceRequest.getDescription());
			statement.setString(7, religiousServiceRequest.getPerson());

			statement.executeUpdate();
			try (ResultSet keys = statement.getGeneratedKeys()) {
				if (!keys.next()) {
					return false;
				}
				religiousServiceRequest.setId(keys.getInt(1));
				return true;
			}
		} catch (SQLException exception) {
			logger.log(Level.WARNING, "Failed to insert ReligiousServiceRequest", exception);
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
						"religious_service_request.create_table"));
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
	public boolean update(ReligiousServiceRequest religiousServiceRequest) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("religious_service_request.update")
			);
			statement.setTimestamp(1,
					Timestamp.valueOf(religiousServiceRequest.getTimeRequested()));
			statement.setTimestamp(2,
					Timestamp.valueOf(religiousServiceRequest.getTimeCompleted()));
			statement.setString(3, religiousServiceRequest.getWhoCompleted());
			statement.setString(4, religiousServiceRequest.getLocationNode().getNodeId());
			statement.setString(5, religiousServiceRequest.getType().name());
			statement.setString(6, religiousServiceRequest.getDescription());
			statement.setString(7, religiousServiceRequest.getPerson());
			statement.setInt(8, religiousServiceRequest.getId());

			return statement.executeUpdate() == 1;
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "Failed to update ReligiousServiceRequest", ex);
		}
		return false;
	}

	@Override
	public boolean delete(ReligiousServiceRequest religiousServiceRequest) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("religious_service_request.delete"));
			statement.setInt(1, religiousServiceRequest.getId());
			return statement.executeUpdate() == 1;
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "FAILED to delete ReligiousServiceRequest");
		}
		return false;
	}
}
