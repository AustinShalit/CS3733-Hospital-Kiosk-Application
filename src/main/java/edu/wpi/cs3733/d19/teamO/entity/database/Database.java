package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;

public class Database {

  private final NodeDaoDb nodeDaoDb;
  private final EdgeDaoDb edgeDaoDb;
  private final SecurityRequestDaoDb securityRequestDaoDb;

  Database(DatabaseConnectionFactory dcf) throws SQLException {
    this.nodeDaoDb = new NodeDaoDb(dcf);
    this.edgeDaoDb = new EdgeDaoDb(dcf);
    this.securityRequestDaoDb = new SecurityRequestDaoDb(dcf);
  }

  /**
   * Create a new database in memory.
   *
   * @param memoryName The name of the database
   */
  public Database(String memoryName) throws SQLException {
    this(new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
        memoryName));
  }

  public Database() throws SQLException {
    this(new DatabaseConnectionFactoryEmbedded());
  }

  /*
   * Node methods
   */

  public Optional<Node> getNode(String id) {
    return nodeDaoDb.get(id);
  }

  public Set<Node> getAllNodes() {
    return nodeDaoDb.getAll();
  }

  public boolean insertNode(Node node) {
    return nodeDaoDb.insert(node);
  }

  public boolean deleteNode(Node node) {
    return nodeDaoDb.delete(node);
  }

  public boolean updateNode(Node node) {
    return nodeDaoDb.update(node);
  }

  /*
   * Edge
   */
  public Optional<Edge> getEdge(String id) {
    return edgeDaoDb.get(id);
  }

  public Set<Edge> getAllEdges() {
    return edgeDaoDb.getAll();
  }

  public boolean insertEdge(Edge edge) {
    return edgeDaoDb.insert(edge);
  }

  public boolean deleteEdge(Edge edge) {
    return edgeDaoDb.delete(edge);
  }

  public boolean updateEdge(Edge edge) {
    return edgeDaoDb.update(edge);
  }

  public Set<Edge> getEdgesFor(Node node) {
    return edgeDaoDb.getEdgesFor(node);
  }

  /*
   * Sanitation
   */


  /*
   * Security
   */

  public Optional<SecurityRequest> getSecurityRequest(Integer id) {
    return securityRequestDaoDb.get(id);
  }

  public Set<SecurityRequest> getAllSecurityRequests() {
    return securityRequestDaoDb.getAll();
  }

  public boolean insertSecurityRequest(SecurityRequest securityRequest) {
    return securityRequestDaoDb.insert(securityRequest);
  }

  public boolean deleteSecurityRequest(SecurityRequest securityRequest) {
    return securityRequestDaoDb.delete(securityRequest);
  }

  public boolean updateSecurity(SecurityRequest securityRequest) {
    return securityRequestDaoDb.update(securityRequest);
  }


  /*
   * Scheduling
   */

}
