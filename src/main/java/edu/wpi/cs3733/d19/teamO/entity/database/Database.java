package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import edu.wpi.cs3733.d19.teamO.entity.AudioVisualRequest;
import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Employee;
import edu.wpi.cs3733.d19.teamO.entity.ExternalTransportationRequest;
import edu.wpi.cs3733.d19.teamO.entity.GiftRequest;
import edu.wpi.cs3733.d19.teamO.entity.ITSupportRequest;
import edu.wpi.cs3733.d19.teamO.entity.InternalTransportationRequest;
import edu.wpi.cs3733.d19.teamO.entity.InterpreterRequest;
import edu.wpi.cs3733.d19.teamO.entity.Login;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.PatientInfoRequest;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;
import edu.wpi.cs3733.d19.teamO.entity.SchedulingRequest;
import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.GodClass", "PMD.ExcessivePublicCount"})

public class Database {

  private final NodeDao nodeDao;
  private final EdgeDao edgeDao;
  private final SecurityRequestDao securityRequestDao;
  private final SanitationRequestDao sanitationRequestDao;
  private final SchedulingRequestDao schedulingRequestDao;
  private final LoginDao loginDao;
  private final InternalTransportationRequestDao itransportationDao;

  private final AudioVisualRequestDao audioVisualRequestDao;

  private final PatientInfoRequestDaoDb patientInfoRequestDao;
  private final ExternalTransportationRequestDao etransportationDao;
  private final GiftRequestDao giftRequestDao;
  private final ITSupportRequestDao itSupportDao;
  private final EmployeeDao employeeDao;
  private final InterpreterRequestDao interpreterDao;

  Database(DatabaseConnectionFactory dcf) throws SQLException {
    this.nodeDao = new NodeDaoDb(dcf);
    this.edgeDao = new EdgeDaoDb(dcf);
    this.securityRequestDao = new SecurityRequestDaoDb(dcf);
    this.sanitationRequestDao = new SanitationRequestDaoDb(dcf);
    this.schedulingRequestDao = new SchedulingRequestDaoDb(dcf);
    this.itransportationDao = new InternalTransportationRequestDaoDb(dcf);

    this.audioVisualRequestDao = new AudioVisualRequestDaoDb(dcf);

    this.patientInfoRequestDao = new PatientInfoRequestDaoDb(dcf);
    this.etransportationDao = new ExternalTransportationRequestDaoDb(dcf);
    this.itSupportDao = new ITSupportRequestDaoDb(dcf);
    this.employeeDao = new EmployeeDaoDb(dcf);
    this.loginDao = new LoginDaoDb(dcf);
    this.interpreterDao = new InterpreterRequestDaoDb(dcf);
    this.giftRequestDao = new GiftRequestDaoDb(dcf);
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


  public Set<Node> getAllRooms(String type) {
    return nodeDao.getAllRooms(type);
  }

  public Set<Node> getFloor(String floor) {
    return nodeDao.getFloor(floor);
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
  public Optional<SchedulingRequest> getSchedulingRequest(int id) {
    return schedulingRequestDao.get(id);
  }

  public Set<SchedulingRequest> getAllSchedulingRequests() {
    return schedulingRequestDao.getAll();
  }

  public boolean insertSchedulingrequest(SchedulingRequest schedulingRequest) {
    return schedulingRequestDao.insert(schedulingRequest);
  }

  public boolean deleteSchedulingRequest(SchedulingRequest schedulingRequest) {
    return schedulingRequestDao.delete(schedulingRequest);
  }

  public boolean updateScheduling(SchedulingRequest schedulingRequest) {
    return schedulingRequestDao.update(schedulingRequest);
  }

  /**
   * Used to check for conflicts before inserting them.
   *
   * @param schedulingRequest The scheduling request you want that might get added.
   * @return Returns false if no conflict, Returns true if there would be a conflict.
   */
  public boolean schedulingRequestWouldConflict(SchedulingRequest schedulingRequest) {
    return schedulingRequestDao.wouldConflict(schedulingRequest);
  }

  /*
   * Employee
   */
  public Optional<Employee> getEmployee(int id) {
    return employeeDao.get(id);
  }

  public Set<Employee> getAllEmployees() {
    return employeeDao.getAll();
  }

  public boolean insertEmployee(Employee employee) {
    return employeeDao.insert(employee);
  }

  public boolean deleteEmployee(Employee employee) {
    return employeeDao.delete(employee);
  }

  public boolean updateEmployee(Employee employee) {
    return employeeDao.update(employee);
  }

  /*
   * Audio Visual
   */
  public Optional<AudioVisualRequest> getAudioVisualRequest(int id) {
    return audioVisualRequestDao.get(id);
  }

  public Set<AudioVisualRequest> getAllAudioVisualRequests() {
    return audioVisualRequestDao.getAll();
  }

  public boolean insertAudioVisualRequest(
      AudioVisualRequest audioVisualRequest) {
    return audioVisualRequestDao.insert(audioVisualRequest);
  }

  public boolean deleteAudioVisualRequest(
      AudioVisualRequest audioVisualRequest) {
    return audioVisualRequestDao.delete(audioVisualRequest);
  }

  public boolean updateAudioVisualRequest(
      AudioVisualRequest audioVisualRequest) {
    return audioVisualRequestDao.update(audioVisualRequest);
  }

  /*
   * Internal Transporation
   */
  public Optional<InternalTransportationRequest> getInternalTransportationRequest(int id) {
    return itransportationDao.get(id);
  }

  public Set<InternalTransportationRequest> getAllInternalTransportationRequests() {
    return itransportationDao.getAll();
  }

  public boolean insertInternalTransportationRequest(
      InternalTransportationRequest internalTransportationRequest) {
    return itransportationDao.insert(internalTransportationRequest);
  }

  public boolean deleteInternalTransportationRequest(
      InternalTransportationRequest internalTransportationRequest) {
    return itransportationDao.delete(internalTransportationRequest);
  }

  public boolean updateInternalTransportationRequest(
      InternalTransportationRequest internalTransportationRequest) {
    return itransportationDao.update(internalTransportationRequest);
  }

  /*
   * Patient info
   */
  public Optional<PatientInfoRequest> getPatientInfoRequest(int id) {
    return patientInfoRequestDao.get(id);
  }

  public Set<PatientInfoRequest> getAllPatientInfoRequests() {
    return patientInfoRequestDao.getAll();
  }

  public boolean insertPatientInfoRequest(PatientInfoRequest patientInfoRequest) {
    return patientInfoRequestDao.insert(patientInfoRequest);
  }

  public boolean deletePatientInfoRequest(PatientInfoRequest patientInfoRequest) {
    return patientInfoRequestDao.delete(patientInfoRequest);
  }

  public boolean updatePatientInfoRequest(PatientInfoRequest patientInfoRequest) {
    return patientInfoRequestDao.update(patientInfoRequest);
  }

  /*
   * IT Support
   */
  public Optional<ITSupportRequest> getITSupportRequest(int id) {
    return itSupportDao.get(id);
  }

  public Set<ITSupportRequest> getAllITSupportRequests() {
    return itSupportDao.getAll();
  }

  public boolean insertITSupportRequest(
      ITSupportRequest itSupportRequest) {
    return itSupportDao.insert(itSupportRequest);
  }

  public boolean deleteITSupportRequest(
      ITSupportRequest itSupportRequest) {
    return itSupportDao.delete(itSupportRequest);
  }

  public boolean updateITSupportRequest(
      ITSupportRequest itSupportRequest) {
    return itSupportDao.update(itSupportRequest);
  }

  /*
   * Interpretation
   */
  public Optional<InterpreterRequest> getInterpreterRequest(int id) {
    return interpreterDao.get(id);
  }

  public Set<InterpreterRequest> getAllInterpreterRequests() {
    return interpreterDao.getAll();
  }

  public boolean insertInterpreterRequest(InterpreterRequest interpreterRequest) {
    return interpreterDao.insert(interpreterRequest);
  }

  public boolean deleteInterpreterRequest(InterpreterRequest interpreterRequest) {
    return interpreterDao.delete(interpreterRequest);
  }

  public boolean updateInterpreterRequest(InterpreterRequest interpreterRequest) {
    return interpreterDao.update(interpreterRequest);
  }

  /*
   * External Transporation
   */
  public Optional<ExternalTransportationRequest> getExternalTransportationRequest(int id) {
    return etransportationDao.get(id);
  }

  public Set<ExternalTransportationRequest> getAllExternalTransportationRequests() {
    return etransportationDao.getAll();
  }

  public boolean insertExternalTransportationRequest(
      ExternalTransportationRequest externalTransportationRequest) {
    return etransportationDao.insert(externalTransportationRequest);
  }

  public boolean deleteExternalTransportationRequest(
      ExternalTransportationRequest externalTransportationRequest) {
    return etransportationDao.delete(externalTransportationRequest);
  }

  public boolean updateExternalTransportationRequest(
      ExternalTransportationRequest externalTransportationRequest) {
    return etransportationDao.update(externalTransportationRequest);
  }

  /*
   * Gift Request
   */
  public Optional<GiftRequest> getGiftRequest(int id) {
    return giftRequestDao.get(id);
  }

  public Set<GiftRequest> getAllGiftRequests() {
    return giftRequestDao.getAll();
  }

  public boolean insertGiftRequest(
      GiftRequest giftRequest) {
    return giftRequestDao.insert(giftRequest);
  }

  public boolean deleteGiftRequest(
      GiftRequest giftRequest) {
    return giftRequestDao.delete(giftRequest);
  }

  public boolean updateGiftRequest(
      GiftRequest giftRequest) {
    return giftRequestDao.update(giftRequest);
  }
}
