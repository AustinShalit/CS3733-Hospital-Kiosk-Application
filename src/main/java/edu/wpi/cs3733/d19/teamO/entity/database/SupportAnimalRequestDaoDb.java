package edu.wpi.cs3733.d19.teamO.entity.database;

import edu.wpi.cs3733.d19.teamO.entity.SupportAnimalRequest;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SupportAnimalRequestDaoDb implements SupportAnimalRequestDao {

    private static final String QUERY_FILE_NAME =
            "support_animal_request_queries.properties";

    private static final Logger logger =
            Logger.getLogger(SupportAnimalRequestDaoDb.class.getName());
    private static final Properties queries;

    static {
        queries = new Properties();
        try (InputStream is = SupportAnimalRequestDaoDb.class
                .getResourceAsStream(QUERY_FILE_NAME)) {
            queries.load(is);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unable to load property file: " + QUERY_FILE_NAME, ex);
        }
    }

    private static final String TABLE_NAME = queries.getProperty(
            "support_animal_request.table_name");
    private DatabaseConnectionFactory dcf;
    private NodeDaoDb nodeDaoDb;

    SupportAnimalRequestDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
        this.dcf = dcf;
        nodeDaoDb = new NodeDaoDb(dcf);
        createTable();
    }

    SupportAnimalRequestDaoDb() throws SQLException {
        this(new DatabaseConnectionFactoryEmbedded());
    }

    @Override
    public Optional<SupportAnimalRequest> get(final Integer id) {
        try (Connection connection = dcf.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    queries.getProperty("support_animal_request.select")
            );

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(extractRequestFromResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            logger.log(Level.WARNING, "Failed to get SupportAnimalRequest", exception);
        }
        return Optional.empty();
    }

    @Override
    public Set<SupportAnimalRequest> getAll() {
        try (Connection connection = dcf.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    queries.getProperty("support_animal_request.select_all")
            );

            try (ResultSet resultSet = statement.executeQuery()) {
                Set<SupportAnimalRequest> requests = new HashSet<>();
                while (resultSet.next()) {
                    requests.add(extractRequestFromResultSet(resultSet));
                }
                return requests;
            }
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "Failed to get SupportAnimalRequest", ex);
        }
        return Collections.emptySet();
    }

    @Override
    public boolean insert(final SupportAnimalRequest request) {
        try (Connection connection = dcf.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    queries.getProperty("support_animal_request.insert"),
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setTimestamp(1,
                    Timestamp.valueOf(request.getTimeRequested()));
            statement.setTimestamp(2,
                    Timestamp.valueOf(request.getTimeCompleted()));
            statement.setString(3, request.getWhoCompleted());
            statement.setString(4,
                    request.getLocationNode().getNodeId());
            statement.setString(5, request.getType().name());
            statement.setString(6, request.getDescription());
            statement.setString(7, request.getPerson());

            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    return false;
                }
                request.setId(keys.getInt(1));
                return true;
            }
        } catch (SQLException exception) {
            logger.log(Level.WARNING, "Failed to insert SupportAnimalRequest", exception);
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
                        "support_animal_request.create_table"));
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
    public boolean update(SupportAnimalRequest request) {
        try (Connection connection = dcf.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    queries.getProperty("support_animal_request.update")
            );
            statement.setTimestamp(1,
                    Timestamp.valueOf(request.getTimeRequested()));
            statement.setTimestamp(2,
                    Timestamp.valueOf(request.getTimeCompleted()));
            statement.setString(3, request.getWhoCompleted());
            statement.setString(4, request.getLocationNode().getNodeId());
            statement.setString(5, request.getType().name());
            statement.setString(6, request.getDescription());
            statement.setString(7, request.getPerson());
            statement.setInt(8, request.getId());

            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "Failed to update SupportAnimalRequest", ex);
        }
        return false;
    }

    @Override
    public boolean delete(SupportAnimalRequest request) {
        try (Connection connection = dcf.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    queries.getProperty("support_animal_request.delete"));
            statement.setInt(1, request.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "FAILED to delete SupportAnimalRequest");
        }
        return false;
    }

    private SupportAnimalRequest extractRequestFromResultSet(
            final ResultSet resultSet) throws SQLException {
        return new SupportAnimalRequest(
                resultSet.getInt("id"),
                resultSet.getTimestamp("time_requested").toLocalDateTime(),
                resultSet.getTimestamp("time_completed").toLocalDateTime(),
                nodeDaoDb.get(resultSet
                        .getString("location"))
                        .orElseThrow(() -> new SQLException(
                                "Could not get node for support animal request")),
                resultSet.getString("who_completed"),
                SupportAnimalRequest.SupportAnimalRequestType.get(
                        resultSet.getString("type")),
                resultSet.getString("description"),
                resultSet.getString("name")
        );
    }
}
