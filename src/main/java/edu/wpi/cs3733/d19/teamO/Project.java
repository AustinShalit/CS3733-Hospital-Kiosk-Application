package edu.wpi.cs3733.d19.teamO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.sun.javafx.application.PlatformImpl;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import edu.wpi.cs3733.d19.teamO.controller.v2.ControllerModule;
import edu.wpi.cs3733.d19.teamO.controller.v2.LoginController;
import edu.wpi.cs3733.d19.teamO.controller.v2.MainController;
import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.csv.EdgeCsvReaderWriter;
import edu.wpi.cs3733.d19.teamO.entity.csv.NodeCsvReaderWriter;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class Project extends Application {

  private static final Logger logger
      = Logger.getLogger(Project.class.getName());

  @Inject
  private EventBus eventBus;
  @Inject
  private LoginController.Factory loginControllerFactory;

  private Injector injector;
  private Parent root;

  @Override
  public void init() {
    logger.config("Application init");

    injector = Guice.createInjector(new ProjectModule(), new ControllerModule());
    injector.injectMembers(this);


//    Database database = new Database();
//
//    NodeCsvReaderWriter ncrw = new NodeCsvReaderWriter();
//    EdgeCsvReaderWriter ecrw = new EdgeCsvReaderWriter(database);
//    Path path = Paths.get("src/main/resources/edu/wpi/cs3733/d19/teamO/csv");
//    List<Node> nodes = ncrw.readNodes(Files.newBufferedReader(path.resolve("nodesv4.csv")));
//    for (Node node : nodes) {
//      database.insertNode(node);
//    }
//    List<Edge> edges = ecrw.readEdges(Files.newBufferedReader(path.resolve("edgesv5.csv")));
//    for (Edge edge : edges) {
//      database.insertEdge(edge);
//    }


    logger.config("Application init complete");
  }

  @Override
  public void start(final Stage primaryStage) throws IOException {
    logger.config("Starting application");

    FXMLLoader loader = new FXMLLoader(MainController.class.getResource("Main.fxml"));
    loader.setControllerFactory(injector::getInstance);
    root = loader.load();

    Platform.runLater(()
        -> eventBus.post(new ChangeMainViewEvent(loginControllerFactory.create(), false)));

    primaryStage.setTitle("Team O Kiosk Application");
    primaryStage.setScene(new Scene(root));

    // Set original window size and position
    primaryStage.setMinWidth(1280);
    primaryStage.setMinHeight(780);
    primaryStage.setWidth(1280);
    primaryStage.setHeight(780);

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
