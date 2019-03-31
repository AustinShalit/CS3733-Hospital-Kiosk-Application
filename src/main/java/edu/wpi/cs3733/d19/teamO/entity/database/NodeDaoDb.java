package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.cs3733.d19.teamO.entity.Node;

class NodeDaoDb implements NodeDao {

  private static final Logger logger
      = Logger.getLogger(NodeDaoDb.class.getName());

  private static final String TABLE_NAME = "node";

  private DatabaseConnectionFactory dcf;

  NodeDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
    this.dcf = dcf;
    createTable();
  }

  NodeDaoDb() throws SQLException {
    this(new DatabaseConnectionFactoryEmbedded());
  }

  @Override
  public Optional<Node> get(final String id) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement
          = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE node_id=?");
      statement.setString(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(extractNodeFromResultSet(resultSet));
        }
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get Node", ex);
    }
    return Optional.empty();
  }

  @Override
  public Set<Node> getAll() {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement
          = connection.prepareStatement("SELECT * FROM " + TABLE_NAME);
      try (ResultSet resultSet = statement.executeQuery()) {
        Set<Node> nodes = new HashSet<>();
        while (resultSet.next()) {
          nodes.add(extractNodeFromResultSet(resultSet));
        }
        return nodes;
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get Nodes", ex);
    }
    return Collections.emptySet();
  }

  @Override
  public boolean insert(final Node node) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO " + TABLE_NAME
          + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
      statement.setString(1, node.getNodeId());
      statement.setInt(2, node.getXcoord());
      statement.setInt(3, node.getYcoord());
      statement.setInt(4, node.getFloor());
      statement.setString(5, node.getBuilding());
      statement.setString(6, node.getNodeType().name());
      statement.setString(7, node.getLongName());
      statement.setString(8, node.getShortName());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to insert Node", ex);
    }
    return false;
  }

  @Override
  public boolean update(final Node node) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement("UPDATE " + TABLE_NAME + " "
          + "SET x_coord=?, "
          + "y_coord=?, floor=?, building=?, "
          + "node_type=?, long_name=?, short_name=?"
          + "WHERE node_id=?");
      statement.setInt(1, node.getXcoord());
      statement.setInt(2, node.getYcoord());
      statement.setInt(3, node.getFloor());
      statement.setString(4, node.getBuilding());
      statement.setString(5, node.getNodeType().name());
      statement.setString(6, node.getLongName());
      statement.setString(7, node.getShortName());
      statement.setString(8, node.getNodeId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to update Node", ex);
    }
    return false;
  }

  @Override
  public boolean delete(final Node node) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement
          = connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE node_id=?");
      statement.setString(1, node.getNodeId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to delete Node", ex);
    }
    return false;
  }

  private Node extractNodeFromResultSet(final ResultSet resultSet) throws SQLException {
    return new Node(
        resultSet.getString("node_id"),
        resultSet.getInt("xcoord"),
        resultSet.getInt("ycoord"),
        resultSet.getInt("floor"),
        resultSet.getString("building"),
        Node.NodeType.get(resultSet.getString("node_type")),
        resultSet.getString("long_name"),
        resultSet.getString("short_name")
    );
  }

  private void createTable() throws SQLException {
    try (Connection connection = dcf.getConnection();
         ResultSet resultSet = connection.getMetaData().getTables(null, null, TABLE_NAME, null)) {
      if (!resultSet.next()) {
        logger.info("Table " + TABLE_NAME + " does not exist. Creating");
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE " + TABLE_NAME
            + "(node_id VARCHAR(100) PRIMARY KEY,"
            + "xcoord INT,"
            + "yoord INT,"
            + "floor INT,"
            + "building VARCHAR(100),"
            + "node_type VARCHAR(100),"
            + "long_name VARCHAR(100),"
            + "short_name VARCHAR(100))");
        statement.executeUpdate();
        logger.info("Table " + TABLE_NAME + " created");
      } else {
        logger.info("Table " + TABLE_NAME + " exists");
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to create table", ex);
      throw ex;
    }
  }
}
