package edu.wpi.cs3733.d19.teamO.controller.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.RequestController;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
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
  private RequestController.Factory checkRequestsControllerFactory;
  @Inject
  private PrescriptionRequestPopupController.Factory prescriptionPopupFactory;

  private JFXPopup addPopup;

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

    // Initialize the Add Request Popup
    addPopup = new JFXPopup(prescriptionPopupFactory.create().root);
    addPopup.setOnAutoHide(
        event -> {
          ColorAdjust reset = new ColorAdjust();
          reset.setBrightness(0);
          root.setEffect(reset);
          requestsTableView.getItems().clear();
          requestsTableView.getItems().setAll(db.getAllPrescriptionRequest());
        }
    );

    //assignButton.setDisable(true);
    deleteEntryButton.setDisable(true);

    // Disable complete request and assign employee button if no request is selected
    requestsTableView.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          if (newValue == null) {
            //assignButton.setDisable(true);
            deleteEntryButton.setDisable(true);
          } else {
            //assignButton.setDisable(false);
            deleteEntryButton.setDisable(false);
          }
        }
    );
  }

  @FXML
  void goBackButtonAction() {
    eventBus.post(new ChangeMainViewEvent(checkRequestsControllerFactory.create()));
  }

  @FXML
  void onAddButtonAction() {
    ColorAdjust colorAdjust = new ColorAdjust();
    colorAdjust.setBrightness(-0.2);
    root.setEffect(colorAdjust);
    addPopup.show(root);
    addPopup.setX(
        (root.getScene().getWindow().getWidth() - addPopup.getWidth()) / 2
    );
    addPopup.setY(
        (root.getScene().getWindow().getHeight() - addPopup.getHeight()) / 2
    );
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
