package edu.wpi.cs3733.d19.teamO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.javafx.application.PlatformImpl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.csv.EdgeCsvReaderWriter;
import edu.wpi.cs3733.d19.teamO.entity.csv.NodeCsvReaderWriter;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class Project extends Application {

  private final Controller controller = new Controller();

  private static final Logger logger
      = Logger.getLogger(Project.class.getName());

  private static Database database;
  private Parent root;

  @Override
  public void start(final Stage primaryStage) throws IOException, SQLException {
    logger.config("Starting application");

    database = new Database();

    NodeCsvReaderWriter ncrw = new NodeCsvReaderWriter();
    EdgeCsvReaderWriter ecrw = new EdgeCsvReaderWriter(database);
    Path path = Paths.get("src/main/resources/edu/wpi/cs3733/d19/teamO/csv");
    List<Node> nodes = ncrw.readNodes(Files.newBufferedReader(path.resolve("nodesv4.csv")));
    for (Node node : nodes) {
      database.insertNode(node);
    }
    List<Edge> edges = ecrw.readEdges(Files.newBufferedReader(path.resolve("edgesv5.csv")));
    for (Edge edge : edges) {
      database.insertEdge(edge);
    }

    FXMLLoader loader = new FXMLLoader(getClass().getResource("controller/LoginWindow.fxml"));
    root = loader.load();

    primaryStage.setTitle("Team O Kiosk Application");

    primaryStage.setScene(new Scene(root));

    // Set original window size and position
    controller.minWindowSize(primaryStage);
    controller.setWindowSize(primaryStage, 1200, 900);
    controller.setWindowPosition(primaryStage, 0, 0);

    primaryStage.hide();
    primaryStage.show();
    logger.config("Startup complete");

    Thread.setDefaultUncaughtExceptionHandler(this::onThreadException);
  }

  /**
   * The version of this build.
   *
   * @return String representing the version
   */
  public static String getVersion() {
    String version = Project.class.getPackage().getImplementationVersion();
    if (version == null) {
      return "Development";
    }
    return version;
  }

  @SuppressWarnings("PMD.AvoidCatchingThrowable")
  private void onThreadException(Thread thread, Throwable throwable) {
    PlatformImpl.runAndWait(() -> {
      try {
        // Don't create more than one exception dialog at the same time
        final ExceptionAlert exceptionAlert = new ExceptionAlert(root, throwable,
            thread.getName() + ": " + throwable.getMessage(), getHostServices());
        exceptionAlert.setInitialFocus();
        exceptionAlert.showAndWait();
      } catch (Throwable ex) {
        // Well in this case something has gone very, very wrong
        // We don't want to create a feedback loop either.
        logger.log(Level.SEVERE, "Failed to show exception alert", throwable);
      }
    });
  }
}
