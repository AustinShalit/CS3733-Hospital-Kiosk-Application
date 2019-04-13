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
import javafx.stage.Stage;

import edu.wpi.cs3733.d19.teamO.controller.ControllerModule;
import edu.wpi.cs3733.d19.teamO.controller.EmployeeController;
import edu.wpi.cs3733.d19.teamO.controller.LoginController;
import edu.wpi.cs3733.d19.teamO.controller.MainController;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.DefaultInformationLoader;
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
  private LoginController.Factory loginControllerFactory;

  private Injector injector;
  private Parent root;

  @Override
  public void init() {
    logger.config("Application init");

    injector = Guice.createInjector(new ProjectModule(), new ControllerModule(),
        new DatabaseModule());
    injector.injectMembers(this);

    new DefaultInformationLoader(database).loadIfEmpty();

    logger.config("Application init complete");
  }

  @Override
  public void start(final Stage primaryStage) throws IOException {
    logger.config("Starting application");

    FXMLLoader loader = new FXMLLoader(MainController.class.getResource("Main.fxml"));
    loader.setControllerFactory(injector::getInstance);
    root = loader.load();

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
}
