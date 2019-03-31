package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import edu.wpi.cs3733.d19.teamO.entity.Node;

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


  /*
   * Security
   */


  /*
   * Scheduling
   */

}
