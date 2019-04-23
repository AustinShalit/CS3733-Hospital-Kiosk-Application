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

import edu.wpi.cs3733.d19.teamO.entity.FloristRequest;

public class FloristRequestDaoDb implements FloristRequestDao {

	private static final String QUERY_FILE_NAME =
			"florist_request_queries.properties";

	private static final Logger logger =
			Logger.getLogger(FloristRequestDaoDb.class.getName());
	private static final Properties queries;

	static {
		queries = new Properties();
		try (InputStream is = FloristRequestDaoDb.class
				.getResourceAsStream(QUERY_FILE_NAME)) {
			queries.load(is);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Unable to load property file: " + QUERY_FILE_NAME, ex);
		}
	}

	private static final String TABLE_NAME = queries.getProperty(
			"florist_request_queries.table_name");
	private DatabaseConnectionFactory dcf;
	private NodeDaoDb nodeDaoDb;

	FloristRequestDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
		this.dcf = dcf;
		nodeDaoDb = new NodeDaoDb(dcf);
		createTable();
	}

	FloristRequestDaoDb() throws SQLException {
		this(new DatabaseConnectionFactoryEmbedded());
	}

	@Override
	public Optional<FloristRequest> get(final Integer id) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("florist_request_queries.select")
			);

			statement.setInt(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return Optional.of(extractFloristRequestFromResultSet(resultSet));
				}
			}
		} catch (SQLException exception) {
			logger.log(Level.WARNING, "Failed to get florist Request", exception);
		}
		return Optional.empty();
	}

	@Override
	public Set<FloristRequest> getAll() {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("florist_request_queries.select_all")
			);

			try (ResultSet resultSet = statement.executeQuery()) {
				Set<FloristRequest> floristRequests = new HashSet<>();
				while (resultSet.next()) {
					floristRequests.add(extractFloristRequestFromResultSet(
							resultSet));
				}
				return floristRequests;
			}
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "Failed to get FloristRequests", ex);
		}
		return Collections.emptySet();
	}

	private FloristRequest extractFloristRequestFromResultSet(
			final ResultSet resultSet) throws SQLException {
		return new FloristRequest(
				resultSet.getInt("sr_id"),
				resultSet.getTimestamp("TIMEREQUESTED").toLocalDateTime(),
				resultSet.getTimestamp("TIMECOMPLETED").toLocalDateTime(),
				nodeDaoDb.get(resultSet
						.getString("LOCATIONNODEID"))
						.orElseThrow(() -> new SQLException(
								"Could not get node for florist request")),
				resultSet.getString("WHOCOMPLETED"),
				FloristRequest.FloristRequestType.get(
						resultSet.getString("FLORISTREQUESTTYPE")),
				resultSet.getString("DESCRIPTION"),
				resultSet.getString("NAME")
		);
	}

	@Override
	public boolean insert(final FloristRequest floristRequest) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("florist_request_queries.insert"),
					Statement.RETURN_GENERATED_KEYS
			);
			statement.setTimestamp(1,
					Timestamp.valueOf(floristRequest.getTimeRequested()));
			statement.setTimestamp(2,
					Timestamp.valueOf(floristRequest.getTimeCompleted()));
			statement.setString(3, floristRequest.getWhoCompleted());
			statement.setString(4,
					floristRequest.getLocationNode().getNodeId());
			statement.setString(5, floristRequest.getType().name());
			statement.setString(6, floristRequest.getDescription());
			statement.setString(7, floristRequest.getPerson());

			statement.executeUpdate();
			try (ResultSet keys = statement.getGeneratedKeys()) {
				if (!keys.next()) {
					return false;
				}
				floristRequest.setId(keys.getInt(1));
				return true;
			}
		} catch (SQLException exception) {
			logger.log(Level.WARNING, "Failed to insert FloristRequest", exception);
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
						"florist_request_queries.create_table"));
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
	public boolean update(FloristRequest floristRequest) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("florist_request_queries.update")
			);
			statement.setTimestamp(1,
					Timestamp.valueOf(floristRequest.getTimeRequested()));
			statement.setTimestamp(2,
					Timestamp.valueOf(floristRequest.getTimeCompleted()));
			statement.setString(3, floristRequest.getWhoCompleted());
			statement.setString(4, floristRequest.getLocationNode().getNodeId());
			statement.setString(5, floristRequest.getType().name());
			statement.setString(6, floristRequest.getDescription());
			statement.setString(7, floristRequest.getPerson());
			statement.setInt(8, floristRequest.getId());

			return statement.executeUpdate() == 1;
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "Failed to update FloristTransportationRequest", ex);
		}
		return false;
	}

	@Override
	public boolean delete(FloristRequest floristRequest) {
		try (Connection connection = dcf.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					queries.getProperty("florist_request_queries.delete"));
			statement.setInt(1, floristRequest.getId());
			return statement.executeUpdate() == 1;
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "FAILED to delete FloristTransportationRequest");
		}
		return false;
	}
}
