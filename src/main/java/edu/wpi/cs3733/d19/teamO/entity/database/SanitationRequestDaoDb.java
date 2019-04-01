package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
  private static final Logger kLogger = Logger.getLogger(SanitationRequestDaoDb.class.getName());
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
          + " ON " + TABLE_NAME + ".LOCATIONNODEID" + "=" + NodeDaoDb.TABLE_NAME + ".NODEID"
          + " WHERE ID=?");
      statement.setInt(1, id);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(extractSanitationRequestFromResultSet(resultSet));
        }
      }
    } catch (SQLException exception) {
      kLogger.log(Level.WARNING, "Failed to get SanitationRequest", exception);
    }
    return Optional.empty();
  }

  @Override
  public Set<SanitationRequest> getAll() {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME
          + " INNER JOIN " + NodeDaoDb.TABLE_NAME
          + " ON " + TABLE_NAME + ".LOCATIONNODEID" + "=" + NodeDaoDb.TABLE_NAME + ".NODEID"
      );
      try (ResultSet resultSet = statement.executeQuery()) {
        Set<SanitationRequest> sanitationRequest = new HashSet<>();
        while (resultSet.next()) {
          sanitationRequest.add(extractSanitationRequestFromResultSet(resultSet));
        }
        return sanitationRequest;
      }
    } catch (SQLException ex) {
      kLogger.log(Level.WARNING, "Failed to get SanitationRequests", ex);
    }
    return Collections.emptySet();
  }

  private SanitationRequest extractSanitationRequestFromResultSet(final ResultSet resultSet)
      throws SQLException {
    Node node = new Node(resultSet.getString("NODEID"),
        resultSet.getInt("x"),
        resultSet.getInt("y"),
        resultSet.getInt("floor"),
        resultSet.getString("building"),
        Node.NodeType.get(resultSet.getString("type")),
        resultSet.getString("long_name"),
        resultSet.getString("short_name"));

    return new SanitationRequest(
        resultSet.getInt("ID"),
        resultSet.getTimestamp("TIMEREQUESTED").toLocalDateTime(),
        resultSet.getTimestamp("TIMECOMPLETED").toLocalDateTime(),
        node,
        SanitationRequest.SanitationRequestType.get(
            resultSet.getString("SANITATIONTYPE")),
        resultSet.getString("DESCRIPTION")
    );
  }

  @Override
  public boolean insert(final SanitationRequest sanitationRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO " + TABLE_NAME
          + " VALUES (?, ?, ?, ?, ?, ?)");

      statement.setInt(1, sanitationRequest.getId());
      statement.setTimestamp(2,
          Timestamp.valueOf(sanitationRequest.getTimeRequested()));
      statement.setTimestamp(3,
          Timestamp.valueOf(sanitationRequest.getTimeCompleted()));
      statement.setString(4, sanitationRequest.getLocationNode().getNodeId());
      statement.setString(5, sanitationRequest.getType().name());
      statement.setString(6, sanitationRequest.getDescription());
      return statement.executeUpdate() == 1;
    } catch (SQLException exception) {
      kLogger.log(Level.WARNING, "Failed to insert SanitationRequest", exception);
    }
    return false;
  }

  private void createTable() throws SQLException {
    try (Connection connection = dcf.getConnection();
         ResultSet resultSet = connection.getMetaData().getTables(null, null, TABLE_NAME, null)) {
      if (!resultSet.next()) {
        kLogger.info("Table " + TABLE_NAME + " does not exist. Creating");
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE " + TABLE_NAME
            + "(ID INT PRIMARY KEY,"
            + "TIMEREQUESTED TIMESTAMP,"
            + "TIMECOMPLETED TIMESTAMP,"
            + "LOCATIONNODEID VARCHAR(255),"
            + "SANITATIONTYPE VARCHAR(255),"
            + "DESCRIPTION VARCHAR(255),"
            + "FOREIGN KEY (LOCATIONNODEID) REFERENCES NODE(NODEID))");
        statement.executeUpdate();
        kLogger.info("Table " + TABLE_NAME + " created");
      } else {
        kLogger.info("Table " + TABLE_NAME + " exists");
      }
    } catch (SQLException exception) {
      kLogger.log(Level.WARNING, "Failed to create table", exception);
      throw exception;
    }
  }

  @Override
  public boolean update(SanitationRequest sanitationRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement("UPDATE " + TABLE_NAME
          + " SET TIMEREQUESTED=?,"
          + "TIMECOMPLETED=?,"
          + "LOCATIONNODEID=?,"
          + "SANITATIONTYPE=?,"
          + "DESCRIPTION=?"
          + "WHERE ID=?");
      statement.setTimestamp(1,
          Timestamp.valueOf(sanitationRequest.getTimeRequested()));
      statement.setTimestamp(2,
          Timestamp.valueOf(sanitationRequest.getTimeCompleted()));
      statement.setString(3, sanitationRequest.getLocationNode().getNodeId());
      statement.setString(4, sanitationRequest.getType().name());
      statement.setString(5, sanitationRequest.getDescription());
      statement.setInt(6, sanitationRequest.getId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      kLogger.log(Level.WARNING, "Failed to update SanitationRequest", ex);
    }
    return false;
  }

  @Override
  public boolean delete(SanitationRequest sanitationRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM " + TABLE_NAME
          + " WHERE ID=?");
      statement.setInt(1, sanitationRequest.getId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      kLogger.log(Level.WARNING, "FAILED to delete SanitationRequest");
    }
    return false;
  }
}
