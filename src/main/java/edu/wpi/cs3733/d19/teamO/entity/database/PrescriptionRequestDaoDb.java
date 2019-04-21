package edu.wpi.cs3733.d19.teamO.entity.database;

import edu.wpi.cs3733.d19.teamO.entity.PrescriptionRequest;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrescriptionRequestDaoDb implements PrescriptionRequestDao {

    private static final String QUERY_FILE_NAME =
            "prescription_request_queries.properties";

    private static final Logger logger =
            Logger.getLogger(PrescriptionRequestDaoDb.class.getName());
    private static final Properties queries;

    static {
        queries = new Properties();
        try (InputStream is = PrescriptionRequestDaoDb.class
                .getResourceAsStream(QUERY_FILE_NAME)) {
            queries.load(is);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unable to load property file: " + QUERY_FILE_NAME, ex);
        }
    }

    private static final String TABLE_NAME = queries.getProperty(
            "prescription_request.table_name");
    private DatabaseConnectionFactory dcf;

    PrescriptionRequestDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
        this.dcf = dcf;
        createTable();
    }

    PrescriptionRequestDaoDb() throws SQLException {
        this(new DatabaseConnectionFactoryEmbedded());
    }

    @Override
    public Optional<PrescriptionRequest> get(final Integer id) {
        try (Connection connection = dcf.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    queries.getProperty("prescription_request.select")
            );

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(extractPrescriptionRequestFromResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            logger.log(Level.WARNING, "Failed to get PrescriptionRequest", exception);
        }
        return Optional.empty();
    }

    @Override
    public Set<PrescriptionRequest> getAll() {
        try (Connection connection = dcf.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    queries.getProperty("prescription_request.select_all")
            );

            try (ResultSet resultSet = statement.executeQuery()) {
                Set<PrescriptionRequest> prescriptionRequests = new HashSet<>();
                while (resultSet.next()) {
                    prescriptionRequests.add(extractPrescriptionRequestFromResultSet(
                            resultSet));
                }
                return prescriptionRequests;
            }
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "Failed to get PrescriptionRequests.", ex);
        }
        return Collections.emptySet();
    }

    private PrescriptionRequest extractPrescriptionRequestFromResultSet(
            final ResultSet resultSet) throws SQLException {
        return new PrescriptionRequest(resultSet.getInt("sr_id"),
                resultSet.getTimestamp("TIMEREQUESTED").toLocalDateTime(),
                resultSet.getTimestamp("TIMECOMPLETED").toLocalDateTime(),
                resultSet.getString("WHOREQUESTED"),
                resultSet.getString("PATIENTNAME"),
                resultSet.getDate("PATIENTDOB").toLocalDate(),
                resultSet.getString("MEDICATIONNAME"),
                resultSet.getString("MEDICATIONDOSAGE"),
                resultSet.getString("DESCRIPTION")
        );

    }


    @Override
    public boolean insert(final PrescriptionRequest prescriptionRequest) {
        try (Connection connection = dcf.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    queries.getProperty("prescription_request.insert"),
                    Statement.RETURN_GENERATED_KEYS
            );

            statement.setTimestamp(1,
                    Timestamp.valueOf(prescriptionRequest.getTimeRequested()));
            statement.setTimestamp(2,
                    Timestamp.valueOf(prescriptionRequest.getTimeCompleted()));
            statement.setString(3,
                    prescriptionRequest.getWhoRequested());
            statement.setString(4,
                    prescriptionRequest.getPatientName());
            statement.setDate(5,
                    Date.valueOf(prescriptionRequest.getPatientDOB()));
            statement.setString(6,
                    prescriptionRequest.getMedicationName());
            statement.setString(7,
                    prescriptionRequest.getMedicationDosage());
            statement.setString(8,
                    prescriptionRequest.getDescription());

            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    return false;
                }
                prescriptionRequest.setId(keys.getInt(1));
                return true;
            }
        } catch (SQLException exception) {
            logger.log(Level.WARNING, "Failed to insert PrescriptionRequest", exception);
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
                        "prescription_request.create_table"));
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
    public boolean update(PrescriptionRequest prescriptionRequest) {
        try (Connection connection = dcf.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    queries.getProperty("prescription_request.update")
            );

            statement.setTimestamp(1,
                    Timestamp.valueOf(prescriptionRequest.getTimeRequested()));
            statement.setTimestamp(2,
                    Timestamp.valueOf(prescriptionRequest.getTimeCompleted()));
            statement.setString(3,
                    prescriptionRequest.getWhoRequested());
            statement.setString(4,
                    prescriptionRequest.getPatientName());
            statement.setDate(5,
                    Date.valueOf(prescriptionRequest.getPatientDOB()));
            statement.setString(6,
                    prescriptionRequest.getMedicationName());
            statement.setString(7,
                    prescriptionRequest.getMedicationDosage());
            statement.setString(8,
                    prescriptionRequest.getDescription());
            statement.setInt(9, prescriptionRequest.getId());

            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "Failed to update PrescriptionRequest", ex);
        }
        return false;
    }

    @Override
    public boolean delete(PrescriptionRequest prescriptionRequest) {
        try (Connection connection = dcf.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    queries.getProperty("prescription_request.delete"));
            statement.setInt(1, prescriptionRequest.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "FAILED to delete PrescriptionRequest");
        }
        return false;
    }
}
