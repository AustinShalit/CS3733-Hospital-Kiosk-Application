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

import edu.wpi.cs3733.d19.teamO.entity.ITSupportRequest;

public class ITSupportRequestDaoDb implements ITSupportRequestDao {

  private static final String QUERY_FILE_NAME =
      "it_support_request_queries.properties";

  private static final Logger logger =
      Logger.getLogger(ITSupportRequestDaoDb.class.getName());
  private static final Properties queries;

  static {
    queries = new Properties();
    try (InputStream is = ITSupportRequestDaoDb.class
        .getResourceAsStream(QUERY_FILE_NAME)) {
      queries.load(is);
    } catch (IOException ex) {
      logger.log(Level.SEVERE, "Unable to load property file: " + QUERY_FILE_NAME, ex);
    }
  }

  private static final String TABLE_NAME = queries.getProperty(
      "it_support_request.table_name");
  private DatabaseConnectionFactory dcf;
  private NodeDaoDb nodeDaoDb;

  ITSupportRequestDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
    this.dcf = dcf;
    nodeDaoDb = new NodeDaoDb(dcf);
    createTable();
  }

  ITSupportRequestDaoDb() throws SQLException {
    this(new DatabaseConnectionFactoryEmbedded());
  }

  @Override
  public Optional<ITSupportRequest> get(final Integer id) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("it_support_request.select")
      );

      statement.setInt(1, id);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(extractITSupportRequestFromResultSet(resultSet));
        }
      }
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to get ITSupportRequest", exception);
    }
    return Optional.empty();
  }

  @Override
  public Set<ITSupportRequest> getAll() {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("it_support_request.select_all")
      );

      try (ResultSet resultSet = statement.executeQuery()) {
        Set<ITSupportRequest> itSupportRequests = new HashSet<>();
        while (resultSet.next()) {
          itSupportRequests.add(extractITSupportRequestFromResultSet(
              resultSet));
        }
        return itSupportRequests;
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get ITSupportRequests", ex);
    }
    return Collections.emptySet();
  }

  private ITSupportRequest extractITSupportRequestFromResultSet(
      final ResultSet resultSet) throws SQLException {
    return new ITSupportRequest(
        resultSet.getInt("sr_id"),
        resultSet.getTimestamp("TIMEREQUESTED").toLocalDateTime(),
        resultSet.getTimestamp("TIMECOMPLETED").toLocalDateTime(),
        nodeDaoDb.get(resultSet
            .getString("LOCATIONNODEID"))
            .orElseThrow(() -> new SQLException(
                "Could not get node for IT Support request")),
        resultSet.getString("WHOCOMPLETED"),
        ITSupportRequest.ITSupportRequestType.get(
            resultSet.getString("ITSUPPORTTYPE")),
        resultSet.getString("DESCRIPTION"),
        resultSet.getString("NAME")
    );
  }

  @Override
  public boolean insert(final ITSupportRequest itSupportRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("it_support_request.insert"),
          Statement.RETURN_GENERATED_KEYS
      );
      statement.setTimestamp(1,
          Timestamp.valueOf(itSupportRequest.getTimeRequested()));
      statement.setTimestamp(2,
          Timestamp.valueOf(itSupportRequest.getTimeCompleted()));
      statement.setString(3, itSupportRequest.getWhoCompleted());
      statement.setString(4,
          itSupportRequest.getLocationNode().getNodeId());
      statement.setString(5, itSupportRequest.getType().name());
      statement.setString(6, itSupportRequest.getDescription());
      statement.setString(7, itSupportRequest.getPerson());

      statement.executeUpdate();
      try (ResultSet keys = statement.getGeneratedKeys()) {
        if (!keys.next()) {
          return false;
        }
        itSupportRequest.setId(keys.getInt(1));
        return true;
      }
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to insert ITSupportRequest", exception);
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
            "it_support_request.create_table"));
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
  public boolean update(ITSupportRequest itSupportRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("it_support_request.update")
      );
      statement.setTimestamp(1,
          Timestamp.valueOf(itSupportRequest.getTimeRequested()));
      statement.setTimestamp(2,
          Timestamp.valueOf(itSupportRequest.getTimeCompleted()));
      statement.setString(3, itSupportRequest.getWhoCompleted());
      statement.setString(4, itSupportRequest.getLocationNode().getNodeId());
      statement.setString(5, itSupportRequest.getType().name());
      statement.setString(6, itSupportRequest.getDescription());
      statement.setString(7, itSupportRequest.getPerson());
      statement.setInt(8, itSupportRequest.getId());

      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to update ITSupportRequest", ex);
    }
    return false;
  }

  @Override
  public boolean delete(ITSupportRequest itSupportRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("it_support_request.delete"));
      statement.setInt(1, itSupportRequest.getId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "FAILED to delete ITSupportRequest");
    }
    return false;
  }
}
