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
  private LoginController.Factory loginControllerFactory;

  @FXML
  void settingsAction(MouseEvent event) {

  }

  @FXML
  void signoutAction(MouseEvent event) {
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
