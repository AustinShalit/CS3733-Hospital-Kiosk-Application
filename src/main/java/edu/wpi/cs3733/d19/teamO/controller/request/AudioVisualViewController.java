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

import edu.wpi.cs3733.d19.teamO.controller.CheckRequestsController;
import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.AudioVisualRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "AudioVisualView.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class AudioVisualViewController implements Controller {

  @FXML
  private BorderPane root;
  @FXML
  private JFXButton goBackButton;
  @FXML
  private JFXButton addButton;
  @FXML
  private JFXButton assignButton;
  @FXML
  private JFXButton completedButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TableView<AudioVisualRequest> requestsTableView;
  @FXML
  private TableColumn<AudioVisualRequest, Integer> idTableCol;
  @FXML
  private TableColumn<AudioVisualRequest, LocalDateTime> timeRequestedCol;
  @FXML
  private TableColumn<AudioVisualRequest, LocalDateTime> timeCompletedCol;
  @FXML
  private TableColumn<AudioVisualRequest, String> whoCompletedCol;
  @FXML
  private TableColumn<AudioVisualRequest, String> locationTableCol;
  @FXML
  private TableColumn<AudioVisualRequest,
      AudioVisualRequest.AudioVisualRequestType> categoryTableCol;
  @FXML
  private TableColumn<AudioVisualRequest, String> descriptionCol;
  @FXML
  private TableColumn<AudioVisualRequest, String> nameCol;

  @Inject
  private EventBus eventBus;
  @Inject
  private Database db;
  @Inject
  private CheckRequestsController.Factory checkRequestsControllerFactory;
  @Inject
  private AudioVisualPopupController.Factory audioVisualPopupFactory;

  private JFXPopup optionsPopup;

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

    requestsTableView.getItems().setAll(db.getAllAudioVisualRequests());

    // sort by id
    requestsTableView.getSortOrder().add(idTableCol);

    // make columns auto-resize
    requestsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    for (TableColumn column : requestsTableView.getColumns()) {
      column.setPrefWidth(1000); // must be a value larger than the starting window size
    }

    // Initialize the Add Request Popup
    optionsPopup = new JFXPopup(audioVisualPopupFactory.create().root);
    optionsPopup.setOnAutoHide(
        event -> {
          ColorAdjust reset = new ColorAdjust();
          reset.setBrightness(0);
          root.setEffect(reset);
          requestsTableView.getItems().clear();
          requestsTableView.getItems().setAll(db.getAllAudioVisualRequests());
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
    optionsPopup.show(root);
    optionsPopup.setX(
        (root.getScene().getWindow().getWidth() - optionsPopup.getWidth()) / 2
    );
    optionsPopup.setY(
        (root.getScene().getWindow().getHeight() - optionsPopup.getHeight()) / 2
    );
  }

  @FXML
  void onAssignButtonAction() {
    String id = DialogHelper.textInputDialog("Enter Security Request ID",
        "Enter Request ID", "ID: ");
    int idInt = Integer.parseInt(id);
    String name = DialogHelper.textInputDialog("Enter Employee Name",
        "Enter Employee Name", "Employee Name: ");

    AudioVisualRequest sr = db.getAudioVisualRequest(idInt).get();
    sr.setWhoCompleted(name);
    db.updateAudioVisualRequest(sr);

    requestsTableView.getItems().setAll(db.getAllAudioVisualRequests());
    requestsTableView.getSortOrder().add(idTableCol); //sort
  }

  @FXML
  void onCompletedButtonAction() {
    if (requestsTableView.getSelectionModel().isEmpty()) {
      new Shake(completedButton).play();
      return;
    }

    AudioVisualRequest selectedItem =
        requestsTableView.getSelectionModel().getSelectedItem();
    requestsTableView.getItems().remove(selectedItem);

    db.deleteAudioVisualRequest(selectedItem);
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    AudioVisualViewController create();
  }
}
