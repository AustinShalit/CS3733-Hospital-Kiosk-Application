package edu.wpi.cs3733.d19.teamO.request.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;

@FxmlController(url = "Main.fxml")
public class MainController implements Controller {

  @FXML
  private BorderPane root;

  @Inject
  private EventBus eventBus;

  @FXML
  void initialize() {
    eventBus.register(this);
  }


  @Subscribe
  @SuppressWarnings("PMD.UnusedPrivateMethod")
  private void acceptController(ChangeMainViewEvent event) {
    root.setCenter(event.getController().getRoot());
  }

  @Override
  public Parent getRoot() {
    return root;
  }
}
