package edu.wpi.cs3733.d19.teamO.controller;

import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class SanitationViewWindowController extends Controller{

  @FXML
  private Button goBackButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TableView requestsTableView;
  @FXML
  private TableColumn idTableCol;
  @FXML
  private TableColumn locationTableCol;
  @FXML
  private TableColumn categoryTableCol;


  @FXML
  void goBackButtonAction(ActionEvent event) {
    if (event.getSource() == goBackButton) {
      switchScenes("MainSanitationWindow.fxml", goBackButton.getScene().getWindow());
    }
  }

  Set<SanitationRequest> getAll(ActionEvent event) {
    HashSet<SanitationRequest> requests = new HashSet<SanitationRequest>();
    //requests.add(new SanitationRequest());
    return requests;
  }
}
