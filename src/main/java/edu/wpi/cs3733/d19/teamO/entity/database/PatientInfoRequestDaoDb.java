package edu.wpi.cs3733.d19.teamO.entity.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.cs3733.d19.teamO.entity.InternalTransportationRequest;
import edu.wpi.cs3733.d19.teamO.entity.PatientInfoRequest;

public class PatientInfoRequestDaoDb implements PatientInfoRequestDao {

  private static final String QUERY_FILE_NAME =
      "patient_info_request_queries.properties";

  private static final Logger logger =
      Logger.getLogger(PatientInfoRequestDaoDb.class.getName());
  private static final Properties queries;

  static {
    queries = new Properties();
    try (InputStream is = PatientInfoRequestDaoDb.class
        .getResourceAsStream(QUERY_FILE_NAME)) {
      queries.load(is);
    } catch (IOException ex) {
      logger.log(Level.SEVERE, "Unable to load property file: " + QUERY_FILE_NAME, ex);
    }
  }

  private static final String TABLE_NAME = queries.getProperty(
      "patient_info_request.table_name");
  private DatabaseConnectionFactory dcf;
  private NodeDaoDb nodeDaoDb;

  PatientInfoRequestDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
    this.dcf = dcf;
    nodeDaoDb = new NodeDaoDb(dcf);
    createTable();
  }

  PatientInfoRequestDaoDb() throws SQLException {
    this(new DatabaseConnectionFactoryEmbedded());
  }

  @Override
  public Optional<PatientInfoRequest> get(final Integer id) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("patient_info_request.select")
      );

      statement.setInt(1, id);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(extractPatientInfoRequestFromResultSet(resultSet));
        }
      }
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to get PatientInfoRequest", exception);
    }
    return Optional.empty();
  }

  @Override
  public Set<PatientInfoRequest> getAll() {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("patient_info_request.select_all")
      );

      try (ResultSet resultSet = statement.executeQuery()) {
        Set<PatientInfoRequest> patientInfoRequests = new HashSet<>();
        while (resultSet.next()) {
          patientInfoRequests.add(extractPatientInfoRequestFromResultSet(
              resultSet));
        }
        return patientInfoRequests;
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get PatientInfoRequests", ex);
    }
    return Collections.emptySet();
  }

  private PatientInfoRequest extractPatientInfoRequestFromResultSet(
      final ResultSet resultSet) throws SQLException {
    return new PatientInfoRequest(
        resultSet.getInt("sr_id"),
        resultSet.getTimestamp("TIMEREQUESTED").toLocalDateTime(),
        resultSet.getTimestamp("TIMECOMPLETED").toLocalDateTime(),
        nodeDaoDb.get(resultSet
            .getString("LOCATIONNODEID"))
            .orElseThrow(() -> new SQLException(
                "Could not get node for patient info request")),
        resultSet.getString("WHOCOMPLETED"),
        resultSet.getString("DESCRIPTION"),
        resultSet.getString("PATIENTNAME"),
        resultSet.getDate("PATIENTDOB").toLocalDate(),
        PatientInfoRequest.PatientInfoSex.get(
            resultSet.getString("PATIENTSEX"))
    );
  }

  @Override
  public boolean insert(final PatientInfoRequest patientInfoRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("patient_info_request.insert"),
          Statement.RETURN_GENERATED_KEYS
      );
      statement.setTimestamp(1,
          Timestamp.valueOf(patientInfoRequest.getTimeRequested()));
      statement.setTimestamp(2,
          Timestamp.valueOf(patientInfoRequest.getTimeCompleted()));
      statement.setString(3, patientInfoRequest.getWhoCompleted());
      statement.setString(4, patientInfoRequest.getDescription());
      statement.setString(5,
          patientInfoRequest.getLocationNode().getNodeId());
      statement.setString(6, patientInfoRequest.getPatientName());
      statement.setDate(7, Date.valueOf(patientInfoRequest.getPatientDOB()));
      statement.setString(8, patientInfoRequest.getPatientSex().toString());

      statement.executeUpdate();
      try (ResultSet keys = statement.getGeneratedKeys()) {
        if (!keys.next()) {
          return false;
        }
        patientInfoRequest.setId(keys.getInt(1));
        return true;
      }
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to insert PatientInfoRequest", exception);
    }
    return false;
  }

  private void createTable() throws SQLException {

    try (Connection connection = dcf.getConnection();
         ResultSet resultSet = connection.getMetaData().getTables(null, null,
             TABLE_NAME, null)) {
      if (!resultSet.next()) {
        logger.info("Table "
            + TABLE_NAME
            + " does not exist. Creating");
        PreparedStatement statement
            = connection.prepareStatement(queries.getProperty(
            "patient_info_request.create_table"));
        statement.executeUpdate();
        logger.info("Table " + TABLE_NAME + " created");
      } else {
        logger.info("Table " + TABLE_NAME + " exists");
      }
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to create table", exception);
      throw exception;
    }
  }

  @Override
  public boolean update(PatientInfoRequest patientInfoRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("patient_info_request.update")
      );
      statement.setTimestamp(1,
          Timestamp.valueOf(patientInfoRequest.getTimeRequested()));
      statement.setTimestamp(2,
          Timestamp.valueOf(patientInfoRequest.getTimeCompleted()));
      statement.setString(3, patientInfoRequest.getWhoCompleted());
      statement.setString(4, patientInfoRequest.getDescription());
      statement.setString(5, patientInfoRequest.getLocationNode().getNodeId());
      statement.setString(6, patientInfoRequest.getPatientName());
      statement.setDate(7, Date.valueOf(patientInfoRequest.getPatientDOB()));
      statement.setString(8, patientInfoRequest.getPatientSex().toString());
      statement.setInt(9, patientInfoRequest.getId());

      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to update PatientInfoRequest", ex);
    }
    return false;
  }

  @Override
  public boolean delete(PatientInfoRequest patientInfoRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("patient_info_request.delete"));
      statement.setInt(1, patientInfoRequest.getId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "FAILED to delete PatientInfoRequest");
    }
    return false;
  }
}
