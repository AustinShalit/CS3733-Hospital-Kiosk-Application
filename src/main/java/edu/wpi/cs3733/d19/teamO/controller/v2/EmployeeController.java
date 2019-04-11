package edu.wpi.cs3733.d19.teamO.controller.v2;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.Login;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "EmployeeManagement.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class EmployeeController implements Controller{

  @FXML
  private BorderPane root;
  @FXML
  private JFXButton goBackButton;
  @FXML
  private JFXButton addEmpButton;
  @FXML
  private JFXButton delEmpButton;
  @FXML
  private JFXButton updateEmpButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TableView<Login> employeeTableView;
  @FXML
  private TableColumn<Login, String> usernameCol;
  @FXML
  private TableColumn<Login, String> passwordCol;
  @FXML
  private TableColumn<Login, Login.EmployeeType> positionCol;
  @FXML
  private TableColumn<Login, String> nameCol;

  @Inject
  private EventBus eventBus;
  @Inject
  private Database db;
  @Inject
  private AdminController.Factory adminControllerFactory;
  @Inject
  private AddEmployeeController.Factory addEmployeeControllerFactory;
  @Inject
  private UpdateEmployeeController.Factory updateEmployeeControllerFactory;

  private UpdateEmployeeController updateEmployeeController;

  @FXML
  void initialize() {
    usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
    passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
    positionCol.setCellValueFactory(new PropertyValueFactory<>("employeeType"));
    nameCol.setCellValueFactory(new PropertyValueFactory<>("user"));

    employeeTableView.getItems().setAll(db.getAllLogin());

    employeeTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    employeeTableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Login>) c -> {
      if (updateEmployeeController != null) {
        updateEmployeeController.setLogin(employeeTableView.getSelectionModel().getSelectedItem());
      }
    });
    // sorts table by name
//    employeeTableView.getSortOrder().add(nameCol);

    // makes columns auto-resize
//    for (TableColumn column : employeeTableView.getColumns()) {
//      column.setPrefWidth(1000); // must be a value larger than the starting window size
//    }

  }

  @FXML
  void goBackButtonAction() {
    eventBus.post(new ChangeMainViewEvent(adminControllerFactory.create()));
  }

  @FXML
  void addEmpOnAction() {
    root.setLeft(addEmployeeControllerFactory.create().getRoot());
  }

  @FXML
  void delEmpOnAction() {
    Login selectedLogin = employeeTableView.getSelectionModel().getSelectedItem();
    employeeTableView.getItems().remove(selectedLogin);

    db.deleteLogin(selectedLogin);
  }

  @FXML
  void updateEmpOnAction() {
    updateEmployeeController = updateEmployeeControllerFactory.create();
    root.setLeft(updateEmployeeController.getRoot());
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    EmployeeController create();
  }

}