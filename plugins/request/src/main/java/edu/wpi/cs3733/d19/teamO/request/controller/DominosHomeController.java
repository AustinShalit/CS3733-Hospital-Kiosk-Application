package edu.wpi.cs3733.d19.teamO.request.controller;

import java.awt.Label;
import java.awt.event.ActionEvent;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.request.controller.request.ChangeMainViewEvent;

@FxmlController(url = "DominosHome.fxml")
public class DominosHomeController implements Controller {

  @FXML
  private BorderPane root;
  @FXML
  private Label titlelbl;
  @FXML
  private JFXButton orderbtn;
  @FXML
  private JFXButton viewbtn;

  @Inject
  private EventBus eventBus;
  @Inject
  private OrderTableController.Factory orderTableControllerFactory;

  @FXML
  void orderAction(ActionEvent event) {

  }

  @FXML
  void viewAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(orderTableControllerFactory.create()));
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    DominosHomeController create();
  }
}
