package edu.wpi.cs3733.d19.teamO.entity.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.cs3733.d19.teamO.entity.Node;

class NodeDaoDb implements NodeDao {

  private static final String QUERY_FILE_NAME = "node_queries.properties";

  private static final Logger logger
      = Logger.getLogger(NodeDaoDb.class.getName());
  private static final Properties queries;

  static {
    queries = new Properties();
    try (InputStream is = NodeDaoDb.class.getResourceAsStream(QUERY_FILE_NAME)) {
      queries.load(is);
    } catch (IOException ex) {
      logger.log(Level.SEVERE, "Unable to load property file: " + QUERY_FILE_NAME, ex);
    }
  }

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
          = connection.prepareStatement(queries.getProperty("node.select"));
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
          = connection.prepareStatement(queries.getProperty("node.select_all"));
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
      PreparedStatement statement = connection.prepareStatement(queries.getProperty("node.insert"));
      statement.setString(1, node.getNodeId());
      statement.setInt(2, node.getXcoord());
      statement.setInt(3, node.getYcoord());
      statement.setString(4, node.getFloor());
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
      PreparedStatement statement = connection.prepareStatement(queries.getProperty("node.update"));
      statement.setInt(1, node.getXcoord());
      statement.setInt(2, node.getYcoord());
      statement.setString(3, node.getFloor());
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
          = connection.prepareStatement(queries.getProperty("node.delete"));
      statement.setString(1, node.getNodeId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to delete Node", ex);
    }
    return false;
  }

  private Node extractNodeFromResultSet(final ResultSet resultSet) throws SQLException {
    return new Node(
        resultSet.getString("id"),
        resultSet.getInt("x"),
        resultSet.getInt("y"),
        resultSet.getString("floor"),
        resultSet.getString("building"),
        Node.NodeType.get(resultSet.getString("type")),
        resultSet.getString("long_name"),
        resultSet.getString("short_name")
    );
  }

  private void createTable() throws SQLException {
    try (Connection connection = dcf.getConnection();
         ResultSet resultSet = connection.getMetaData().getTables(null, null,
             queries.getProperty("node.table_name"), null)) {
      if (!resultSet.next()) {
        logger.info("Table "
            + queries.getProperty("node.table_name")
            + " does not exist. Creating");
        PreparedStatement statement
            = connection.prepareStatement(queries.getProperty("node.create_table"));
        statement.executeUpdate();
        logger.info("Table " + queries.getProperty("node.table_name") + " created");
      } else {
        logger.info("Table " + queries.getProperty("node.table_name") + " exists");
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to create table", ex);
      throw ex;
    }
  }

  @Override
  public Set<Node> getAllRooms(String type) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement
          = connection.prepareStatement(queries.getProperty("node.select_room"));
      statement.setString(1, type);
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
  public Set<Node> getFloor(String floor) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement
          = connection.prepareStatement(queries.getProperty("node.select_floor"));
      statement.setString(1, floor);
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
}
