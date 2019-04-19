package edu.wpi.cs3733.d19.teamO.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXToolbar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;

@FxmlController(url = "Main.fxml")
public class MainController implements Controller {

  @FXML
  private BorderPane root;
  @FXML
  private StackPane optionsBurger;
  @Inject
  private EventBus eventBus;
  @Inject
  private OptionsPopupController.Factory optionsPopupControllerFactory;

  private JFXPopup optionsPopup;

  @FXML
  void initialize() {
    eventBus.register(this);

    optionsPopup = new JFXPopup(optionsPopupControllerFactory.create().list);
    optionsBurger.setOnMouseClicked(e ->
        optionsPopup.show(optionsBurger,
            JFXPopup.PopupVPosition.TOP,
            JFXPopup.PopupHPosition.RIGHT,
            -12,
            15));
  }

  @Subscribe
  @SuppressWarnings("PMD.UnusedPrivateMethod")
  private void acceptController(ChangeMainViewEvent event) {
    root.setCenter(event.getController().getRoot());

    if (optionsPopup.isShowing()) {
      optionsPopup.hide();
    }
  }

  @Override
  public Parent getRoot() {
    return root;
  }
}
