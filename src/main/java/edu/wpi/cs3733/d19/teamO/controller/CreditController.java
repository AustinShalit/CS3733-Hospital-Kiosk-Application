package edu.wpi.cs3733.d19.teamO.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

@FxmlController(url = "Credit.fxml")
public class CreditController implements Controller{

  @FXML
  private BorderPane root;
  @FXML
  private Text credPage;
  @FXML
  private Text apiUsed;
  @FXML
  private Text api1;
  @FXML
  private Text api2;
  @FXML
  private Text api3;
  @FXML
  private Text api4;
  @FXML
  private Text api5;
  @FXML
  private Text api6;
  @FXML
  private Text api7;

  public interface Factory {
    CreditController create();
  }

  @Override
  public Parent getRoot() {
    return root;
  }

}
