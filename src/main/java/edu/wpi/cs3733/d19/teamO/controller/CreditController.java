package edu.wpi.cs3733.d19.teamO.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

@FxmlController(url = "Credit.fxml")
public class CreditController implements Controller {

  @FXML
  BorderPane root;

  public interface Factory {
    CreditController create();
  }

  @Override
  public Parent getRoot() {
    return root;
  }

}
