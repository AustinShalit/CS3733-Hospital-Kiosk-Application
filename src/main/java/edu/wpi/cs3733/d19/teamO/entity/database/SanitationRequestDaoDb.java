package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
    this(new DatabaseConnectionFactoryImpl());
  }

  @Override
  public Optional<SanitationRequest> get(final Integer id) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME
          + ", INNER JOIN + " + NodeDaoDb.TABLE_NAME + " WHERE id=? AND " + TABLE_NAME + ".?="
          + NodeDaoDb.TABLE_NAME + ".?");
      statement.setInt(1, id);
      statement.setInt(2, id);
      statement.setString(3, Integer.toString(id));

      try (ResultSet resultSet = statement.executeQuery()) {
        return Optional.of(extractSanitationRequestFromResultSet(resultSet));
      }
    } catch (SQLException exception) {
      kLogger.log(Level.WARNING, "Failed to get SanitationRequest", exception);
    }
    return Optional.empty();
  }

  @Override
  public Set<SanitationRequest> getAll() {
    // todo
    return new HashSet<>();
  }

  private SanitationRequest extractSanitationRequestFromResultSet(final ResultSet resultSet)
      throws SQLException {
    return new SanitationRequest(
        resultSet.getInt("ID"),
        resultSet.getTimestamp("TIMERECIEVED").toLocalDateTime(),
        resultSet.getTimestamp("TIMECOMPLETED").toLocalDateTime(),
        new Node(resultSet.getString("NODEID"),
            resultSet.getInt("XCOORD"),
            resultSet.getInt("YCOORD"),
            resultSet.getInt("FLOOR"),
            resultSet.getString("BUILDING"),
            Node.NodeType.get(resultSet.getString("NODETYPE")),
            resultSet.getString("LONGNAME"),
            resultSet.getString("SHORTNAME")),
        SanitationRequest.SanitationRequestType.get(
            resultSet.getString("SANITATIONTYPE")),
        resultSet.getString("DESCRIPTION"));
  }

  @Override
  public boolean insert(final SanitationRequest sanitationRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO " + TABLE_NAME
          + " VALUES (?, ?, ?, ?, ?, ?)");
      statement.setInt(1, sanitationRequest.getId());
      statement.setTimestamp(2,
          Timestamp.valueOf(sanitationRequest.getTimetimeReceived()));
      statement.setTimestamp(3,
          Timestamp.valueOf(sanitationRequest.getTimeCompleted()));
      statement.setInt(4, sanitationRequest.getId());
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
            + "TIMERECIEVED TIMESTAMP,"
            + "TIMECOMPLETED TIMESTAMP,"
            + "LOCATION VARCHAR(255),"
            + "SANITATIONTYPE VARCHAR(255),"
            + "DESCRIPTION VARCHAR(255),"
            + "FOREIGN KEY (LOCATION) REFERENCES NODE(NODEID))");
        //        statement.setString(1, NodeDaoDb.TABLE_NAME);
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
    //todo
    return false;
  }

  @Override
  public boolean delete(SanitationRequest sanitationRequest) {
    // todo
    return false;
  }


}
