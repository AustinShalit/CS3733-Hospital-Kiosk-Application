package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Database {
  List<Node> ll;

  /**
   * The class containing all interaction implementations with the database.
   */
  public Database() {
    CSVReader csvReader = new CSVReader();
    ll = csvReader.getNodesInCSV();

    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
    } catch (ClassNotFoundException ex) {
      System.out.println("Soo.. the driver is missing..");
      ex.printStackTrace();
    }

    Connection connection = null;  // NOPMD

    try {
      // Establish Connection
      DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
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


  List<Node> getNodes() {
    return ll;
  }

  void updateNode(String nodeId, Node newNode) {
    //
  }

  void deleteNode(String nodeId) {
    //
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
        new String[] {
            searchCriteria
        });
    while (rs.next()) {
      set.add(rs.getString("TABLE_NAME").toLowerCase(Locale.ENGLISH));
    }
  }
}
