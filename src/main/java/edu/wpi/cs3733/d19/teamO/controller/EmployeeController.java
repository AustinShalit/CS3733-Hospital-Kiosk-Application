package edu.wpi.cs3733.d19.teamO.controller;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;

import animatefx.animation.Shake;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.entity.Employee;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "EmployeeManagement.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class EmployeeController implements Controller {

  @FXML
  private BorderPane root;
  @FXML
  private JFXButton addEmpButton;
  @FXML
  private JFXButton delEmpButton;
  @FXML
  private JFXButton updateEmpButton;
  @FXML
  private JFXButton emergencyButton;
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
  private TableColumn<Employee, String> phoneCol;
  @FXML
  private Label infoLabel;

  @Inject
  private Database db;
  @Inject
  private AddEmployeeController.Factory addEmployeeControllerFactory;
  @Inject
  private UpdateEmployeeController.Factory updateEmployeeControllerFactory;
  @Inject
  private SmsController.Factory smsControllerFactory;

  private UpdateEmployeeController updateEmployeeController;
  private JFXPopup addPopup;
  private JFXPopup updatePopup;
  private JFXPopup emergencyPopup;

  @FXML
  void initialize() {
    usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
    passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
    positionCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

    updateEmployeeController = updateEmployeeControllerFactory.create();

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

    addPopup = new JFXPopup(addEmployeeControllerFactory.create().root);
    updatePopup = new JFXPopup(updateEmployeeController.upRoot);
    emergencyPopup = new JFXPopup(smsControllerFactory.create().root);
    addPopup.setOnAutoHide(
        event -> {
          ColorAdjust reset = new ColorAdjust();
          reset.setBrightness(0);
          root.setEffect(reset);
          employeeTableView.getItems().clear();
          employeeTableView.getItems().setAll(db.getAllEmployee());
        }
    );
    updatePopup.setOnAutoHide(
        event -> {
          ColorAdjust reset = new ColorAdjust();
          reset.setBrightness(0);
          root.setEffect(reset);
          employeeTableView.getItems().clear();
          employeeTableView.getItems().setAll(db.getAllEmployee());
        }
    );
    emergencyPopup.setOnAutoHide(
        event -> {
          ColorAdjust reset = new ColorAdjust();
          reset.setBrightness(0);
          root.setEffect(reset);
          employeeTableView.getItems().clear();
          employeeTableView.getItems().setAll(db.getAllEmployee());
        }
    );

    updateEmpButton.setDisable(true);
    delEmpButton.setDisable(true);


    // Disable complete request and assign employee button if no request is selected
    employeeTableView.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          if (newValue == null) {
            updateEmpButton.setDisable(true);
            delEmpButton.setDisable(true);
          } else {
            updateEmpButton.setDisable(false);
            delEmpButton.setDisable(false);
          }
        }
    );
  }

  @FXML
  void addEmpOnAction() {
    // root.setLeft(addEmployeeControllerFactory.create().getRoot());
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
  void delEmpOnAction() {
    if (employeeTableView.getSelectionModel().getSelectedItem() == null) {
      selectEmplyeeShake();
      return;
    }

    boolean check = DialogHelper.showConfirmDialog("Confirm Employee Deletion.",
        "Confirm Employee Deletion.",
        "Are you sure you would like to delete the selected employee?");
    if (check) {
      infoLabel.setText("");
      Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
      employeeTableView.getItems().remove(selectedEmployee);

      db.deleteEmployee(selectedEmployee);
    }
  }

  @FXML
  void updateEmpOnAction() {
    if (employeeTableView.getSelectionModel().getSelectedItem() == null) {
      selectEmplyeeShake();
      return;
    }

    infoLabel.setText("");
    //root.setLeft(updateEmployeeController.getRoot());
    ColorAdjust colorAdjust = new ColorAdjust();
    colorAdjust.setBrightness(-0.2);
    root.setEffect(colorAdjust);
    updatePopup.show(root);
    updatePopup.setX(
        (root.getScene().getWindow().getWidth() - updatePopup.getWidth()) / 2
    );
    updatePopup.setY(
        (root.getScene().getWindow().getHeight() - updatePopup.getHeight()) / 2
    );
  }

  @FXML
  void emergencyOnAction() {
    infoLabel.setText("");
    ColorAdjust colorAdjust = new ColorAdjust();
    colorAdjust.setBrightness(-0.2);
    root.setEffect(colorAdjust);
    emergencyPopup.show(root);
    emergencyPopup.setX(
        (root.getScene().getWindow().getWidth() - emergencyPopup.getWidth()) / 2
    );
    emergencyPopup.setY(
        (root.getScene().getWindow().getHeight() - emergencyPopup.getHeight()) / 2
    );
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
