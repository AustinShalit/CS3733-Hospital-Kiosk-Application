package edu.wpi.cs3733.d19.teamO.controller.v2;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXListView;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;

@FxmlController(url = "OptionsPopup.fxml")
public class OptionsPopupController implements Controller {

  @FXML
  JFXListView<Label> list;

  @Inject
  private EventBus eventBus;
  @Inject
  private AdminController.Factory adminControllerFactory;
  @Inject
  private LoginController.Factory loginControllerFactory;
  @Inject
  private CheckRequestsController.Factory checkRequestsControllerFactory;

  @FXML
  void onAction(MouseEvent event) {
    list.getSelectionModel().getSelectedItem().fireEvent(event);
  }

  @FXML
  void settingsAction(MouseEvent event) {

  }

  @FXML
  void adminAction(MouseEvent event) {
    event.consume();
    eventBus.post(new ChangeMainViewEvent(adminControllerFactory.create()));
  }

  @FXML
  void checkAction(MouseEvent event) {
    event.consume();
    eventBus.post(new ChangeMainViewEvent(checkRequestsControllerFactory.create()));
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
