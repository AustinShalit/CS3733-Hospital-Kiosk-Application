package edu.wpi.cs3733.d19.teamO.controller;

import java.time.LocalDateTime;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import animatefx.animation.Shake;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "SecurityViewWindow.fxml")
public class SecurityViewController implements Controller {

  @FXML
  private GridPane root;
  @FXML
  private JFXButton goBackButton;
  @FXML
  private Label titleLabel;
  @FXML
  private JFXButton assignButton;
  @FXML
  private JFXButton completedButton;
  @FXML
  private TableView<SecurityRequest> table;
  @FXML
  private TableColumn<SecurityRequest, Integer> idTableCol;
  @FXML
  private TableColumn<SecurityRequest, LocalDateTime> timeRequestedCol;
  @FXML
  private TableColumn<SecurityRequest, LocalDateTime> timeCompletedCol;
  @FXML
  private TableColumn<SecurityRequest, String> whoCompletedCol;
  @FXML
  private TableColumn<SecurityRequest, String> locationTableCol;

  @Inject
  private Database db;

  @FXML
  void initialize() {
    idTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    timeRequestedCol.setCellValueFactory(new PropertyValueFactory<>("timeRequested"));
    timeCompletedCol.setCellValueFactory(new PropertyValueFactory<>("timeCompleted"));
    whoCompletedCol.setCellValueFactory(new PropertyValueFactory<>("whoCompleted"));
    locationTableCol.setCellValueFactory(new PropertyValueFactory<>("location"));


    table.getItems().setAll(db.getAllSecurityRequests());

    // sort by id
    table.getSortOrder().add(idTableCol);

    // make columns auto-resize
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    for (TableColumn column : table.getColumns()) {
      column.setPrefWidth(1000); // must be a value larger than the starting window size
    }
  }

  @FXML
  void onAssignButtonAction() {
    String id = DialogHelper.textInputDialog("Enter Security Request ID",
        "Enter Request ID", "ID: ");
    int idInt = Integer.parseInt(id);
    String name = DialogHelper.textInputDialog("Enter Employee Name",
        "Enter Employee Name", "Employee Name: ");

    SecurityRequest sr = db.getSecurityRequest(idInt).get();
    sr.setWhoCompleted(name);
    db.updateSecurity(sr);

    table.getItems().setAll(db.getAllSecurityRequests());
    table.getSortOrder().add(idTableCol); //sort
  }

  @FXML
  void onCompletedButtonAction() {
    if (table.getSelectionModel().isEmpty()) {
      new Shake(completedButton).play();
      return;
    }

    SecurityRequest selectedItem =
        table.getSelectionModel().getSelectedItem();
    table.getItems().remove(selectedItem);

    db.deleteSecurityRequest(selectedItem);

  }

  public interface Factory {
    SecurityViewController create();
  }

  @Override
  public Parent getRoot() {
    return root;
  }
}
