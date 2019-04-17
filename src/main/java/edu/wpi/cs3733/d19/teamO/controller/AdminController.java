package edu.wpi.cs3733.d19.teamO.controller;


import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;

@FxmlController(url = "AdminWindow.fxml")
public class AdminController implements Controller {

  @FXML
  private VBox root;
  @FXML
  private JFXButton mapeditButton;
  @FXML
  private JFXButton employeeButton;


  @Inject
  private EventBus eventBus;

  @Inject
  private MapEditController.Factory mapEditControllerFactory;
  @Inject
  private EmployeeController.Factory employeeControllerFactory;


  @FXML
  void initialize() {

  }

  @FXML
  void mapeditOnAction() {
    eventBus.post(new ChangeMainViewEvent(mapEditControllerFactory.create()));
  }

  @FXML
  void employeeOnAction() {
    eventBus.post(new ChangeMainViewEvent(employeeControllerFactory.create()));
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    AdminController create();
  }
}
