package edu.wpi.cs3733.d19.teamO.controller.request;

import animatefx.animation.Shake;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.RequestController;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.Employee;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.BorderPane;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@FxmlController(url = "SanitationView.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class SanitationViewController implements Controller {

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
    private TableView<SanitationRequest> requestsTableView;
    @FXML
    private TableColumn<SanitationRequest, Integer> idTableCol;
    @FXML
    private TableColumn<SanitationRequest, LocalDateTime> timeRequestedCol;
    @FXML
    private TableColumn<SanitationRequest, LocalDateTime> timeCompletedCol;
    @FXML
    private TableColumn<SanitationRequest, String> whoCompletedCol;
    @FXML
    private TableColumn<SanitationRequest, String> locationTableCol;
    @FXML
    private TableColumn<SanitationRequest,
            SanitationRequest.SanitationRequestType> categoryTableCol;
    @FXML
    private TableColumn<SanitationRequest, String> descriptionCol;
    @FXML
    private TableColumn<SanitationRequest, String> nameCol;

    @Inject
    private EventBus eventBus;
    @Inject
    private Database db;
    @Inject
    private RequestController.Factory checkRequestsControllerFactory;
    @Inject
    private SanitationPopupController.Factory sanitationPopupFactory;

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

        requestsTableView.getItems().setAll(db.getAllSanitationRequests());

        // sort by id
        requestsTableView.getSortOrder().add(idTableCol);

        // make columns auto-resize
        requestsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for (TableColumn column : requestsTableView.getColumns()) {
            column.setPrefWidth(1000); // must be a value larger than the starting window size
        }

        // Initialize the Add Request Popup
        addPopup = new JFXPopup(sanitationPopupFactory.create().root);
        addPopup.setOnAutoHide(
                event -> {
                    ColorAdjust reset = new ColorAdjust();
                    reset.setBrightness(0);
                    root.setEffect(reset);
                    requestsTableView.getItems().clear();
                    requestsTableView.getItems().setAll(db.getAllSanitationRequests());
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
        addPopup.show(getRoot());
        addPopup.setX(
                (getRoot().getScene().getWidth() - addPopup.getWidth()) / 2
        );
        addPopup.setY(
                (getRoot().getScene().getHeight() - addPopup.getHeight()) / 2
        );
    }

    @FXML
    void onAssignButtonAction() {
        List<String> choices = new ArrayList<>();
        Set<Employee> emps = db.getAllEmployee();
        for (Employee employee : emps) {
            if (employee.getEmployeeAttributes().getCanFulfillSanitation()) {
                System.out.println(employee.getType());
                choices.add(employee.getName());
            }
        }

        String name = DialogHelper.choiceInputDialog(choices, "Assign",
                "Assign a Sanitation Employee", "Select an Employee");

        SanitationRequest selectedItem =
                requestsTableView.getSelectionModel().getSelectedItem();
        selectedItem.setWhoCompleted(name);
        db.updateSanitationRequest(selectedItem);

        requestsTableView.getItems().setAll(db.getAllSanitationRequests());
        requestsTableView.getSortOrder().add(idTableCol); //sort
    }

    @FXML
    void onCompletedButtonAction() {
        if (requestsTableView.getSelectionModel().isEmpty()) {
            new Shake(completedButton).play();
            return;
        }

        SanitationRequest selectedItem =
                requestsTableView.getSelectionModel().getSelectedItem();
        requestsTableView.getItems().remove(selectedItem);

        db.deleteSanitationRequest(selectedItem);

    }

    @Override
    public Parent getRoot() {
        return root;
    }

    public interface Factory {
        SanitationViewController create();
    }
}
