package edu.wpi.cs3733.d19.teamO.controller.v2.request;

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
import edu.wpi.cs3733.d19.teamO.entity.GiftRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "GiftView.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class GiftViewController implements Controller {

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
  private TableView<GiftRequest> requestsTableView;
  @FXML
  private TableColumn<GiftRequest, Integer> idTableCol;
  @FXML
  private TableColumn<GiftRequest, LocalDateTime> timeRequestedCol;
  @FXML
  private TableColumn<GiftRequest, LocalDateTime> timeCompletedCol;
  @FXML
  private TableColumn<GiftRequest, String> whoCompletedCol;
  @FXML
  private TableColumn<GiftRequest, String> locationTableCol;
  @FXML
  private TableColumn<GiftRequest,
      GiftRequest.GiftRequestType> categoryTableCol;
  @FXML
  private TableColumn<GiftRequest, String> descriptionCol;
  @FXML
  private TableColumn<GiftRequest, String> nameCol;

  @Inject
  private EventBus eventBus;
  @Inject
  private Database db;
  @Inject
  private RequestController.Factory requestControllerFactory;

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

    requestsTableView.getItems().setAll(db.getAllGiftRequests());

    // sort by id
    requestsTableView.getSortOrder().add(idTableCol);

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
    String id = DialogHelper.textInputDialog("Enter Security Request ID",
        "Enter Request ID", "ID: ");
    int idInt = Integer.parseInt(id);
    String name = DialogHelper.textInputDialog("Enter Employee Name",
        "Enter Employee Name", "Employee Name: ");

    GiftRequest sr = db.getGiftRequest(idInt).get();
    sr.setWhoCompleted(name);
    db.updateGiftRequest(sr);

    requestsTableView.getItems().setAll(db.getAllGiftRequests());
    requestsTableView.getSortOrder().add(idTableCol); //sort
  }

  @FXML
  void onCompletedButtonAction() {
    GiftRequest selectedItem =
        requestsTableView.getSelectionModel().getSelectedItem();
    requestsTableView.getItems().remove(selectedItem);

    db.deleteGiftRequest(selectedItem);

  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    GiftViewController create();
  }
}
