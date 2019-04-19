package edu.wpi.cs3733.d19.teamO.request;

import java.io.IOException;

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

import edu.wpi.cs3733.d19.teamO.ExceptionAlert;
import edu.wpi.cs3733.d19.teamO.ProjectModule;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.ProjectPreloader;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.DefaultInformationLoader;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import edu.wpi.cs3733.d19.teamO.entity.database.DatabaseModule;
import edu.wpi.cs3733.d19.teamO.request.controller.ControllerModule;
import edu.wpi.cs3733.d19.teamO.request.controller.DominosHomeController;
import edu.wpi.cs3733.d19.teamO.request.controller.MainController;

public class Project extends Application {

  private int stageMinWidth = 1280;
  public void setStageMinWidth(int stageMinWidth) {
    this.stageMinWidth = stageMinWidth;
  }

  private int stageMinHeight = 720;
  public void setStageMinHeight(int stageMinHeight) {
    this.stageMinHeight = stageMinHeight;
  }

  private int stageX = 0;
  public void setStageX(int stageX) {
    this.stageX = stageX;
  }

  private int stageY = 0;
  public void setStageY(int stageY) {
    this.stageY = stageY;
  }

  @Inject
  private EventBus eventBus;
  @Inject
  private Database database;
  @Inject
  private DominosHomeController.Factory dominosHomeControllerFactory;

  private Injector injector;
  private Parent root;

  @Override
  public void init() throws IOException {
    notifyPreloader(new ProjectPreloader.StateNotification("Setting up loggers", 0));

    notifyPreloader(
        new ProjectPreloader.StateNotification("Injecting members", 0.2));
    injector = Guice.createInjector(new ProjectModule(), new ControllerModule(),
        new DatabaseModule());
    injector.injectMembers(this);

    new DefaultInformationLoader(database).loadIfEmpty();
    notifyPreloader(
        new ProjectPreloader.StateNotification("Starting", 1.0));

  }

  @Override
  public void start(final Stage primaryStage) throws IOException {

    FXMLLoader loader = new FXMLLoader(MainController.class.getResource("Main.fxml"));
    loader.setControllerFactory(injector::getInstance);
    root = loader.load();

    eventBus.post(new ChangeMainViewEvent(dominosHomeControllerFactory.create()));

    primaryStage.setTitle("Team O - Dominos Plugin API");
    primaryStage.setScene(new Scene(root));

    // Set original window size and position
    primaryStage.setX(stageX);
    primaryStage.setY(stageY);

    if (stageMinWidth == -1 && stageMinHeight == -1) {
      primaryStage.setMinWidth(1080);
      primaryStage.setMinHeight(720);
      primaryStage.setMaximized(true);
    } else {
      primaryStage.setMinWidth(stageMinWidth);
      primaryStage.setMinHeight(stageMinHeight);
    }

    primaryStage.show();

    Thread.setDefaultUncaughtExceptionHandler(this::onThreadException);
  }

  @Override
  public void stop() {
    database.close();
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
      }
    });
  }
}
