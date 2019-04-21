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
import edu.wpi.cs3733.d19.teamO.entity.InterpreterRequest;
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

@FxmlController(url = "InterpreterView.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class InterpreterViewController implements Controller {

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
    private TableView<InterpreterRequest> requestsTableView;
    @FXML
    private TableColumn<InterpreterRequest, Integer> idTableCol;
    @FXML
    private TableColumn<InterpreterRequest, String> nameCol;
    @FXML
    private TableColumn<InterpreterRequest, LocalDateTime> timeRequestedCol;
    @FXML
    private TableColumn<InterpreterRequest, LocalDateTime> timeCompletedCol;
    @FXML
    private TableColumn<InterpreterRequest, String> whoCompletedCol;
    @FXML
    private TableColumn<InterpreterRequest, String> locationTableCol;
    @FXML
    private TableColumn<InterpreterRequest,
            InterpreterRequest.Language> categoryTableCol;
    @FXML
    private TableColumn<InterpreterRequest, String> descriptionCol;

    @Inject
    private EventBus eventBus;
    @Inject
    private Database db;
    @Inject
    private RequestController.Factory checkRequestsControllerFactory;
    @Inject
    private InterpreterPopupController.Factory interpreterPopupFactory;

    private JFXPopup addPopup;

    @FXML
    void initialize() {
        idTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        timeRequestedCol.setCellValueFactory(new PropertyValueFactory<>("timeRequested"));
        //timeCompletedCol.setCellValueFactory(new PropertyValueFactory<>("timeCompletedString"));
        whoCompletedCol.setCellValueFactory(new PropertyValueFactory<>("whoCompleted"));
        locationTableCol.setCellValueFactory(new PropertyValueFactory<>("locationNodeIdString"));
        categoryTableCol.setCellValueFactory(new PropertyValueFactory<>("language"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        requestsTableView.getItems().setAll(db.getAllInterpreterRequests());

        // sort by id
        requestsTableView.getSortOrder().add(idTableCol);

        // make columns auto-resize
        requestsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for (TableColumn column : requestsTableView.getColumns()) {
            column.setPrefWidth(1000); // must be a value larger than the starting window size
        }

        // Initialize the Add Request Popup
        addPopup = new JFXPopup(interpreterPopupFactory.create().root);
        addPopup.setOnAutoHide(
                event -> {
                    ColorAdjust reset = new ColorAdjust();
                    reset.setBrightness(0);
                    root.setEffect(reset);
                    requestsTableView.getItems().clear();
                    requestsTableView.getItems().setAll(db.getAllInterpreterRequests());
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
        List<String> choices = new ArrayList<>();
        Set<Employee> emps = db.getAllEmployee();
        for (Employee employee : emps) {
            if (employee.getEmployeeAttributes().getCanFulfillInterpreter()) {
                System.out.println(employee.getType());
                choices.add(employee.getName());
            }
        }

        String name = DialogHelper.choiceInputDialog(choices, "Assign",
                "Assign a Interpreter Employee", "Select an Employee");

        InterpreterRequest selectedItem =
                requestsTableView.getSelectionModel().getSelectedItem();
        selectedItem.setWhoCompleted(name);
        db.updateInterpreterRequest(selectedItem);

        requestsTableView.getItems().setAll(db.getAllInterpreterRequests());
        requestsTableView.getSortOrder().add(idTableCol); //sort
    }

    @FXML
    void onCompletedButtonAction() {
        if (requestsTableView.getSelectionModel().isEmpty()) {
            new Shake(completedButton).play();
            return;
        }

        InterpreterRequest selectedItem =
                requestsTableView.getSelectionModel().getSelectedItem();
        requestsTableView.getItems().remove(selectedItem);

        db.deleteInterpreterRequest(selectedItem);
    }

    @Override
    public Parent getRoot() {
        return root;
    }

    public interface Factory {
        InterpreterViewController create();
    }
}
