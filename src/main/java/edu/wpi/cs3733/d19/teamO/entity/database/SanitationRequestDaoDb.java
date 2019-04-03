package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;

public class SanitationRequestDaoDb implements SanitationRequestDao {

  private static final Logger logger = Logger.getLogger(SanitationRequestDaoDb.class.getName());

  private static final String TABLE_NAME = "SANITATION";
  private DatabaseConnectionFactory dcf;

  SanitationRequestDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
    this.dcf = dcf;
    createTable();
  }

  SanitationRequestDaoDb() throws SQLException {
    this(new DatabaseConnectionFactoryEmbedded());
  }

  @Override
  public Optional<SanitationRequest> get(final Integer id) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME
              + " INNER JOIN " + NodeDaoDb.TABLE_NAME
              + " ON " + TABLE_NAME + ".LOCATIONNODEID" + "=" + NodeDaoDb.TABLE_NAME + ".id"
              + " WHERE " + TABLE_NAME + ".sr_id=?",
          Statement.RETURN_GENERATED_KEYS
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
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME
              + " INNER JOIN " + NodeDaoDb.TABLE_NAME
              + " ON " + TABLE_NAME + ".LOCATIONNODEID" + "=" + NodeDaoDb.TABLE_NAME + ".id",
          Statement.RETURN_GENERATED_KEYS
      );

      try (ResultSet resultSet = statement.executeQuery()) {
        Set<SanitationRequest> sanitationRequest = new HashSet<>();
        while (resultSet.next()) {
          sanitationRequest.add(extractSanitationRequestFromResultSet(resultSet));
        }
        return sanitationRequest;
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get SanitationRequests", ex);
    }
    return Collections.emptySet();
  }

  private SanitationRequest extractSanitationRequestFromResultSet(final ResultSet resultSet)
      throws SQLException {
    Node node = new Node(resultSet.getString("id"),
        resultSet.getInt("x"),
        resultSet.getInt("y"),
        resultSet.getString("floor"),
        resultSet.getString("building"),
        Node.NodeType.get(resultSet.getString("type")),
        resultSet.getString("long_name"),
        resultSet.getString("short_name"));

    return new SanitationRequest(
        resultSet.getInt("sr_id"),
        resultSet.getTimestamp("TIMEREQUESTED").toLocalDateTime(),
        resultSet.getTimestamp("TIMECOMPLETED").toLocalDateTime(),
        node,
        resultSet.getString("WHOCOMPLETED"),
        SanitationRequest.SanitationRequestType.get(
            resultSet.getString("SANITATIONTYPE")),
        resultSet.getString("DESCRIPTION")
    );
  }

  @Override
  public boolean insert(final SanitationRequest sanitationRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO " + TABLE_NAME
          + " (TIMEREQUESTED, TIMECOMPLETED, WHOCOMPLETED, LOCATIONNODEID, "
          + "SANITATIONTYPE, DESCRIPTION) "
          + "VALUES (?, ?, ?, ?, ?, ?)");

      statement.setTimestamp(1,
          Timestamp.valueOf(sanitationRequest.getTimeRequested()));
      statement.setTimestamp(2,
          Timestamp.valueOf(sanitationRequest.getTimeCompleted()));
      statement.setString(3, sanitationRequest.getWhoCompleted());
      statement.setString(4, sanitationRequest.getLocationNode().getNodeId());
      statement.setString(5, sanitationRequest.getType().name());
      statement.setString(6, sanitationRequest.getDescription());

      return statement.executeUpdate() == 1;
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to insert SanitationRequest", exception);
    }
    return false;
  }

  private void createTable() throws SQLException {

    try (Connection connection = dcf.getConnection();
         ResultSet resultSet = connection.getMetaData().getTables(null, null, TABLE_NAME, null)) {
      if (!resultSet.next()) {
        logger.info("Table " + TABLE_NAME + " does not exist. Creating");
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE " + TABLE_NAME
            + " (sr_id INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,"
            + "TIMEREQUESTED TIMESTAMP,"
            + "TIMECOMPLETED TIMESTAMP,"
            + "WHOCOMPLETED VARCHAR(255),"
            + "LOCATIONNODEID VARCHAR(255),"
            + "SANITATIONTYPE VARCHAR(255),"
            + "DESCRIPTION VARCHAR(255),"
            + "FOREIGN KEY (LOCATIONNODEID) REFERENCES NODE(id))");
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
      PreparedStatement statement = connection.prepareStatement("UPDATE " + TABLE_NAME
          + " SET TIMEREQUESTED=?,"
          + "TIMECOMPLETED=?,"
          + "WHOCOMPLETED=?,"
          + "LOCATIONNODEID=?,"
          + "SANITATIONTYPE=?,"
          + "DESCRIPTION=?"
          + "WHERE sr_id=?");
      statement.setTimestamp(1,
          Timestamp.valueOf(sanitationRequest.getTimeRequested()));
      statement.setTimestamp(2,
          Timestamp.valueOf(sanitationRequest.getTimeCompleted()));
      statement.setString(3, sanitationRequest.getWhoCompleted());
      statement.setString(4, sanitationRequest.getLocationNode().getNodeId());
      statement.setString(5, sanitationRequest.getType().name());
      statement.setString(6, sanitationRequest.getDescription());
      statement.setInt(7, sanitationRequest.getId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to update SanitationRequest", ex);
    }
    return false;
  }

  @Override
  public boolean delete(SanitationRequest sanitationRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM " + TABLE_NAME
          + " WHERE sr_id=?");
      statement.setInt(1, sanitationRequest.getId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "FAILED to delete SanitationRequest");
    }
    return false;
  }
}
