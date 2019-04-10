package edu.wpi.cs3733.d19.teamO.controller.v2.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import animatefx.animation.Shake;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.v2.CheckRequestsController;
import edu.wpi.cs3733.d19.teamO.controller.v2.Controller;
import edu.wpi.cs3733.d19.teamO.controller.v2.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.v2.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.PatientInfoRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "PatientInfoView.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class PatientInfoViewController implements Controller {

  @FXML
  private BorderPane root;
  @FXML
  private JFXButton goBackButton;
  @FXML
  private JFXButton deleteEntryButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TableView<PatientInfoRequest> requestsTableView;
  @FXML
  private TableColumn<PatientInfoRequest, Integer> idCol;
  @FXML
  private TableColumn<PatientInfoRequest, LocalDateTime> timeRequestedCol;
  @FXML
  private TableColumn<PatientInfoRequest, String> whoCompletedCol;
  @FXML
  private TableColumn<PatientInfoRequest, String> descriptionCol;
  @FXML
  private TableColumn<PatientInfoRequest, String> locationTableCol;
  @FXML
  private TableColumn<PatientInfoRequest, String> patientNameCol;
  @FXML
  private TableColumn<PatientInfoRequest, LocalDate> patientDOBCol;
  @FXML
  private TableColumn<PatientInfoRequest, PatientInfoRequest.PatientInfoSex> patientSexCol;

  @Inject
  private EventBus eventBus;
  @Inject
  private Database db;
  @Inject
  private CheckRequestsController.Factory checkRequestsControllerFactory;

  @FXML
  void initialize() {
    idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    timeRequestedCol.setCellValueFactory(new PropertyValueFactory<>("timeRequested"));
    whoCompletedCol.setCellValueFactory(new PropertyValueFactory<>("whoCompleted"));
    descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    locationTableCol.setCellValueFactory(new PropertyValueFactory<>("locationNodeIdString"));
    patientNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    patientDOBCol.setCellValueFactory(new PropertyValueFactory<>("patientDOB"));
    patientSexCol.setCellValueFactory(new PropertyValueFactory<>("patientSex"));


    requestsTableView.getItems().setAll(db.getAllPatientInfoRequests());

    // sort by id
    requestsTableView.getSortOrder().add(idCol);

    // make columns auto-resize
    requestsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    for (TableColumn column : requestsTableView.getColumns()) {
      column.setPrefWidth(1000); // must be a value larger than the starting window size
    }
  }

  @FXML
  void goBackButtonAction() {
    eventBus.post(new ChangeMainViewEvent(checkRequestsControllerFactory.create()));
  }

  @FXML
  void onAssignButtonAction() {
    String id = DialogHelper.textInputDialog("Enter Patient Info Request ID",
        "Enter Request ID", "ID: ");
    int idInt = Integer.parseInt(id);
    String name = DialogHelper.textInputDialog("Enter Employee Name",
        "Enter Employee Name", "Employee Name: ");

    PatientInfoRequest sr = db.getPatientInfoRequest(idInt).get();
    sr.setWhoCompleted(name);
    db.updatePatientInfoRequest(sr);

    requestsTableView.getItems().setAll(db.getAllPatientInfoRequests());
    requestsTableView.getSortOrder().add(idCol); //sort
  }

  @FXML
  void onDeleteEntryButtonAction() {
    if (requestsTableView.getSelectionModel().isEmpty()) {
      new Shake(deleteEntryButton).play();
      return;
    }

    PatientInfoRequest selectedItem =
        requestsTableView.getSelectionModel().getSelectedItem();
    requestsTableView.getItems().remove(selectedItem);

    db.deletePatientInfoRequest(selectedItem);

  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    PatientInfoViewController create();
  }
}
