package edu.wpi.cs3733.d19.teamO;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.sun.javafx.application.PlatformImpl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import edu.wpi.cs3733.d19.teamO.controller.ControllerModule;
import edu.wpi.cs3733.d19.teamO.controller.LoginController;
import edu.wpi.cs3733.d19.teamO.controller.MainController;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.DefaultInformationLoader;
import edu.wpi.cs3733.d19.teamO.entity.Floor;
import edu.wpi.cs3733.d19.teamO.entity.Floors;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import edu.wpi.cs3733.d19.teamO.entity.database.DatabaseModule;

public class Project extends Application {

  private static final Logger logger
      = Logger.getLogger(Project.class.getName());

  @Inject
  private EventBus eventBus;
  @Inject
  private Database database;
  @Inject
  private Floors floors;
  @Inject
  private LoginController.Factory loginControllerFactory;

  private Injector injector;
  private Parent root;

  @Override
  public void init() throws IOException {
    notifyPreloader(new ProjectPreloader.StateNotification("Setting up loggers", 0));
    LogManager.setupLoggers();

    logger.config("Application init");

    notifyPreloader(
        new ProjectPreloader.StateNotification("Injecting members", 0.2));
    injector = Guice.createInjector(new ProjectModule(), new ControllerModule(),
        new DatabaseModule());
    injector.injectMembers(this);

    notifyPreloader(
        new ProjectPreloader.StateNotification("Loading floor maps", 0.45));
    registerMap(3, "3", "03_thethirdfloor.png", 0.5);
    registerMap(2, "2", "02_thesecondfloor.png", 0.55);
    registerMap(1, "1", "01_thefirstfloor.png", 0.6);
    registerMap(0, "G", "00_thegroundfloor.png", 0.65);
    registerMap(-1, "L1", "00_thelowerlevel1.png", 0.7);
    registerMap(-2, "L2", "00_thelowerlevel2.png", 0.75);

    notifyPreloader(
        new ProjectPreloader.StateNotification("Loading default database", 0.8));
    new DefaultInformationLoader(database).loadIfEmpty();
    notifyPreloader(
        new ProjectPreloader.StateNotification("Starting", 1.0));

    logger.config("Application init complete");
  }

  @Override
  public void start(final Stage primaryStage) throws IOException {
    logger.config("Starting application");

    FXMLLoader loader = new FXMLLoader(MainController.class.getResource("Main.fxml"));
    loader.setControllerFactory(injector::getInstance);
    root = loader.load();
    root.getStylesheets().add(
        Project.class.getResource("controller/style/KioskStyle.css").toExternalForm());

    eventBus.post(new ChangeMainViewEvent(loginControllerFactory.create(), false));

    primaryStage.setTitle("Team O Kiosk Application");
    primaryStage.setScene(new Scene(root));

    // Set original window size and position
    primaryStage.setMinWidth(1280);
    primaryStage.setMinHeight(720);

    primaryStage.setMaximized(true);

    primaryStage.show();
    logger.config("Startup complete");

    Thread.setDefaultUncaughtExceptionHandler(this::onThreadException);
  }

  @Override
  public void stop() {
    logger.config("Application stopping");
    database.close();
    logger.config("Application stop finished");
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

  private void registerMap(int number, String name, String filename, double progress)
      throws IOException {
    notifyPreloader(new ProjectPreloader.StateNotification("Loading floor map: " + filename,
        progress));
    floors.register(new Floor(number, name,
        new Image(Floors.class.getResource(filename).openStream())));
  }
}
