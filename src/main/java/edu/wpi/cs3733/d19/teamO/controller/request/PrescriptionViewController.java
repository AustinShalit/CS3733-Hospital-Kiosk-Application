package edu.wpi.cs3733.d19.teamO.controller.v2.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.v2.Controller;
import edu.wpi.cs3733.d19.teamO.controller.v2.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.v2.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.v2.RequestController;
import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.PrescriptionRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "PrescriptionRequestView.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class PrescriptionViewController implements Controller {

  @FXML
  private BorderPane root;
  @FXML
  private JFXButton goBackButton;
  @FXML
  private JFXButton deleteEntryButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TableView<PrescriptionRequest> requestsTableView;
  @FXML
  private TableColumn<PrescriptionRequest, Integer> idCol;
  @FXML
  private TableColumn<PrescriptionRequest, LocalDateTime> timeRequestedCol;
  @FXML
  private TableColumn<PrescriptionRequest, LocalDateTime> timeCompletedCol;
  @FXML
  private TableColumn<PrescriptionRequest, String> whoRequestedCol;
  @FXML
  private TableColumn<PrescriptionRequest, String> patientNameCol;
  @FXML
  private TableColumn<PrescriptionRequest, LocalDate> patientDOBCol;
  @FXML
  private TableColumn<PrescriptionRequest, String> medNameCol;
  @FXML
  private TableColumn<PrescriptionRequest, String> medDoseCol;
  @FXML
  private TableColumn<PrescriptionRequest, String> medDescCol;

  @Inject
  private EventBus eventBus;
  @Inject
  private Database db;
  @Inject
  private RequestController.Factory requestControllerFactory;

  @FXML
  void initialize() {
    idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    timeRequestedCol.setCellValueFactory(new PropertyValueFactory<>("timeRequested"));
    timeCompletedCol.setCellValueFactory(new PropertyValueFactory<>("timeCompleted"));
    whoRequestedCol.setCellValueFactory(new PropertyValueFactory<>("whoRequested"));
    patientNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    patientDOBCol.setCellValueFactory(new PropertyValueFactory<>("patientDOB"));
    medNameCol.setCellValueFactory(new PropertyValueFactory<>("medicationName"));
    medDoseCol.setCellValueFactory(new PropertyValueFactory<>("medicationDosage"));
    medDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));


    requestsTableView.getItems().setAll(db.getAllPrescriptionRequest());

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
    eventBus.post(new ChangeMainViewEvent(requestControllerFactory.create()));
  }

  @FXML
  void onAssignButtonAction() {
    String id = DialogHelper.textInputDialog("Enter Prescription Request ID",
        "Enter Request ID", "ID: ");
    int idInt = Integer.parseInt(id);
    String name = DialogHelper.textInputDialog("Enter Employee Name",
        "Enter Employee Name", "Employee Name: ");

    PrescriptionRequest sr = db.getPrescriptionRequest(idInt).get();
    sr.setWhoCompleted(name);
    db.updatePrescriptionRequest(sr);

    requestsTableView.getItems().setAll(db.getAllPrescriptionRequest());
    requestsTableView.getSortOrder().add(idCol); //sort
  }

  @FXML
  void onDeleteEntryButtonAction() {
    PrescriptionRequest selectedItem =
        requestsTableView.getSelectionModel().getSelectedItem();
    requestsTableView.getItems().remove(selectedItem);

    db.deletePrescriptionRequest(selectedItem);

  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    PrescriptionViewController create();
  }
}
