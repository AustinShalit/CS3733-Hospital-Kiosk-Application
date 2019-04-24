package edu.wpi.cs3733.d19.teamO.controller;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;

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
  private Label editEmp;
  @FXML
  private Label editMap;
  @FXML
  private Label emergency;

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
  @Inject
  private SmsController.Factory smsControllerFactory;
  @Inject
  private GlobalState globalState;

  private JFXPopup settingsPopup;
  private JFXPopup emergencyPopup;

  @FXML
  void initialize() {
    settingsPopup = new JFXPopup(settingsControllerFactory.create().root);
    emergencyPopup = new JFXPopup(smsControllerFactory.create().root);
    globalState.loggedInEmployeeProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        if (newValue.getEmployeeAttributes().getEmployeeType()
            == Employee.EmployeeType.ADMIN
            && list.getItems().contains(editEmp) && list.getItems().contains(editMap)) {
          System.out.println("Logged in");
          list.setMaxHeight(300.0);
        } else if (newValue.getEmployeeAttributes().getEmployeeType()
            == Employee.EmployeeType.ADMIN) {
          list.getItems().add(editEmp);
          list.getItems().add(editMap);
          list.getItems().add(emergency);
          list.setMaxHeight(300.0);
        } else {
          list.getItems().remove(editEmp);
          list.getItems().remove(editMap);
          list.getItems().remove(emergency);
          list.setMaxHeight(80.0);
        }
      }
    });
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
        0,
        0);
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

  @FXML
  void emergencyAction(MouseEvent event) {
    event.consume();
    emergencyPopup.show(list,
        JFXPopup.PopupVPosition.TOP,
        JFXPopup.PopupHPosition.RIGHT,
        0,
        0);
  }

  @Override
  public Parent getRoot() {
    return list;
  }

  public interface Factory {
    OptionsPopupController create();
  }
}
