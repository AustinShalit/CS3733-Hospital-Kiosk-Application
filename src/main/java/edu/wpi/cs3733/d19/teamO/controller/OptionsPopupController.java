package edu.wpi.cs3733.d19.teamO.controller;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;

@FxmlController(url = "OptionsPopup.fxml")
public class OptionsPopupController implements Controller {

  @FXML
  JFXListView<Label> list;

  @Inject
  private EventBus eventBus;
  @Inject
  private SettingsController.Factory settingsControllerFactory;
  @Inject
  private MapEditController.Factory mapEditControllerFactory;
  @Inject
  private EmployeeController.Factory employeeControllerFactory;
  @Inject
  private FireAlarmController.Factory fireAlarmControllerFactory;

  private JFXPopup settingsPopup;

  @FXML
  void initialize() {
    settingsPopup = new JFXPopup(settingsControllerFactory.create().root);
  }

  @FXML
  void onAction(MouseEvent event) {
    list.getSelectionModel().getSelectedItem().fireEvent(event);
  }

  @FXML
  void settingsAction(MouseEvent event) {
    event.consume();
    settingsPopup.show(list,
        JFXPopup.PopupVPosition.TOP,
        JFXPopup.PopupHPosition.RIGHT,
        -12,
        15);
  }

  @FXML
  void employeeAction(MouseEvent event) {
    event.consume();
    eventBus.post(new ChangeMainViewEvent(employeeControllerFactory.create()));
  }

  @FXML
  void mapAction(MouseEvent event) {
    event.consume();
    eventBus.post(new ChangeMainViewEvent(mapEditControllerFactory.create()));
  }

  @FXML
  void fireAction(MouseEvent event) {
    event.consume();
    eventBus.post(new ChangeMainViewEvent(fireAlarmControllerFactory.create()));
  }

  @Override
  public Parent getRoot() {
    return list;
  }

  public interface Factory {
    OptionsPopupController create();
  }
}
