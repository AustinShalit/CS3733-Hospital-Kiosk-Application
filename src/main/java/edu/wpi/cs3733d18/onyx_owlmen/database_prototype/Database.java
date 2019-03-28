package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import java.nio.file.FileAlreadyExistsException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Database implements AutoCloseable {

  private static Database instance;
  private Connection connection;

  /**
   * The class containing all interaction implementations with the database.
   */
  public Database() {
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
      ex.printStackTrace();
    }

    //DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());


    try {
      // Establish Connection
      connection = DriverManager.getConnection("jdbc:derby:myDB;create=true");
      System.out.println("Connection Established");
      // Create tables
      //connection.createStatement().executeUpdate("drop table nodes");

      Set<String> tables = this.getDBTables(connection);
      System.out.println("Found the following tables:");
      for (String s : tables) {
        System.out.println(s);
      }

      if (!tables.contains("nodes")) {
        System.out.println("nodes table not in database, creating table");
        connection.createStatement().executeUpdate("create table nodes\n"
            + "        (\n"
            + "          nodeID varchar(100) not null,\n"
            + "          xcoord int,\n"
            + "          ycoord int,\n"
            + "          floor int,\n"
            + "          building varchar(100),\n"
            + "          nodeType varchar(100),\n"
            + "          longName varchar(100),\n"
            + "          shortName varchar(100)\n"
            + "        )");

        connection.createStatement().executeUpdate("create unique index nodes_nodeID_uindex\n"
            + "          on nodes (nodeID)");

        connection.createStatement().executeUpdate("alter table nodes\n"
            + "          add constraint nodes_pk\n"
            + "            primary key (nodeID)");
      } else {
        System.out.println("nodes table already exists in database.");
      }
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
      System.out.println("connection failed");
    }
  }

  /**
   * Get a singleton instance.
   */
  static synchronized Database getInstance() {
    if (instance == null) {
      instance = new Database();
    }
    return instance;
  }

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  List<Node> getNodes() {
    List<Node> nodes = new LinkedList<>();
    try (ResultSet resultSet
             = connection.prepareStatement("SELECT * from nodes").executeQuery()) {
      while (resultSet.next()) {
        nodes.add(new Node(
            resultSet.getString("nodeID"),
            resultSet.getInt("xcoord"),
            resultSet.getInt("ycoord"),
            resultSet.getInt("floor"),
            resultSet.getString("building"),
            resultSet.getString("nodeType"),
            resultSet.getString("longName"),
            resultSet.getString("shortName")
        ));
      }
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }
    return nodes;
  }

  void updateNode(String nodeId, Node newNode) {
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("UPDATE nodes "
          + "SET xcoord = (?), "
          + "ycoord = (?), floor = (?), building = (?), "
          + "nodeType = (?), longName = (?), shortName = (?)"
          + "WHERE nodeID = (?)");
      preparedStatement.setInt(1, newNode.xcoord);
      preparedStatement.setInt(2, newNode.ycoord);
      preparedStatement.setInt(3, newNode.floor);
      preparedStatement.setString(4, newNode.building);
      preparedStatement.setString(5, newNode.nodeType);
      preparedStatement.setString(6, newNode.longName);
      preparedStatement.setString(7, newNode.shortName);
      preparedStatement.setString(8, nodeId);
      preparedStatement.executeUpdate();
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }
  }

  void addNode(Node newNode) {
    try {
      connection.createStatement().executeUpdate(
          String.format("INSERT INTO nodes VALUES ('%s', %d, %d, %d, '%s', '%s', '%s', '%s')",
              newNode.nodeID,
              newNode.xcoord,
              newNode.ycoord,
              newNode.floor,
              newNode.building,
              newNode.nodeType,
              newNode.longName,
              newNode.shortName)
      );
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }
  }

  void deleteNode(String nodeId) {
    // Yes this is vulnerable to an SQL Injection attack.
    // Will refactor later
    try {
      PreparedStatement statement
          = connection.prepareStatement("DELETE FROM nodes WHERE nodeID=?");
      statement.setString(1, nodeId);
      statement.executeUpdate();
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }
  }

  private Set<String> getDBTables(Connection targetDBConn) throws SQLException {
    Set<String> set = new HashSet<String>();
    DatabaseMetaData dbmeta = targetDBConn.getMetaData();
    readDBTable(set, dbmeta, "TABLE", null);
    readDBTable(set, dbmeta, "VIEW", null);
    return set;
  }

  private void readDBTable(
      Set<String> set,
      DatabaseMetaData dbmeta,
      String searchCriteria,
      String schema) throws SQLException {
    ResultSet rs = dbmeta.getTables( // NOPMD
        null,
        schema,
        null,
        new String[]{
            searchCriteria
        });
    while (rs.next()) {
      set.add(rs.getString("TABLE_NAME").toLowerCase(Locale.ENGLISH));
    }
  }

  @Override
  public void close() throws SQLException {
    connection.close();
  }
}
