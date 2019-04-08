package edu.wpi.cs3733.d19.teamO.controller.v2;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;

@FxmlController(url = "Home.fxml")
public class HomeController implements Controller {

  @FXML
  private VBox root;
  @FXML
  private JFXButton navigationButton;
  @FXML
  private JFXButton requestButton;
  @FXML
  private JFXButton scheduleButton;
  @FXML
  private JFXButton securityButton;
  @Inject
  private EventBus eventBus;
  @Inject
  private RequestController.Factory requestControllerFactory;
  @Inject
  private NavigationController.Factory navigationControllerFactory;

  @FXML
  void navigationOnAction() {
    eventBus.post(new ChangeMainViewEvent(navigationControllerFactory.create()));
  }

  @FXML
  void requestOnAction() {
    eventBus.post(new ChangeMainViewEvent(requestControllerFactory.create()));
  }

  @FXML
  void scheduleOnAction() {

  }

  @FXML
  void securityOnAction() {
    if (DialogHelper.showConfirmDialog("Confirmation Dialog",
        "Security Request Notification",
        "Are you sure you want to alert the security?")) {
      System.out.println("Notifying");
      // TODO send to database
    }
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    HomeController create();
  }
}
