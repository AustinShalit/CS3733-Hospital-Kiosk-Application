package edu.wpi.cs3733.d19.teamO.entity;

import java.io.InputStreamReader;
import java.util.List;

import edu.wpi.cs3733.d19.teamO.entity.csv.EdgeCsvReaderWriter;
import edu.wpi.cs3733.d19.teamO.entity.csv.NodeCsvReaderWriter;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DefaultInformationLoader {

  private final Database database;

  public DefaultInformationLoader(final Database database) {
    this.database = database;
  }

  /**
   * Load information from default only if the database is empty.
   */
  public void loadIfEmpty() {
    if (database.getAllNodes().size() == 0) {
      loadNodes();
      loadSchedulingNodes();
    }
    if (database.getAllEdges().size() == 0) {
      loadEdges();
    }
    if (database.getAllLogin().size() == 0) {
      loadEmployeesAndLogins();
    }
  }

  private void loadNodes() {
    NodeCsvReaderWriter ncrw = new NodeCsvReaderWriter();
    List<Node> nodes = ncrw.readNodes(new InputStreamReader(DefaultInformationLoader.class
        .getResourceAsStream("nodesv4.csv"), UTF_8));
    for (Node node : nodes) {
      database.insertNode(node);
    }
  }

  private void loadSchedulingNodes() {
    NodeCsvReaderWriter ncrw = new NodeCsvReaderWriter();
    List<Node> nodes = ncrw.readNodes(new InputStreamReader(DefaultInformationLoader.class
        .getResourceAsStream("schedulingNodes.csv"), UTF_8));
    for (Node node : nodes) {
      database.insertNode(node);
    }
  }

  private void loadEdges() {
    EdgeCsvReaderWriter ecrw = new EdgeCsvReaderWriter(database);
    List<Edge> edges = ecrw.readEdges(
        new InputStreamReader(DefaultInformationLoader.class
            .getResourceAsStream("edgesv5.csv"), UTF_8));
    for (Edge edge : edges) {
      database.insertEdge(edge);
    }
  }

  private void loadEmployeesAndLogins() {

    Login user1 = new Login("admin", "wong", "AdminTest",
        Login.EmployeeType.ADMIN);
    Login user2 = new Login("staff", "staff", "StaffTest",
        Login.EmployeeType.SECURITY);
    Login user3 = new Login("teamo", "won", "TeamOTest",
        Login.EmployeeType.DEFAULT);

    database.insertLogin(user1);
    database.insertLogin(user2);
    database.insertLogin(user3);
  }
}
