package edu.wpi.cs3733.d19.teamO;

import java.io.IOException;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import edu.wpi.cs3733.d19.teamO.controller.v2.ControllerModule;
import edu.wpi.cs3733.d19.teamO.controller.v2.LoginController;
import edu.wpi.cs3733.d19.teamO.controller.v2.MainController;

public class Project extends Application {

  private static final Logger logger
      = Logger.getLogger(Project.class.getName());

  @Inject
  private EventBus eventBus;
  @Inject
  private LoginController.Factory loginControllerFactory;

  @Override
  public void start(final Stage primaryStage) throws IOException {
    logger.config("Starting application");

    Injector injector = Guice.createInjector(new ProjectModule(), new ControllerModule());
    injector.injectMembers(this);

    FXMLLoader loader = new FXMLLoader(MainController.class.getResource("Main.fxml"));
    loader.setControllerFactory(injector::getInstance);
    Parent root = loader.load();

    eventBus.post(loginControllerFactory.create());

    primaryStage.setTitle("Team O Kiosk Application");
    primaryStage.setScene(new Scene(root));

    // Set original window size and position
    primaryStage.setMinWidth(1280);
    primaryStage.setMinHeight(780);
    primaryStage.setWidth(1280);
    primaryStage.setHeight(780);

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
