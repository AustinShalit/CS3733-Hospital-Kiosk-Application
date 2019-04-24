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
    if (database.getAllEmployee().size() == 0) {
      loadEmployees();
    }
  }

  private void loadNodes() {
    NodeCsvReaderWriter ncrw = new NodeCsvReaderWriter();
    List<Node> nodes = ncrw.readNodes(new InputStreamReader(DefaultInformationLoader.class
        .getResourceAsStream("nodes.csv"), UTF_8));
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
            .getResourceAsStream("edges.csv"), UTF_8));
    for (Edge edge : edges) {
      database.insertEdge(edge);
    }
  }

  private void loadEmployees() {

    Employee user1 = new Employee("admin", "wong", "AdminTest",
        Employee.EmployeeType.ADMIN, "2038028974");
    Employee user2 = new Employee("staff", "staff", "StaffTest",
        Employee.EmployeeType.DEFAULT, "5087333883");
    Employee user3 = new Employee("teamo", "won", "Ken",
        Employee.EmployeeType.DEFAULT, "8605027788");

    database.insertEmployee(user1);
    database.insertEmployee(user2);
    database.insertEmployee(user3);
  }
}
