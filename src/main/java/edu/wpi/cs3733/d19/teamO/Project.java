package edu.wpi.cs3733.d19.teamO;

import java.io.IOException;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Project extends Application {

  private static final Logger logger
      = Logger.getLogger(Project.class.getName());

  @Override
  public void start(final Stage primaryStage) throws IOException {
    logger.config("Starting application");

    FXMLLoader loader = new FXMLLoader(getClass().getResource("controller/LoginWindow.fxml"));
    Parent mainWindow = loader.load();

    primaryStage.setTitle("Team O Kiosk Application");
    primaryStage.setScene(new Scene(mainWindow));
    primaryStage.show();
    logger.config("Startup complete");
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
}
