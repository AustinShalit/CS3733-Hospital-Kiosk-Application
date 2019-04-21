package edu.wpi.cs3733.d19.teamO.entity.database;

import edu.wpi.cs3733.d19.teamO.entity.AudioVisualRequest;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AudioVisualRequestDaoDb implements AudioVisualRequestDao {

    private static final String QUERY_FILE_NAME =
            "audio_visual_request_queries.properties";

    private static final Logger logger =
            Logger.getLogger(AudioVisualRequestDaoDb.class.getName());
    private static final Properties queries;

    static {
        queries = new Properties();
        try (InputStream is = AudioVisualRequestDaoDb.class
                .getResourceAsStream(QUERY_FILE_NAME)) {
            queries.load(is);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unable to load property file: " + QUERY_FILE_NAME, ex);
        }
    }

    private static final String TABLE_NAME = queries.getProperty(
            "audio_visual_request.table_name");
    private DatabaseConnectionFactory dcf;
    private NodeDaoDb nodeDaoDb;

    AudioVisualRequestDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
        this.dcf = dcf;
        nodeDaoDb = new NodeDaoDb(dcf);
        createTable();
    }

    AudioVisualRequestDaoDb() throws SQLException {
        this(new DatabaseConnectionFactoryEmbedded());
    }

    @Override
    public Optional<AudioVisualRequest> get(final Integer id) {
        try (Connection connection = dcf.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    queries.getProperty("audio_visual_request.select")
            );

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(extractAudioVisualRequestFromResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            logger.log(Level.WARNING, "Failed to get AudioVisualRequest", exception);
        }
        return Optional.empty();
    }

    @Override
    public Set<AudioVisualRequest> getAll() {
        try (Connection connection = dcf.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    queries.getProperty("audio_visual_request.select_all")
            );

            try (ResultSet resultSet = statement.executeQuery()) {
                Set<AudioVisualRequest> audioVisualRequests = new HashSet<>();
                while (resultSet.next()) {
                    audioVisualRequests.add(extractAudioVisualRequestFromResultSet(
                            resultSet));
                }
                return audioVisualRequests;
            }
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "Failed to get AudioVisualRequests", ex);
        }
        return Collections.emptySet();
    }

    private AudioVisualRequest extractAudioVisualRequestFromResultSet(
            final ResultSet resultSet) throws SQLException {
        return new AudioVisualRequest(
                resultSet.getInt("sr_id"),
                resultSet.getTimestamp("TIMEREQUESTED").toLocalDateTime(),
                resultSet.getTimestamp("TIMECOMPLETED").toLocalDateTime(),
                nodeDaoDb.get(resultSet
                        .getString("LOCATIONNODEID"))
                        .orElseThrow(() -> new SQLException(
                                "Could not get node for audio visual request")),
                resultSet.getString("WHOCOMPLETED"),
                AudioVisualRequest.AudioVisualRequestType.get(
                        resultSet.getString("AUDIOVISUALTYPE")),
                resultSet.getString("DESCRIPTION"),
                resultSet.getString("NAME")
        );
    }

    @Override
    public boolean insert(final AudioVisualRequest audioVisualRequest) {
        try (Connection connection = dcf.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    queries.getProperty("audio_visual_request.insert"),
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setTimestamp(1,
                    Timestamp.valueOf(audioVisualRequest.getTimeRequested()));
            statement.setTimestamp(2,
                    Timestamp.valueOf(audioVisualRequest.getTimeCompleted()));
            statement.setString(3, audioVisualRequest.getWhoCompleted());
            statement.setString(4,
                    audioVisualRequest.getLocationNode().getNodeId());
            statement.setString(5, audioVisualRequest.getType().name());
            statement.setString(6, audioVisualRequest.getDescription());
            statement.setString(7, audioVisualRequest.getPerson());

            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    return false;
                }
                audioVisualRequest.setId(keys.getInt(1));
                return true;
            }
        } catch (SQLException exception) {
            logger.log(Level.WARNING, "Failed to insert AudioVisualRequest", exception);
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
                        "audio_visual_request.create_table"));
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
    public boolean update(AudioVisualRequest audioVisualRequest) {
        try (Connection connection = dcf.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    queries.getProperty("audio_visual_request.update")
            );
            statement.setTimestamp(1,
                    Timestamp.valueOf(audioVisualRequest.getTimeRequested()));
            statement.setTimestamp(2,
                    Timestamp.valueOf(audioVisualRequest.getTimeCompleted()));
            statement.setString(3, audioVisualRequest.getWhoCompleted());
            statement.setString(4, audioVisualRequest.getLocationNode().getNodeId());
            statement.setString(5, audioVisualRequest.getType().name());
            statement.setString(6, audioVisualRequest.getDescription());
            statement.setString(7, audioVisualRequest.getPerson());
            statement.setInt(8, audioVisualRequest.getId());

            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "Failed to update AudioVisualRequest", ex);
        }
        return false;
    }

    @Override
    public boolean delete(AudioVisualRequest audioVisualRequest) {
        try (Connection connection = dcf.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    queries.getProperty("audio_visual_request.delete"));
            statement.setInt(1, audioVisualRequest.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "FAILED to delete AudioVisualRequest");
        }
        return false;
    }
}
