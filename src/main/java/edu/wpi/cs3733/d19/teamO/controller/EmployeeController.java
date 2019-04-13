package edu.wpi.cs3733.d19.teamO.controller;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import animatefx.animation.Shake;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.Employee;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "EmployeeManagement.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class EmployeeController implements Controller {

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
  private TableView<Employee> employeeTableView;
  @FXML
  private TableColumn<Employee, String> usernameCol;
  @FXML
  private TableColumn<Employee, String> passwordCol;
  @FXML
  private TableColumn<Employee, Employee.EmployeeType> positionCol;
  @FXML
  private TableColumn<Employee, String> nameCol;
  @FXML
  private Label infoLabel;

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
    positionCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

    employeeTableView.getItems().setAll(db.getAllEmployee());
    employeeTableView.getSortOrder().add(nameCol); // sort by name

    employeeTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    employeeTableView.getSelectionModel().getSelectedItems()
        .addListener((ListChangeListener<Employee>) c -> {
          if (updateEmployeeController != null) {
            updateEmployeeController.setEmployee(
                employeeTableView.getSelectionModel().getSelectedItem());
          }
        });
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
    if (employeeTableView.getSelectionModel().getSelectedItem() == null) {
      selectEmplyeeShake();
      return;
    }

    infoLabel.setText("");
    Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
    employeeTableView.getItems().remove(selectedEmployee);

    db.deleteEmployee(selectedEmployee);
  }

  @FXML
  void updateEmpOnAction() {
    if(employeeTableView.getSelectionModel().getSelectedItem() == null) {
    selectEmplyeeShake();
    return;
    }

    infoLabel.setText("");
    updateEmployeeController = updateEmployeeControllerFactory.create();
    root.setLeft(updateEmployeeController.getRoot());
  }

  void selectEmplyeeShake() {
    infoLabel.setText("Please select an Employee.");
    new Shake(infoLabel).play();
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    EmployeeController create();
  }

}
