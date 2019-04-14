package edu.wpi.cs3733.d19.teamO.controller.request;

import java.time.LocalDateTime;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;

import animatefx.animation.Shake;
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
import edu.wpi.cs3733.d19.teamO.entity.ExternalTransportationRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "ExternalTransportationView.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class ExternalTransportationViewController implements Controller {

  @FXML
  private BorderPane root;
  @FXML
  private JFXButton goBackButton;
  @FXML
  private JFXButton assignButton;
  @FXML
  private JFXButton completedButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TableView<ExternalTransportationRequest> requestsTableView;
  @FXML
  private TableColumn<ExternalTransportationRequest, Integer> idTableCol;
  @FXML
  private TableColumn<ExternalTransportationRequest, LocalDateTime> timeRequestedCol;
  @FXML
  private TableColumn<ExternalTransportationRequest, LocalDateTime> timeCompletedCol;
  @FXML
  private TableColumn<ExternalTransportationRequest, String> whoCompletedCol;
  @FXML
  private TableColumn<ExternalTransportationRequest, String> locationTableCol;
  @FXML
  private TableColumn<ExternalTransportationRequest,
      ExternalTransportationRequest.ExternalTransportationRequestType> categoryTableCol;
  @FXML
  private TableColumn<ExternalTransportationRequest, String> descriptionCol;
  @FXML
  private TableColumn<ExternalTransportationRequest, String> nameCol;

  @Inject
  private EventBus eventBus;
  @Inject
  private Database db;
  @Inject
  private RequestController.Factory checkRequestsControllerFactory;
  @Inject
  private ExternalTransportationPopupController.Factory externalTransportPopupFactory;

  private JFXPopup addPopup;

  @FXML
  void initialize() {
    idTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    timeRequestedCol.setCellValueFactory(new PropertyValueFactory<>("timeRequested"));
    //timeCompletedCol.setCellValueFactory(new PropertyValueFactory<>("timeCompletedString"));
    whoCompletedCol.setCellValueFactory(new PropertyValueFactory<>("whoCompleted"));
    locationTableCol.setCellValueFactory(new PropertyValueFactory<>("locationNodeIdString"));
    categoryTableCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    nameCol.setCellValueFactory(new PropertyValueFactory<>("person"));

    requestsTableView.getItems().setAll(db.getAllExternalTransportationRequests());

    // sort by id
    requestsTableView.getSortOrder().add(idTableCol);

    // make columns auto-resize
    requestsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    for (TableColumn column : requestsTableView.getColumns()) {
      column.setPrefWidth(1000); // must be a value larger than the starting window size
    }

    // Initialize the Add Request Popup
    addPopup = new JFXPopup(externalTransportPopupFactory.create().root);
    addPopup.setOnAutoHide(
        event -> {
          ColorAdjust reset = new ColorAdjust();
          reset.setBrightness(0);
          root.setEffect(reset);
          requestsTableView.getItems().clear();
          requestsTableView.getItems().setAll(db.getAllExternalTransportationRequests());
        }
    );

    assignButton.setDisable(true);
    completedButton.setDisable(true);

    // Disable complete request and assign employee button if no request is selected
    requestsTableView.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          if (newValue == null) {
            assignButton.setDisable(true);
            completedButton.setDisable(true);
          } else {
            assignButton.setDisable(false);
            completedButton.setDisable(false);
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
    String id = DialogHelper.textInputDialog("Enter Security Request ID",
        "Enter Request ID", "ID: ");
    int idInt = Integer.parseInt(id);
    String name = DialogHelper.textInputDialog("Enter Employee Name",
        "Enter Employee Name", "Employee Name: ");

    ExternalTransportationRequest sr = db.getExternalTransportationRequest(idInt).get();
    sr.setWhoCompleted(name);
    db.updateExternalTransportationRequest(sr);

    requestsTableView.getItems().setAll(db.getAllExternalTransportationRequests());
    requestsTableView.getSortOrder().add(idTableCol); //sort
  }

  @FXML
  void onCompletedButtonAction() {
    if (requestsTableView.getSelectionModel().isEmpty()) {
      new Shake(completedButton).play();
      return;
    }

    ExternalTransportationRequest selectedItem =
        requestsTableView.getSelectionModel().getSelectedItem();
    requestsTableView.getItems().remove(selectedItem);

    db.deleteExternalTransportationRequest(selectedItem);

  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    ExternalTransportationViewController create();
  }
}
