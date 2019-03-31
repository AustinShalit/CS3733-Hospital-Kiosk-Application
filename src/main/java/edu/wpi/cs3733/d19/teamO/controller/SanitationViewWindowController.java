package edu.wpi.cs3733.d19.teamO.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
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
    requestsTableView.setItems(FXCollections.observableList(new ArrayList<>(getAll())));
  }

  @FXML
  void goBackButtonAction() {
    switchScenes("MainSanitationWindow.fxml", goBackButton.getScene().getWindow());
  }

  private Set<SanitationRequest> getAll() {
    HashSet<SanitationRequest> requests = new HashSet<SanitationRequest>();
    requests.add(new SanitationRequest(15, LocalDateTime.MIN, LocalDateTime.MAX,
        null, SanitationRequest.SanitationRequestType.SPILL, "There is huge spill in the hallway"));
    requests.add(new SanitationRequest(27, LocalDateTime.MIN, LocalDateTime.MAX,
        null, SanitationRequest.SanitationRequestType.VOMIT, "Ken threw up"));
    requests.add(new SanitationRequest(5, LocalDateTime.MIN, LocalDateTime.MAX,
        null, SanitationRequest.SanitationRequestType.OTHERS, "The water fountain is leaking"));
    return requests;
  }
}
