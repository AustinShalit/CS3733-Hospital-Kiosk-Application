package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;

public class Database {

  private final NodeDaoDb nodeDaoDb;
  final SanitationRequestDaoDb sanitationRequestDaoDb;

  Database(DatabaseConnectionFactoryImpl dcf) throws SQLException {
    this.nodeDaoDb = new NodeDaoDb(dcf);
    this.sanitationRequestDaoDb = new SanitationRequestDaoDb(dcf);
  }

  public Database() throws SQLException {
    this(new DatabaseConnectionFactoryImpl());
  }

  /*
   * Node methods
   */

  /**
   * Node methods.
   *
   * @see NodeDaoDb
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


  /*
   * Sanitation
   */
  public Optional<SanitationRequest> getSanitationRequest(int id) {
    return sanitationRequestDaoDb.get(id);
  }

  public Set<SanitationRequest> getAllSanitationRequests() {
    return sanitationRequestDaoDb.getAll();
  }

  public boolean insertSanitationRequest(SanitationRequest sanitationRequest) {
    return sanitationRequestDaoDb.insert(sanitationRequest);
  }

  public boolean deleteSanitationRequest(SanitationRequest sanitationRequest) {
    return sanitationRequestDaoDb.delete(sanitationRequest);
  }

  public boolean updateSanitationRequest(SanitationRequest sanitationRequest) {
    return sanitationRequestDaoDb.update(sanitationRequest);
  }

  /*
   * Security
   */


  /*
   * Scheduling
   */

}
