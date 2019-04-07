package edu.wpi.cs3733.d19.teamO.controller.v2;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXListView;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import edu.wpi.cs3733.d19.teamO.controller.LoginWindowController;

public class OptionsPopupController {

  @FXML
  JFXListView<Label> list;

  @Inject
  private ContentSwitcher contentSwitcher;

  @FXML
  void onAction(MouseEvent event) {
    list.getSelectionModel().getSelectedItem().fireEvent(event);
  }

  @FXML
  void settingsAction(MouseEvent event) {

  }

  @FXML
  void signoutAction(MouseEvent event) {
    event.consume();
    contentSwitcher.accept(LoginWindowController.class, "LoginWindow.fxml");
  }
}
