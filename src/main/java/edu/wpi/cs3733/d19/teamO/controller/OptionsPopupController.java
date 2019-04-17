package edu.wpi.cs3733.d19.teamO.controller;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXListView;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import edu.wpi.cs3733.d19.teamO.GlobalState;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.Employee;

@FxmlController(url = "OptionsPopup.fxml")
public class OptionsPopupController implements Controller {

  @FXML
  JFXListView<Label> list;
  @FXML
  private Label adminItem;

  @Inject
  private EventBus eventBus;
  @Inject
  private AdminController.Factory adminControllerFactory;
  @Inject
  private GuestViewController.Factory loginControllerFactory;
  @Inject
  private SettingsController.Factory settingsControllerFactory;
  @Inject
  private GlobalState globalState;


  @FXML
  void initialize() {
    globalState.loggedInEmployeeProperty().addListener(((observable, oldValue, newValue) -> {
      if (newValue != null) {
        if (newValue.getEmployeeAttributes().getEmployeeType()
            == Employee.EmployeeType.ADMIN && !list.getItems().contains(adminItem)) {
          list.getItems().add(adminItem);
        } else {
          list.getItems().remove(adminItem);
        }
      }
    }));

  }

  @FXML
  void onAction(MouseEvent event) {
    list.getSelectionModel().getSelectedItem().fireEvent(event);
  }

  @FXML
  void settingsAction(MouseEvent event) {
    event.consume();
    eventBus.post(new ChangeMainViewEvent(settingsControllerFactory.create()));
  }

  @FXML
  void adminAction(MouseEvent event) {
    event.consume();
    eventBus.post(new ChangeMainViewEvent(adminControllerFactory.create()));
  }

  @FXML
  void signoutAction(MouseEvent event) {
    event.consume();
    eventBus.post(new ChangeMainViewEvent(loginControllerFactory.create(), false));
  }

  @Override
  public Parent getRoot() {
    return list;
  }

  public interface Factory {
    OptionsPopupController create();
  }
}
