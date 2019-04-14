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

import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;

public class SanitationRequestDaoDb implements SanitationRequestDao {

  private static final String QUERY_FILE_NAME =
      "sanitation_request_queries.properties";

  private static final Logger logger =
      Logger.getLogger(SanitationRequestDaoDb.class.getName());
  private static final Properties queries;

  static {
    queries = new Properties();
    try (InputStream is = SanitationRequestDaoDb.class
        .getResourceAsStream(QUERY_FILE_NAME)) {
      queries.load(is);
    } catch (IOException ex) {
      logger.log(Level.SEVERE, "Unable to load property file: " + QUERY_FILE_NAME, ex);
    }
  }

  private static final String TABLE_NAME = queries.getProperty(
      "sanitation_request.table_name");
  private final DatabaseConnectionFactory dcf;
  private final NodeDaoDb nodeDaoDb;

  SanitationRequestDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
    this.dcf = dcf;
    nodeDaoDb = new NodeDaoDb(dcf);
    createTable();
  }

  @Override
  public Optional<SanitationRequest> get(final Integer id) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("sanitation_request.select")
      );

      statement.setInt(1, id);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(extractSanitationRequestFromResultSet(resultSet));
        }
      }
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to get SanitationRequest", exception);
    }
    return Optional.empty();
  }

  @Override
  public Set<SanitationRequest> getAll() {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("sanitation_request.select_all")
      );

      try (ResultSet resultSet = statement.executeQuery()) {
        Set<SanitationRequest> sanitationRequests = new HashSet<>();
        while (resultSet.next()) {
          sanitationRequests.add(extractSanitationRequestFromResultSet(
              resultSet));
        }
        return sanitationRequests;
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get Sanitation Requests", ex);
    }
    return Collections.emptySet();
  }

  private SanitationRequest extractSanitationRequestFromResultSet(
      final ResultSet resultSet) throws SQLException {
    return new SanitationRequest(
        resultSet.getInt("sr_id"),
        resultSet.getTimestamp("TIMEREQUESTED").toLocalDateTime(),
        resultSet.getTimestamp("TIMECOMPLETED").toLocalDateTime(),
        nodeDaoDb.get(resultSet
            .getString("LOCATIONNODEID"))
            .orElseThrow(() -> new SQLException(
                "Could not get node for sanitation request")),
        resultSet.getString("WHOCOMPLETED"),
        SanitationRequest.SanitationRequestType.get(
            resultSet.getString("SANITATIONTYPE")),
        resultSet.getString("DESCRIPTION"),
        resultSet.getString("NAME")
    );
  }

  @Override
  public boolean insert(final SanitationRequest sanitationRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("sanitation_request.insert"),
          Statement.RETURN_GENERATED_KEYS
      );
      statement.setTimestamp(1,
          Timestamp.valueOf(sanitationRequest.getTimeRequested()));
      statement.setTimestamp(2,
          Timestamp.valueOf(sanitationRequest.getTimeCompleted()));
      statement.setString(3, sanitationRequest.getWhoCompleted());
      statement.setString(4,
          sanitationRequest.getLocationNode().getNodeId());
      statement.setString(5, sanitationRequest.getType().name());
      statement.setString(6, sanitationRequest.getDescription());
      statement.setString(7, sanitationRequest.getPerson());

      statement.executeUpdate();
      try (ResultSet keys = statement.getGeneratedKeys()) {
        if (!keys.next()) {
          return false;
        }
        sanitationRequest.setId(keys.getInt(1));
        return true;
      }
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to insert SanitationRequest", exception);
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
            "sanitation_request.create_table"));
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
  public boolean update(SanitationRequest sanitationRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("sanitation_request.update")
      );
      statement.setTimestamp(1,
          Timestamp.valueOf(sanitationRequest.getTimeRequested()));
      statement.setTimestamp(2,
          Timestamp.valueOf(sanitationRequest.getTimeCompleted()));
      statement.setString(3, sanitationRequest.getWhoCompleted());
      statement.setString(4, sanitationRequest.getLocationNode().getNodeId());
      statement.setString(5, sanitationRequest.getType().name());
      statement.setString(6, sanitationRequest.getDescription());
      statement.setString(7, sanitationRequest.getPerson());
      statement.setInt(8, sanitationRequest.getId());

      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to update SanitationRequest", ex);
    }
    return false;
  }

  @Override
  public boolean delete(SanitationRequest sanitationRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("sanitation_request.delete"));
      statement.setInt(1, sanitationRequest.getId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "FAILED to delete SanitationRequest");
    }
    return false;
  }
}
