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

import edu.wpi.cs3733.d19.teamO.entity.ExternalTransportationRequest;

public class ExternalTransportationRequestDaoDb implements ExternalTransportationRequestDao {

  private static final String QUERY_FILE_NAME =
      "external_transportation_request_queries.properties";

  private static final Logger logger =
      Logger.getLogger(ExternalTransportationRequestDaoDb.class.getName());
  private static final Properties queries;

  static {
    queries = new Properties();
    try (InputStream is = ExternalTransportationRequestDaoDb.class
        .getResourceAsStream(QUERY_FILE_NAME)) {
      queries.load(is);
    } catch (IOException ex) {
      logger.log(Level.SEVERE, "Unable to load property file: " + QUERY_FILE_NAME, ex);
    }
  }

  private static final String TABLE_NAME = queries.getProperty(
      "external_transportation_request.table_name");
  private final DatabaseConnectionFactory dcf;
  private final NodeDaoDb nodeDaoDb;

  ExternalTransportationRequestDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
    this.dcf = dcf;
    nodeDaoDb = new NodeDaoDb(dcf);
    createTable();
  }

  @Override
  public Optional<ExternalTransportationRequest> get(final Integer id) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("external_transportation_request.select")
      );

      statement.setInt(1, id);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(extractExternalTransportationRequestFromResultSet(resultSet));
        }
      }
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to get ExternalTransportationRequest", exception);
    }
    return Optional.empty();
  }

  @Override
  public Set<ExternalTransportationRequest> getAll() {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("external_transportation_request.select_all")
      );

      try (ResultSet resultSet = statement.executeQuery()) {
        Set<ExternalTransportationRequest> externalTransportationRequests = new HashSet<>();
        while (resultSet.next()) {
          externalTransportationRequests.add(extractExternalTransportationRequestFromResultSet(
              resultSet));
        }
        return externalTransportationRequests;
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get ExternalTransportationRequests", ex);
    }
    return Collections.emptySet();
  }

  private ExternalTransportationRequest extractExternalTransportationRequestFromResultSet(
      final ResultSet resultSet) throws SQLException {
    return new ExternalTransportationRequest(
        resultSet.getInt("sr_id"),
        resultSet.getTimestamp("TIMEREQUESTED").toLocalDateTime(),
        resultSet.getTimestamp("TIMECOMPLETED").toLocalDateTime(),
        nodeDaoDb.get(resultSet
            .getString("LOCATIONNODEID"))
            .orElseThrow(() -> new SQLException(
                "Could not get node for External transportation request")),
        resultSet.getString("WHOCOMPLETED"),
        ExternalTransportationRequest.ExternalTransportationRequestType.get(
            resultSet.getString("EXTERNALTRANSPORTATIONTYPE")),
        resultSet.getString("DESCRIPTION"),
        resultSet.getString("NAME")
    );
  }

  @Override
  public boolean insert(final ExternalTransportationRequest externalTransportationRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("external_transportation_request.insert"),
          Statement.RETURN_GENERATED_KEYS
      );
      statement.setTimestamp(1,
          Timestamp.valueOf(externalTransportationRequest.getTimeRequested()));
      statement.setTimestamp(2,
          Timestamp.valueOf(externalTransportationRequest.getTimeCompleted()));
      statement.setString(3, externalTransportationRequest.getWhoCompleted());
      statement.setString(4,
          externalTransportationRequest.getLocationNode().getNodeId());
      statement.setString(5, externalTransportationRequest.getType().name());
      statement.setString(6, externalTransportationRequest.getDescription());
      statement.setString(7, externalTransportationRequest.getPerson());

      statement.executeUpdate();
      try (ResultSet keys = statement.getGeneratedKeys()) {
        if (!keys.next()) {
          return false;
        }
        externalTransportationRequest.setId(keys.getInt(1));
        return true;
      }
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to insert ExternalTransportationRequest", exception);
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
            "external_transportation_request.create_table"));
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
  public boolean update(ExternalTransportationRequest externalTransportationRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("external_transportation_request.update")
      );
      statement.setTimestamp(1,
          Timestamp.valueOf(externalTransportationRequest.getTimeRequested()));
      statement.setTimestamp(2,
          Timestamp.valueOf(externalTransportationRequest.getTimeCompleted()));
      statement.setString(3, externalTransportationRequest.getWhoCompleted());
      statement.setString(4, externalTransportationRequest.getLocationNode().getNodeId());
      statement.setString(5, externalTransportationRequest.getType().name());
      statement.setString(6, externalTransportationRequest.getDescription());
      statement.setString(7, externalTransportationRequest.getPerson());
      statement.setInt(8, externalTransportationRequest.getId());

      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to update externalTransportationRequest", ex);
    }
    return false;
  }

  @Override
  public boolean delete(ExternalTransportationRequest externalTransportationRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("external_transportation_request.delete"));
      statement.setInt(1, externalTransportationRequest.getId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "FAILED to delete externalTransportationRequest");
    }
    return false;
  }

  @Override
  public Set<ExternalTransportationRequest> getNumberByCategory(String type) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement
          = connection.prepareStatement(queries.getProperty(
              "external_transportation_request.category"));
      statement.setString(1, type);
      try (ResultSet resultSet = statement.executeQuery()) {
        Set<ExternalTransportationRequest> externalTransportationRequests = new HashSet<>();
        while (resultSet.next()) {
          externalTransportationRequests.add(
              extractExternalTransportationRequestFromResultSet(resultSet));
        }
        return externalTransportationRequests;
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get Nodes", ex);
    }
    return Collections.emptySet();
  }
}
