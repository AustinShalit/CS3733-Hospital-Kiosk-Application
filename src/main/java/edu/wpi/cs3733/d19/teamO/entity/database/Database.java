package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Login;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;
import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;

@SuppressWarnings("PMD.TooManyMethods")
public class Database {

  private final NodeDao nodeDao;
  private final EdgeDao edgeDao;
  private final SecurityRequestDao securityRequestDao;
  private final SanitationRequestDao sanitationRequestDao;
  private final LoginDao loginDao;


  Database(DatabaseConnectionFactory dcf) throws SQLException {
    this.nodeDao = new NodeDaoDb(dcf);
    this.edgeDao = new EdgeDaoDb(dcf);
    this.securityRequestDao = new SecurityRequestDaoDb(dcf);
    this.sanitationRequestDao = new SanitationRequestDaoDb(dcf);
    this.loginDao = new LoginDaoDb(dcf);
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
    return nodeDao.get(id);
  }

  public Set<Node> getAllNodes() {
    return nodeDao.getAll();
  }

  public boolean insertNode(Node node) {
    return nodeDao.insert(node);
  }

  public boolean deleteNode(Node node) {
    return nodeDao.delete(node);
  }

  public boolean updateNode(Node node) {
    return nodeDao.update(node);
  }

  /*
   * Edge
   */
  public Optional<Edge> getEdge(String id) {
    return edgeDao.get(id);
  }

  public Set<Edge> getAllEdges() {
    return edgeDao.getAll();
  }

  public boolean insertEdge(Edge edge) {
    return edgeDao.insert(edge);
  }

  public boolean deleteEdge(Edge edge) {
    return edgeDao.delete(edge);
  }

  public boolean updateEdge(Edge edge) {
    return edgeDao.update(edge);
  }

  public Set<Edge> getEdgesFor(Node node) {
    return edgeDao.getEdgesFor(node);
  }

  /*
   * Sanitation
   */
  public Optional<SanitationRequest> getSanitationRequest(int id) {
    return sanitationRequestDao.get(id);
  }

  public Set<SanitationRequest> getAllSanitationRequests() {
    return sanitationRequestDao.getAll();
  }

  public boolean insertSanitationRequest(SanitationRequest sanitationRequest) {
    return sanitationRequestDao.insert(sanitationRequest);
  }

  public boolean deleteSanitationRequest(SanitationRequest sanitationRequest) {
    return sanitationRequestDao.delete(sanitationRequest);
  }

  public boolean updateSanitationRequest(SanitationRequest sanitationRequest) {
    return sanitationRequestDao.update(sanitationRequest);
  }

  /*
   * Security
   */
  public Optional<SecurityRequest> getSecurityRequest(int id) {
    return securityRequestDao.get(id);
  }

  public Set<SecurityRequest> getAllSecurityRequests() {
    return securityRequestDao.getAll();
  }

  public boolean insertSecurityRequest(SecurityRequest securityRequest) {
    return securityRequestDao.insert(securityRequest);
  }

  public boolean deleteSecurityRequest(SecurityRequest securityRequest) {
    return securityRequestDao.delete(securityRequest);
  }

  public boolean updateSecurity(SecurityRequest securityRequest) {
    return securityRequestDao.update(securityRequest);
  }

  /*
   * Login
   */
  public Optional<Login> getLogin(String username) {
    return loginDao.get(username);
  }

  public Set<Login> getAllLogin() {
    return loginDao.getAll();
  }

  public boolean insertLogin(Login login) {
    return loginDao.insert(login);
  }

  public boolean deleteLogin(Login login) {
    return loginDao.delete(login);
  }

  public boolean updateLogin(Login login) {
    return loginDao.update(login);
  }

  /*
   * Scheduling
   */

}
