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

import edu.wpi.cs3733.d19.teamO.entity.GiftRequest;

public class GiftRequestDaoDb implements GiftRequestDao {

  private static final String QUERY_FILE_NAME =
      "gift_request_queries.properties";

  private static final Logger logger =
      Logger.getLogger(GiftRequestDaoDb.class.getName());
  private static final Properties queries;

  static {
    queries = new Properties();
    try (InputStream is = GiftRequestDaoDb.class
        .getResourceAsStream(QUERY_FILE_NAME)) {
      queries.load(is);
    } catch (IOException ex) {
      logger.log(Level.SEVERE, "Unable to load property file: " + QUERY_FILE_NAME, ex);
    }
  }

  private static final String TABLE_NAME = queries.getProperty(
      "gift_request.table_name");
  private final DatabaseConnectionFactory dcf;
  private final NodeDaoDb nodeDaoDb;

  GiftRequestDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
    this.dcf = dcf;
    nodeDaoDb = new NodeDaoDb(dcf);
    createTable();
  }

  @Override
  public Optional<GiftRequest> get(final Integer id) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("gift_request.select")
      );

      statement.setInt(1, id);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(extractGiftRequestFromResultSet(resultSet));
        }
      }
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to get GiftRequest", exception);
    }
    return Optional.empty();
  }

  @Override
  public Set<GiftRequest> getAll() {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("gift_request.select_all")
      );

      try (ResultSet resultSet = statement.executeQuery()) {
        Set<GiftRequest> giftRequests = new HashSet<>();
        while (resultSet.next()) {
          giftRequests.add(extractGiftRequestFromResultSet(
              resultSet));
        }
        return giftRequests;
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get GiftRequests", ex);
    }
    return Collections.emptySet();
  }

  private GiftRequest extractGiftRequestFromResultSet(
      final ResultSet resultSet) throws SQLException {
    return new GiftRequest(
        resultSet.getInt("sr_id"),
        resultSet.getTimestamp("TIMEREQUESTED").toLocalDateTime(),
        resultSet.getTimestamp("TIMECOMPLETED").toLocalDateTime(),
        nodeDaoDb.get(resultSet
            .getString("LOCATIONNODEID"))
            .orElseThrow(() -> new SQLException(
                "Could not get node for gift request")),
        resultSet.getString("WHOCOMPLETED"),
        GiftRequest.GiftRequestType.get(
            resultSet.getString("GIFT_TYPE")),
        resultSet.getString("DESCRIPTION"),
        resultSet.getString("NAME")
    );
  }

  @Override
  public boolean insert(final GiftRequest giftRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("gift_request.insert"),
          Statement.RETURN_GENERATED_KEYS
      );
      statement.setTimestamp(1,
          Timestamp.valueOf(giftRequest.getTimeRequested()));
      statement.setTimestamp(2,
          Timestamp.valueOf(giftRequest.getTimeCompleted()));
      statement.setString(3, giftRequest.getWhoCompleted());
      statement.setString(4,
          giftRequest.getLocationNode().getNodeId());
      statement.setString(5, giftRequest.getType().name());
      statement.setString(6, giftRequest.getDescription());
      statement.setString(7, giftRequest.getPerson());

      statement.executeUpdate();
      try (ResultSet keys = statement.getGeneratedKeys()) {
        if (!keys.next()) {
          return false;
        }
        giftRequest.setId(keys.getInt(1));
        return true;
      }
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to insert GiftRequest", exception);
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
            "gift_request.create_table"));
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
  public boolean update(GiftRequest giftRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("gift_request.update")
      );
      statement.setTimestamp(1,
          Timestamp.valueOf(giftRequest.getTimeRequested()));
      statement.setTimestamp(2,
          Timestamp.valueOf(giftRequest.getTimeCompleted()));
      statement.setString(3, giftRequest.getWhoCompleted());
      statement.setString(4, giftRequest.getLocationNode().getNodeId());
      statement.setString(5, giftRequest.getType().name());
      statement.setString(6, giftRequest.getDescription());
      statement.setString(7, giftRequest.getPerson());
      statement.setInt(8, giftRequest.getId());

      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to update GiftRequest", ex);
    }
    return false;
  }

  @Override
  public boolean delete(GiftRequest giftRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("gift_request.delete"));
      statement.setInt(1, giftRequest.getId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "FAILED to delete GiftRequest");
    }
    return false;
  }

  @Override
  public Set<GiftRequest> getNumberByCategory(String type) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement
          = connection.prepareStatement(queries.getProperty("gift_request.category"));
      statement.setString(1, type);
      try (ResultSet resultSet = statement.executeQuery()) {
        Set<GiftRequest> giftRequests = new HashSet<>();
        while (resultSet.next()) {
          giftRequests.add(extractGiftRequestFromResultSet(resultSet));
        }
        return giftRequests;
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get Nodes", ex);
    }
    return Collections.emptySet();
  }
}
