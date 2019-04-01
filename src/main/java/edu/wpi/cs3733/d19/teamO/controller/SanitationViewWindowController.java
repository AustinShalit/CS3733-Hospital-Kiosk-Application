package edu.wpi.cs3733.d19.teamO.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;


public class SanitationViewWindowController extends Controller {

  @FXML
  private Button goBackButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TableView<SanitationRequest> requestsTableView;
  @FXML
  private TableColumn<SanitationRequest, Integer> idTableCol;
  @FXML
  private TableColumn<SanitationRequest, String> locationTableCol;
  @FXML
  private TableColumn<SanitationRequest, SanitationRequest.SanitationRequestType> categoryTableCol;

  @FXML
  void initialize() {
    idTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    locationTableCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    categoryTableCol.setCellValueFactory(new PropertyValueFactory<>("type"));
  }

  @FXML
  void goBackButtonAction() {
    switchScenes("MainSanitationWindow.fxml", goBackButton.getScene().getWindow());
  }
}
