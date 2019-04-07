package edu.wpi.cs3733.d19.teamO.controller.v2;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

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

  @FXML
  void navigationOnAction() {

  }

  @FXML
  void requestOnAction() {
    eventBus.post(requestControllerFactory.create());
  }

  @FXML
  void scheduleOnAction() {

  }

  @FXML
  void securityOnAction() {

  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    HomeController create();
  }
}
