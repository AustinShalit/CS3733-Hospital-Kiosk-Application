package edu.wpi.cs3733.d19.teamO.request.controller;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "Reorder.fxml")
public class ReorderController implements Controller {

  @Inject
  private EventBus eventBus;
  @Inject
  private OrderController.Factory orderControllerFactory;
  @Inject
  private Database db;

  @FXML
  private BorderPane root;
  @FXML
  private JFXButton orderButton;
  @FXML
  private JFXButton viewButton;


  @FXML
  void orderAction(ActionEvent event) {
    eventBus.post(new ChangeMainViewEvent(orderControllerFactory.create()));
  }


  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    ReorderController create();
  }
}
