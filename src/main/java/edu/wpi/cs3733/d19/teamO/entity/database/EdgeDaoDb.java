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

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;

class EdgeDaoDb implements EdgeDao {

  private static final String QUERY_FILE_NAME = "edge_queries.properties";

  private static final Logger logger
      = Logger.getLogger(EdgeDaoDb.class.getName());
  private static final Properties queries;

  static {
    queries = new Properties();
    try (InputStream is = EdgeDaoDb.class.getResourceAsStream(QUERY_FILE_NAME)) {
      queries.load(is);
    } catch (IOException ex) {
      logger.log(Level.SEVERE, "Unable to load property file: " + QUERY_FILE_NAME, ex);
    }
  }

  private DatabaseConnectionFactory dcf;
  private NodeDaoDb nodeDaoDb;

  EdgeDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
    this.dcf = dcf;
    nodeDaoDb = new NodeDaoDb(dcf);
    createTable();
  }

  EdgeDaoDb() throws SQLException {
    this(new DatabaseConnectionFactoryImpl());
  }

  @Override
  public Optional<Edge> get(final String id) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement
          = connection.prepareStatement(queries.getProperty("edge.select"));
      statement.setString(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(extractEdgeFromResultSet(resultSet));
        }
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get Edge", ex);
    }
    return Optional.empty();
  }

  @Override
  public Set<Edge> getAll() {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement
          = connection.prepareStatement(queries.getProperty("edge.select_all"));
      try (ResultSet resultSet = statement.executeQuery()) {
        Set<Edge> nodes = new HashSet<>();
        while (resultSet.next()) {
          nodes.add(extractEdgeFromResultSet(resultSet));
        }
        return nodes;
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get Edges", ex);
    }
    return Collections.emptySet();
  }

  @Override
  public boolean insert(final Edge edge) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(queries.getProperty("edge.insert"));
      statement.setString(1, edge.getEdgeId());
      statement.setString(2, edge.getStartNode().getNodeId());
      statement.setString(3, edge.getEndNode().getNodeId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to insert Node", ex);
    }
    return false;
  }

  @Override
  public boolean update(final Edge edge) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(queries.getProperty("edge.update"));
      statement.setString(1, edge.getStartNode().getNodeId());
      statement.setString(2, edge.getEndNode().getNodeId());
      statement.setString(3, edge.getEdgeId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to update Edge", ex);
    }
    return false;
  }

  @Override
  public boolean delete(final Edge edge) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement
          = connection.prepareStatement(queries.getProperty("edge.delete"));
      statement.setString(1, edge.getEdgeId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to delete Node", ex);
    }
    return false;
  }

  @Override
  public Set<Edge> getEdgesFor(final Node node) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement
          = connection.prepareStatement(queries.getProperty("edge.get_edges_for"));
      statement.setString(1, node.getNodeId());
      statement.setString(2, node.getNodeId());
      try (ResultSet resultSet = statement.executeQuery()) {
        Set<Edge> nodes = new HashSet<>();
        while (resultSet.next()) {
          nodes.add(extractEdgeFromResultSet(resultSet));
        }
        return nodes;
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get Edges", ex);
    }
    return Collections.emptySet();
  }

  private Edge extractEdgeFromResultSet(final ResultSet resultSet) throws SQLException {
    return new Edge(
        resultSet.getString("id"),
        nodeDaoDb.get(resultSet
            .getString("start_node"))
            .orElseThrow(() -> new SQLException("Could not get node for edge")),
        nodeDaoDb.get(resultSet
            .getString("end_node"))
            .orElseThrow(() -> new SQLException("Could not get node for edge"))
    );
  }

  private void createTable() throws SQLException {
    try (Connection connection = dcf.getConnection();
         ResultSet resultSet = connection.getMetaData().getTables(null, null,
             queries.getProperty("edge.table_name"), null)) {
      if (!resultSet.next()) {
        logger.info("Table "
            + queries.getProperty("edge.table_name")
            + " does not exist. Creating");
        PreparedStatement statement
            = connection.prepareStatement(queries.getProperty("edge.create_table"));
        statement.executeUpdate();
        logger.info("Table " + queries.getProperty("edge.table_name") + " created");
      } else {
        logger.info("Table " + queries.getProperty("edge.table_name") + " exists");
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to create table", ex);
      throw ex;
    }
  }
}
